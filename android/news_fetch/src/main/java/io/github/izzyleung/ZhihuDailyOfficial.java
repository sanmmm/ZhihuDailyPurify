package io.github.izzyleung;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.github.izzyleung.utils.Network;
import io.github.izzyleung.utils.Story;
import io.github.izzyleung.utils.Tuple;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class ZhihuDailyOfficial {
    private ZhihuDailyOfficial() {

    }

    private static String mkString(InputStream input) {
        return new BufferedReader(new InputStreamReader(input))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private static JSONObject[] toArray(JSONArray jsonArray) throws JSONException {
        JSONObject[] result = new JSONObject[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            result[i] = jsonArray.getJSONObject(i);
        }

        return result;
    }

    private static Flowable<Story> convertToStory(JSONObject j) throws JSONException {
        Flowable<Integer> idFlowable = Flowable.just(j.getInt("id"));
        Flowable<String> titleFlowable = Flowable.just(j.getString("title"));
        Flowable<String> thumbnailUrlFlowable = Flowable.just(j)
                .filter(x -> x.has("images"))
                .map(x -> x.getJSONArray("images"))
                .map(x -> (String) x.get(0))
                .switchIfEmpty(Flowable.just(""));

        return Flowable.zip(idFlowable, titleFlowable, thumbnailUrlFlowable, (id, title, thumbnailUrl) ->
                Story.newBuilder()
                        .setId(id)
                        .setTitle(title)
                        .setThumbnailUrl(thumbnailUrl)
                        .build()
        );
    }

    public static Flowable<Optional<Document>> documents(InputStream in) throws JSONException {
        return Flowable.just(new JSONObject(mkString(in)))
                .filter(j -> j.has("body"))
                .map(j -> Optional.of(Jsoup.parse(j.getString("body"))))
                .switchIfEmpty(Flowable.just(Optional.empty()));
    }

    public static Flowable<Story> stories(InputStream in) {
        return Flowable.just(mkString(in))
                .map(JSONObject::new)
                .map(j -> j.getJSONArray("stories"))
                .flatMap(j -> Flowable.fromArray(toArray(j)))
                .flatMap(ZhihuDailyOfficial::convertToStory);
    }

    private static Flowable<ZhihuDailyPurify.News> convertToNews(Tuple<String, Story, Optional<Document>> triple) {
        return getQuestions(triple)
                .filter(ZhihuDailyOfficial::isValidZhihuQuestion)
                .toList()
                .filter(qs -> qs.size() > 0)
                .map(qs -> ZhihuDailyPurify.News
                        .newBuilder()
                        .setDate(triple.getLeft())
                        .setTitle(triple.getMiddle().getTitle())
                        .setThumbnailUrl(triple.getMiddle().getThumbnailUrl())
                        .addAllQuestions(qs)
                        .build())
                .toFlowable();
    }

    private static Flowable<ZhihuDailyPurify.Question> getQuestions(Tuple<String, Story, Optional<Document>> triple) {
        String dailyTitle = triple.getMiddle().getTitle();
        //noinspection ConstantConditions
        Document document = triple.getRight().get();

        return Flowable.fromIterable(getQuestionElements(document))
                .map(questionElement ->
                        ZhihuDailyPurify.Question
                                .newBuilder()
                                .setTitle(obtainQuestionTitle(questionElement).orElse(dailyTitle))
                                .setUrl(obtainQuestionUrl(questionElement).orElse(""))
                                .build())
                .filter(ZhihuDailyOfficial::isValidZhihuQuestion);
    }

    private static Elements getQuestionElements(Document document) {
        return document.select(Selectors.QUESTION);
    }

    private static Optional<String> obtainQuestionTitle(Element questionElement) {
        return Optional
                .ofNullable(questionElement.select(Selectors.QUESTION_TITLES).first())
                .map(Element::text)
                .filter(s -> !s.isEmpty());
    }

    private static Optional<String> obtainQuestionUrl(Element questionElement) {
        return Optional
                .ofNullable(questionElement.select(Selectors.QUESTION_LINKS).first())
                .map(e -> e.attr("href"));
    }

    private static boolean isValidZhihuQuestion(ZhihuDailyPurify.Question question) {
        return Optional
                .ofNullable(question.getUrl())
                .map(q -> q.contains("zhihu.com/question/"))
                .orElse(false);
    }

    public static Single<ZhihuDailyPurify.Feed> feedForDate(String date) throws IOException {
        Flowable<Story> stories = stories(ZhihuDaily.storiesForDate(date));
        Flowable<Optional<Document>> documents = stories.map(ZhihuDaily::storyDetail).flatMap(ZhihuDailyOfficial::documents);
        Flowable<String> dates = stories.map(s -> date);

        Single<ZhihuDailyPurify.Feed.Builder> builder = Single.just(ZhihuDailyPurify.Feed.newBuilder());
        Single<List<ZhihuDailyPurify.News>> news = Flowable
                .zip(dates, stories, documents, Tuple::new)
                .filter(tuple -> tuple.getRight().isPresent())
                .flatMap(ZhihuDailyOfficial::convertToNews)
                .toList();

        return Single.zip(builder, news, (b, n) -> b.addAllNews(n).build());
    }

    private static class ZhihuDaily {
        private static final String ZHIHU_DAILY_NEWS_BASE = "https://news-at.zhihu.com/api/4/news/";
        private static final String ZHIHU_DAILY_NEWS_BEFORE = ZHIHU_DAILY_NEWS_BASE + "before/";

        private static InputStream storiesForDate(String date) throws IOException {
            return Network.openInputStream(ZHIHU_DAILY_NEWS_BEFORE + date);
        }

        private static InputStream storyDetail(Story story) throws IOException {
            return Network.openInputStream(ZHIHU_DAILY_NEWS_BASE + story.getId());
        }
    }

    private static class Selectors {
        private static final String QUESTION = "div.question";
        private static final String QUESTION_TITLES = "h2.question-title";
        private static final String QUESTION_LINKS = "div.view-more a";
    }
}
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
        Single<Integer> idFlowable = Single.just(j.getInt("id"));
        Single<String> titleFlowable = Single.just(j.getString("title"));
        Single<String> thumbnailUrlFlowable =
                Single.just(j)
                        .filter(x -> x.has("images"))
                        .map(x -> x.getJSONArray("images"))
                        .map(x -> (String) x.get(0))
                        .switchIfEmpty(Single.just(""));

        return Single.zip(idFlowable, titleFlowable, thumbnailUrlFlowable, (id, title, thumbnailUrl) ->
                Story.newBuilder()
                        .setId(id)
                        .setTitle(title)
                        .setThumbnailUrl(thumbnailUrl)
                        .build()
        ).toFlowable();
    }

    public static Flowable<Document> documents(InputStream in) throws JSONException {
        return Flowable.just(new JSONObject(mkString(in)))
                .filter(j -> j.has("body"))
                .map(j -> Jsoup.parse(j.getString("body")));
    }

    public static Flowable<Story> stories(InputStream in) {
        return Flowable.just(mkString(in))
                .map(JSONObject::new)
                .map(j -> j.getJSONArray("stories"))
                .flatMap(j -> Flowable.fromArray(toArray(j)))
                .flatMap(ZhihuDailyOfficial::convertToStory);
    }

    private static Optional<ZhihuDailyPurify.News> convertToNews(Pair<Story, Document> pair, String date) {
        return Optional.of(getQuestions(pair))
                .filter(qs -> qs.size() > 0)
                .map(qs ->
                        ZhihuDailyPurify.News
                                .newBuilder()
                                .setDate(date)
                                .setTitle(pair.first.getTitle())
                                .setThumbnailUrl(pair.first.getThumbnailUrl())
                                .addAllQuestions(qs)
                                .build());
    }

    private static List<ZhihuDailyPurify.Question> getQuestions(Pair<Story, Document> pair) {
        String dailyTitle = pair.first.getTitle();
        Document document = pair.second;

        return getQuestionElements(document).stream()
                .map(questionElement ->
                        ZhihuDailyPurify.Question
                                .newBuilder()
                                .setTitle(obtainQuestionTitle(questionElement).orElse(dailyTitle))
                                .setUrl(obtainQuestionUrl(questionElement).orElse(""))
                                .build())
                .filter(ZhihuDailyOfficial::isValidZhihuQuestion)
                .collect(Collectors.toList());
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

    @SuppressWarnings("CheckReturnValue")
    public static Single<ZhihuDailyPurify.Feed> feedForDate(String date) throws IOException {
        Flowable<Story> stories = stories(Network.openInputStream(Url.ZHIHU_DAILY_NEWS_BEFORE + date));
        Flowable<Document> documents =
                stories
                        .map(s -> Network.openInputStream(Url.ZHIHU_DAILY_NEWS_BASE + s.getId()))
                        .flatMap(ZhihuDailyOfficial::documents);

        ZhihuDailyPurify.Feed.Builder builder = ZhihuDailyPurify.Feed.newBuilder();

        Flowable
                .zip(stories, documents, Pair::new)
                .filter(pair -> pair.second != null)
                .map(pair -> convertToNews(pair, date))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(builder::addNews);

        return Single.just(builder.build());
    }

    private static class Url {
        private static final String ZHIHU_DAILY_NEWS_BASE = "https://news-at.zhihu.com/api/4/news/";
        private static final String ZHIHU_DAILY_NEWS_BEFORE = ZHIHU_DAILY_NEWS_BASE + "before/";
    }

    private static class Selectors {
        private static final String QUESTION = "div.question";
        private static final String QUESTION_TITLES = "h2.question-title";
        private static final String QUESTION_LINKS = "div.view-more a";
    }
}
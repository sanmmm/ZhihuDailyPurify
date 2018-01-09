import requests
import json

from bs4 import BeautifulSoup

from proto.zhihu_daily_purify_pb2 import News, Question, Feed

ZHIHU_DAILY_URL = 'https://news-at.zhihu.com/api/4/news/'
ZHIHU_DAILY_BEFORE_URL = ZHIHU_DAILY_URL + 'before/'


def stories_for_date(date):
    stories_json = _json_from_url(ZHIHU_DAILY_BEFORE_URL + date)["stories"]
    return list(map(lambda s: _Story(s["id"], s["title"], s.get("images", [""])[0]), stories_json))


def document_for_story(story):
    return BeautifulSoup(
        _json_from_url(ZHIHU_DAILY_URL + str(story.story_id)).get("body", ""),
        "html.parser"
    )


def feed_for_date(date):
    stories = stories_for_date(date)
    documents = map(document_for_story, stories)
    pairs = list(zip(stories, documents))

    feed = Feed()
    feed.news.extend(
        filter(lambda news: len(news.questions) > 0, [_to_news(pair, date) for pair in pairs])
    )

    return feed


def _json_from_url(url):
    return json.loads(requests.get(url, headers={"User-Agent": "Mozilla/5.0"}).text)


def _to_news(pair, date):
    story, document = pair
    question_elements = _get_question_elements(document)

    news = News()

    news.date = date
    news.title = story.title
    news.thumbnailUrl = story.thumbnail_url
    news.questions.extend(
        filter(
            lambda q: _is_question_url_valid(q.url),
            [_to_question(question_element) for question_element in question_elements]
        )
    )

    for question in news.questions:
        question.title = question.title or story.title

    return news


def _to_question(question_element):
    question = Question()

    question_title_element = _get_question_title_element(question_element)
    if question_title_element:
        question.title = question_title_element.get_text()

    question.url = _get_question_url(question_element)

    return question


def _is_question_url_valid(question_url):
    return 'zhihu.com/question/' in question_url


def _get_question_elements(document):
    return document.find_all('div', attrs={'class': 'question'})


def _get_question_title_element(question_element):
    return question_element.find('h2', attrs={'class': 'question-title'})


def _get_question_url(question_element):
    return (question_element.select_one('div.view-more a') or {}).get('href', '')


class _Story(object):
    def __init__(self, story_id, title, thumbnail_url):
        super(_Story, self).__init__()
        self.story_id = story_id
        self.title = title
        self.thumbnail_url = thumbnail_url

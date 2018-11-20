import json

from six.moves.urllib import request
from bs4 import BeautifulSoup

from proto.zhihu_daily_purify_pb2 import News, Question, Feed

ZHIHU_DAILY_URL = 'https://news-at.zhihu.com/api/4/news/'
ZHIHU_DAILY_BEFORE_URL = ZHIHU_DAILY_URL + 'before/'


class Story(object):
    def __init__(self, pair):
        metadata, document = pair
        self.metadata = metadata
        self.document = document

    @staticmethod
    def of(date):
        metadata = Story.Metadata.from_json(_http_get(ZHIHU_DAILY_BEFORE_URL + date))
        documents = map(Story._document_for, metadata)

        return [Story(p) for p in zip(metadata, documents)]

    @staticmethod
    def _document_for(metadata):
        json_content = _json_from_url(ZHIHU_DAILY_URL + str(metadata.story_id))
        body = json_content.get('body', '')

        return _soup(body)

    class Metadata(object):
        def __init__(self, story_id, title, thumbnail_url):
            self.story_id = story_id
            self.title = title
            self.thumbnail_url = thumbnail_url

        @staticmethod
        def from_json(json_content):
            from_zhihu = _to_json(json_content).get('stories', [])

            return map(Story.Metadata._convert, from_zhihu)

        @staticmethod
        def _convert(story_json):
            story_id = story_json.get('id', 0)
            title = story_json.get('title', '')

            thumbnails = story_json.get('images', [])
            thumbnail_url = (thumbnails or [''])[0]

            return Story.Metadata(story_id, title, thumbnail_url)


class ZhihuDailyOfficial(object):
    def __init__(self, date):
        self.date = date

    def feed(self):
        news = [self.to_news(story) for story in Story.of(self.date)]

        feed = Feed()
        feed.news.extend([n for n in news if n])

        return feed

    def to_news(self, story):
        questions = ZhihuDailyOfficial._questions(story)

        news = News()

        news.date = self.date
        news.title = story.metadata.title
        news.thumbnailUrl = story.metadata.thumbnail_url
        news.questions.extend([q for q in questions if q])

        return news if news.questions else None

    @staticmethod
    def _questions(story):
        elements = _get_question_elements(story.document)

        return [ZhihuDailyOfficial._to_question(e, story) for e in elements]

    @staticmethod
    def _to_question(question_element, story):
        question = Question()

        question.title = _question_title(question_element) or story.metadata.title
        question.url = _question_url(question_element)

        url_valid = ZhihuDailyOfficial._is_question_url_valid(question.url)

        return question if url_valid else None

    @staticmethod
    def _is_question_url_valid(question_url):
        return 'zhihu.com/question/' in question_url


def _to_json(content):
    try:
        return json.loads(content)
    except ValueError:
        return {}


def _http_get(url):
    req = request.Request(url)
    req.add_header('User-Agent', 'Mozilla/5.0')
    return request.urlopen(req).read()


def _soup(ingridients):
    return BeautifulSoup(ingridients, 'html.parser')


def _json_from_url(url):
    return _to_json(_http_get(url))


def _get_question_elements(document):
    return document.select('div.question')


def _question_title(element):
    return _question_title_element(element).get_text()


def _question_title_element(element):
    return element.select_one('h2.question-title') or _soup('')


def _question_url(element):
    return _question_url_element(element).get('href', '')


def _question_url_element(element):
    return element.select_one('div.view-more a') or {}

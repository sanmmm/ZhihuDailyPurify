import json
import certifi

from six.moves.urllib import request
from bs4 import BeautifulSoup

from proto.zhihu_daily_purify_pb2 import News, Question, Feed

ZHIHU_DAILY_URL = 'https://news-at.zhihu.com/api/4/news/'
ZHIHU_DAILY_BEFORE_URL = ZHIHU_DAILY_URL + 'before/'


class Story(object):
    def __init__(self, story_id, title, thumbnail_url):
        super(Story, self).__init__()
        self.story_id = story_id
        self.title = title
        self.thumbnail_url = thumbnail_url

    @staticmethod
    def of(date):
        stories = Story.from_json(_http_get(ZHIHU_DAILY_BEFORE_URL + date))
        documents = map(Story._document, stories)

        return list(zip(stories, documents))

    @staticmethod
    def from_json(json_content):
        from_zhihu = _to_json(json_content).get('stories', [])

        return map(Story._convert, from_zhihu)

    @staticmethod
    def _convert(story_json):
        story_id = story_json.get('id', 0)
        story_title = story_json.get('title', '')
        thumbnail_url = (story_json.get('images', []) or [''])[0]

        return Story(story_id, story_title, thumbnail_url)

    def _document(self):
        json_content = _json_from_url(ZHIHU_DAILY_URL + str(self.story_id))
        body = json_content.get('body', '')

        return BeautifulSoup(body, 'html.parser')


class ZhihuDailyOfficial(object):
    def __init__(self, date):
        super(ZhihuDailyOfficial, self).__init__()
        self.date = date

    def feed(self):
        pairs = Story.of(self.date)
        news = [self.to_news(pair) for pair in pairs]

        feed = Feed()
        feed.news.extend([n for n in news if n])

        return feed

    def to_news(self, pair):
        story, document = pair
        questions = ZhihuDailyOfficial._questions(pair)

        news = News()

        news.date = self.date
        news.title = story.title
        news.thumbnailUrl = story.thumbnail_url
        news.questions.extend([q for q in questions if q])

        return news if news.questions else None

    @staticmethod
    def _questions(pair):
        story, document = pair
        elements = _get_question_elements(document)

        return [ZhihuDailyOfficial._to_question(e, story) for e in elements]

    @staticmethod
    def _to_question(question_element, story):
        question = Question()

        question.title = _question_title(question_element, story)
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
    return request.urlopen(req, cafile=certifi.where()).read()


def _json_from_url(url):
    return _to_json(_http_get(url))


def _get_question_elements(document):
    return document.find_all('div', attrs={'class': 'question'})


def _question_title(element, story):
    title_element = _question_title_element(element)
    from_html = (title_element or BeautifulSoup('', 'html.parser')).get_text()
    return from_html or story.title


def _question_title_element(element):
    return element.find('h2', attrs={'class': 'question-title'})


def _question_url(element):
    return (element.select_one('div.view-more a') or {}).get('href', '')

import requests
import json

from bs4 import BeautifulSoup

from proto.zhihu_daily_purify_pb2 import News, Question, Feed

ZHIHU_DAILY_URL = 'https://news-at.zhihu.com/api/4/news/'
ZHIHU_DAILY_BEFORE_URL = ZHIHU_DAILY_URL + 'before/'


def stories_for_date(date):
    return stories_from_json(_get_text(ZHIHU_DAILY_BEFORE_URL + date))


def document_for_story(story):
    return BeautifulSoup(
        _json_from_url(ZHIHU_DAILY_URL + str(story.story_id)).get('body', ''),
        'html.parser'
    )


def feed_for_date(date):
    stories = stories_for_date(date)
    documents = map(document_for_story, stories)
    pairs = list(zip(stories, documents))

    feed = Feed()
    feed.news.extend(
        filter(
            lambda n: len(n.questions) > 0,
            [to_news(p, date) for p in pairs]
        )
    )

    return feed


def _to_json(content):
    try:
        return json.loads(content)
    except ValueError:
        return {}


def _get_text(url):
    return requests.get(url, headers={'User-Agent': 'Mozilla/5.0'}).text


def _json_from_url(url):
    return _to_json(_get_text(url))


def stories_from_json(text):
    return list(map(lambda s: _to_story(s), _to_json(text).get('stories', [])))


def _to_story(story_json):
    story_id = story_json.get('id', 0)
    story_title = story_json.get('title', '')
    thumbnail_url = (story_json.get('images', []) or [''])[0]
    return Story(story_id, story_title, thumbnail_url)


def to_news(pair, date):
    story, document = pair
    elements = _get_question_elements(document)

    news = News()

    news.date = date
    news.title = story.title
    news.thumbnailUrl = story.thumbnail_url
    news.questions.extend(
        filter(
            lambda q: _is_question_url_valid(q.url),
            [_to_question(element, story) for element in elements]
        )
    )

    return news


def _to_question(question_element, story):
    question = Question()

    question.title = _question_title(question_element, story)
    question.url = _question_url(question_element)

    return question


def _is_question_url_valid(question_url):
    return 'zhihu.com/question/' in question_url


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


class Story(object):
    def __init__(self, story_id, title, thumbnail_url):
        super(Story, self).__init__()
        self.story_id = story_id
        self.title = title
        self.thumbnail_url = thumbnail_url

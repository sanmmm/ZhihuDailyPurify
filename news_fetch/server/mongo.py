from os import path

from six.moves import configparser
from pymongo import MongoClient, ASCENDING, DESCENDING

from proto.zhihu_daily_purify_pb2 import Feed, News, Question


def has_date_cached(date):
    return _news_collection().find({'date': date}).count() > 0


def feed_for_date(date):
    feed = Feed()
    feed.news.extend(_news_list_to_feed(_query_collection({'date': date})))

    return feed


def search(keyword):
    criteria = {
        "$or": [
            {"title": _criteria_keyword_ignore_case(keyword)},
            {"questions.title": _criteria_keyword_ignore_case(keyword)},
        ]
    }

    feed = Feed()
    feed.news.extend(_news_list_to_feed(_query_collection(criteria)))

    return feed


def _news_list_to_feed(news_list):
    return [_dict_to_news(news_dict) for news_dict in news_list]


def _dict_to_news(d):
    news = News()

    news.date = d['date']
    news.title = d['title']
    news.thumbnailUrl = d['thumbnailUrl']
    news.questions.extend([_dict_to_question(qd) for qd in d['questions']])

    return news


def _dict_to_question(d):
    question = Question()

    question.title = d['title']
    question.url = d['url']

    return question


def save_feed(feed):
    news_list = [_news_to_dict(i, n) for i, n in enumerate(feed.news)]
    _news_collection().insert_many(news_list)


def _news_to_dict(index, news):
    return {
        "index": index,
        "date": news.date,
        "title": news.title,
        "thumbnailUrl": news.thumbnailUrl,
        "questions": [_question_to_dict(q) for q in news.questions],
    }


def _question_to_dict(question):
    return {
        "title": question.title,
        "url": question.url,
    }


def _criteria_keyword_ignore_case(keyword):
    return {
        "$regex": ".*{}.*".format(keyword),
        "$options": "i",
    }


def _query_collection(criteria):
    collection = _news_collection()
    search_result = collection \
        .find(criteria, {'_id': False, 'index': False}) \
        .sort([('date', DESCENDING), ('index', ASCENDING)])
    return list(search_result)


def _news_collection():
    config = configparser.ConfigParser()

    bath_path = '/app' if path.exists('/.dockerenv') else '.'
    config.read(bath_path + '/news_fetch/server/database.ini')

    uri = config.get('Database', 'URI')
    user = config.get('Credential', 'User')
    password = config.get('Credential', 'Password')
    database = config.get('Database', 'Name')
    collection = config.get('Database', 'Collection')

    mongo_uri = 'mongodb://{}:{}@{}/{}'.format(user, password, uri, database)

    client = MongoClient(mongo_uri)
    db = client.get_default_database()

    return db[collection]

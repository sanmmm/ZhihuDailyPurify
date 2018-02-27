from os import environ

from tornado.web import RequestHandler, Application
from tornado.ioloop import IOLoop

from proto.zhihu_daily_purify_pb2 import Feed
from news_fetch.python.official import ZhihuDailyOfficial
from news_fetch.server import mongo
from news_fetch.server.datetimechina import DateTimeChina


class IndexHandler(RequestHandler):
    def get(self):
        self.write("Index")


class FeedHandler(RequestHandler):
    def get(self, date):
        self.write(_feed_of(date))


class SearchHandler(RequestHandler):
    def get(self):
        keyword = self.get_argument("q", default="")
        self.write(_search(keyword))


def _feed_of(date):
    dt = DateTimeChina.parse(date)

    if dt is None or dt.is_before_birthday():
        return _empty_feed(date).SerializeToString()

    if dt.is_after_current_date_in_china():
        date = DateTimeChina.current_date()

    if mongo.has_date_cached(date):
        feed = mongo.feed_for_date(date)
    else:
        feed = ZhihuDailyOfficial(date).feed()
        mongo.save_feed(feed)

    return feed.SerializeToString()


def _search(keyword):
    return mongo.search(keyword).SerializeToString()


def _empty_feed(date):
    feed = Feed()
    feed.date = date

    return feed


if __name__ == '__main__':
    app = Application([
        (r'/', IndexHandler),
        (r'/news/([0-9]{8})', FeedHandler),
        (r'/search', SearchHandler),
    ])

    app.listen(int(environ.get('PORT', 5000)), '0.0.0.0')
    IOLoop.current().start()

from os import environ

from bottle import route, run, request

from proto.zhihu_daily_purify_pb2 import Feed
from news_fetch.python.official import ZhihuDailyOfficial
from news_fetch.server import mongo
from news_fetch.server.datetimechina import DateTimeChina


@route('/')
def index():
    return 'Index'


@route('/news/<date>')
def _feed_of(date):
    dt = DateTimeChina.parse(date)
    bypass_cache = request.GET.get('bypass_cache', '') == 'true'

    if dt is None or dt.is_before_birthday():
        return Feed().SerializeToString()

    if dt.is_after_current_date_in_china():
        date = DateTimeChina.current_date()

    if bypass_cache:
        feed = ZhihuDailyOfficial(date).feed()
    elif mongo.has_date_cached(date):
        feed = mongo.feed_for_date(date)
    else:
        feed = ZhihuDailyOfficial(date).feed()
        mongo.save_feed(feed)

    return feed.SerializeToString()

@route('/search/')
def _search():
    keyword = request.GET.get('q', '')
    return mongo.search(keyword).SerializeToString()


if __name__ == '__main__':
    port = int(environ.get('PORT', 5000))
    run(host='0.0.0.0', port=port)

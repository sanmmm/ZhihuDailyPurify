import official
import mongo

from proto.zhihu_daily_purify_pb2 import Feed
from datetimechina import DateTimeChina


def feed_of_date(date):
    dt = DateTimeChina.parse(date)

    if dt is None or dt.is_before_birthday():
        return _empty_feed(date).SerializeToString()

    if dt.is_after_current_date_in_china():
        date = DateTimeChina.current_date()

    if mongo.has_date_cached(date):
        feed = mongo.feed_for_date(date)
    else:
        feed = official.feed_for_date(date)
        mongo.save_feed(feed)

    return feed.SerializeToString()


def search(keyword):
    return mongo.search(keyword).SerializeToString()


def _empty_feed(date):
    feed = Feed()
    feed.date = date

    return feed

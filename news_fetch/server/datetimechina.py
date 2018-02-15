from datetime import datetime
from pytz import timezone


class DateTimeChina(object):
    BIRTHDAY = datetime(2013, 5, 19)
    DATE_FORMAT = '%Y%m%d'

    def __init__(self, dt):
        super(DateTimeChina, self).__init__()
        self.dt = dt

    def is_before_birthday(self):
        return self.dt <= DateTimeChina._convert_to_time_in_china(DateTimeChina.BIRTHDAY)

    def is_after_current_date_in_china(self):
        return self.dt > DateTimeChina._current_time_in_china()

    @staticmethod
    def parse(date):
        try:
            d = datetime.strptime(date, DateTimeChina.DATE_FORMAT)
            return DateTimeChina(DateTimeChina._convert_to_time_in_china(d))
        except ValueError:
            return None

    @staticmethod
    def current_date_in_china():
        return datetime.strftime(DateTimeChina._current_time_in_china(), DateTimeChina.DATE_FORMAT)

    @staticmethod
    def _get_gmt_timezone():
        return timezone('GMT')

    @staticmethod
    def _get_china_timezone():
        return timezone('Asia/Shanghai')

    @staticmethod
    def _convert_to_time_in_china(date):
        return DateTimeChina._get_china_timezone().localize(date)

    @staticmethod
    def _current_time_in_china():
        shanghai = DateTimeChina._get_china_timezone()
        gmt = DateTimeChina._get_gmt_timezone()
        now_gmt = gmt.localize(datetime.now())
        return now_gmt.astimezone(shanghai)

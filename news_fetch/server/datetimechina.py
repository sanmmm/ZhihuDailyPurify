from datetime import datetime
from pytz import timezone


class DateTimeChina(object):
    BIRTHDAY = datetime(2013, 5, 19)
    FORMAT = '%Y%m%d'

    def __init__(self, dt):
        super(DateTimeChina, self).__init__()
        self.dt = dt

    def is_before_birthday(self):
        return self.dt <= DateTimeChina._convert(DateTimeChina.BIRTHDAY)

    def is_after_current_date_in_china(self):
        return self.dt > DateTimeChina._current()

    @staticmethod
    def parse(date):
        try:
            d = datetime.strptime(date, DateTimeChina.FORMAT)
            return DateTimeChina(DateTimeChina._convert(d))
        except ValueError:
            return None

    @staticmethod
    def current_date():
        return datetime.strftime(DateTimeChina._current(), DateTimeChina.FORMAT)

    @staticmethod
    def _get_gmt_timezone():
        return timezone('GMT')

    @staticmethod
    def _timezone_for_shanghai():
        return timezone('Asia/Shanghai')

    @staticmethod
    def _convert(date):
        return DateTimeChina._timezone_for_shanghai().localize(date)

    @staticmethod
    def _current():
        shanghai = DateTimeChina._timezone_for_shanghai()
        gmt = DateTimeChina._get_gmt_timezone()
        now_gmt = gmt.localize(datetime.now())
        return now_gmt.astimezone(shanghai)

from datetime import datetime, timedelta


class DateTimeChina(object):
    BIRTHDAY = datetime(2013, 5, 19)

    def __init__(self, dt):
        super(DateTimeChina, self).__init__()
        self.dt = dt

    def is_before_birthday(self):
        return self.dt <= DateTimeChina.BIRTHDAY

    def is_after_current_date_in_china(self):
        return self.dt > DateTimeChina._current()

    @staticmethod
    def parse(date):
        try:
            return DateTimeChina(datetime.strptime(date, '%Y%m%d'))
        except ValueError:
            return None

    @staticmethod
    def current_date():
        return '{:%Y%m%d}'.format(DateTimeChina._current())

    @staticmethod
    def _current():
        return datetime.utcnow() + timedelta(hours=8)

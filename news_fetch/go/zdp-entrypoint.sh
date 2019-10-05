#!/bin/sh
(/app/ZhihuDailyPurify &) && /usr/local/bin/envoy -c /app/envoy-heroku.yaml

exec "$@"

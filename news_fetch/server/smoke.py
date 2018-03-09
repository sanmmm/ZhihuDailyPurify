from six.moves.urllib import request

URL_BASE = 'https://zhihudailypurify.herokuapp.com'

"""
Get news
"""
news_request = request.urlopen(URL_BASE + '/news/20171122')
assert(news_request.getcode() == 200)

"""
Get news, but not from cache
"""
no_cache_news_request = request.urlopen(URL_BASE + '/news/20171122?bypass_cache=true')
assert(no_cache_news_request.getcode() == 200)

"""
Search
"""
search_request = request.urlopen(URL_BASE + '/search/?q=2017')
assert(search_request.getcode() == 200)

print('\nSmoke passed.\n')
from six.moves.urllib import request

URL_BASE = 'https://zhihudailypurify.herokuapp.com'

"""
Smoke index page
"""
index_page_request = request.urlopen(URL_BASE)
assert(index_page_request.getcode() == 200)

"""
Smoke news page
"""
news_request = request.urlopen(URL_BASE + '/news/20171122')
assert(news_request.getcode() == 200)

"""
Smoke news page, but force fetching from Zhihu Daily
"""
no_cache_news_request = request.urlopen(URL_BASE + '/news/20171122?bypass_cache=true')
assert(no_cache_news_request.getcode() == 200)

"""
Smoke search page.
"""
search_request = request.urlopen(URL_BASE + '/search/?q=2017')
assert(search_request.getcode() == 200)

print('\nSmoke passed.\n')
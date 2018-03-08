import six
import unittest

import io
import os
from bs4 import BeautifulSoup

from official import ZhihuDailyOfficial, Story

DATE = 'Date'
STORY = Story(1, 'Story Title', 'Thumbnail URL')
OFFICIAL = ZhihuDailyOfficial(DATE)


def read_file(file_name):
    prefix = os.environ['TEST_SRCDIR']
    test_files = 'ZhihuDailyPurify/news_fetch/test_files'
    file_path = '{}/{}/{}'.format(prefix, test_files, file_name)
    with io.open(file_path, encoding='utf-8') as f:
        return f.read()


def setup_document(file_name):
    return BeautifulSoup(read_file(file_name), 'html.parser')


def setup_pair(file_name):
    return STORY, setup_document(file_name)


class TestStories(unittest.TestCase):
    def test_error_response(self):
        content = read_file('json/error_stories.json')
        self.assertEqual(Story.from_json(content), [])

    def test_no_stories(self):
        content = read_file('json/no_stories.json')
        self.assertEqual(Story.from_json(content), [])

    def test_empty_stories(self):
        content = read_file('json/empty_stories.json')
        self.assertEqual(Story.from_json(content), [])

    def test_no_thumbnail_url(self):
        content = read_file('json/empty_images.json')
        stories = Story.from_json(content)
        self.assertEqual(stories[0].thumbnail_url, '')

    def test_multiple_thumbnail_urls(self):
        content = read_file('json/multiple_images.json')
        stories = Story.from_json(content)
        self.assertEqual(stories[0].thumbnail_url, 'should be selected')

    def test_normal_scenario(self):
        content = read_file('json/normal.json')
        stories = Story.from_json(content)
        self.assertEqual(len(stories), 2)

        first_story = stories[0]
        self.assertEqual(first_story.story_id, 1)
        self.assertEqual(first_story.title, 'first title')
        self.assertEqual(first_story.thumbnail_url, 'image url 1')

        second_story = stories[1]
        self.assertEqual(second_story.story_id, 2)
        self.assertEqual(second_story.title, 'second title')
        self.assertEqual(second_story.thumbnail_url, 'image url 2')


class TestToNews(unittest.TestCase):
    def test_no_questions(self):
        pair = setup_pair('html/no_questions.html')
        news = OFFICIAL.to_news(pair)
        self.assertEqual(news, None)

    def test_no_question_title(self):
        pair = setup_pair('html/no_title.html')
        news = OFFICIAL.to_news(pair)
        self.assertEqual(news.questions[0].title, STORY.title)

    def test_empty_question_title(self):
        pair = setup_pair('html/empty_question_title.html')
        news = OFFICIAL.to_news(pair)
        self.assertEqual(news.questions[0].title, STORY.title)

    def test_no_question_url(self):
        pair = setup_pair('html/no_question_url.html')
        news = OFFICIAL.to_news(pair)
        self.assertEqual(news, None)

    def test_invalid_question_url(self):
        pair = setup_pair('html/invalid_question_url.html')
        news = OFFICIAL.to_news(pair)
        self.assertEqual(news, None)

    def test_normal_scenario(self):
        pair = setup_pair('html/normal.html')
        news = OFFICIAL.to_news(pair)
        self.assertEqual(len(news.questions), 2)

        first_question = news.questions[0]
        self.assertEqual(first_question.title, 'First')
        self.assertTrue(first_question.url.endswith('1234567'))

        second_question = news.questions[1]
        self.assertEqual(second_question.title, 'Second')
        self.assertTrue(second_question.url.endswith('2345678'))


if __name__ == '__main__':
    unittest.main()

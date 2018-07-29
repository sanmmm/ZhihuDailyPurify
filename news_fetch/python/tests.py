import six
import unittest

import io
import os
from bs4 import BeautifulSoup

from official import ZhihuDailyOfficial, Story

OFFICIAL = ZhihuDailyOfficial('Date')


def _read_file(file_name):
    prefix = os.environ['TEST_SRCDIR']
    test_files = 'ZhihuDailyPurify/news_fetch/test_files'
    file_path = '{}/{}/{}'.format(prefix, test_files, file_name)
    with io.open(file_path, encoding='utf-8') as f:
        return f.read()


def _setup_document(file_name):
    return BeautifulSoup(_read_file(file_name), 'html.parser')


def _setup_story(file_name):
    metadata = Story.Metadata(1, 'Story Title', 'Thumbnail URL')
    document = _setup_document(file_name)
    return Story((metadata, document))


class TestStories(unittest.TestCase):
    def test_error_response(self):
        content = _read_file('json/error_stories.json')
        self.assertEqual(Story.Metadata.from_json(content), [])

    def test_no_stories(self):
        content = _read_file('json/no_stories.json')
        self.assertEqual(Story.Metadata.from_json(content), [])

    def test_empty_stories(self):
        content = _read_file('json/empty_stories.json')
        self.assertEqual(Story.Metadata.from_json(content), [])

    def test_no_thumbnail_url(self):
        content = _read_file('json/empty_images.json')
        metadata = Story.Metadata.from_json(content)
        self.assertEqual(metadata[0].thumbnail_url, '')

    def test_multiple_thumbnail_urls(self):
        content = _read_file('json/multiple_images.json')
        metadata = Story.Metadata.from_json(content)
        self.assertEqual(metadata[0].thumbnail_url, 'should be selected')

    def test_normal_scenario(self):
        content = _read_file('json/normal.json')
        metadata = Story.Metadata.from_json(content)
        self.assertEqual(len(metadata), 2)

        first = metadata[0]
        self.assertEqual(first.story_id, 1)
        self.assertEqual(first.title, 'first title')
        self.assertEqual(first.thumbnail_url, 'image url 1')

        second = metadata[1]
        self.assertEqual(second.story_id, 2)
        self.assertEqual(second.title, 'second title')
        self.assertEqual(second.thumbnail_url, 'image url 2')


class TestToNews(unittest.TestCase):
    def test_no_questions(self):
        story = _setup_story('html/no_questions.html')
        news = OFFICIAL.to_news(story)
        self.assertEqual(news, None)

    def test_no_question_title(self):
        story = _setup_story('html/no_title.html')
        news = OFFICIAL.to_news(story)
        self.assertEqual(news.questions[0].title, story.metadata.title)

    def test_empty_question_title(self):
        story = _setup_story('html/empty_question_title.html')
        news = OFFICIAL.to_news(story)
        self.assertEqual(news.questions[0].title, story.metadata.title)

    def test_no_question_url(self):
        story = _setup_story('html/no_question_url.html')
        news = OFFICIAL.to_news(story)
        self.assertEqual(news, None)

    def test_invalid_question_url(self):
        story = _setup_story('html/invalid_question_url.html')
        news = OFFICIAL.to_news(story)
        self.assertEqual(news, None)

    def test_normal_scenario(self):
        story = _setup_story('html/normal.html')
        news = OFFICIAL.to_news(story)
        self.assertEqual(len(news.questions), 2)

        first_question = news.questions[0]
        self.assertEqual(first_question.title, 'First')
        self.assertTrue(first_question.url.endswith('1234567'))

        second_question = news.questions[1]
        self.assertEqual(second_question.title, 'Second')
        self.assertTrue(second_question.url.endswith('2345678'))


if __name__ == '__main__':
    unittest.main()

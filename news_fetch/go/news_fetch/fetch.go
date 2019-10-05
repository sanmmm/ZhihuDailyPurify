package news_fetch

import (
	"encoding/json"
	"errors"
	"github.com/PuerkitoBio/goquery"
	zdp "github.com/izzyleung/ZhihuDailyPurify/proto"
	"golang.org/x/net/html"
	"io/ioutil"
	"net/http"
	"net/url"
	"sort"
	"strconv"
	"strings"
	"sync"
)

const (
	zhihuDailyNewsApiUrl = "https://news-at.zhihu.com/api/4/news/"
	zhihuDailyApiUrl     = zhihuDailyNewsApiUrl + "before/"
)

type zResponse struct {
	Date    string   `json:"date"`
	Stories []zStory `json:"stories"`
}

type zStory struct {
	Title  string   `json:"title"`
	Id     int64    `json:"id"`
	Images []string `json:"images"`
}

type zStoryDetail struct {
	Body string `json:"body"`
}

type zNews struct {
	News *zdp.News
	id   int8
}

func GetNewsFeed(date string) (*zdp.Feed, error) {
	zResp, err := requestStoriesResponse(date)
	if err != nil {
		return nil, err
	}

	feed := zdp.Feed{}
	ch := make(chan *zNews, len(zResp.Stories))
	var wg sync.WaitGroup

	for index, story := range zResp.Stories {
		wg.Add(1)

		go func(i int, s zStory) {
			news, err := s.toNews()
			if err == nil {
				news.Date = date
				ch <- &zNews{
					News: news,
					id:   int8(i),
				}
			}
			wg.Done()
		}(index, story)
	}

	wg.Wait()
	close(ch)
	var zNewsZ []*zNews

	for news := range ch {
		zNewsZ = append(zNewsZ, news)
	}

	sort.Slice(zNewsZ, func(i, j int) bool {
		return zNewsZ[i].id - zNewsZ[j].id < 0
	})

	for _, zNews := range zNewsZ {
		feed.News = append(feed.News, zNews.News)
	}

	return &feed, nil
}

func requestStoriesResponse(date string) (*zResponse, error) {
	resp, err := httpGet(zhihuDailyApiUrl + date)
	if err != nil {
		return nil, err
	}

	var zResp zResponse
	err = json.Unmarshal(resp, &zResp)
	if err != nil {
		return nil, err
	}

	return &zResp, nil
}

func (s zStory) toNews() (*zdp.News, error) {
	resp, err := httpGet(zhihuDailyNewsApiUrl + strconv.FormatInt(s.Id, 10))
	if err != nil {
		return nil, err
	}

	var detail zStoryDetail
	err = json.Unmarshal(resp, &detail)
	if err != nil {
		return nil, err
	}

	doc, err := goquery.NewDocumentFromReader(strings.NewReader(detail.Body))
	if err != nil {
		return nil, err
	}

	questionElements := doc.Find("div.question")
	if len(questionElements.Nodes) == 0 {
		return nil, errors.New("no Zhihu questions found in story details")
	}

	result := zdp.News{Title: s.Title}
	if len(s.Images) > 0 {
		result.ThumbnailUrl = s.Images[0]
	}

	for _, questionNode := range questionElements.Nodes {
		question, err := findQuestion(questionNode)
		if err != nil {
			continue
		}

		if len(question.Title) == 0 {
			question.Title = s.Title
		}

		result.Questions = append(result.Questions, question)
	}

	if len(result.Questions) == 0 {
		return nil, errors.New("no Zhihu questions found in story details")
	}

	return &result, nil
}

func findQuestion(node *html.Node) (*zdp.Question, error) {
	element := goquery.NewDocumentFromNode(node)
	title := element.Find("h2.question-title").Text()

	link, found := element.Find("div.view-more a").Attr("href")
	if !found {
		return nil, errors.New("html element contains no link")
	}

	questionLink, err := url.Parse(link)
	if err != nil {
		return nil, err
	}
	if !strings.Contains(questionLink.Host, "zhihu.com") {
		return nil, errors.New("not a link to zhihu.com")
	}
	if !strings.HasPrefix(questionLink.Path, "/question/") {
		return nil, errors.New("not a link to zhihu question")
	}

	return &zdp.Question{
		Title: title,
		Url:   link,
	}, nil
}

func httpGet(url string) ([]byte, error) {
	resp, err := http.Get(url)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return nil, err
	}

	return body, nil
}

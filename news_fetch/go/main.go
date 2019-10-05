//go:generate protoc -I proto/ --go_out=plugins=grpc:proto/ proto/zdp.proto
//go:generate protoc -I proto/ --include_imports --descriptor_set_out=proto/zdp_descriptor.pb proto/zdp.proto

package main

import (
	"context"
	"fmt"
	"github.com/izzyleung/ZhihuDailyPurify/news_fetch"
	"github.com/izzyleung/ZhihuDailyPurify/proto"
	"google.golang.org/grpc"
	"net"
	"os"
)

type ZhihuDailPurifyServer struct{}

func (s *ZhihuDailPurifyServer) GetNews(ctx context.Context, req *generated.GetNewsRequest) (*generated.Feed, error) {
	fmt.Println("Received get news request: ", req)

	return news_fetch.GetNewsFeed(req.Date)
}

func (s *ZhihuDailPurifyServer) SearchNews(ctx context.Context, req *generated.SearchNewsRequest) (*generated.Feed, error) {
	fmt.Println("Received search news request: ", req)

	question := &generated.Question{
		Title: "Question",
		Url:   "https://zhihu.com/question/123",
	}

	news := &generated.News{
		Date:         "20191002",
		Title:        "SearchNewsTitle",
		ThumbnailUrl: "http://fake.com/not_real.jpg",
		Questions:    []*generated.Question{question},
	}

	return &generated.Feed{
		News: []*generated.News{news},
	}, nil
}

func main() {
	listener, err := net.Listen("tcp", ":10001")
	if err != nil {
		fmt.Sprintln("Failed to listen: ", err)
		os.Exit(1)
	}

	server := grpc.NewServer()
	generated.RegisterZhihuDailyPurifyServiceServer(server, &ZhihuDailPurifyServer{})

	if err := server.Serve(listener); err != nil {
		fmt.Println("Failed to serve: ", err)
		os.Exit(1)
	}
}

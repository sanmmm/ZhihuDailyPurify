FROM golang:1.13.1 AS build

WORKDIR /go/src/app
COPY . .

RUN PROTOBUF_VERSION=3.10.0 \
    && wget \
        "https://repo1.maven.org/maven2/com/google/protobuf/protoc/$PROTOBUF_VERSION/protoc-$PROTOBUF_VERSION-linux-$(uname -m).exe" \
        -O /usr/bin/protoc \
    && chmod +x /usr/bin/protoc \
    && go get github.com/golang/protobuf/protoc-gen-go \
    && go generate \
    && go build

FROM envoyproxy/envoy-alpine:v1.11.1

COPY --from=build /go/src/app/ZhihuDailyPurify /app/
COPY --from=build /go/src/app/proto/zdp_descriptor.pb /app/
COPY envoy-heroku.yaml /app
COPY zdp-entrypoint.sh /

ENTRYPOINT ["/zdp-entrypoint.sh"]

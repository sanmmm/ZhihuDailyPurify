// Code generated by protoc-gen-go. DO NOT EDIT.
// source: zdp.proto

package generated

import (
	context "context"
	fmt "fmt"
	proto "github.com/golang/protobuf/proto"
	_ "google.golang.org/genproto/googleapis/api/annotations"
	grpc "google.golang.org/grpc"
	codes "google.golang.org/grpc/codes"
	status "google.golang.org/grpc/status"
	math "math"
)

// Reference imports to suppress errors if they are not otherwise used.
var _ = proto.Marshal
var _ = fmt.Errorf
var _ = math.Inf

// This is a compile-time assertion to ensure that this generated file
// is compatible with the proto package it is being compiled against.
// A compilation error at this line likely means your copy of the
// proto package needs to be updated.
const _ = proto.ProtoPackageIsVersion3 // please upgrade the proto package

type GetNewsRequest struct {
	Date                 string   `protobuf:"bytes,1,opt,name=date,proto3" json:"date,omitempty"`
	XXX_NoUnkeyedLiteral struct{} `json:"-"`
	XXX_unrecognized     []byte   `json:"-"`
	XXX_sizecache        int32    `json:"-"`
}

func (m *GetNewsRequest) Reset()         { *m = GetNewsRequest{} }
func (m *GetNewsRequest) String() string { return proto.CompactTextString(m) }
func (*GetNewsRequest) ProtoMessage()    {}
func (*GetNewsRequest) Descriptor() ([]byte, []int) {
	return fileDescriptor_e5ae3dd70a7dd17a, []int{0}
}

func (m *GetNewsRequest) XXX_Unmarshal(b []byte) error {
	return xxx_messageInfo_GetNewsRequest.Unmarshal(m, b)
}
func (m *GetNewsRequest) XXX_Marshal(b []byte, deterministic bool) ([]byte, error) {
	return xxx_messageInfo_GetNewsRequest.Marshal(b, m, deterministic)
}
func (m *GetNewsRequest) XXX_Merge(src proto.Message) {
	xxx_messageInfo_GetNewsRequest.Merge(m, src)
}
func (m *GetNewsRequest) XXX_Size() int {
	return xxx_messageInfo_GetNewsRequest.Size(m)
}
func (m *GetNewsRequest) XXX_DiscardUnknown() {
	xxx_messageInfo_GetNewsRequest.DiscardUnknown(m)
}

var xxx_messageInfo_GetNewsRequest proto.InternalMessageInfo

func (m *GetNewsRequest) GetDate() string {
	if m != nil {
		return m.Date
	}
	return ""
}

type SearchNewsRequest struct {
	Query                string   `protobuf:"bytes,1,opt,name=query,proto3" json:"query,omitempty"`
	XXX_NoUnkeyedLiteral struct{} `json:"-"`
	XXX_unrecognized     []byte   `json:"-"`
	XXX_sizecache        int32    `json:"-"`
}

func (m *SearchNewsRequest) Reset()         { *m = SearchNewsRequest{} }
func (m *SearchNewsRequest) String() string { return proto.CompactTextString(m) }
func (*SearchNewsRequest) ProtoMessage()    {}
func (*SearchNewsRequest) Descriptor() ([]byte, []int) {
	return fileDescriptor_e5ae3dd70a7dd17a, []int{1}
}

func (m *SearchNewsRequest) XXX_Unmarshal(b []byte) error {
	return xxx_messageInfo_SearchNewsRequest.Unmarshal(m, b)
}
func (m *SearchNewsRequest) XXX_Marshal(b []byte, deterministic bool) ([]byte, error) {
	return xxx_messageInfo_SearchNewsRequest.Marshal(b, m, deterministic)
}
func (m *SearchNewsRequest) XXX_Merge(src proto.Message) {
	xxx_messageInfo_SearchNewsRequest.Merge(m, src)
}
func (m *SearchNewsRequest) XXX_Size() int {
	return xxx_messageInfo_SearchNewsRequest.Size(m)
}
func (m *SearchNewsRequest) XXX_DiscardUnknown() {
	xxx_messageInfo_SearchNewsRequest.DiscardUnknown(m)
}

var xxx_messageInfo_SearchNewsRequest proto.InternalMessageInfo

func (m *SearchNewsRequest) GetQuery() string {
	if m != nil {
		return m.Query
	}
	return ""
}

type Question struct {
	Title                string   `protobuf:"bytes,1,opt,name=title,proto3" json:"title,omitempty"`
	Url                  string   `protobuf:"bytes,2,opt,name=url,proto3" json:"url,omitempty"`
	XXX_NoUnkeyedLiteral struct{} `json:"-"`
	XXX_unrecognized     []byte   `json:"-"`
	XXX_sizecache        int32    `json:"-"`
}

func (m *Question) Reset()         { *m = Question{} }
func (m *Question) String() string { return proto.CompactTextString(m) }
func (*Question) ProtoMessage()    {}
func (*Question) Descriptor() ([]byte, []int) {
	return fileDescriptor_e5ae3dd70a7dd17a, []int{2}
}

func (m *Question) XXX_Unmarshal(b []byte) error {
	return xxx_messageInfo_Question.Unmarshal(m, b)
}
func (m *Question) XXX_Marshal(b []byte, deterministic bool) ([]byte, error) {
	return xxx_messageInfo_Question.Marshal(b, m, deterministic)
}
func (m *Question) XXX_Merge(src proto.Message) {
	xxx_messageInfo_Question.Merge(m, src)
}
func (m *Question) XXX_Size() int {
	return xxx_messageInfo_Question.Size(m)
}
func (m *Question) XXX_DiscardUnknown() {
	xxx_messageInfo_Question.DiscardUnknown(m)
}

var xxx_messageInfo_Question proto.InternalMessageInfo

func (m *Question) GetTitle() string {
	if m != nil {
		return m.Title
	}
	return ""
}

func (m *Question) GetUrl() string {
	if m != nil {
		return m.Url
	}
	return ""
}

type News struct {
	Date                 string      `protobuf:"bytes,1,opt,name=date,proto3" json:"date,omitempty"`
	Title                string      `protobuf:"bytes,2,opt,name=title,proto3" json:"title,omitempty"`
	ThumbnailUrl         string      `protobuf:"bytes,3,opt,name=thumbnailUrl,proto3" json:"thumbnailUrl,omitempty"`
	Questions            []*Question `protobuf:"bytes,4,rep,name=questions,proto3" json:"questions,omitempty"`
	XXX_NoUnkeyedLiteral struct{}    `json:"-"`
	XXX_unrecognized     []byte      `json:"-"`
	XXX_sizecache        int32       `json:"-"`
}

func (m *News) Reset()         { *m = News{} }
func (m *News) String() string { return proto.CompactTextString(m) }
func (*News) ProtoMessage()    {}
func (*News) Descriptor() ([]byte, []int) {
	return fileDescriptor_e5ae3dd70a7dd17a, []int{3}
}

func (m *News) XXX_Unmarshal(b []byte) error {
	return xxx_messageInfo_News.Unmarshal(m, b)
}
func (m *News) XXX_Marshal(b []byte, deterministic bool) ([]byte, error) {
	return xxx_messageInfo_News.Marshal(b, m, deterministic)
}
func (m *News) XXX_Merge(src proto.Message) {
	xxx_messageInfo_News.Merge(m, src)
}
func (m *News) XXX_Size() int {
	return xxx_messageInfo_News.Size(m)
}
func (m *News) XXX_DiscardUnknown() {
	xxx_messageInfo_News.DiscardUnknown(m)
}

var xxx_messageInfo_News proto.InternalMessageInfo

func (m *News) GetDate() string {
	if m != nil {
		return m.Date
	}
	return ""
}

func (m *News) GetTitle() string {
	if m != nil {
		return m.Title
	}
	return ""
}

func (m *News) GetThumbnailUrl() string {
	if m != nil {
		return m.ThumbnailUrl
	}
	return ""
}

func (m *News) GetQuestions() []*Question {
	if m != nil {
		return m.Questions
	}
	return nil
}

type Feed struct {
	News                 []*News  `protobuf:"bytes,1,rep,name=news,proto3" json:"news,omitempty"`
	XXX_NoUnkeyedLiteral struct{} `json:"-"`
	XXX_unrecognized     []byte   `json:"-"`
	XXX_sizecache        int32    `json:"-"`
}

func (m *Feed) Reset()         { *m = Feed{} }
func (m *Feed) String() string { return proto.CompactTextString(m) }
func (*Feed) ProtoMessage()    {}
func (*Feed) Descriptor() ([]byte, []int) {
	return fileDescriptor_e5ae3dd70a7dd17a, []int{4}
}

func (m *Feed) XXX_Unmarshal(b []byte) error {
	return xxx_messageInfo_Feed.Unmarshal(m, b)
}
func (m *Feed) XXX_Marshal(b []byte, deterministic bool) ([]byte, error) {
	return xxx_messageInfo_Feed.Marshal(b, m, deterministic)
}
func (m *Feed) XXX_Merge(src proto.Message) {
	xxx_messageInfo_Feed.Merge(m, src)
}
func (m *Feed) XXX_Size() int {
	return xxx_messageInfo_Feed.Size(m)
}
func (m *Feed) XXX_DiscardUnknown() {
	xxx_messageInfo_Feed.DiscardUnknown(m)
}

var xxx_messageInfo_Feed proto.InternalMessageInfo

func (m *Feed) GetNews() []*News {
	if m != nil {
		return m.News
	}
	return nil
}

func init() {
	proto.RegisterType((*GetNewsRequest)(nil), "io.github.izzyleung.GetNewsRequest")
	proto.RegisterType((*SearchNewsRequest)(nil), "io.github.izzyleung.SearchNewsRequest")
	proto.RegisterType((*Question)(nil), "io.github.izzyleung.Question")
	proto.RegisterType((*News)(nil), "io.github.izzyleung.News")
	proto.RegisterType((*Feed)(nil), "io.github.izzyleung.Feed")
}

func init() { proto.RegisterFile("zdp.proto", fileDescriptor_e5ae3dd70a7dd17a) }

var fileDescriptor_e5ae3dd70a7dd17a = []byte{
	// 355 bytes of a gzipped FileDescriptorProto
	0x1f, 0x8b, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0xff, 0x8c, 0x52, 0xcf, 0x4a, 0xf3, 0x40,
	0x10, 0x27, 0x6d, 0xbe, 0x4f, 0x33, 0x2d, 0x45, 0xd7, 0x82, 0xb1, 0x28, 0x94, 0x28, 0x52, 0x0f,
	0x26, 0x50, 0xf1, 0xe4, 0x4d, 0x44, 0x6f, 0xa2, 0x2d, 0x5e, 0x7a, 0x29, 0xdb, 0x66, 0x4c, 0x16,
	0xe2, 0x6e, 0xbb, 0xd9, 0xb5, 0xa4, 0xe2, 0xc5, 0x37, 0x10, 0x1f, 0xcd, 0x27, 0x10, 0x7c, 0x10,
	0xc9, 0xb6, 0xa5, 0x2d, 0xe6, 0xe0, 0x6d, 0x67, 0xe6, 0xf7, 0x67, 0xf8, 0xcd, 0x82, 0x33, 0x0d,
	0x47, 0xfe, 0x48, 0x0a, 0x25, 0xc8, 0x0e, 0x13, 0x7e, 0xc4, 0x54, 0xac, 0x07, 0x3e, 0x9b, 0x4e,
	0xb3, 0x04, 0x35, 0x8f, 0x1a, 0xfb, 0x91, 0x10, 0x51, 0x82, 0x01, 0x1d, 0xb1, 0x80, 0x72, 0x2e,
	0x14, 0x55, 0x4c, 0xf0, 0x74, 0x46, 0xf1, 0x8e, 0xa0, 0x76, 0x83, 0xea, 0x16, 0x27, 0x69, 0x07,
	0xc7, 0x1a, 0x53, 0x45, 0x08, 0xd8, 0x21, 0x55, 0xe8, 0x5a, 0x4d, 0xab, 0xe5, 0x74, 0xcc, 0xdb,
	0x3b, 0x81, 0xed, 0x2e, 0x52, 0x39, 0x8c, 0x57, 0x81, 0x75, 0xf8, 0x37, 0xd6, 0x28, 0xb3, 0x39,
	0x72, 0x56, 0x78, 0x6d, 0xd8, 0xbc, 0xcf, 0xc7, 0x4c, 0xf0, 0x1c, 0xa1, 0x98, 0x4a, 0x16, 0x5a,
	0xb3, 0x82, 0x6c, 0x41, 0x59, 0xcb, 0xc4, 0x2d, 0x99, 0x5e, 0xfe, 0xf4, 0xde, 0x2d, 0xb0, 0x73,
	0xe5, 0x22, 0xef, 0xa5, 0x48, 0x69, 0x55, 0xc4, 0x83, 0xaa, 0x8a, 0xf5, 0xd3, 0x80, 0x53, 0x96,
	0x3c, 0xc8, 0xc4, 0x2d, 0x9b, 0xe1, 0x5a, 0x8f, 0x5c, 0x80, 0x33, 0x9e, 0xaf, 0x92, 0xba, 0x76,
	0xb3, 0xdc, 0xaa, 0xb4, 0x0f, 0xfc, 0x82, 0x88, 0xfc, 0xc5, 0xc2, 0x9d, 0x25, 0xde, 0x3b, 0x07,
	0xfb, 0x1a, 0x31, 0x24, 0xa7, 0x60, 0x73, 0x9c, 0xa4, 0xae, 0x65, 0xf8, 0x7b, 0x85, 0x7c, 0x93,
	0x8a, 0x81, 0xb5, 0xbf, 0x2c, 0xd8, 0xed, 0xc5, 0x2c, 0xd6, 0x57, 0x94, 0x25, 0xd9, 0x9d, 0x96,
	0xec, 0x31, 0xeb, 0xa2, 0x7c, 0x66, 0x43, 0x24, 0x7d, 0xd8, 0x98, 0x67, 0x4d, 0x0e, 0x0b, 0x75,
	0xd6, 0x2f, 0xd1, 0x28, 0x36, 0xcb, 0xb7, 0xf2, 0xea, 0x6f, 0x9f, 0xdf, 0x1f, 0xa5, 0x1a, 0xa9,
	0x06, 0xb9, 0x6b, 0xf0, 0x92, 0x27, 0xf5, 0x4a, 0x10, 0x60, 0x79, 0x26, 0x72, 0x5c, 0x48, 0xff,
	0x75, 0xc7, 0xbf, 0xdb, 0xf4, 0x53, 0xc3, 0xbd, 0xac, 0xf4, 0x9c, 0x08, 0x39, 0x4a, 0xaa, 0x30,
	0x1c, 0xfc, 0x37, 0xff, 0xe8, 0xec, 0x27, 0x00, 0x00, 0xff, 0xff, 0x08, 0xde, 0xd9, 0x9d, 0x87,
	0x02, 0x00, 0x00,
}

// Reference imports to suppress errors if they are not otherwise used.
var _ context.Context
var _ grpc.ClientConn

// This is a compile-time assertion to ensure that this generated file
// is compatible with the grpc package it is being compiled against.
const _ = grpc.SupportPackageIsVersion4

// ZhihuDailyPurifyServiceClient is the client API for ZhihuDailyPurifyService service.
//
// For semantics around ctx use and closing/ending streaming RPCs, please refer to https://godoc.org/google.golang.org/grpc#ClientConn.NewStream.
type ZhihuDailyPurifyServiceClient interface {
	GetNews(ctx context.Context, in *GetNewsRequest, opts ...grpc.CallOption) (*Feed, error)
	SearchNews(ctx context.Context, in *SearchNewsRequest, opts ...grpc.CallOption) (*Feed, error)
}

type zhihuDailyPurifyServiceClient struct {
	cc *grpc.ClientConn
}

func NewZhihuDailyPurifyServiceClient(cc *grpc.ClientConn) ZhihuDailyPurifyServiceClient {
	return &zhihuDailyPurifyServiceClient{cc}
}

func (c *zhihuDailyPurifyServiceClient) GetNews(ctx context.Context, in *GetNewsRequest, opts ...grpc.CallOption) (*Feed, error) {
	out := new(Feed)
	err := c.cc.Invoke(ctx, "/io.github.izzyleung.ZhihuDailyPurifyService/GetNews", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *zhihuDailyPurifyServiceClient) SearchNews(ctx context.Context, in *SearchNewsRequest, opts ...grpc.CallOption) (*Feed, error) {
	out := new(Feed)
	err := c.cc.Invoke(ctx, "/io.github.izzyleung.ZhihuDailyPurifyService/SearchNews", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

// ZhihuDailyPurifyServiceServer is the server API for ZhihuDailyPurifyService service.
type ZhihuDailyPurifyServiceServer interface {
	GetNews(context.Context, *GetNewsRequest) (*Feed, error)
	SearchNews(context.Context, *SearchNewsRequest) (*Feed, error)
}

// UnimplementedZhihuDailyPurifyServiceServer can be embedded to have forward compatible implementations.
type UnimplementedZhihuDailyPurifyServiceServer struct {
}

func (*UnimplementedZhihuDailyPurifyServiceServer) GetNews(ctx context.Context, req *GetNewsRequest) (*Feed, error) {
	return nil, status.Errorf(codes.Unimplemented, "method GetNews not implemented")
}
func (*UnimplementedZhihuDailyPurifyServiceServer) SearchNews(ctx context.Context, req *SearchNewsRequest) (*Feed, error) {
	return nil, status.Errorf(codes.Unimplemented, "method SearchNews not implemented")
}

func RegisterZhihuDailyPurifyServiceServer(s *grpc.Server, srv ZhihuDailyPurifyServiceServer) {
	s.RegisterService(&_ZhihuDailyPurifyService_serviceDesc, srv)
}

func _ZhihuDailyPurifyService_GetNews_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(GetNewsRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(ZhihuDailyPurifyServiceServer).GetNews(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/io.github.izzyleung.ZhihuDailyPurifyService/GetNews",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(ZhihuDailyPurifyServiceServer).GetNews(ctx, req.(*GetNewsRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _ZhihuDailyPurifyService_SearchNews_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(SearchNewsRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(ZhihuDailyPurifyServiceServer).SearchNews(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/io.github.izzyleung.ZhihuDailyPurifyService/SearchNews",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(ZhihuDailyPurifyServiceServer).SearchNews(ctx, req.(*SearchNewsRequest))
	}
	return interceptor(ctx, in, info, handler)
}

var _ZhihuDailyPurifyService_serviceDesc = grpc.ServiceDesc{
	ServiceName: "io.github.izzyleung.ZhihuDailyPurifyService",
	HandlerType: (*ZhihuDailyPurifyServiceServer)(nil),
	Methods: []grpc.MethodDesc{
		{
			MethodName: "GetNews",
			Handler:    _ZhihuDailyPurifyService_GetNews_Handler,
		},
		{
			MethodName: "SearchNews",
			Handler:    _ZhihuDailyPurifyService_SearchNews_Handler,
		},
	},
	Streams:  []grpc.StreamDesc{},
	Metadata: "zdp.proto",
}

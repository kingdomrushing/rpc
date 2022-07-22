# 自定义RPC实现

## 项目模块介绍
### rpc-common       公共模块
### rpc-compress     数据压缩模块
> 该模块定义了对数据进行压缩和解压缩的接口，目前实现了Gzip压缩算法
### rpc-proto        协议模块
> 该模块定义了数据传输过程中的协议，如请求和响应格式
### rpc-registry     注册中心模块
> 该模块定义了服务注册和服务发现接口，以及基于Zookeeper的注册中心实现
### rpc-serialize    序列化模块
> 该模块定义了对数据进行序列化和反序列化的接口，
> 目前实现了JSON、Kyro、Hessian2、Protostuff序列化和反序列化算法
### rpc-tansport     网络传输模块
> 该模块使用了BIO和基于NIO的Netty实现网络传输
### rpc-example-api  测试用的服务接口          
### rpc-example-server  服务端测试
### rpc-example-client  客户端测试
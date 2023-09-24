# Raft_KV_Platform

# 快速开始
下载，解压RaftKV-2.2.tar.gz

1.配置conf目录下文件config.properties

```properties
replication=3
replicator1=127.0.0.1:5001
replicator2=127.0.0.1:5002
replicator3=127.0.0.1:5003
```

replication第一个是多少台机器

以下必须严格遵守格式replicator3

2.配置whoami

```txt
nodeid:1
```

只能修改数字，确保当前节点的id，

如果不设置，需要在启动RaftServerStart 加--nodeid xx

3.启动

```shell
./RaftServerStart
```

在logs会产生当前时间的log文件。

4.客户端连接

```shell
./RaftClient --server 127.0.0.1:5001,127.0.0.1:5002,127.0.0.1:5003
```

使用--server或者-s 会选择其中leader连接

```shell
./RaftClient --d 127.0.0.1:5001
```

使用-d 会连接特定的，不管是不是leader

5.CLI使用
输入put k v
```shell
put raft hello
OK
```
输入get raft
```shell
get raft
KeyFound | hello
```
输入get delete
```shell
delete raft
OK
```
RaftKV-2.2.tar.gz
项目成品可以直接用

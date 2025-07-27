package com.cuhk.raft.pb;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.42.1)",
    comments = "Source: raft.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class RaftNodeGrpc {

  private RaftNodeGrpc() {}

  public static final String SERVICE_NAME = "com.cuhk.raft.pb.RaftNode";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.ProposeArgs,
      com.cuhk.raft.pb.Raft.ProposeReply> getProposeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Propose",
      requestType = com.cuhk.raft.pb.Raft.ProposeArgs.class,
      responseType = com.cuhk.raft.pb.Raft.ProposeReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.ProposeArgs,
      com.cuhk.raft.pb.Raft.ProposeReply> getProposeMethod() {
    io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.ProposeArgs, com.cuhk.raft.pb.Raft.ProposeReply> getProposeMethod;
    if ((getProposeMethod = RaftNodeGrpc.getProposeMethod) == null) {
      synchronized (RaftNodeGrpc.class) {
        if ((getProposeMethod = RaftNodeGrpc.getProposeMethod) == null) {
          RaftNodeGrpc.getProposeMethod = getProposeMethod =
              io.grpc.MethodDescriptor.<com.cuhk.raft.pb.Raft.ProposeArgs, com.cuhk.raft.pb.Raft.ProposeReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Propose"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cuhk.raft.pb.Raft.ProposeArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cuhk.raft.pb.Raft.ProposeReply.getDefaultInstance()))
              .setSchemaDescriptor(new RaftNodeMethodDescriptorSupplier("Propose"))
              .build();
        }
      }
    }
    return getProposeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.GetValueArgs,
      com.cuhk.raft.pb.Raft.GetValueReply> getGetValueMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetValue",
      requestType = com.cuhk.raft.pb.Raft.GetValueArgs.class,
      responseType = com.cuhk.raft.pb.Raft.GetValueReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.GetValueArgs,
      com.cuhk.raft.pb.Raft.GetValueReply> getGetValueMethod() {
    io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.GetValueArgs, com.cuhk.raft.pb.Raft.GetValueReply> getGetValueMethod;
    if ((getGetValueMethod = RaftNodeGrpc.getGetValueMethod) == null) {
      synchronized (RaftNodeGrpc.class) {
        if ((getGetValueMethod = RaftNodeGrpc.getGetValueMethod) == null) {
          RaftNodeGrpc.getGetValueMethod = getGetValueMethod =
              io.grpc.MethodDescriptor.<com.cuhk.raft.pb.Raft.GetValueArgs, com.cuhk.raft.pb.Raft.GetValueReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetValue"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cuhk.raft.pb.Raft.GetValueArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cuhk.raft.pb.Raft.GetValueReply.getDefaultInstance()))
              .setSchemaDescriptor(new RaftNodeMethodDescriptorSupplier("GetValue"))
              .build();
        }
      }
    }
    return getGetValueMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.SetElectionTimeoutArgs,
      com.cuhk.raft.pb.Raft.SetElectionTimeoutReply> getSetElectionTimeoutMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SetElectionTimeout",
      requestType = com.cuhk.raft.pb.Raft.SetElectionTimeoutArgs.class,
      responseType = com.cuhk.raft.pb.Raft.SetElectionTimeoutReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.SetElectionTimeoutArgs,
      com.cuhk.raft.pb.Raft.SetElectionTimeoutReply> getSetElectionTimeoutMethod() {
    io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.SetElectionTimeoutArgs, com.cuhk.raft.pb.Raft.SetElectionTimeoutReply> getSetElectionTimeoutMethod;
    if ((getSetElectionTimeoutMethod = RaftNodeGrpc.getSetElectionTimeoutMethod) == null) {
      synchronized (RaftNodeGrpc.class) {
        if ((getSetElectionTimeoutMethod = RaftNodeGrpc.getSetElectionTimeoutMethod) == null) {
          RaftNodeGrpc.getSetElectionTimeoutMethod = getSetElectionTimeoutMethod =
              io.grpc.MethodDescriptor.<com.cuhk.raft.pb.Raft.SetElectionTimeoutArgs, com.cuhk.raft.pb.Raft.SetElectionTimeoutReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SetElectionTimeout"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cuhk.raft.pb.Raft.SetElectionTimeoutArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cuhk.raft.pb.Raft.SetElectionTimeoutReply.getDefaultInstance()))
              .setSchemaDescriptor(new RaftNodeMethodDescriptorSupplier("SetElectionTimeout"))
              .build();
        }
      }
    }
    return getSetElectionTimeoutMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.SetHeartBeatIntervalArgs,
      com.cuhk.raft.pb.Raft.SetHeartBeatIntervalReply> getSetHeartBeatIntervalMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SetHeartBeatInterval",
      requestType = com.cuhk.raft.pb.Raft.SetHeartBeatIntervalArgs.class,
      responseType = com.cuhk.raft.pb.Raft.SetHeartBeatIntervalReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.SetHeartBeatIntervalArgs,
      com.cuhk.raft.pb.Raft.SetHeartBeatIntervalReply> getSetHeartBeatIntervalMethod() {
    io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.SetHeartBeatIntervalArgs, com.cuhk.raft.pb.Raft.SetHeartBeatIntervalReply> getSetHeartBeatIntervalMethod;
    if ((getSetHeartBeatIntervalMethod = RaftNodeGrpc.getSetHeartBeatIntervalMethod) == null) {
      synchronized (RaftNodeGrpc.class) {
        if ((getSetHeartBeatIntervalMethod = RaftNodeGrpc.getSetHeartBeatIntervalMethod) == null) {
          RaftNodeGrpc.getSetHeartBeatIntervalMethod = getSetHeartBeatIntervalMethod =
              io.grpc.MethodDescriptor.<com.cuhk.raft.pb.Raft.SetHeartBeatIntervalArgs, com.cuhk.raft.pb.Raft.SetHeartBeatIntervalReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SetHeartBeatInterval"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cuhk.raft.pb.Raft.SetHeartBeatIntervalArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cuhk.raft.pb.Raft.SetHeartBeatIntervalReply.getDefaultInstance()))
              .setSchemaDescriptor(new RaftNodeMethodDescriptorSupplier("SetHeartBeatInterval"))
              .build();
        }
      }
    }
    return getSetHeartBeatIntervalMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.RequestVoteArgs,
      com.cuhk.raft.pb.Raft.RequestVoteReply> getRequestVoteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RequestVote",
      requestType = com.cuhk.raft.pb.Raft.RequestVoteArgs.class,
      responseType = com.cuhk.raft.pb.Raft.RequestVoteReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.RequestVoteArgs,
      com.cuhk.raft.pb.Raft.RequestVoteReply> getRequestVoteMethod() {
    io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.RequestVoteArgs, com.cuhk.raft.pb.Raft.RequestVoteReply> getRequestVoteMethod;
    if ((getRequestVoteMethod = RaftNodeGrpc.getRequestVoteMethod) == null) {
      synchronized (RaftNodeGrpc.class) {
        if ((getRequestVoteMethod = RaftNodeGrpc.getRequestVoteMethod) == null) {
          RaftNodeGrpc.getRequestVoteMethod = getRequestVoteMethod =
              io.grpc.MethodDescriptor.<com.cuhk.raft.pb.Raft.RequestVoteArgs, com.cuhk.raft.pb.Raft.RequestVoteReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RequestVote"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cuhk.raft.pb.Raft.RequestVoteArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cuhk.raft.pb.Raft.RequestVoteReply.getDefaultInstance()))
              .setSchemaDescriptor(new RaftNodeMethodDescriptorSupplier("RequestVote"))
              .build();
        }
      }
    }
    return getRequestVoteMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.AppendEntriesArgs,
      com.cuhk.raft.pb.Raft.AppendEntriesReply> getAppendEntriesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AppendEntries",
      requestType = com.cuhk.raft.pb.Raft.AppendEntriesArgs.class,
      responseType = com.cuhk.raft.pb.Raft.AppendEntriesReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.AppendEntriesArgs,
      com.cuhk.raft.pb.Raft.AppendEntriesReply> getAppendEntriesMethod() {
    io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.AppendEntriesArgs, com.cuhk.raft.pb.Raft.AppendEntriesReply> getAppendEntriesMethod;
    if ((getAppendEntriesMethod = RaftNodeGrpc.getAppendEntriesMethod) == null) {
      synchronized (RaftNodeGrpc.class) {
        if ((getAppendEntriesMethod = RaftNodeGrpc.getAppendEntriesMethod) == null) {
          RaftNodeGrpc.getAppendEntriesMethod = getAppendEntriesMethod =
              io.grpc.MethodDescriptor.<com.cuhk.raft.pb.Raft.AppendEntriesArgs, com.cuhk.raft.pb.Raft.AppendEntriesReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AppendEntries"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cuhk.raft.pb.Raft.AppendEntriesArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cuhk.raft.pb.Raft.AppendEntriesReply.getDefaultInstance()))
              .setSchemaDescriptor(new RaftNodeMethodDescriptorSupplier("AppendEntries"))
              .build();
        }
      }
    }
    return getAppendEntriesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.CheckEventsArgs,
      com.cuhk.raft.pb.Raft.CheckEventsReply> getCheckEventsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CheckEvents",
      requestType = com.cuhk.raft.pb.Raft.CheckEventsArgs.class,
      responseType = com.cuhk.raft.pb.Raft.CheckEventsReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.CheckEventsArgs,
      com.cuhk.raft.pb.Raft.CheckEventsReply> getCheckEventsMethod() {
    io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.CheckEventsArgs, com.cuhk.raft.pb.Raft.CheckEventsReply> getCheckEventsMethod;
    if ((getCheckEventsMethod = RaftNodeGrpc.getCheckEventsMethod) == null) {
      synchronized (RaftNodeGrpc.class) {
        if ((getCheckEventsMethod = RaftNodeGrpc.getCheckEventsMethod) == null) {
          RaftNodeGrpc.getCheckEventsMethod = getCheckEventsMethod =
              io.grpc.MethodDescriptor.<com.cuhk.raft.pb.Raft.CheckEventsArgs, com.cuhk.raft.pb.Raft.CheckEventsReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CheckEvents"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cuhk.raft.pb.Raft.CheckEventsArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cuhk.raft.pb.Raft.CheckEventsReply.getDefaultInstance()))
              .setSchemaDescriptor(new RaftNodeMethodDescriptorSupplier("CheckEvents"))
              .build();
        }
      }
    }
    return getCheckEventsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.WhoAreYouArgs,
      com.cuhk.raft.pb.Raft.WhoAreYouReply> getWhoAreYouMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "WhoAreYou",
      requestType = com.cuhk.raft.pb.Raft.WhoAreYouArgs.class,
      responseType = com.cuhk.raft.pb.Raft.WhoAreYouReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.WhoAreYouArgs,
      com.cuhk.raft.pb.Raft.WhoAreYouReply> getWhoAreYouMethod() {
    io.grpc.MethodDescriptor<com.cuhk.raft.pb.Raft.WhoAreYouArgs, com.cuhk.raft.pb.Raft.WhoAreYouReply> getWhoAreYouMethod;
    if ((getWhoAreYouMethod = RaftNodeGrpc.getWhoAreYouMethod) == null) {
      synchronized (RaftNodeGrpc.class) {
        if ((getWhoAreYouMethod = RaftNodeGrpc.getWhoAreYouMethod) == null) {
          RaftNodeGrpc.getWhoAreYouMethod = getWhoAreYouMethod =
              io.grpc.MethodDescriptor.<com.cuhk.raft.pb.Raft.WhoAreYouArgs, com.cuhk.raft.pb.Raft.WhoAreYouReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "WhoAreYou"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cuhk.raft.pb.Raft.WhoAreYouArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cuhk.raft.pb.Raft.WhoAreYouReply.getDefaultInstance()))
              .setSchemaDescriptor(new RaftNodeMethodDescriptorSupplier("WhoAreYou"))
              .build();
        }
      }
    }
    return getWhoAreYouMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RaftNodeStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RaftNodeStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RaftNodeStub>() {
        @java.lang.Override
        public RaftNodeStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RaftNodeStub(channel, callOptions);
        }
      };
    return RaftNodeStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RaftNodeBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RaftNodeBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RaftNodeBlockingStub>() {
        @java.lang.Override
        public RaftNodeBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RaftNodeBlockingStub(channel, callOptions);
        }
      };
    return RaftNodeBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RaftNodeFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RaftNodeFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RaftNodeFutureStub>() {
        @java.lang.Override
        public RaftNodeFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RaftNodeFutureStub(channel, callOptions);
        }
      };
    return RaftNodeFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class RaftNodeImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Desc:
     * Propose initializes proposing a new operation, and replies with the
     * result of committing this operation. Propose should not return until
     * this operation has been committed, or this node is not leader now.
     * If the we put a new &lt;k, v&gt; pair or deleted an existing &lt;k, v&gt; pair
     * successfully, it should return OK; If it tries to delete an non-existing
     * key, a KeyNotFound should be returned; If this node is not leader now,
     * it should return WrongNode as well as the currentLeader id.
     * Params:
     * args: the operation to propose
     * reply: as specified in Desc
     * </pre>
     */
    public void propose(com.cuhk.raft.pb.Raft.ProposeArgs request,
        io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.ProposeReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getProposeMethod(), responseObserver);
    }

    /**
     * <pre>
     * Desc:GetValue
     * GetValue looks up the value for a key, and replies with the value or with
     * the Status KeyNotFound.
     * Params:
     * args: the key to check
     * reply: the value and status for this lookup of the given key
     * </pre>
     */
    public void getValue(com.cuhk.raft.pb.Raft.GetValueArgs request,
        io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.GetValueReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetValueMethod(), responseObserver);
    }

    /**
     * <pre>
     * Desc:
     * Set both the the electionTimeoutLow and electionTimeoutHigh of this node to be args.Timeout.
     * You also need to stop current timer and reset it to fire after args.Timeout milliseconds.
     * Params:
     * args: the election timeout duration
     * reply: no use
     * </pre>
     */
    public void setElectionTimeout(com.cuhk.raft.pb.Raft.SetElectionTimeoutArgs request,
        io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.SetElectionTimeoutReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSetElectionTimeoutMethod(), responseObserver);
    }

    /**
     * <pre>
     * Desc:
     * Set heartBeatInterval as args.Interval milliseconds.
     * You also need to stop current ticker and reset it to fire every args.Interval milliseconds.
     * Params:
     * args: the heartbeat duration
     * reply: no use
     * </pre>
     */
    public void setHeartBeatInterval(com.cuhk.raft.pb.Raft.SetHeartBeatIntervalArgs request,
        io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.SetHeartBeatIntervalReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSetHeartBeatIntervalMethod(), responseObserver);
    }

    /**
     * <pre>
     * Desc:
     * Receive a RequestVote message from another Raft Node. Check the paper for more details.
     * Params:
     * args: the RequestVote Message, you must include From(src node id) and To(dst node id) when
     * you call this API
     * Return:
     * reply: the RequestVote Reply Message
     * </pre>
     */
    public void requestVote(com.cuhk.raft.pb.Raft.RequestVoteArgs request,
        io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.RequestVoteReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRequestVoteMethod(), responseObserver);
    }

    /**
     * <pre>
     * Desc:
     * Receive a AppendEntries message from another Raft Node. Check the paper for more details.
     * Params:
     * args: the AppendEntries M/essage, you must include From(src node id) and To(dst node id) when
     * you call this API
     * reply: the AppendEntries Reply Message
     * </pre>
     */
    public void appendEntries(com.cuhk.raft.pb.Raft.AppendEntriesArgs request,
        io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.AppendEntriesReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAppendEntriesMethod(), responseObserver);
    }

    /**
     * <pre>
     * Desc:
     * For testing purpose only. You should implement it and reply nil directly,
     * </pre>
     */
    public void checkEvents(com.cuhk.raft.pb.Raft.CheckEventsArgs request,
        io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.CheckEventsReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCheckEventsMethod(), responseObserver);
    }

    /**
     */
    public void whoAreYou(com.cuhk.raft.pb.Raft.WhoAreYouArgs request,
        io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.WhoAreYouReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getWhoAreYouMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getProposeMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.cuhk.raft.pb.Raft.ProposeArgs,
                com.cuhk.raft.pb.Raft.ProposeReply>(
                  this, METHODID_PROPOSE)))
          .addMethod(
            getGetValueMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.cuhk.raft.pb.Raft.GetValueArgs,
                com.cuhk.raft.pb.Raft.GetValueReply>(
                  this, METHODID_GET_VALUE)))
          .addMethod(
            getSetElectionTimeoutMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.cuhk.raft.pb.Raft.SetElectionTimeoutArgs,
                com.cuhk.raft.pb.Raft.SetElectionTimeoutReply>(
                  this, METHODID_SET_ELECTION_TIMEOUT)))
          .addMethod(
            getSetHeartBeatIntervalMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.cuhk.raft.pb.Raft.SetHeartBeatIntervalArgs,
                com.cuhk.raft.pb.Raft.SetHeartBeatIntervalReply>(
                  this, METHODID_SET_HEART_BEAT_INTERVAL)))
          .addMethod(
            getRequestVoteMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.cuhk.raft.pb.Raft.RequestVoteArgs,
                com.cuhk.raft.pb.Raft.RequestVoteReply>(
                  this, METHODID_REQUEST_VOTE)))
          .addMethod(
            getAppendEntriesMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.cuhk.raft.pb.Raft.AppendEntriesArgs,
                com.cuhk.raft.pb.Raft.AppendEntriesReply>(
                  this, METHODID_APPEND_ENTRIES)))
          .addMethod(
            getCheckEventsMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.cuhk.raft.pb.Raft.CheckEventsArgs,
                com.cuhk.raft.pb.Raft.CheckEventsReply>(
                  this, METHODID_CHECK_EVENTS)))
          .addMethod(
            getWhoAreYouMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.cuhk.raft.pb.Raft.WhoAreYouArgs,
                com.cuhk.raft.pb.Raft.WhoAreYouReply>(
                  this, METHODID_WHO_ARE_YOU)))
          .build();
    }
  }

  /**
   */
  public static final class RaftNodeStub extends io.grpc.stub.AbstractAsyncStub<RaftNodeStub> {
    private RaftNodeStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RaftNodeStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RaftNodeStub(channel, callOptions);
    }

    /**
     * <pre>
     * Desc:
     * Propose initializes proposing a new operation, and replies with the
     * result of committing this operation. Propose should not return until
     * this operation has been committed, or this node is not leader now.
     * If the we put a new &lt;k, v&gt; pair or deleted an existing &lt;k, v&gt; pair
     * successfully, it should return OK; If it tries to delete an non-existing
     * key, a KeyNotFound should be returned; If this node is not leader now,
     * it should return WrongNode as well as the currentLeader id.
     * Params:
     * args: the operation to propose
     * reply: as specified in Desc
     * </pre>
     */
    public void propose(com.cuhk.raft.pb.Raft.ProposeArgs request,
        io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.ProposeReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getProposeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Desc:GetValue
     * GetValue looks up the value for a key, and replies with the value or with
     * the Status KeyNotFound.
     * Params:
     * args: the key to check
     * reply: the value and status for this lookup of the given key
     * </pre>
     */
    public void getValue(com.cuhk.raft.pb.Raft.GetValueArgs request,
        io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.GetValueReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetValueMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Desc:
     * Set both the the electionTimeoutLow and electionTimeoutHigh of this node to be args.Timeout.
     * You also need to stop current timer and reset it to fire after args.Timeout milliseconds.
     * Params:
     * args: the election timeout duration
     * reply: no use
     * </pre>
     */
    public void setElectionTimeout(com.cuhk.raft.pb.Raft.SetElectionTimeoutArgs request,
        io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.SetElectionTimeoutReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSetElectionTimeoutMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Desc:
     * Set heartBeatInterval as args.Interval milliseconds.
     * You also need to stop current ticker and reset it to fire every args.Interval milliseconds.
     * Params:
     * args: the heartbeat duration
     * reply: no use
     * </pre>
     */
    public void setHeartBeatInterval(com.cuhk.raft.pb.Raft.SetHeartBeatIntervalArgs request,
        io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.SetHeartBeatIntervalReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSetHeartBeatIntervalMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Desc:
     * Receive a RequestVote message from another Raft Node. Check the paper for more details.
     * Params:
     * args: the RequestVote Message, you must include From(src node id) and To(dst node id) when
     * you call this API
     * Return:
     * reply: the RequestVote Reply Message
     * </pre>
     */
    public void requestVote(com.cuhk.raft.pb.Raft.RequestVoteArgs request,
        io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.RequestVoteReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRequestVoteMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Desc:
     * Receive a AppendEntries message from another Raft Node. Check the paper for more details.
     * Params:
     * args: the AppendEntries M/essage, you must include From(src node id) and To(dst node id) when
     * you call this API
     * reply: the AppendEntries Reply Message
     * </pre>
     */
    public void appendEntries(com.cuhk.raft.pb.Raft.AppendEntriesArgs request,
        io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.AppendEntriesReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAppendEntriesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Desc:
     * For testing purpose only. You should implement it and reply nil directly,
     * </pre>
     */
    public void checkEvents(com.cuhk.raft.pb.Raft.CheckEventsArgs request,
        io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.CheckEventsReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCheckEventsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void whoAreYou(com.cuhk.raft.pb.Raft.WhoAreYouArgs request,
        io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.WhoAreYouReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getWhoAreYouMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class RaftNodeBlockingStub extends io.grpc.stub.AbstractBlockingStub<RaftNodeBlockingStub> {
    private RaftNodeBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RaftNodeBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RaftNodeBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Desc:
     * Propose initializes proposing a new operation, and replies with the
     * result of committing this operation. Propose should not return until
     * this operation has been committed, or this node is not leader now.
     * If the we put a new &lt;k, v&gt; pair or deleted an existing &lt;k, v&gt; pair
     * successfully, it should return OK; If it tries to delete an non-existing
     * key, a KeyNotFound should be returned; If this node is not leader now,
     * it should return WrongNode as well as the currentLeader id.
     * Params:
     * args: the operation to propose
     * reply: as specified in Desc
     * </pre>
     */
    public com.cuhk.raft.pb.Raft.ProposeReply propose(com.cuhk.raft.pb.Raft.ProposeArgs request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getProposeMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Desc:GetValue
     * GetValue looks up the value for a key, and replies with the value or with
     * the Status KeyNotFound.
     * Params:
     * args: the key to check
     * reply: the value and status for this lookup of the given key
     * </pre>
     */
    public com.cuhk.raft.pb.Raft.GetValueReply getValue(com.cuhk.raft.pb.Raft.GetValueArgs request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetValueMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Desc:
     * Set both the the electionTimeoutLow and electionTimeoutHigh of this node to be args.Timeout.
     * You also need to stop current timer and reset it to fire after args.Timeout milliseconds.
     * Params:
     * args: the election timeout duration
     * reply: no use
     * </pre>
     */
    public com.cuhk.raft.pb.Raft.SetElectionTimeoutReply setElectionTimeout(com.cuhk.raft.pb.Raft.SetElectionTimeoutArgs request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSetElectionTimeoutMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Desc:
     * Set heartBeatInterval as args.Interval milliseconds.
     * You also need to stop current ticker and reset it to fire every args.Interval milliseconds.
     * Params:
     * args: the heartbeat duration
     * reply: no use
     * </pre>
     */
    public com.cuhk.raft.pb.Raft.SetHeartBeatIntervalReply setHeartBeatInterval(com.cuhk.raft.pb.Raft.SetHeartBeatIntervalArgs request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSetHeartBeatIntervalMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Desc:
     * Receive a RequestVote message from another Raft Node. Check the paper for more details.
     * Params:
     * args: the RequestVote Message, you must include From(src node id) and To(dst node id) when
     * you call this API
     * Return:
     * reply: the RequestVote Reply Message
     * </pre>
     */
    public com.cuhk.raft.pb.Raft.RequestVoteReply requestVote(com.cuhk.raft.pb.Raft.RequestVoteArgs request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRequestVoteMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Desc:
     * Receive a AppendEntries message from another Raft Node. Check the paper for more details.
     * Params:
     * args: the AppendEntries M/essage, you must include From(src node id) and To(dst node id) when
     * you call this API
     * reply: the AppendEntries Reply Message
     * </pre>
     */
    public com.cuhk.raft.pb.Raft.AppendEntriesReply appendEntries(com.cuhk.raft.pb.Raft.AppendEntriesArgs request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAppendEntriesMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Desc:
     * For testing purpose only. You should implement it and reply nil directly,
     * </pre>
     */
    public com.cuhk.raft.pb.Raft.CheckEventsReply checkEvents(com.cuhk.raft.pb.Raft.CheckEventsArgs request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCheckEventsMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.cuhk.raft.pb.Raft.WhoAreYouReply whoAreYou(com.cuhk.raft.pb.Raft.WhoAreYouArgs request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getWhoAreYouMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RaftNodeFutureStub extends io.grpc.stub.AbstractFutureStub<RaftNodeFutureStub> {
    private RaftNodeFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RaftNodeFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RaftNodeFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Desc:
     * Propose initializes proposing a new operation, and replies with the
     * result of committing this operation. Propose should not return until
     * this operation has been committed, or this node is not leader now.
     * If the we put a new &lt;k, v&gt; pair or deleted an existing &lt;k, v&gt; pair
     * successfully, it should return OK; If it tries to delete an non-existing
     * key, a KeyNotFound should be returned; If this node is not leader now,
     * it should return WrongNode as well as the currentLeader id.
     * Params:
     * args: the operation to propose
     * reply: as specified in Desc
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cuhk.raft.pb.Raft.ProposeReply> propose(
        com.cuhk.raft.pb.Raft.ProposeArgs request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getProposeMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Desc:GetValue
     * GetValue looks up the value for a key, and replies with the value or with
     * the Status KeyNotFound.
     * Params:
     * args: the key to check
     * reply: the value and status for this lookup of the given key
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cuhk.raft.pb.Raft.GetValueReply> getValue(
        com.cuhk.raft.pb.Raft.GetValueArgs request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetValueMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Desc:
     * Set both the the electionTimeoutLow and electionTimeoutHigh of this node to be args.Timeout.
     * You also need to stop current timer and reset it to fire after args.Timeout milliseconds.
     * Params:
     * args: the election timeout duration
     * reply: no use
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cuhk.raft.pb.Raft.SetElectionTimeoutReply> setElectionTimeout(
        com.cuhk.raft.pb.Raft.SetElectionTimeoutArgs request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSetElectionTimeoutMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Desc:
     * Set heartBeatInterval as args.Interval milliseconds.
     * You also need to stop current ticker and reset it to fire every args.Interval milliseconds.
     * Params:
     * args: the heartbeat duration
     * reply: no use
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cuhk.raft.pb.Raft.SetHeartBeatIntervalReply> setHeartBeatInterval(
        com.cuhk.raft.pb.Raft.SetHeartBeatIntervalArgs request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSetHeartBeatIntervalMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Desc:
     * Receive a RequestVote message from another Raft Node. Check the paper for more details.
     * Params:
     * args: the RequestVote Message, you must include From(src node id) and To(dst node id) when
     * you call this API
     * Return:
     * reply: the RequestVote Reply Message
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cuhk.raft.pb.Raft.RequestVoteReply> requestVote(
        com.cuhk.raft.pb.Raft.RequestVoteArgs request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRequestVoteMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Desc:
     * Receive a AppendEntries message from another Raft Node. Check the paper for more details.
     * Params:
     * args: the AppendEntries M/essage, you must include From(src node id) and To(dst node id) when
     * you call this API
     * reply: the AppendEntries Reply Message
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cuhk.raft.pb.Raft.AppendEntriesReply> appendEntries(
        com.cuhk.raft.pb.Raft.AppendEntriesArgs request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAppendEntriesMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Desc:
     * For testing purpose only. You should implement it and reply nil directly,
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cuhk.raft.pb.Raft.CheckEventsReply> checkEvents(
        com.cuhk.raft.pb.Raft.CheckEventsArgs request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCheckEventsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cuhk.raft.pb.Raft.WhoAreYouReply> whoAreYou(
        com.cuhk.raft.pb.Raft.WhoAreYouArgs request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getWhoAreYouMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PROPOSE = 0;
  private static final int METHODID_GET_VALUE = 1;
  private static final int METHODID_SET_ELECTION_TIMEOUT = 2;
  private static final int METHODID_SET_HEART_BEAT_INTERVAL = 3;
  private static final int METHODID_REQUEST_VOTE = 4;
  private static final int METHODID_APPEND_ENTRIES = 5;
  private static final int METHODID_CHECK_EVENTS = 6;
  private static final int METHODID_WHO_ARE_YOU = 7;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RaftNodeImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RaftNodeImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PROPOSE:
          serviceImpl.propose((com.cuhk.raft.pb.Raft.ProposeArgs) request,
              (io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.ProposeReply>) responseObserver);
          break;
        case METHODID_GET_VALUE:
          serviceImpl.getValue((com.cuhk.raft.pb.Raft.GetValueArgs) request,
              (io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.GetValueReply>) responseObserver);
          break;
        case METHODID_SET_ELECTION_TIMEOUT:
          serviceImpl.setElectionTimeout((com.cuhk.raft.pb.Raft.SetElectionTimeoutArgs) request,
              (io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.SetElectionTimeoutReply>) responseObserver);
          break;
        case METHODID_SET_HEART_BEAT_INTERVAL:
          serviceImpl.setHeartBeatInterval((com.cuhk.raft.pb.Raft.SetHeartBeatIntervalArgs) request,
              (io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.SetHeartBeatIntervalReply>) responseObserver);
          break;
        case METHODID_REQUEST_VOTE:
          serviceImpl.requestVote((com.cuhk.raft.pb.Raft.RequestVoteArgs) request,
              (io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.RequestVoteReply>) responseObserver);
          break;
        case METHODID_APPEND_ENTRIES:
          serviceImpl.appendEntries((com.cuhk.raft.pb.Raft.AppendEntriesArgs) request,
              (io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.AppendEntriesReply>) responseObserver);
          break;
        case METHODID_CHECK_EVENTS:
          serviceImpl.checkEvents((com.cuhk.raft.pb.Raft.CheckEventsArgs) request,
              (io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.CheckEventsReply>) responseObserver);
          break;
        case METHODID_WHO_ARE_YOU:
          serviceImpl.whoAreYou((com.cuhk.raft.pb.Raft.WhoAreYouArgs) request,
              (io.grpc.stub.StreamObserver<com.cuhk.raft.pb.Raft.WhoAreYouReply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class RaftNodeBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RaftNodeBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.cuhk.raft.pb.Raft.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RaftNode");
    }
  }

  private static final class RaftNodeFileDescriptorSupplier
      extends RaftNodeBaseDescriptorSupplier {
    RaftNodeFileDescriptorSupplier() {}
  }

  private static final class RaftNodeMethodDescriptorSupplier
      extends RaftNodeBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    RaftNodeMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (RaftNodeGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RaftNodeFileDescriptorSupplier())
              .addMethod(getProposeMethod())
              .addMethod(getGetValueMethod())
              .addMethod(getSetElectionTimeoutMethod())
              .addMethod(getSetHeartBeatIntervalMethod())
              .addMethod(getRequestVoteMethod())
              .addMethod(getAppendEntriesMethod())
              .addMethod(getCheckEventsMethod())
              .addMethod(getWhoAreYouMethod())
              .build();
        }
      }
    }
    return result;
  }
}

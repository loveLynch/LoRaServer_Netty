<center><font face="黑体" size=6>LoRa服务器系统说明</font></center>

> 说明：从文档是本项目中期文档，源码并未更新，仅供参考

# 1.系统概况

> Lora是一种低功耗的广域网，Lora系统可以实现低功耗、自主网、远距离传输的无线数据传输。在智能抄表、智能停车、物流跟踪、智慧农业等领域有着重要的应用。Lora 网络服务器作为Lora网络的一个子系统（以下称网络服务器），其功能主要完成与网关的上下行通信以及与前端应用服务器的上下行通信。

## 1.1 系统用途

> LoRa网络服务器组件的职责是对网关接收到的上行链路帧进行重复数据解析，处理LoRaWAN MAC层和调度下行链路数据传输等。并负责与应用服务器进行上下行通信与响应。

## 1.2 系统运行

> 本软件支持在Window或者Linux(Mac)下运行。运行该系统只需要配置Java环境、Redis服务、RabbitMQ服务以及安装Mysql数据库：

1. 安装java环境
2. 配置redis服务
3. 配置RabbitMq服务
4. 安装MySql服务

## 1.3 系统结构

> 本软件采用的是四层结构设计，包括应用层、网络通信层、业务逻辑层和数据访问层（持久化层），如图所示。

<center><img src="https://document-store.oss-cn-shenzhen.aliyuncs.com/LoRaNSStructure_20190422202332.png?Expires=1587471813&OSSAccessKeyId=LTAIl4SxXuRjoq2z&Signature=FLwg%2BZvC%2B3%2F5Fabc9vxhiV4yRTU%3D" width="50%" height="50%" />
</center>

> 各层功能如下：

- 应用层是针对前端交互相关通信，后面的技术框图将会具体提到；
- 网络通信层是解决lora网络连接通信的问题，确保网关和服务器之间高效即时的通信；
- 业务逻辑层具体处理了各通信和数据涉及到的逻辑关系，同时响应终端的各项操作指令等；
- 数据访问层解决lora服务器数据缓存和存储的问题，满足数据安全性的同时，完成终端数据的持久化过程。

# 2.LoRa数据处理

## 2.1 数据处理流程

> 此部分是按照LoRa协议设计处理上下行数据,网络服务器与网关之间采用TCP建立两个双向链接，两者之间互相为服务器与客户端。网络服务器收到网关上行后，若为确认帧则需处理完后马上回复下行；若为非确认帧，则只需处理不需要回复网关，具体的mac层数据处理如下图所示。

<center><img src="https://document-store.oss-cn-shenzhen.aliyuncs.com/LoRaServerFlowChart_20190422202714.png?Expires=1587472034&OSSAccessKeyId=LTAIl4SxXuRjoq2z&Signature=%2FZEFOlAU0YOgm1uaS0P6HW9L9pQ%3D" width="80%" height="50%" />
</center>

## 2.2 模块详解

### 2.2.1 数据帧处理

> 根据LoRa协议共有6种MAC帧数据类型，网络服务器根据解析出来的帧头，对数据进行相应的处理，判断是否回复下行与相应的下行的构造。6帧MAC帧的说明如下：

<center><img src="https://document-store.oss-cn-shenzhen.aliyuncs.com/MType_20190422202916.png?Expires=1587472156&OSSAccessKeyId=LTAIl4SxXuRjoq2z&Signature=dINJ6H9rmWbPudt%2FUS5BA6VoWm8%3D" width="80%" height="50%" />
</center>

### 2.2.2 入网判断模块

> 当终端设备是一个新设备时，网络服务器需对该设备进行DevEUI的分配与AES128密钥的分配，用于服务器与终端负载（framploay)数据的加解密。

### 2.2.3 Json解析、Base64解码、AES解密模块

> 此模块相当于上行的中间件，当网关收到射频信号，网关将接收到的数据包通过TCP上行连接发送到网络服务器，服务器根据继续出来的帧类型数据进行相应的操作，如进行AES解密、Base64解码和Json解析，如果是确认帧则需要构造下行回复。

### 2.2.4 AES加密、Base64编码、Json构造模块

> 此模块相当于下行中间件，该模块根据数据帧处理模块得出的帧类型，构造下行数据，下行数据构造主要涉及三种情况：上行数据为确认帧类型、上行数据中有MAC命令需回复和上层应用主动发起请求需构造下行数据。此过程涉及到Josn构造、Base64编码和AES解码，封装后的数据包下发给网关。

### 2.2.5 Redis模块

> redis数据库是一种非关系型键值存储的数据库，数据存储在内存中，执行效率非常高，方便快速读写。本系统中主要将每个终端与对应的aes密钥存储在redis中，方便快速对上下行负载中的应用数据进行解密与加密。

### 2.2.6 Mysql模块

> mysql是一种关系型数据库，数据存储在磁盘中。本系统住扰存储的是终端和网关的一些系统信息，以及涉及到固体应用构建相应的应用表的数据库。

# 3.服务器技术说明

## 3.1 技术路线

> 本系统主要有两个作用：一是和lora网关进行tcp上下行通信，并处理相应的数据，具体数据处理请参考LoRa协议与第2章的说明。与网关通信过程中，服务器与网关互为Socket tcp通信的客户端与服务器，服务器主要Netty通信框架和业务处理进行相应业务处理。上行netty有四handler，下行有三个handler，具体业务将在后续做一个简要说明。二是和lora应用（前端）进行具体应用处理，两者之间的交互通过rabbitmq进行通信，基于消息队列的方式进行数据传输与维护，具体流程将在后续说明。



## 3.2 Spring Boot

> Spring Boot 是由 Pivotal 团队开发的框架，其作用是用来简化新 Spring 应用的初始搭建以及开发过程。该框架使用了特定的方式来进行配置，从而使开发人员不再需要定义样板化的配置，简单理解就是springboot并不是什么新型的框架，而是整合了spring，springmvc等框架，默认了很多配置，从而减少了开发者的开发时间。
> Spring Boot 简化了基于 Spring 的应用开发,通过少量的代码就能创建一个独立的、产品级别的 Spring 应用。Spring Boot 为 Spring 平台及第三方库提供开箱即用的设置。
> SpringBoot有点：

- 使用 Spring 项目引导页面可以在几秒构建一个项目
- 支持关系数据库和非关系数据库
- 支持运行期内嵌容器，如 Tomcat、Jetty
- 强大的开发包，支持热启动
- 自动管理依赖
- 自带应用监控
- 支持各种 IDE，如 IntelliJ IDEA 、NetBeans

> 本系统正是利用Spring Boot这些特点，并结合高级项目管理工具Maven，对LoRa网络服务器进行开发与管理。

## 3.3 Netty

### 3.3.1 通信框架

> 本软件的通信采用Netty高并发框架，其中workgroup执行线程见图3所示。Netty采用了串行化设计理念，从消息的读取、编码以及后续Handler的执行，始终都由IO线程NioEventLoop负责，这就意外着整个流程不会进行线程上下文的切换，数据也不会面临被并发修改的风险。一个NioEventLoop聚合了一个多路复用器Selector，因此可以处理成百上千的客户端连接，Netty的处理策略是每当有一个新的客户端接入，则从NioEventLoop线程组中顺序获取一个可用的NioEventLoop，当到达数组上限之后，重新返回到0，通过这种方式，可以基本保证各个NioEventLoop的负载均衡。
> Netty 的EventLoop 是协同设计的一部分，它采用了两个基本的 API：并发和网络编程。首先，io.netty.util.concurrent 包构建在 JDK 的java.util.concurrent 包上，用来提供线程执行器。其次，io.netty.channel 包中的类，为了与Channel 的事件进行交互，扩展了这些接口/类。NIO传输实现只使用了少量的EventLoop（以及和它们相关联的Thread）,而且在当前线程模型中，它们可能会被多个Channel 所共享。这使得可以通过尽可能少量的Thread来支撑大量的Channel，而不是每个Channel分配一个Thread。下图显示了一个EventLoopGroup，它具有3个固定大小的EventLoop（每个EventLoop都由一个Thread支撑）。在创建EventLoopGroup时就直接分配了EventLoop （以及支撑它们的Thread），以确保在需要时它们是可用的。

<center><img src="https://document-store.oss-cn-shenzhen.aliyuncs.com/Netty_NIO_EventLoop_20190422203319.png?Expires=1587472399&OSSAccessKeyId=LTAIl4SxXuRjoq2z&Signature=Hv6WP5yPt%2FS8o6lBDgdimhXKges%3D" width="60%" height="60%" /></center>

### 3.3.2 业务处理

> 业务处理采用责任链模式，Channel、ChannelPipeline、ChannleHandlerContext、ChannelHandler等类之间的关系和职责如下图所示。

<center><img src="https://document-store.oss-cn-shenzhen.aliyuncs.com/Channel_20190422203422.png?Expires=1587472462&OSSAccessKeyId=LTAIl4SxXuRjoq2z&Signature=t8fBlVH9Zjz9c7TBznqw9w7stP8%3D" width="80%" height="60%" /></center>

> 各类的职责如下：

- Channel：封装了JDK原生的Channel，是Netty最核心的接口，提供统一的API，作为其它各个功能组件的容器。
- ChannelPipeline：责任链模式的核心组件，ChannelHandler的容器，按顺序组织各个ChannelHandler，并在它们之间转发事件。
- ChannelHandlerContext：封装一个具体的ChannelHandler，并为ChannelHandler的执行提供一个线程环境（ChannelHandlerInvoker）可以理解为ChannelPipeline链路上的一个节点，节点里面包含有指向前后节点的指针，事件在各个ChannelHandler之间传递，靠的就是ChannelHandlerContext。
- ChannelHandlerInvoker：顾名思义，是ChannelHandler的一个Invoker，它存在的意义是为ChannelHandler提供一个运行的线程环境，默认的实现DefaultChannelHandlerInvoker有一个EventExecutor类型的成员，就是Netty的EventLoop线程，所以默认ChannelHandler的处理逻辑在EventLoop线程内。当然也可以提供不同的实现，替换默认的线程模型。
- ChannelHandler： 真正对IO事件作出响应和处理的地方，也是Netty暴露给业务代码的一个扩展点。一般来说，主要业务逻辑就是以自定义ChannelHandler的方式来实现的。
  ChannelPipeline的工作是一个事件流模型，其派生了ChannelInboundHandler 和 ChannelOutboundHandler接口，这些对象接收事件、执行它们所实现的处理逻辑，并将数据传递给链中的下一个ChannelHandler。它们的执行顺序是由它们被添加的顺序所决定的，事件执行的入站和出站见下图所示。最接近socket的是head，而最接近业务层的是tail，业务配置的handler都是介于两者之间。

<center><img src="https://document-store.oss-cn-shenzhen.aliyuncs.com/EventInOut_20190422203804.png?Expires=1587472684&OSSAccessKeyId=LTAIl4SxXuRjoq2z&Signature=dA89%2F7SxRJlOuK4Q6%2BZr8iWGhPE%3D" width="80%" height="60%" /></center>

> 在这个软件中Netty这个模块都是用于处理和网关交互的通信和具体的数据处理，每个事件都可以被分配给ChannelHandler类中的某个实现方法，同时Netty的ChannelHandler为处理器提供了一个基本的抽象，而业务处理就是通过将各Handeler进行实例逻辑处理，本模块将分为上行和下行业务进行展开讲解，上行业务有四个handler，下行业务有三个handler。

#### 3.3.3.1 上行业务介绍

#### 3.3.3.1.1 数据包的接收与解码

> MessageServerHandler类继承MessageToMessageDecoder<T>类，T指定了decode() 方法的输入参数msg 的类型，它是必须实现的唯一方法，用来解码Lora网络服务器收到来自网关的数据包。由于服务器与网关之间采用的TCP连接，因此MessageToMessageDecoder将字节转换为消息。如下图所示，将解码的String将被添加到传出的List中，并转发给下一个ChannelInboundHandler。其设计见图7所示。在这之后还会涉及到（3.2.3.1.4 数据处理与入队）这一节中入队数据的发送，在Netty中整个业务模块是个循环过程。

<center><img src="https://document-store.oss-cn-shenzhen.aliyuncs.com/ByteBufToString_20190422205424.png?Expires=1587473664&OSSAccessKeyId=LTAIl4SxXuRjoq2z&Signature=FC7GTCRXLww92OTw7satl4QEqHw%3D" width="80%" height="60%" /></center>

#### 3.3.3.1.2 数据包解析

> ParseJsonHandler类继承ChannelIboundHandlerAdapter用来事件处理，此处用来解析JSON语句，通过channelRead方法来读取ChannelInboundHandler中的数据。本类中将分别解析LoRa模式终端数据、FSK模式终端数据和网关状态数据。在解析完之后，利用ChannelHandler有一个处理器方法叫做userEventTriggered()，它就是被设计用来满足这种特殊的用户需求，本次用来针对三种解析调用ChannelPipeline中下一个ChannlInboundHandler的userEventTriggered(Channelercontext,Object)方法。

#### 3.3.3.1.3 数据持久化

> PersistDataHandler类同样继承ChannelIboundHandlerAdapter用来事件处理，重写userEventTriggered()方法以处理自定义事件，此处结合Hibernate用来将解析后数据保存到Mysql。

#### 3.3.3.1.4 数据处理与入队

> MacDataHandler类扩展了ChannelInboundAdapter，用来处理收到的数据。具体见3.3.3.1.1节所示的图的事件流，也是利用userEventTriggered()方法，通过不同的帧头构造下行数据，将下行数据入队，等待下行队列的发送。此过程中若是非确认帧数据则无需构造下行回复包，若是确认帧数据则需构造下行回复包。

#### 3.3.3.2 下行业务介绍

#### 3.3.3.2.1 下行客户端运行中断开重连

> MyClientInboundHandler类继SimpleChannelInboundHandler类，用与程序运行中连接断掉需要重连，在ChannelHandler监测连接是否断掉，断掉的话要重连。该类实现了三个方法，channelInactive(), channelRead0()和exceptionCaught()。channelInactive()客户端与服务器之间连接断开；channelRead0()用于客户端读取服务端信息；exceptionCaught() 事件处理方法是当出现 Throwable 对象才会被调用， 即当 Netty 由于 IO 错误或者处理器在处理事件时抛出的异常时。在大部分情况下，捕获的异常应该被记录下来并且把关联的 channel 给关闭掉。然而这个方法的处理方式会在遇到不同异常的情况下有不同的实现，比如你可能想在关闭连接之前发送一个错误码的响应消息。
> 同样在Netty Client启动的时候需要重连，若未连接成功也需要重连，该类不是采用channelHanler方式，而是实现ChannelFutureListener 用来启动时监测是否连接成功，不成功的话重试。程序中是ConnectionClientListener实现了ChannelFutureListener接口。

#### 3.3.3.2.2 IdleStateHandler心跳机制

> 心跳是在TCP长连接中，客户端和服务端定时向对方发送数据包通知对方自己还在线，保证连接的有效性的一种机制。
> 在服务器和客户端之间一定时间内没有数据交互时,即处于idle状态时, 客户端或服务器会发送一个特殊的数据包给对方,当接收方收到这个数据报文后, 也立即发送一个特殊的数据报文, 回应发送方,此即一个PING-PONG 交互. 自然地,当某一端收到心跳消息后, 就知道了对方仍然在线, 这就确保 TCP 连接的有效性。
> 本系统是在客户端实现心跳机制，客户端添加IdleStateHandler心跳检测处理器，并添加自定义处理Handler类实现userEventTriggered()方法作为超时事件的逻辑处理；设定IdleStateHandler心跳检测每2秒进行一次写检测，如果2秒内write()方法未被调用则触发一次userEventTrigger()方法，实现客户端每四秒向服务端发送一次消息。

#### 3.3.3.2.3 下行数据包的出队与发送

> MessageClientHandler处理类继承ChannlInboundHandlerAdapter，实现自定义userEventTrigger()方法，如果出现超时时间就会被触发，包括读空闲超时或者写空闲超时；本系统设置每两秒进行一次写操作，从下行队列中读取要下发的数据，并通过tcp连接将数据发送到网关。

## 3.4 RabbitMq

> RabbitMQ是以AMQP协议实现的一种中间件产品，它可以支持多种操作系统，多种编程语言，几乎可以覆盖所有主流的企业级技术平台。
> AMQP是Advanced Message Queuing Protocol的简称，它是一个面向消息中间件的开放式标准应用层协议。AMQP定义了这些特性：

- 消息方向
- 消息队列
- 消息路由（包括：点到点和发布-订阅模式）
- 可靠性
- 安全性

> 本次针对路灯应用设计了两个队列，从Lora网络服务器的角度出发：

- lora_down：将灯信息发送给其他服务器的消息队列
- lora_up：接收其他服务器对灯的控制或反馈的消息队列
- send-lora：将灯信息发送给其他服务器的消息中的路由键
- receive-lora：接收其他服务器对灯的控制或反馈的消息中的路由键
  	

> 消息队列服务器的web后台如下图，可以看到消息服务的上下行两个队列信息。
>
> //图去除

> rabbitMq消息队列中的消息格式，即json数据的构造详细见百亚云的消息设计要求。

## 3.5 Redis

> Redis是一个开源的、高性能、基于键值对的缓存与存储系统，通过提供多种键值数据类型来适应不同场景下的缓存与存储需求。同时Redis的诸多高层级功能使其可以胜任消息队列、任务队列等不同的角色。Redis共有16个数据库（0～15），默认使用数据库0。本次LoRa网络服务器主要使用Redis来存储接收终端与其对应的密钥。如下图：

<center><img src="https://document-store.oss-cn-shenzhen.aliyuncs.com/redis_key_20190422213648.png?Expires=1587476208&OSSAccessKeyId=LTAIl4SxXuRjoq2z&Signature=9SQ%2B8N7Y0msxcQARx7WElwFnB0o%3D" width="70%" height="70%" /></center>

## 3.6 Mysql

> Mysql是一个关系型数据库管理系统，本软件利用jpa基于Hibernate的数据表创建，其共有三个系统数据表和现阶段的三个应用数据表。
> 三个系统数据表，分别是网关状态信息表，终端LoRa模式下信息和终端FSK模式下信息，三个表字段信息如下：

<center><img src="https://document-store.oss-cn-shenzhen.aliyuncs.com/gateway_stat_20190422215641.png?Expires=1587477401&OSSAccessKeyId=LTAIl4SxXuRjoq2z&Signature=zdoWnhFSt6hRhgsYg2fv%2Fe9QpcU%3D" width="70%" height="70%" /></center>
<center><img src="https://document-store.oss-cn-shenzhen.aliyuncs.com/end_lora_20190422215706.png?Expires=1587477426&OSSAccessKeyId=LTAIl4SxXuRjoq2z&Signature=ykiBlGUUnEyznvjNFJKVM3xlWMU%3D" width="70%" height="70%" /></center>
<center><img src="https://document-store.oss-cn-shenzhen.aliyuncs.com/end_fsk_20190422215722.png?Expires=1587477442&OSSAccessKeyId=LTAIl4SxXuRjoq2z&Signature=MHrMfhWN41DbO0%2BIPvJs4eFE6nQ%3D" width="70%" height="70%" /></center>

> 现阶段关于路灯控制主要有三张表，分别是终端的信息表、路灯最近数据关系表和路灯历史数据表。

<center><img src="https://document-store.oss-cn-shenzhen.aliyuncs.com/deviceInfo_20190422220023.png?Expires=1587477623&OSSAccessKeyId=LTAIl4SxXuRjoq2z&Signature=qdCnV07jBq%2FXAYLM6V2ov1ZLWhk%3D" width="70%" height="70%" /></center>
<center><img src="https://document-store.oss-cn-shenzhen.aliyuncs.com/device_light_20190422220101.png?Expires=1587477661&OSSAccessKeyId=LTAIl4SxXuRjoq2z&Signature=4VLnRrfyoQaZ8efQ22qOG%2B1Q98A%3D" width="70%" height="70%" /></center>
<center><img src="https://document-store.oss-cn-shenzhen.aliyuncs.com/lightInfo_20190422220046.png?Expires=1587477646&OSSAccessKeyId=LTAIl4SxXuRjoq2z&Signature=2fe9BUJ565fm%2BoC4OXU9C2ZKjGk%3D" width="70%" height="70%" /></center>

# 4.系统后续开发与问题

## 4.1 后续开发

### 4.1.1 终端设备功能点识别

> 针对不同应用或设备注册实现功能点识别。

### 4.1.2 具体业务二次开发

> 针对不同业务场景，比如现在实现了路灯控制，以后涉及到其他具体应用需针对具体应用进行二次开发。

### 4.1.3 Class B开发

> class b现在主要问题是和终端两个实现发送与窗口打开同步问题，具体需注意的时间在4.2.1节中均已提到，服务器方面基本已实现该模式，主要是后续和终端进行联调。

### 4.1.4 系统进行分布式分离

> 任何的服务器的性能都是有极限的，面对海量的互联网访问需求，是不可能单靠一台服务器或者一个CPU来承担的。所以我们一般都会在运行时架构设计之初，就考虑如何能利用多个CPU、多台服务器来分担负载，这就是所谓分布的策略。因此本服务器在以后面对海量终端设备和多网关时需考虑服务分离与分布式部署，进行业务解耦,方便扩容,方便系统按模块升级,模块重用等。

## 4.2 相关问题

### 4.2.1 时间戳问题

#### 4.2.1.1 UTC时间与GPS时间差

> UTC时间起点为1970年1月1日0h0m0s，GPS时间起点为1980年1月6日0h0m0s，因此两者的时间差为315964800秒，如下图:

<center><img src="https://document-store.oss-cn-shenzhen.aliyuncs.com/GPSandUTC_start_20190422214209.png?Expires=1587476529&OSSAccessKeyId=LTAIl4SxXuRjoq2z&Signature=zusFJhR4BiG39OCJXLksYOYztSE%3D" width="70%" height="50%" /></center>

#### 4.2.1.2 GPS与UTC时间存在的偏差值

> 截止2018年，GPS时间与UTC时间相差18秒，其具体解释见下图:

<center>![](https://document-store.oss-cn-shenzhen.aliyuncs.com/GPSandUTC_deviation_20190422214411.png?Expires=1587476651&OSSAccessKeyId=LTAIl4SxXuRjoq2z&Signature=8ib9ggqM0QYF%2BeSEg17Vs8AO1aQ%3D)</center>

### 4.2.2 性能检测与测试

> 性能与压力测试，此次测试是网关与LoRa服务器之间的通信及LoRa服务器的处理与相应，测试使用的是jmeter，详细见报告文档。

#### 4.2.2.1 jmeter介绍

> Apache JMeter是100%纯JAVA桌面应用程序，被设计为用于测试客户端/服务端结构的软件(例如web应用程序)。它可以用来测试静态和动态资源的性能，例如：静态文件，Java Servlet,CGI Scripts,Java Object,数据库和FTP服务器等等。JMeter可用于模拟大量负载来测试一台服务器，网络或者对象的健壮性或者分析不同负载下的整体性能。其他介绍可见Apache  jmeter官网https://jmeter.apache.org/或博客https://www.jianshu.com/p/de29e4ff71d0

#### 4.2.2.2 jmeter测试说明

- 测试报告说明：
  jmeter引入了Dashboard Report，用于生成HTML页面格式图形化报告的扩展模块。
- 直接生成HTML报告：
  jmeter -n -t xxx.jmx -l xxx.csv -e -o xxx-reports
  （注意:xxx.csv与xxx-reports必须在保存目录中原来不存在）
- 使用之前的测试结果，生成测试报告：
  - jmeter -n -t aaa.jmx -l bbb.jtl
  - jmeter -g bbb.jtl -o ccc-reports
- 报告详解可参考博客：https://www.jianshu.com/p/4f32918d66bb

#### 4.2.2.3 后续测试说明

> LoRa网络服务器涉及到两个交互，既于网关之间通信，又与上层应用（或者虚拟设备）通信。上面利用jmeter主要是测试与网关之间的通信与服务器的响应，而与上层的消息队列交互并未测试，消息队列核心是解耦、异步和削峰，主要关注其吞吐量、时效性、可用性、消息可靠性等。

​	












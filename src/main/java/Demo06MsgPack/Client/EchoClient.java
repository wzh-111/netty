package Demo06MsgPack.Client;

import Demo06MsgPack.msgPack.MsgPackDecoder;
import Demo06MsgPack.msgPack.MsgPackEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class EchoClient {
    private String host;
    private int port;
    private int sendNum;

    public EchoClient(String host, int port, int sendNum) {
        this.host = host;
        this.port = port;
        this.sendNum = sendNum;
    }

    public void run() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 处理粘包，使用LengthFieldBasedFrameDecoder在消息头部增加两个字节，表示消息的长度
                            socketChannel.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
                            socketChannel.pipeline().addLast("msg decoder", new MsgPackDecoder());
                            socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
                            socketChannel.pipeline().addLast("msg encoder", new MsgPackEncoder());
                            socketChannel.pipeline().addLast(new EchoClientHandler(sendNum));
                        }
                    });
            ChannelFuture f = b.connect(host, port).sync();
            System.out.println("客户端启动成功, host: " + host + " ,port: " + port);
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new EchoClient("127.0.0.1", 8080, 100).run();
    }
}

package Demo08Protocal;

import Demo08Protocal.Codec.NettyMessageDecoder;
import Demo08Protocal.Codec.NettyMessageEncoder;
import Demo08Protocal.Enum.NettyConstant;
import Demo08Protocal.Handler.HeartBeatRespHandler;
import Demo08Protocal.Handler.LoginAuthRespHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class NettyServer {
    public void bind() throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        try {
            b.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyMessageDecoder(1024*1024, 4, 4));
                            socketChannel.pipeline().addLast(new NettyMessageEncoder());
                            socketChannel.pipeline().addLast(new ReadTimeoutHandler(50));
                            socketChannel.pipeline().addLast(new LoginAuthRespHandler());
                            socketChannel.pipeline().addLast(new HeartBeatRespHandler());
                        }
                    });
            ChannelFuture f = b.bind(NettyConstant.REMOTEIP, NettyConstant.PORT).sync();
            System.out.println("netty server start ok: "+ NettyConstant.REMOTEIP+" : "+NettyConstant.PORT);
            f.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyServer().bind();
    }
}

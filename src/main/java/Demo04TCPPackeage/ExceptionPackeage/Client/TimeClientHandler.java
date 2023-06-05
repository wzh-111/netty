package Demo04TCPPackeage.ExceptionPackeage.Client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

public class TimeClientHandler extends ChannelHandlerAdapter {

    private byte[] req;
    private int counter;

    public TimeClientHandler() {
        req = ("time"+System.getProperty("line.separator")).getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 客户端和服务端TCP链路建立成功之后，Netty的NIO线程会调用channelActive方法, 发送
     * 查询时间的指令给服务端
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf message = null;
        for (int i=0; i<200; i++) {
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("当前时间: " + body + " ; the counter is: " + ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}

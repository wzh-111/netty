package Demo08Protocal.Handler;

import Demo08Protocal.Enum.MessageType;
import Demo08Protocal.pojo.Header;
import Demo08Protocal.pojo.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class HeartBeatRespHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        // 返回心跳应答消息
        if (message.getHeader()!=null && message.getHeader().getType()== MessageType.HEARTBEAT_REQ.value()) {
            System.out.println("receive client heart beat message: " + message);
            NettyMessage heartBeatResp = buildHeartBeatResp();
            System.out.println("send heart beat resp to client: " + heartBeatResp);
            ctx.writeAndFlush(heartBeatResp);
        } else {
            ctx.fireChannelRead(msg);
        }

    }

    private NettyMessage buildHeartBeatResp() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value());
        message.setHeader(header);
        return message;
    }
}

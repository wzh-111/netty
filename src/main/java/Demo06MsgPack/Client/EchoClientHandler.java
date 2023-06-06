package Demo06MsgPack.Client;

import Demo06MsgPack.UserInfo;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoClientHandler extends ChannelHandlerAdapter {
    private int sendNum;

    public EchoClientHandler(int sendNum) {
        this.sendNum = sendNum;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接建立成功，准备发送数据");
        UserInfo[] userInfos = new UserInfo[sendNum];
        for (int i=0; i<sendNum; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setAge(i);
            userInfo.setName("name " + i);
            userInfos[i] = userInfo;
        }

        for (UserInfo userInfo: userInfos) {
            ctx.writeAndFlush(userInfo);
        }
        System.out.println("客户端数据发送成功");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("收到服务端传来的序列化消息: " + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}

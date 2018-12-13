package cn.hylexus.jt808.client;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class ClientHandler extends ChannelHandlerAdapter{
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try{
			//接收服务端返回的数据
			String data = (String)msg;
			System.out.println("Client接收到的数据："+data);
		}finally{
			//因为没有进行写操作，所以需要自己来释放
			ReferenceCountUtil.release(msg);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}

package cn.hylexus.jt808.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Client {
	public static void main(String[] args) throws Exception {
		//1、创建线程组，客户端和服务端不一样，客户端只需要一个即可
		EventLoopGroup workGroup  = new NioEventLoopGroup();
		//创建辅助类,和服务端的不一样，服务端的是ServerBootstrap，而客户端只是BootStrap
		Bootstrap bs = new Bootstrap();
		//加入线程组
		bs.group(workGroup)
		//指定通道类型
		.channel(NioSocketChannel.class)
		//绑定事件处理器
		.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				//设置特殊分隔符
				ByteBuf buf  = Unpooled.copiedBuffer(new byte[] { 0x7e });
				ByteBuf buf2  = Unpooled.copiedBuffer(new byte[] { 0x7e});				
				
				sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf,buf2));
				//设置字符串形式的编码
				sc.pipeline().addLast(new StringEncoder());
				//设置字符串解码
				sc.pipeline().addLast(new StringDecoder());
				sc.pipeline().addLast(new ClientHandler());
			}
		});
		
		//链接服务端
		ChannelFuture cf = bs.connect("127.0.0.1", 20048).sync();
		//给服务端写数据,现在不再需要是写缓冲区了，因为我们上面的配置加了字符串的编码，直接写入字符串即可。
		cf.channel().writeAndFlush("0x7E0200003801328226177202C300000000200C00C30245427806BCC868007A02E4001F18112710100301040007D300030202DA2504000000002B040021000030011931010F1F0x7E");  //记得加上$_，因为这个分隔符是拿来判断消息是否该发送的。
		
		//异步监听管道的关闭，如果关闭了就往下继续执行
		cf.channel().closeFuture().sync();
		workGroup.shutdownGracefully();
	}
}

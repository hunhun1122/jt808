package cn.hylexus.jt808.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;



public class test2 {

	public static void main(String[] args) throws UnknownHostException, IOException {
		//1.创建客户端Socket，指定服务器地址和端口
        Socket so=new Socket("localhost", 20048);//端口号要和服务器端相同
        //2.获取输出流，向服务器端发送登录的信息
        OutputStream os=so.getOutputStream();//字节输出流
        PrintWriter pw=new PrintWriter(os);//字符输出流
        BufferedWriter bw=new BufferedWriter(pw);//加上缓冲流
        bw.write("7E0200003801328226177202C300000000200C00C30245427806BCC868007A02E4001F18112710100301040007D300030202DA2504000000002B040021000030011931010F1F7E");
        bw.flush();
        so.shutdownOutput();//关闭输出流
        //3.关闭资源
//        bw.close();
//        pw.close();
//        os.close();
//        so.close();
		
		
	}
	
}

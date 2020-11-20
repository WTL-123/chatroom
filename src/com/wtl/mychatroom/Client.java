package com.wtl.mychatroom;

import java.io.*;
import java.net.Socket;

/**
 * 在线聊天室：客户端
 */
public class Client {
    public static void main(String[] args) throws IOException {
        System.out.println("-----client-----");
//创建Socket建立连接
        Socket client = new Socket("localhost", 8888);
        System.out.println("请输入用户名：");
        String name=ChatRoomUtils.getStringFromConsole();
//客户端发送消息
        new Thread(new Send(client,name)).start();
//客户端接收消息
        new Thread(new Receive(client)).start();
    }
}


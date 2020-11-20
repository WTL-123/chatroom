package com.wtl.mychatroom;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 客户端接收消息类adasdsdasda
 */
public class Receive implements Runnable {
    private DataInputStream dis;
    private Socket client;
    private boolean isRunning;
    public Receive(Socket client){
        this.client=client;
        isRunning=true;
        try {
            this.dis=new DataInputStream(client.getInputStream());
        } catch (IOException e) {
            System.out.println("---1---");
            release();
        }
    }
    @Override
    public void run() {
        while (isRunning){
            String msg=receive();
            if (!msg.equals("")){
                System.out.println(msg);
            }
        }
    }
    //接收消息
    private String receive(){
        String msg="";
        try {
            msg = dis.readUTF();
        } catch (IOException e) {
            System.out.println("---2---");
            release();
        }
        return msg;
    }
    //释放资源
    private void release(){
        this.isRunning=false;
        ChatRoomUtils.close(dis,client);
    }
}


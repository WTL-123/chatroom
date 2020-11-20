package com.wtl.mychatroom;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * 客户端发送消息类
 */
public class Send implements Runnable {
    private BufferedReader console;
    private DataOutputStream dos;
    private Socket client;
    private boolean isRunning;
    private String name;
    public Send(Socket client,String name){
        this.client=client;
        this.console=new BufferedReader(new InputStreamReader(System.in));
        isRunning=true;
        this.name=name;
        try {
            this.dos = new DataOutputStream(client.getOutputStream());
            send(name);
        } catch (IOException e) {
            System.out.println("--1--");
            release();
        }
    }
    @Override
    public void run() {
        while (isRunning){
            String msg=ChatRoomUtils.getStringFromConsole();
            if (!msg.equals("")){
                send(msg);
            }
        }
    }
    //发送消息
    private void send(String msg){
        try {
            dos.writeUTF(msg);
            dos.flush();
        } catch (IOException e) {
            System.out.println("--3--");
            release();
        }
    }
    //释放资源
    private void release(){
        this.isRunning=false;
        ChatRoomUtils.close(dos,client);
    }
}


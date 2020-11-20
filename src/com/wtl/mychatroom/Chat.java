package com.wtl.mychatroom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 在线聊天室：服务端
 */
public class Chat {
    private static CopyOnWriteArrayList<Channel> all=new CopyOnWriteArrayList<>();
    public static void main(String[] args) throws IOException {
        System.out.println("-----server-----");
//创建ServerSocket
        ServerSocket server = new ServerSocket(8888);
        while(true) {
//阻塞式等待连接
            Socket accept = server.accept();
            System.out.println("一个客户端建立了连接");
            Channel c=new Channel(accept);
            all.add(c);//管理所有的成员
            new Thread(c).start();
        }
    }
    static class Channel implements Runnable{
        private DataInputStream dis;
        private DataOutputStream dos;
        private Socket accept;
        private boolean isRunning;
        private String name;
        public Channel(Socket accept){
            this.accept=accept;
            try {
                this.dis = new DataInputStream(accept.getInputStream());
                this.dos = new DataOutputStream(accept.getOutputStream());
                this.isRunning=true;
                this.name=receive();
                send("欢迎你的到来");
                sendOthers(this.name+"来到了聊天室",true);
            } catch (IOException e) {
                System.out.println("-----1-----");
                release();
            }
        }
        //接收消息
        private String receive(){
            String msg="";
            try {
                msg = dis.readUTF();
            } catch (IOException e) {
                System.out.println("-----2-----");
                release();
            }
            return msg;
        }
        //发送消息
        private void send(String msg){
            try {
                dos.writeUTF(msg);
                dos.flush();
            } catch (IOException e) {
                System.out.println("-----3-----");
                release();
            }
        }

        /**
         * 群聊：获取自己的消息，发给其他人
         * 私聊：约定数据格式：@xxx：msg
         * @param msg
         */
        private void sendOthers(String msg,boolean isSys){
            boolean isPrivate=msg.startsWith("@");
            if (isPrivate){
                int idx = msg.indexOf(":");
                String targetName=msg.substring(1,idx);
                msg=msg.substring(idx+1);
                for (Channel other:all){
                    if (other.name.equals(targetName)){
                        other.send(this.name+"悄悄地对你说："+msg);
                    }
                }
            }else {
                for (Channel other : all) {
                    if (other == this) {
                        continue;
                    }
                    if (!isSys) {
                        other.send(this.name + "对所有人说：" + msg);
                    } else {
                        other.send(msg);
                    }
                }
            }
        }
        //释放资源
        private void release(){
            this.isRunning=false;
            ChatRoomUtils.close(dis,dos,accept);
            all.remove(this);
            sendOthers(this.name+"离开了大家庭...",true);
        }

        @Override
        public void run() {
            while (isRunning){
                String msg=receive();
                if (!msg.equals("")){
                    sendOthers(msg,false);
                }
            }
        }
    }
}

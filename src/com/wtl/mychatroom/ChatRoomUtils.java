package com.wtl.mychatroom;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 工具类
 */
public class ChatRoomUtils {
    /**
     * 释放资源
     */
    public static void close(Closeable...targets){
        for (Closeable target:targets){
            if(null!=target){
                try {
                    target.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 从控制台获取消息
     * @return
     */
    public static String getStringFromConsole(){
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        try {
            return console.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}

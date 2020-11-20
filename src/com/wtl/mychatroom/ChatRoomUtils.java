package com.wtl.mychatroom;

import java.io.Closeable;
import java.io.IOException;

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
}

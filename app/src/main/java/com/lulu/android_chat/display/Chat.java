package com.lulu.android_chat.display;

import org.litepal.crud.LitePalSupport;
import org.litepal.exceptions.DataSupportException;

public class Chat extends LitePalSupport {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SEND = 1;
    private String content;
    private int type;
    private String time;

    public Chat(String content,int type,String time){
        this.content = content;
        this.type = type;
        this.time = time;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

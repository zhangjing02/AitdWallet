package com.tianqi.baselib.rxhttp.bean;

/**
 * Create by zhangjing on 2020/11/19.
 * Describe :
 */
public class GetMessageBean {

    private boolean isRead;
    private String messageTittle;
    private String messageContent;
    private String time;
    private int type;  //转账消息，还是系统消息

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getMessageTittle() {
        return messageTittle;
    }

    public void setMessageTittle(String messageTittle) {
        this.messageTittle = messageTittle;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

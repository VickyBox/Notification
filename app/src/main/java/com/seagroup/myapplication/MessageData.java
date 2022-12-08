package com.seagroup.myapplication;

public class MessageData {

    public MessageData(String msg, long timeStap, long userID, String userName, int avator) {
        this.avator = avator;
        this.msg = msg;
        this.timeStap = timeStap;
        this.userID = userID;
        this.userName = userName;
    }
    private String msg;
    private long timeStap;
    private long userID;
    private String userName;
    private int avator;

    public long getTimestampSecond() {
        return timeStap;
    }

    public String getMsg() {
        return msg;
    }

    public long getUserId() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public int getAvator() {
        return avator;
    }
}

package com.example.zaid.firechat;

import java.util.Date;

/**
 * Created by Zaid on 5/30/2017.
 */

public class Messages {
    private String user, client;
    private long time;

    public Messages(String u,String c){
        user = u;
        client = c;
        time = new Date().getTime();
    }

    public Messages(){
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

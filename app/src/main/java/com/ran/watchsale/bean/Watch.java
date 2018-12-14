package com.ran.watchsale.bean;

/**
 * 作者 : 527633405@qq.com
 * 时间 : 2016/6/21 0021
 */
public class Watch {
    private long id;//服务器分配给手表的id
    private String number;//手表中的sim卡号码
    private String time;//手表激活时间
    private boolean state;//手表信息上传到服务器的状态

    public Watch(long id, String number, String time, boolean state) {
        this.id = id;
        this.number = number;
        this.time = time;
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Watch{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", time='" + time + '\'' +
                ", state=" + state +
                '}';
    }
}

package com.example.qianz.englishcorner.util;

/**
 * Created by qianz on 2018/4/20.
 */

public class Suggestion {

    private int beg;
    private int len;

    public Suggestion(int beg, int len) {
        this.beg = beg;
        this.len = len;
    }

    public int getBeg() {
        return beg;
    }

    public void setBeg(int beg) {
        this.beg = beg;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }
}

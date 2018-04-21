package com.example.qianz.englishcorner.util;

import java.util.ArrayList;

/**
 * Created by qianz on 2018/4/20.
 */

public class Suggestion {

    private int beg;
    private int end;
    private String erro;
    private ArrayList<String> replacement;

    public Suggestion(int beg, int end , String erroMessage) {
        this.beg = beg;
        this.end = end;
        this.erro = erroMessage;
        replacement = new ArrayList<>();
    }

    public void addReplacement(String replace){
        replacement.add(replace);
    }

    public int getBeg() {
        return beg;
    }

    public void setBeg(int beg) {
        this.beg = beg;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public ArrayList<String> getReplacement() {
        return replacement;
    }
}

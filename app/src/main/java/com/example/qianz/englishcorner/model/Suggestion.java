package com.example.qianz.englishcorner.model;

import java.util.ArrayList;

/**
 * Created by qianz on 2018/4/20.
 */

/**
 * 语法改错建议
 */
public class Suggestion {

    /**
     * 语法错误的起始位置
     */
    private int beg;
    /**
     * 语法错误的结束位置
     */
    private int end;
    /**
     * 语法错误的描述
     */
    private String erro;
    /**
     * 修改的建议
     */
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

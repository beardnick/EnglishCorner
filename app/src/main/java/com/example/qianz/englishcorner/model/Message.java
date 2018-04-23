package com.example.qianz.englishcorner.model;

import android.app.Activity;
import android.text.SpannableString;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

/**
 * Created by qianz on 2018/4/20.
 */

public class Message implements IMessage{

    private Author author;
    private String text;
    private Date createAt;
    private Activity context;

    public Message(Author author, String text , Activity activity) {
        this.author = author;
        this.text = text;
        this.context = activity;
        createAt = new Date();
    }

    public Message(Author author , String text , Date date , Activity activity){
        this.author = author;
        this.text = text;
        this.context = activity;
        createAt = date;
    }

    public Message(Author author, String text) {
        this.author = author;
        this.text = text;
        createAt = new Date();
    }

    @Override
    public String getId() {
        return author.getId();
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public IUser getUser() {
        return author;
    }

    @Override
    public Date getCreatedAt() {
        return createAt;
    }

    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }
}

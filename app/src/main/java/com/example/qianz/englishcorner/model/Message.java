package com.example.qianz.englishcorner.model;

import android.text.SpannableString;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

/**
 * Created by qianz on 2018/4/20.
 */

public class Message implements IMessage{

    private SpannableString span;
    private Author author;
    private String text;
    private Date createAt;

    public Message(Author author, String text) {
        this.author = author;
        this.text = text;
        this.createAt = new Date();

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

    public SpannableString getSpan() {
        return span;
    }

    public void setSpan(SpannableString span) {
        this.span = span;
    }
}

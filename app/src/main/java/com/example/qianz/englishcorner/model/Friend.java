package com.example.qianz.englishcorner.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by qianz on 2018/4/22.
 */

public class Friend extends BmobObject {

    private Author user;
    private Author friend;

    public Friend(Author user, Author friend) {
        this.user = user;
        this.friend = friend;
    }

    public Author getUser() {
        return user;
    }

    public void setUser(Author user) {
        this.user = user;
    }

    public Author getFriend() {
        return friend;
    }

    public void setFriend(Author friend) {
        this.friend = friend;
    }
}


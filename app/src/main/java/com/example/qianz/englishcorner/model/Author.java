package com.example.qianz.englishcorner.model;

import com.stfalcon.chatkit.commons.models.IUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by qianz on 2018/4/20.
 */

public class Author extends BmobUser implements IUser {

    private String id;
    private String avatar;
    // TODO: 2018/4/22 完成Author类的头像操作
    private BmobFile avatarFile;

    public Author(){

    }

    public Author(String id, String name, String avatar) {
        this.id = id;
        this.setUsername(name);
        this.avatar = avatar;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return getUsername();
    }

    @Override
    public String getAvatar() {
        return avatar;
    }
}

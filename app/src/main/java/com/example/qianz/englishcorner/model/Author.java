package com.example.qianz.englishcorner.model;

import com.stfalcon.chatkit.commons.models.IUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by qianz on 2018/4/20.
 */

public class Author extends BmobUser implements IUser {

    private String id;
    // TODO: 2018/4/22 完成Author类的头像操作
    private BmobFile avatar;

    public Author(String id, String name) {
        this.id = id;
        this.setUsername(name);
    }

    @Override
    public String getId() {
        return getObjectId();
    }

    @Override
    public String getName() {
        return getUsername();
    }

    @Override
    public String getAvatar() {
        if(avatar != null){
            return avatar.getFileUrl();
        }else {
            return null;
        }
    }

    public void setAvatar(BmobFile file){
        avatar = file;
    }
}

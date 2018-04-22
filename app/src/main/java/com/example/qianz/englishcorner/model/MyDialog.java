package com.example.qianz.englishcorner.model;

import android.util.Log;

import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;
import java.util.ArrayList;

/**
 * Created by qianz on 2018/4/22.
 */

public class MyDialog implements IDialog {

    // TODO: 2018/4/22 完成dialog的数据操作
    private static final String TAG = "MyDialog";

    private Author friend;
    private Message lastestMessage;


    public MyDialog(Author friend) {
        this.friend = friend;
    }

    public Author getFriend() {
        return friend;
    }

    public void setFriend(Author friend) {
        this.friend = friend;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getDialogPhoto() {
        return friend.getAvatar();
    }

    @Override
    public String getDialogName() {
        return friend.getName();
    }

    @Override
    public ArrayList<Author> getUsers() {
        ArrayList<Author> array = new ArrayList<>();
        array.add(friend);
        return array;
    }

    @Override
    public IMessage getLastMessage() {
        return  new Message(friend , "");
    }

    @Override
    public void setLastMessage(IMessage message) {
        Log.i(TAG, "setLastMessage: arrive here");
    }

    @Override
    public int getUnreadCount() {
        return 0;
    }
}

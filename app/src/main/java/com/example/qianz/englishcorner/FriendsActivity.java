package com.example.qianz.englishcorner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.qianz.englishcorner.model.Author;
import com.example.qianz.englishcorner.model.Friend;
import com.example.qianz.englishcorner.model.MyDialog;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.helper.BmobNative;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class FriendsActivity extends AppCompatActivity {

    Author user;
    private static final String TAG = "FriendsActivity";

    DialogsList dialogsList;
    DialogsListAdapter<MyDialog> dialogAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        onBindView();
        initAdapter();
        oninitData();
    }

    public void onBindView(){
        dialogsList = (DialogsList) findViewById(R.id.dialog_list);
    }

    public void initAdapter(){
        dialogAdapter = new DialogsListAdapter<MyDialog>(new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Picasso.with(FriendsActivity.this).load(url).into(imageView);
            }
        });
        dialogsList.setAdapter(dialogAdapter);
    }

    public void oninitData(){
        user = BmobUser.getCurrentUser(Author.class);
        Log.i(TAG, "currentUser：" + user.getObjectId() + user.getName() + user.getCreatedAt());
        BmobQuery<Friend> q1 = new BmobQuery<>();
        q1.addWhereEqualTo("user" , new BmobPointer(user));
//        q1.include("friend");
        BmobQuery<Friend> q2 = new BmobQuery<>();
        q2.addWhereEqualTo("friend" , new BmobPointer(user));
//        q2.include("user");
        List<BmobQuery<Friend>> queries = new ArrayList<>();
        queries.add(q1);
        queries.add(q2);
        BmobQuery<Friend> mainQuery = new BmobQuery<>();
        BmobQuery<Friend> query = mainQuery.or(queries);
        query.include("user,friend");
        query.findObjects(new FindListener<Friend>() {
            @Override
            public void done(List<Friend> list, BmobException e) {
                if(e == null){
                    ArrayList<MyDialog> dialogs = new ArrayList<>();
                    for (Friend x: list
                         ) {
                        if(x.getUser().getObjectId().equals(user.getObjectId())){
                           dialogs.add(new MyDialog(x.getFriend()));
                        }else {
                           dialogs.add(new MyDialog(x.getUser()));
                        }
                        Log.i(TAG, "user : "
                                + x.getUser().getUsername() + x.getUser().getUpdatedAt()+ "\n"
                                + "friend : " + x.getFriend().getUsername() + x.getFriend().getUpdatedAt());
                        Log.i(TAG, "userid : "
                                + x.getUser().getObjectId() + "\n"
                                + "friendid : " + x.getFriend().getObjectId());
                    }
                    dialogAdapter.setItems(dialogs);
                }else {
                    Log.i(TAG, "done: " + e.getMessage());
                }
            }
        });
        BmobQuery<Author> authorQuery = new BmobQuery<>();
        authorQuery.getObject("1b8f69ea0d", new QueryListener<Author>() {
            @Override
            public void done(Author author, BmobException e) {
                Log.i(TAG, "test : " + author.getName() + author.getObjectId() + author.getCreatedAt());
            }
        });
    }

}

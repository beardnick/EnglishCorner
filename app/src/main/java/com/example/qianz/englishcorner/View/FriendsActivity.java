package com.example.qianz.englishcorner.View;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.qianz.englishcorner.R;
import com.example.qianz.englishcorner.model.Author;
import com.example.qianz.englishcorner.model.Friend;
import com.example.qianz.englishcorner.model.MyDialog;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 朋友界面
 */
public class FriendsActivity extends AppCompatActivity {

    Author user;
    private DrawerLayout mDrawerLayout;
    private static final String TAG = "FriendsActivity";
    private NavigationView navigationView;

    DialogsList dialogsList;
    DialogsListAdapter<MyDialog> dialogAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        onInitView();
        initAdapter();
        oninitData();
        oninitEvent();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Toast.makeText(this,"add friends",Toast.LENGTH_SHORT);
        return true;
    }

    /**
     * 初始化UI组件
     */
    public void onInitView(){
        dialogsList = (DialogsList) findViewById(R.id.dialog_list);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout =(DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView =(NavigationView)findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(FriendsActivity.this ,
                mDrawerLayout , toolbar , R.string.drawer_open , R.string.drawer_close );
        toggle.syncState();
        mDrawerLayout.setDrawerListener(toggle);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }
        navigationView.setCheckedItem(R.id.nav_call);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    /**
     * 初始化朋友列表的的适配器
     */
    public void initAdapter(){
        dialogAdapter = new DialogsListAdapter<MyDialog>(new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Picasso.with(FriendsActivity.this).load(url).into(imageView);
            }
        });
        dialogsList.setAdapter(dialogAdapter);
        dialogAdapter.setOnDialogClickListener(new DialogsListAdapter.OnDialogClickListener<MyDialog>() {
            @Override
            public void onDialogClick(MyDialog dialog) {
                Intent intent = new Intent(FriendsActivity.this , MainActivity.class);
                intent.putExtra("objectid" , dialog.getFriend().getObjectId());
                startActivity(intent);
            }
        });
    }

    /**
     * 查询朋友列表数据
     */
    public void oninitData(){
        user = BmobUser.getCurrentUser(Author.class);
        if(user.getObjectId().length() > 0){
            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        userInfoUtil(user);
                        Log.i(TAG, "connect : 连接成功");
                    }else {
                        Log.i(TAG, "connect : " + e.getMessage());
                    }
                }
            });
        }
        Log.i(TAG, "currentUser：" + user.getObjectId() + user.getName() + user.getCreatedAt());
        BmobQuery<Friend> q1 = new BmobQuery<>();
        q1.addWhereEqualTo("user" , new BmobPointer(user));
        BmobQuery<Friend> q2 = new BmobQuery<>();
        q2.addWhereEqualTo("friend" , new BmobPointer(user));
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
                           userInfoUtil(x.getFriend());
                        }else {
                           dialogs.add(new MyDialog(x.getUser()));
                           userInfoUtil(x.getUser());
                        }
                        Log.i(TAG, "user : "
                                + x.getUser().getUsername() + x.getUser().getAvatar()+ "\n"
                                + "friend : " + x.getFriend().getUsername()  + x.getFriend().getAvatar());
                    }
                    dialogAdapter.setItems(dialogs);
                }else {
                    Log.i(TAG, "done: " + e.getMessage());
                }
            }
        });
    }

    /**
     * 将用户登陆信息和朋友信息暂存到本地
     * @param author 用户或用户的朋友
     */
    public void userInfoUtil(Author author){
        BmobIMUserInfo info = new BmobIMUserInfo();
        info.setUserId(author.getObjectId());
        info.setName(author.getName());
        info.setAvatar(author.getAvatar());
        BmobIM.getInstance().updateUserInfo(info);
    }

    /**
     * 设置UI组件的事件监听者
     */
    public void oninitEvent(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.log_out){
                    user.logOut();
                    Intent intent = new Intent(FriendsActivity.this , LoginActivity.class);
                    startActivity(intent);
                    FriendsActivity.this.finish();
                }
                return  true;
            }
        });
    }
}

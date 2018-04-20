package com.example.qianz.englishcorner.customer;

import android.text.SpannableString;
import android.view.View;

import com.example.qianz.englishcorner.util.LanguageToolUtil;
import com.example.qianz.englishcorner.util.Suggestion;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessageHolders;

import java.util.ArrayList;

/**
 * Created by qianz on 2018/4/20.
 */

public class MyIncomingHolder extends MessageHolders.IncomingTextMessageViewHolder {

    public MyIncomingHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBind(IMessage message) {
        super.onBind(message);
        if(message.getText().length() > 0){
            LanguageToolUtil checker = new LanguageToolUtil(message.getText() , new LanguageToolUtil.OnCheckMessageListener() {
                @Override
                public void onSuccess(SpannableString span) {
                    text.setText(span);
                }
            });
        }
    }
}

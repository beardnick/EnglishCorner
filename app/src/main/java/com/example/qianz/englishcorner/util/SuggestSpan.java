package com.example.qianz.englishcorner.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.qianz.englishcorner.R;

/**
 * Created by qianz on 2018/4/21.
 */

public class SuggestSpan extends ClickableSpan {

    private Context context;
    private Suggestion suggestion;

    public SuggestSpan(Context context, Suggestion suggestion) {
        this.context = context;
        this.suggestion = suggestion;
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Suggestions");
        View contextView = LayoutInflater.from(context).inflate(R.layout.suggestion_dialog , null);
        TextView errorMessage = (TextView) contextView.findViewById(R.id.erro_message);
        TextView replacements = (TextView) contextView.findViewById(R.id.replacements);
        errorMessage.setTypeface(Typeface.createFromAsset(context.getAssets() , "fonts/Rosario-BoldItalic.ttf"));
        replacements.setTypeface(Typeface.createFromAsset(context.getAssets() , "fonts/Rosario-BoldItalic.ttf"));
        errorMessage.setText(suggestion.getErro());
        StringBuilder sb = new StringBuilder();
        for (String s: suggestion.getReplacement()
             ) {
            sb.append(s + "\n");
        }
        replacements.setText(sb.toString());
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setView(contextView);
        builder.show();
    }
}

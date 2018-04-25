package com.example.qianz.englishcorner.custom;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ReplacementSpan;

/**
 * Created by qianz on 2018/4/20.
 */

/**
 * 为字符串加入圆角矩形背景的Spannable
 */
public class RoundBackgroundColorSpan extends ReplacementSpan {

    /**
     * 背景颜色
     */
    private int backColor;
    /**
     * 文字颜色
     */
    private int textColor;
    /**
     * 圆角矩形边缘的圆的半径
     */
    private int radius = 10;
    private int size;

    public RoundBackgroundColorSpan(){
        textColor = Color.parseColor("#FFFFFF");
        backColor = Color.parseColor("#CD0000");
    }

    /**
     * 构造特定样式的背景
     * @param backColor 背景色
     * @param textColor 文字色
     */
    public RoundBackgroundColorSpan(int backColor, int textColor) {
        this.backColor = backColor;
        this.textColor = textColor;
    }

    public int getBackColor() {
        return backColor;
    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence charSequence, int i, int i1, @Nullable Paint.FontMetricsInt fontMetricsInt) {
        size =  (int)(paint.measureText(charSequence , i , i1) + 2 * radius);
        return size;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence charSequence, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
       paint.setColor(backColor);
       canvas.drawRoundRect(new RectF(x , y + paint.ascent() , x + size , y + paint.descent() ) , radius , radius , paint );
       paint.setColor(textColor);
       canvas.drawText(charSequence , start , end , x + radius , y , paint);
    }
}

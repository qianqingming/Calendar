package com.example.calendar.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;

import com.example.calendar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 吸顶悬浮
 */
public class StickyItemDecoration extends RecyclerView.ItemDecoration {


    private List<String> headList = new ArrayList<>();

    private Paint paint;//画矩形框
    private TextPaint textPaint;//画文字
    private float topGap;
    //private float textHeight;

    private DecorationCallback callback;


    public StickyItemDecoration(Context context,StickyItemDecoration.DecorationCallback decorationCallback) {

        this.callback = decorationCallback;

        paint = new Paint();
        paint.setColor(Color.parseColor("#008577"));

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.parseColor("#ffffff"));
        textPaint.setTextSize(context.getResources().getDimension(R.dimen.titleText));
        textPaint.setTextAlign(Paint.Align.CENTER);

        //textHeight = context.getResources().getDimension(R.dimen.titleText);
        topGap = context.getResources().getDimension(R.dimen.titleHeight);//标题栏高度
    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        //Log.v("aaaaaa","onDrawOver",new Exception());

        //可见子项的个数
        int childCount = parent.getChildCount();

        //int itemCount = state.getItemCount();

        int left = parent.getPaddingLeft();
        int right = parent.getWidth()-parent.getPaddingRight();
        int x = parent.getWidth()/2;


        for (int i=0;i<childCount;i++){
            View item = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(item);

            String text = callback.getYearAndMonth(position);

            //Log.d("aaa", String.valueOf(position) + (boolean)item.getTag());

            if (position == 0){
                //初始的时候
                c.drawRect(left,0,right,topGap,paint);
                c.drawText(text, x, 60, textPaint);
            }else {
                if ((boolean)item.getTag()==false){
                    //非标题的子项
                    if (item.getTop() <= 0){
                        c.drawRect(left,0,right,topGap,paint);
                        c.drawText(text, x, 60, textPaint);
                    }
                }else {
                    //处理标题重叠的效果
                    if (item.getTop() > 0 && item.getTop() <= topGap){
                        //获取上个月
                        String lastText = callback.getYearAndMonth(position - 1);
                        //绘制上个月的标题
                        c.drawRect(left,item.getTop()-topGap,right,item.getTop(),paint);
                        c.drawText(lastText, x, item.getTop()-20, textPaint);//topGap - (topGap - textHeight)/2
                        //绘制当前月的标题
                        c.drawRect(left,item.getTop(),parent.getWidth()-parent.getPaddingRight(),topGap+item.getTop(),paint);
                        c.drawText(text, x, 60+item.getTop(), textPaint);
                    }
                    if (item.getTop() <= 0){
                        c.drawRect(left,0,parent.getWidth()-parent.getPaddingRight(),topGap,paint);
                        c.drawText(text, x, 60, textPaint);
                    }
                }
            }

        }
    }

    public interface DecorationCallback {
        //获取当前子项对应的标题
        String getYearAndMonth(int position);
    }
}

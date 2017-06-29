package com.example.administrator.wuziqi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/8.
 */

public class wuziqiPanel extends View{

    private int mPanelWidth;
    private float mLineHeight;
    private  int MAX_LINE = 10;
    Paint mpaint = new Paint();
    private  int MAX_COUNT_IN_LINE = 5;

    Bitmap whitepiece;
    Bitmap blackpiece;
    float ratiopoeceoflineheight = 3*1.0f/4;

    boolean mIswhite = true;
    ArrayList<Point> mwhitearray = new ArrayList<>();
    ArrayList<Point> mblcakarray = new ArrayList<>();


    boolean mIsgameover;
    boolean mIswhitewinner;



    public wuziqiPanel(Context context, AttributeSet attrs) {
        super(context, attrs);

        setBackgroundColor(0x44ff0000);
        init();
    }

    private void init() {
        mpaint.setColor(Color.parseColor("#000000"));
        mpaint.setAntiAlias(true);
        mpaint.setDither(true);
        mpaint.setStyle(Paint.Style.STROKE);

        whitepiece = BitmapFactory.decodeResource(getResources(),R.drawable.stone_w2);
        blackpiece = BitmapFactory.decodeResource(getResources(),R.drawable.stone_b1);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int  widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int  widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int  heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int  heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize,heightSize);

        if(widthMode == MeasureSpec.UNSPECIFIED){
            width = heightSize;
        }else if(heightMode ==MeasureSpec.UNSPECIFIED){
            width =widthSize;
        }

        setMeasuredDimension(width,width);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mIsgameover){
            return false;
        }
    int action = event.getAction();
        if(action==MotionEvent.ACTION_UP){
            int x = (int) event.getX();
            int y = (int) event.getY();

            Point p = getalidpoint(x,y);
            if(mwhitearray.contains(p)||mblcakarray.contains(p)){
                return false;
            }

            if(mIswhite){
                mwhitearray.add(p);
            }else{
                mblcakarray.add(p);
            }
            invalidate();
            mIswhite = !mIswhite;

        }

        return true;
    }

    private Point getalidpoint(int x, int y) {
    return  new Point((int) (x/mLineHeight),(int)(y/mLineHeight));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth  = w;
        mLineHeight = mPanelWidth*1.0f/MAX_LINE;

        int piecewidth = (int) (mLineHeight*ratiopoeceoflineheight);

        whitepiece = Bitmap.createScaledBitmap(whitepiece,piecewidth,piecewidth,false);
        blackpiece = Bitmap.createScaledBitmap(blackpiece,piecewidth,piecewidth,false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPieces(canvas);
        checkgameover();
    }

    private void checkgameover() {
        boolean whitewin =  checkfiveinline(mwhitearray);
        boolean blackwin =  checkfiveinline(mblcakarray);

        if(whitewin||blackwin){
            mIsgameover=true;
            mIswhitewinner =whitewin;

            String text =  mIswhitewinner ?"白旗胜利":"黑棋胜利";
            Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkfiveinline(List<Point> points) {
        for(Point p :points){
            int x = p.x;
            int y = p.y;
            boolean win =checkHorizontal(x,y,points);
            if(win){return true;}
            win =checkRightDiagnal(x,y,points);
            if(win){return true;}
            win =checkVertical(x,y,points);
            if(win){return true;}
            win =checkLeftDiagonal(x,y,points);
            if(win){return true;}
        }



            return false;
    }

    private boolean checkHorizontal(int x, int y, List<Point> points) {
        int count = 1;
        for(int i = 1;i<MAX_COUNT_IN_LINE;i++){
            if(points.contains(new Point(x-i,y))){
                count++;

            }else {
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE){

            return true;
        }

        for(int i = 1;i<MAX_COUNT_IN_LINE;i++){
            if(points.contains(new Point(x+i,y))){
                count++;
            }else {
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE){
            return true;
        }


        return false;
    }

    private boolean checkVertical(int x, int y, List<Point> points) {
        int count = 1;
        for(int i = 1;i<MAX_COUNT_IN_LINE;i++){
            if(points.contains(new Point(x,y-i))){
                count++;
            }else {
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE){
            return true;
        }

        for(int i = 1;i<MAX_COUNT_IN_LINE;i++){
            if(points.contains(new Point(x,y+i))){
                count++;
            }else {
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE){
            return true;
        }


        return false;
    }

    private boolean checkLeftDiagonal(int x, int y, List<Point> points) {
        int count = 1;
        for(int i = 1;i<MAX_COUNT_IN_LINE;i++){
            if(points.contains(new Point(x-i,y+i))){
                count++;
            }else {
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE){
            return true;
        }

        for(int i = 1;i<MAX_COUNT_IN_LINE;i++){
            if(points.contains(new Point(x+i,y-i))){
                count++;
            }else {
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE){
            return true;
        }


        return false;
    }

    private boolean checkRightDiagnal(int x, int y, List<Point> points) {
        int count = 1;
        for(int i = 1;i<MAX_COUNT_IN_LINE;i++){
            if(points.contains(new Point(x-i,y-i))){
                count++;
            }else {
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE){
            return true;
        }

        for(int i = 1;i<MAX_COUNT_IN_LINE;i++){
            if(points.contains(new Point(x+i,y+i))){
                count++;
            }else {
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE){
            return true;
        }


        return false;
    }


    private void drawPieces(Canvas canvas) {
        for(int i = 0,n =mwhitearray.size();i<n;i++){
            Point whitepoint = mwhitearray.get(i);
            canvas.drawBitmap(whitepiece,(whitepoint.x+(1-ratiopoeceoflineheight)/2)*mLineHeight,(whitepoint.y+(1-ratiopoeceoflineheight)/2)*mLineHeight,null);
        }

        for(int i = 0,n =mblcakarray.size();i<n;i++){
            Point blackpoint = mblcakarray.get(i);
            canvas.drawBitmap(blackpiece,(blackpoint.x+(1-ratiopoeceoflineheight)/2)*mLineHeight,(blackpoint.y+(1-ratiopoeceoflineheight)/2)*mLineHeight,null);
        }
    }

    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float lineHeight = mLineHeight;
        for(int i=0;i<MAX_LINE;i++){
            int startX = (int) (lineHeight/2);
            int endX = (int) (w-lineHeight/2);
            int y = (int) ((0.5+i)*lineHeight);
            canvas.drawLine(startX,y,endX,y,mpaint);
            canvas.drawLine(y,startX,y,endX,mpaint);
        }
    }

    private static final String INSTANCE = "instance";
    private static final String INSTANCE_GAME_OVER = "instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY = "instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY = "instance_black_array";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle  = new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER,mIsgameover);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY,mwhitearray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY, mblcakarray);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle){
            Bundle bundle = (Bundle) state;
            mIsgameover= bundle.getBoolean(INSTANCE_GAME_OVER);
            mwhitearray = bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mblcakarray = bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}

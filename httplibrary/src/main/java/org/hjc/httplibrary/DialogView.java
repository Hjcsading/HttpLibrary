package org.hjc.httplibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * 西瓜切面式加载框
 */
public class DialogView extends View{

    /**
     * 边缘颜色， 内部分割线颜色（可以想成半径）, 扇形填充色
     */
    private static final int mOvalColor = R.color.overColor,
            mSplitColor = R.color.splitColor,
            mFanColor = R.color.innerColor;

    /**
     * 分割线宽度、西瓜皮厚度
     */
    private static final int mSplitWidth = R.dimen.split_width,
            mOvalWidth = R.dimen.oval_width;

    /**
     * 每块扇形瓜的角度
     */
    private static final int mAngle = 30;

    /**
     * 默认宽高,为屏幕短边的1/8
     */
    private int mDefaultHeight;

    /**
     * 圆的半径
     */
    private int mRing;

    /**
     * 空心圆画笔， 分割线画笔， 扇形画笔
     */
    private Paint mOvalLinePaint, mSplitPaint, mFanPaint;

    public DialogView(Context context) {
        this(context, null);
    }

    public DialogView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Point point = Tool.getWindowSize(context);
        mDefaultHeight = point.x > point.y ? point.y / 7 : point.x / 7;
        mRing = mDefaultHeight >> 1;

        /*空心圆画笔*/
        mOvalLinePaint = new Paint();
        mOvalLinePaint.setAntiAlias(true);
        mOvalLinePaint.setColor(ContextCompat.getColor(context, mOvalColor));
        mOvalLinePaint.setStrokeWidth(getResources().getDimensionPixelOffset(mOvalWidth));
        mOvalLinePaint.setStyle(Paint.Style.STROKE);

        /*扇形画笔*/
        mFanPaint = new Paint();
        mOvalLinePaint.setAntiAlias(true);
        mFanPaint.setColor(ContextCompat.getColor(context, mFanColor));
        mFanPaint.setStyle(Paint.Style.FILL);

        /*分割线画笔*/
        mSplitPaint = new Paint();
        mSplitPaint.setStrokeWidth(getResources().getDimensionPixelOffset(mSplitWidth));
        mSplitPaint.setColor(ContextCompat.getColor(context, mSplitColor));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*绘制空心圆*/
        canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, mRing - getResources().getDimensionPixelOffset(mOvalWidth), mOvalLinePaint);
        /*绘制实心圆*/
        canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, mRing - (getResources().getDimensionPixelOffset(mOvalWidth) << 1), mFanPaint);
        /*绘制分割线*/
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        for(int i = 0; i < 360 / mAngle; i++){
            canvas.rotate(mAngle);
            canvas.drawLine(0, 0, 0, mRing - (getResources().getDimensionPixelOffset(mOvalWidth) << 1), mSplitPaint);
        }
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(reMeasure(widthMeasureSpec), reMeasure(heightMeasureSpec));
    }

    /**
     * 重计算宽高，设置WRAP_CONTENT时的值，宽高默认值相同
     * @param measureSpec
     * @return
     */
    private int reMeasure(int measureSpec){
        if(MeasureSpec.getMode(measureSpec) == MeasureSpec.EXACTLY){
            mRing = MeasureSpec.getSize(measureSpec) >> 1 < mRing ? MeasureSpec.getSize(measureSpec) >> 1 : mRing;
            return measureSpec;
        }else {
            return MeasureSpec.makeMeasureSpec(mDefaultHeight, MeasureSpec.EXACTLY);
        }
    }
}

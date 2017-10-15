package util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import braude.motorsequence.TestActivity;
import database.RecordTest;

import database.TestType;

import android.view.VelocityTracker;
import android.support.v4.view.VelocityTrackerCompat;

/**
 * https://inducesmile.com/android/android-touch-screen-example-tutorial/
 */
public class TouchView extends View {

    Paint drawPaint;
    private Path path = new Path();

    private int next;
    private PointCluster[] pointClusters;
    private boolean mTestFlag;
    private TestActivity mTestActivity;

    private TextView mTextRounds;
    private boolean mFirstTouchFlag;
    private double startTime;
    private int roundID;

    private BlockingQueue<Item> itemQueue;
    private Thread consumer;
    private VelocityTracker mVelocityTracker = null;

    public TouchView(Context context) {
        super(context);
    }

    public TouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawPaint = new Paint(Paint.DITHER_FLAG);
        drawPaint.setAntiAlias(true);
        drawPaint.setColor(Color.BLACK);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
//        drawPaint.setPathEffect(new CornerPathEffect(10) );
        drawPaint.setStrokeWidth(3);
        setWillNotDraw(false);
        next = 0;
        mTestFlag = false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int width, int height) {
        super.onSizeChanged(w, h, width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, drawPaint);
    }

    public void setArray(PointCluster[] array) {
        this.pointClusters = array;
    }

    public void initTest(RecordTest recordTest, TextView textRounds, TestActivity testActivity) {
        mTestFlag = true;
        mTextRounds = textRounds;
        mTestActivity = testActivity;
        mFirstTouchFlag = true;
        roundID = TestType.NUM_OF_ROUNDS;

        itemQueue = new LinkedBlockingQueue<>();
        consumer = new ThreadItem(itemQueue, testActivity, recordTest);
        consumer.setPriority(Thread.MIN_PRIORITY);
        consumer.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                checkPointInsideCircle(x, y, 0, 0);

                if(mVelocityTracker == null) mVelocityTracker = VelocityTracker.obtain();
                else mVelocityTracker.clear();
                mVelocityTracker.addMovement(event);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                checkPointInsideCircle(x, y, VelocityTrackerCompat.getXVelocity(mVelocityTracker,
                        pointerId), VelocityTrackerCompat.getYVelocity(mVelocityTracker,
                        pointerId));

                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.recycle();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }


    private void checkPointInsideCircle(float x, float y, float v_x, float v_y) {
        if (Math.sqrt(
                (x - pointClusters[next].getCx()) * (x - pointClusters[next].getCx()) +
                        (y - pointClusters[next].getCy()) * (y - pointClusters[next].getCy())) <
                PointCircle.POINT_RADIUS) {
            pointClusters[next].setClusterColor(PointCluster.COLOR_DARK_RED);
            next = (next + 1) % PointCluster.POINT_CLUSTERS_SIZE;
            pointClusters[next].setClusterColor(PointCluster.COLOR_RED);
            if (next == 1) {
                path.reset();
                path.moveTo(x, y);
                if (mTestFlag && !mFirstTouchFlag) {
                    roundID--;
                    mTextRounds.setText(String.valueOf(roundID));
                    try {
                        itemQueue.put(new Item());
                        if (roundID == 0) mTestActivity.finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (mTestFlag) {
            if (mFirstTouchFlag) {
                startTime = System.currentTimeMillis();
                mFirstTouchFlag = false;
            }
            try {
                itemQueue.put(new Item(x, y, System.currentTimeMillis()-startTime, v_x, v_y));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }




}
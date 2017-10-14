package util;

import android.content.Context;
import java.util.concurrent.BlockingQueue;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import java.util.List;

import braude.motorsequence.TestActivity;
import database.RecordRound;
import database.RecordTest;
import database.TestType;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.COMPLEX_UNIT_IN;
import static android.util.TypedValue.COMPLEX_UNIT_MM;
import static android.util.TypedValue.COMPLEX_UNIT_PT;
import static android.util.TypedValue.COMPLEX_UNIT_PX;
import static android.util.TypedValue.COMPLEX_UNIT_SP;

/**
 * Created by ASAF on 13/10/2017.
 */

public class ThreadItem extends Thread {

    private BlockingQueue<Item> localQueue;
    private TestActivity mTestActivity;
    private RecordTest mRecordTest;
    private RecordRound mRecordRound;
    private boolean done;

    public ThreadItem(BlockingQueue<Item> localQueue, TestActivity testActivity, RecordTest recordTest) {
        this.localQueue = localQueue;
        mTestActivity = testActivity;
        mRecordTest = recordTest;
        mRecordRound = recordTest.createRecordRound();
        done = false;
    }

    @Override
    public void run() {
        while (!done) {
            try {
                Item item = localQueue.take();
                if (item.isLocal()) localSave(item.x, item.y, item.s);
                else nextRound();
            } catch (InterruptedException ex) {
//                    Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void localSave(float x, float y, double s) {

//        float w = pxToMm(getWidth(), mTestActivity.getApplicationContext());
        mRecordRound.x.add(x);
        mRecordRound.y.add(y);
        mRecordRound.s.add(s / 1000);
        mRecordRound.v.add(calcVelocity(mRecordRound.x, mRecordRound.y, mRecordRound.s));
        mRecordRound.jerk.add(0.0);
    }

    private double calcVelocity(List<Float> x, List<Float> y, List<Double> s) {
        int size = x.size();
        if (size == 1)
            return 0.0;
        float dx = pxToMm(x.get(size - 1) - x.get(size - 2), mTestActivity.getApplicationContext());
        float dy = pxToMm(y.get(size - 1) - y.get(size - 2), mTestActivity.getApplicationContext());
        double ds = s.get(size - 1) - s.get(size - 2);
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance / ds;
    }



    private static float pxToMm(final float px, final Context context) {
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return px / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1, dm);
    }

    public static float applyDimension(int unit, float value, DisplayMetrics metrics) {
        switch (unit) {
            case COMPLEX_UNIT_PX:
                return value;
            case COMPLEX_UNIT_DIP:
                return value * metrics.density;
            case COMPLEX_UNIT_SP:
                return value * metrics.scaledDensity;
            case COMPLEX_UNIT_PT:
                return value * metrics.xdpi * (1.0f / 72);
            case COMPLEX_UNIT_IN:
                return value * metrics.xdpi;
            case COMPLEX_UNIT_MM:
                return value * metrics.xdpi * (1.0f / 25.4f);
        }
        return 0;
    }

    private void nextRound() {
        mRecordRound.saveXYRound();
        if (TestType.NUM_OF_ROUNDS == mRecordRound.getID()) {
            done = true;
            return;
        }
        mRecordRound = mRecordTest.createRecordRound();
    }
}

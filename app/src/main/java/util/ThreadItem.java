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
                if (item.isLocal()) localSave(item.x, item.y, item.s, item.v_x, item.v_y);
                else nextRound();
            } catch (InterruptedException ex) {
//                    Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void localSave(float x, float y, double s, float v_x, float v_y) {

//        float w = pxToMm(getWidth(), mTestActivity.getApplicationContext());
        mRecordRound.x.add(x);
        mRecordRound.y.add(y);
        mRecordRound.s.add(s / 1000);
        mRecordRound.v.add(pxToMm(Math.sqrt(v_x*v_x + v_y*v_y), mTestActivity.getApplicationContext()));
        mRecordRound.jerk.add(0.0);
    }


    private static double pxToMm(final double px, final Context context) {
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return px / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1, dm);
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

package util.board;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import braude.motorsequence.TestActivity;
import database.oop.RecordRound;
import database.oop.RecordTest;
import database.oop.TestType;

/**
 * Created by ASAF on 13/10/2017.
 */

public class ThreadItem extends Thread {

    private BlockingQueue<Item> localQueue;
    private TestActivity mTestActivity;
    private RecordTest mRecordTest;
    private RecordRound mRecordRound;
    private boolean done;

    private double maxVelocity;
    private int velocityPeaks;
    private double sumJerk;

    public ThreadItem(BlockingQueue<Item> localQueue, TestActivity testActivity, RecordTest recordTest) {
        this.localQueue = localQueue;
        mTestActivity = testActivity;
        mRecordTest = recordTest;
        mRecordRound = recordTest.createRecordRound();
        done = false;

        maxVelocity = 0;
        sumJerk = 0;
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

        mRecordRound.x.add(x);
        mRecordRound.y.add(y);
        mRecordRound.s.add(s / 1000);
        double v = pxToMm(Math.sqrt(v_x*v_x + v_y*v_y), mTestActivity.getApplicationContext());
        mRecordRound.v.add(v);
        int size = mRecordRound.v.size();
        if (size > 1) {
            double jerk = (v - mRecordRound.v.get(size - 2)) /
                    (mRecordRound.s.get(size - 1) - mRecordRound.s.get(size - 2));
            mRecordRound.jerk.add(jerk);
            sumJerk += jerk;
        } else
            mRecordRound.jerk.add(0.0);
        if (v > maxVelocity) maxVelocity = v;
    }


    private static double pxToMm(final double px, final Context context) {
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return px / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1, dm);
    }

    private void nextRound() {
        mRecordRound.saveXYRound();
        velocityPeaks += peakdet(mRecordRound.v, 150, mRecordRound.s);
        if (TestType.NUM_OF_ROUNDS == mRecordRound.getID()) {
            mRecordTest.updateTestParameters(mRecordRound.s.get(mRecordRound.s.size()-1),
                    maxVelocity, velocityPeaks, sumJerk / (mRecordRound.jerk.size()-1));
            done = true;
            return;
        }
        mRecordRound = mRecordTest.createRecordRound();
    }

    private int peakdet(List<Double> v, int delta, List<Double> s){
//        List<Pair> peaksList  = new ArrayList<>();
//        peaksList.add(new Pair(0, v.get(0)));
        int peaks = 0;

        double mn = 1000000;
        double mx = -1000000;
        boolean loopForMax = true;

        for (int i = 0; i < v.size(); i++){
           double curr =  v.get(i);
            if (curr > mx) {
                mx = curr;
//                int index = peaksList.size()-1;
//                peaksList.set(index, new Pair(s.get(i)-s.get(0), mx));
            }
            if (curr < mn) {
                mn = curr;
            }

            if (loopForMax) {
                if (curr < mx - delta) {
//                    peaksList.add(new Pair(s.get(i), v.get(i)));
                    mn = curr;
                    loopForMax = false;
                    peaks++;
                }
            }
            else {
                    if (curr > mn + delta) {
                        mx = curr;
                        loopForMax = true;
                    }
                }

        }
        Log.d("peak", String.valueOf(peaks));
        String listString = "";
//        for (Pair p : peaksList)
//        {
//            listString += "[" + p.first + " , " + p.second + "], ";
//        }
//
//        Log.d("peak", listString);
        return peaks;
    }
}

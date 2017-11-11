package util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;

import braude.motorsequence.R;
import database.RecordRound;
import database.RecordTest;
import database.TestType;

/**
 * Created by ASAF on 5/10/2017.
 */

public class DrawView extends View {


    private Path path = new Path();

    private RecordTest mRecordTest;
    private TestType mTestType;

    private static final double SCALING_FACTOR = 0.5;
    private static final int gradient = Color.rgb(0, 0, 139);

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, RecordTest recordTest, TestType testType) {
        super(context);
        mRecordTest = recordTest;
        mTestType = testType;
        mRecordTest.recordRounds.getAll();
        setWillNotDraw(false);
        setBackgroundColor(ContextCompat.getColor(context, R.color.TouchView));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
    }

    @Override
    protected void onSizeChanged(int w, int h, int width, int height) {
        super.onSizeChanged(w, h, width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint drawPaint = null;
        for (int i = 0; i < mTestType.num_of_rounds-1; i++) {
            RecordRound recordRound = mRecordTest.recordRounds.get(i+1);
            path.moveTo((int) (recordRound.x.get(0)* SCALING_FACTOR), (int) (recordRound.y.get(0)* SCALING_FACTOR));
            int alpha = (int)(255 * (float)i*3/2 / (float)mTestType.num_of_rounds);
            int c = Color.argb(alpha, Color.red(gradient), Color.green(gradient), Color.blue(gradient));
            drawPaint = createPen(c);
            for (int j = 1; j < recordRound.x.size(); j++)
                path.lineTo((int) (recordRound.x.get(j)* SCALING_FACTOR), (int) (recordRound.y.get(j)* SCALING_FACTOR));
            canvas.drawPath(path, drawPaint);
            path.reset();
        }


        drawPaint = createBrush(PointCluster.COLOR_DEFULT);
        canvas.drawCircle((int)(mTestType.A_x* SCALING_FACTOR), (int)(mTestType.A_y* SCALING_FACTOR),
                (int)(PointCircle.POINT_RADIUS* SCALING_FACTOR), drawPaint);
        canvas.drawCircle((int)(mTestType.B_x* SCALING_FACTOR), (int)(mTestType.B_y* SCALING_FACTOR),
                (int)(PointCircle.POINT_RADIUS* SCALING_FACTOR), drawPaint);
        canvas.drawCircle((int)(mTestType.C_x* SCALING_FACTOR), (int)(mTestType.C_y* SCALING_FACTOR),
                (int)(PointCircle.POINT_RADIUS* SCALING_FACTOR), drawPaint);
        canvas.drawCircle((int)(mTestType.D_x* SCALING_FACTOR), (int)(mTestType.D_y* SCALING_FACTOR),
                (int)(PointCircle.POINT_RADIUS* SCALING_FACTOR), drawPaint);
    }


    private Paint createPen(int color) {
        Paint drawPaint = basePaint();
        drawPaint.setColor(color);

        return drawPaint;
    }

    private Paint createBrush(int color) {
        Paint drawPaint = basePaint();
        drawPaint.setColor(color);
        drawPaint.setStyle(Paint.Style.FILL);

        return drawPaint;
    }

    private Paint basePaint(){
        Paint drawPaint = new Paint(Paint.DITHER_FLAG);
        drawPaint.setAntiAlias(true);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
//        drawPaint.setPathEffect(new CornerPathEffect(10) );
        drawPaint.setStrokeWidth(1);
        return drawPaint;
    }
}

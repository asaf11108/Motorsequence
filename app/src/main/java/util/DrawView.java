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

    private PointCluster[] pointClusters;
    private RecordTest mRecordTest;
    private TestType mTestType;

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
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
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
        double widthScale = 1;
        double heightScale = 1;
        RecordRound recordRound = mRecordTest.recordRounds.get(1);
        path.moveTo((int) (recordRound.x.get(0)*widthScale), (int) (recordRound.y.get(0)*heightScale));
        for (int i = 1; i <= mTestType.num_of_rounds; recordRound = mRecordTest.recordRounds.get(++i)) {
            drawPaint = createPen(Color.BLACK);
            for (int j = 1; j < recordRound.x.size(); j++)
                path.lineTo((int) (recordRound.x.get(j)*widthScale), (int) (recordRound.y.get(j)*heightScale));
        }
//        Matrix scaleMatrix = new Matrix();
//        RectF rectF = new RectF();
//        path.computeBounds(rectF, true);
//        scaleMatrix.setScale(0.7f, 0.5f,rectF.centerX(),rectF.centerY());
//        path.transform(scaleMatrix);
        canvas.drawPath(path, drawPaint);


        drawPaint = createBrush(PointCluster.COLOR_DEFULT);
        canvas.drawCircle(mTestType.A_x, mTestType.A_y, PointCircle.POINT_RADIUS, drawPaint);
        canvas.drawCircle(mTestType.B_x, mTestType.B_y, PointCircle.POINT_RADIUS, drawPaint);
        canvas.drawCircle(mTestType.C_x, mTestType.C_y, PointCircle.POINT_RADIUS, drawPaint);
        canvas.drawCircle(mTestType.D_x, mTestType.D_y, PointCircle.POINT_RADIUS, drawPaint);
    }

    private Paint createPen(int color) {
        Paint drawPaint = new Paint(Paint.DITHER_FLAG);
        drawPaint.setAntiAlias(true);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
//        drawPaint.setPathEffect(new CornerPathEffect(10) );
        drawPaint.setStrokeWidth(1);
        drawPaint.setColor(color);

        return drawPaint;
    }

    private Paint createBrush(int color) {
        Paint drawPaint = new Paint(Paint.DITHER_FLAG);
        drawPaint.setAntiAlias(true);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
//        drawPaint.setPathEffect(new CornerPathEffect(10) );
        drawPaint.setStrokeWidth(1);
        drawPaint.setColor(color);
        drawPaint.setStyle(Paint.Style.FILL);

        return drawPaint;
    }
}

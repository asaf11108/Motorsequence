package util.board;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ASAF on 23/9/2017.
 */

public class PointCircle extends View {

    public static final int POINT_RADIUS = 20;

    private Paint paint;

    private int cx;
    private int cy;
    private int circleColor;

    public PointCircle(Context context, int cx, int cy)
    {
        super(context);
        init(context, null, cx, cy);
    }

    public PointCircle(Context context, AttributeSet attrs, int cx, int cy)
    {
        super(context, attrs);
        init(context, attrs, cx, cy);
    }

    private void init(Context context, AttributeSet attrs, int cx, int cy)
    {
        paint = new Paint();
        paint.setAntiAlias(true);
        this.cx = cx;
        this.cy = cy;
        this.circleColor = PointCluster.COLOR_DEFULT;
    }

    public void setCircleColor(int circleColor)
    {
        this.circleColor = circleColor;
        invalidate();
    }

    public int getCircleColor()
    {
        return circleColor;
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        paint.setColor(circleColor);
        canvas.drawCircle(cx, cy, POINT_RADIUS, paint);
    }

    public int getCx() {
        return cx;
    }

    public int getCy() {
        return cy;
    }
}

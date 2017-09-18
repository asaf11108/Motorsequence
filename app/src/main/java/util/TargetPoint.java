package util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class TargetPoint extends View
{
    private static final int DEFAULT_CIRCLE_COLOR = Color.RED;

    private int circleColor = DEFAULT_CIRCLE_COLOR;
    private Paint paint;

    private int cx;
    private int cy;
    private int radius;

    public TargetPoint(Context context, int cx, int cy, int radius)
    {
        super(context);
        init(context, null, cx, cy, radius);
    }

    public TargetPoint(Context context, AttributeSet attrs, int cx, int cy, int radius)
    {
        super(context, attrs);
        init(context, attrs, cx, cy, radius);
    }

    private void init(Context context, AttributeSet attrs, int cx, int cy, int radius)
    {
        paint = new Paint();
        paint.setAntiAlias(true);
        this.cx = cx;
        this.cy = cy;
        this.radius = radius;
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
        canvas.drawCircle(cx, cy, radius, paint);
    }
}
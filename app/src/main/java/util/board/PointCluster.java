package util.board;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by ASAF on 23/9/2017.
 */

public class PointCluster {

    public static final int COLOR_RED = Color.RED;
    public static final int COLOR_DARK_RED = Color.rgb(139, 0, 0);
    public static final int COLOR_DEFULT = COLOR_DARK_RED;
    public static final int POINT_CLUSTERS_SIZE = 4;

    private final TextView pointName;
    private final PointCircle pointCircle;
    private int posX;
    private int posY;


    public PointCluster(Context context, RelativeLayout relativeLayout, String name, int cx, int cy) {
        this.posX = cx + PointCircle.POINT_RADIUS;
        this.posY = cy + PointCircle.POINT_RADIUS;

        pointName = new TextView(context);
        pointName.setText(name);
        pointName.setTextColor(COLOR_DEFULT);
        pointName.measure(0, 0);
        RelativeLayout.LayoutParams paramLayout = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramLayout.leftMargin = cx + PointCircle.POINT_RADIUS - pointName.getMeasuredWidth()/2;
        paramLayout.topMargin = cy - PointCircle.POINT_RADIUS;
        relativeLayout.addView(pointName, paramLayout);

        pointCircle = new PointCircle(context, PointCircle.POINT_RADIUS,
                PointCircle.POINT_RADIUS);
        paramLayout = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramLayout.leftMargin = cx;
        paramLayout.topMargin = cy;
        relativeLayout.addView(pointCircle, paramLayout);
    }

    public int getCx() {
        return posX;
    }

    public int getCy() {
        return posY;
    }

    public void setClusterColor(int circleColor)
    {
        pointName.setTextColor(circleColor);
        pointCircle.setCircleColor(circleColor);
    }
}

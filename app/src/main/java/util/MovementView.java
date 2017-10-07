package util;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import database.RecordTest;
import database.TestType;

/**
 * Created by ASAF on 6/10/2017.
 */

public class MovementView extends RelativeLayout {

    public DrawView drawView;
    public PointCluster[] pointClusters;

    public MovementView(Context context) {
        super(context);
    }

    public MovementView(Context context, RecordTest recordTest, TestType testType) {
        super(context);
        //height 200
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);

        drawView = new DrawView(context, recordTest, testType);
        addView(drawView);

//        pointClusters = new PointCluster[PointCluster.POINT_CLUSTERS_SIZE];
//        pointClusters[0] = new PointCluster(context, this, "A", testType.A_x, testType.A_y);
//        pointClusters[1] = new PointCluster(context, this, "B", testType.B_x, testType.B_y);
//        pointClusters[2] = new PointCluster(context, this, "C", testType.C_x, testType.C_y);
//        pointClusters[3] = new PointCluster(context, this, "D", testType.D_x, testType.D_y);

        float scalingFactor = 0.5f; // scale down to half the size
//        setClipToOutline();
        setPivotX(0);
        setPivotY(0);
        setScaleX(0.6f);
        setScaleY(0.3f);

    }
}

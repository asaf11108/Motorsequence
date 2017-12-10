package util.graph;

import android.content.Context;
import android.graphics.Color;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;

/**
 * Created by ASAF on 14/10/2017.
 */

public class MyGraphView extends GraphView {

    public MyGraphView(Context context, String verticalvAxisName) {
        super(context);
        GridLabelRenderer gridLabelRenderer = getGridLabelRenderer();
        gridLabelRenderer.setHorizontalLabelsColor(Color.BLACK);
        gridLabelRenderer.setHorizontalAxisTitleColor(Color.BLACK);
        gridLabelRenderer.setVerticalLabelsColor(Color.BLACK);
        gridLabelRenderer.setVerticalAxisTitleColor(Color.BLACK);
        gridLabelRenderer.setHorizontalAxisTitle("s");
        gridLabelRenderer.setVerticalAxisTitle(verticalvAxisName);
        gridLabelRenderer.setGridColor(Color.BLACK);

    }
}

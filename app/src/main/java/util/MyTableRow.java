package util;

import android.content.Context;
import android.widget.TableRow;

/**
 * Created by ASAF on 10/9/2017.
 */

public class MyTableRow extends TableRow {
    private final int id;

    public MyTableRow(Context context, int id) {
        super(context);
        this.id = id;
    }


    public int getMyId() {
        return id;
    }
}

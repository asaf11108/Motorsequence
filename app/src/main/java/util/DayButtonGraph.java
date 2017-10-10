package util;

import android.widget.Button;

/**
 * Created by ASAF on 9/10/2017.
 */

public class DayButtonGraph extends ButtonGraph {

    public DayButtonGraph(Button[] dayButtons) {
        this.buttons = dayButtons;
    }

    @Override
    public Button getButton() {
        return buttons[day];
    }

    @Override
    public int getVal() {
        return day;
    }

    @Override
    public int setVal(int val) {
        return day = val;
    }


}

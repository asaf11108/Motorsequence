package util;

import android.widget.Button;

import util.ButtonGraph;

/**
 * Created by ASAF on 10/10/2017.
 */

public class TypeButtonGraph extends ButtonGraph {
    public TypeButtonGraph(Button[] typeButtons) {
        this.buttons = typeButtons;
    }

    @Override
    public Button getButton() {
        return buttons[type];
    }

    @Override
    public int getVal() {
        return type;
    }

    @Override
    public int setVal(int val) {
        return type = val;
    }
}

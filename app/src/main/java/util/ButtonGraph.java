package util;
import android.widget.Button;

/**
 * Created by ASAF on 9/10/2017.
 */

public abstract class ButtonGraph {

    public static int day = 0;
    public static int type = 0;
    public Button[] buttons;

    public abstract Button getButton();
    public abstract int getVal();
    public abstract int setVal(int val);
}

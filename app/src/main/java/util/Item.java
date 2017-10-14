package util;

/**
 * Created by ASAF on 13/10/2017.
 */

public class Item {

    public float x;
    public float y;
    public double s;
    private boolean local;

    public Item() {
        local = false;
    }

    public Item(float x, float y, double s) {
        this.x = x;
        this.y = y;
        this.s = s;
        local = true;
    }

    public boolean isLocal() {
        return local;
    }
}

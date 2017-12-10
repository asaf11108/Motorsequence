package util.board;

/**
 * Created by ASAF on 13/10/2017.
 */

public class Item {

    public float x;
    public float y;
    public double s;
    private boolean local;
    public float v_x;
    public float v_y;

    public Item() {
        local = false;
    }

    public Item(float x, float y, double s, float v_x, float v_y) {
        this.x = x;
        this.y = y;
        this.s = s;
        this.v_x = v_x;
        this.v_y = v_y;
        local = true;
    }

    public boolean isLocal() {
        return local;
    }
}

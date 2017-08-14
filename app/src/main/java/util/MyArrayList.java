package util;

import java.util.ArrayList;
import java.util.List;

import database.Adder;
import database.Identifier;

/**
 * Created by ASAF on 11/8/2017.
 */

public class MyArrayList<E extends Identifier> extends ArrayList<E> implements List<E>{

    public int seq;
    private Adder exe;

    public MyArrayList(int seq, Adder exe) {
        this.seq = seq;
        this.exe = exe;
    }

    /**
     * add new object to list.
     * @param id thr desirable object index
     * @return true or false if the parameter i is invalid
     */
    public boolean fetchToList(int id){
        if (id <= 0 || id > seq) return false;
        insert(id);
        return true;
    }

    /**
     * add to list last record
     */
    public void fetchLastToList(){
        fetchToList(seq);
    }

    /**
     * add to list all records
     */
    public void fetchAllToList(){
        for (int i = 1; i <= seq; i++) fetchToList(i);
    }

    private void insert(int id){
        for (int i = 0; i < size(); i++) {
            if (get(i).getID() < id) continue;
            if (get(i).getID() == id) return;
            exe.myAdd(i, id);
            return;
        }
        exe.myAdd(id);
    }
}

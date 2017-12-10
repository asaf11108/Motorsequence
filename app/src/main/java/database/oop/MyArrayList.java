package database.oop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASAF on 11/8/2017.
 */

public class MyArrayList<E extends Identifier>{

    private int seq;
    private NewObject<E> exe;
    private List<E> data;

    public MyArrayList(int seq, NewObject<E> exe) {
        this.seq = seq;
        this.exe = exe;
        data = new ArrayList<>();
    }

    private E fetchToList(int id) {
        E element = null;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getID() < id) continue;
            if (data.get(i).getID() == id) return data.get(i);
            element = exe.newObject(id);
            data.add(i,element);
            return element;
        }
        element = exe.newObject(id);
        data.add(element);
        return element;
    }

    /**
     * getButton to list last record
     */
    public E getLast() {
        E element = null;
        if(seq == 0) return element;
        if (!data.isEmpty() && (element = data.get(data.size()-1)).getID() == seq)
            return element;
        element = exe.newObject(seq);
        data.add(element);
        return element;
    }

    /**
     * getButton to list all records
     */
    public List<E> getAll() {
        for (int i = 1; i <= seq; i++) fetchToList(i);
        return data;
    }

    /**
     * getButton new object to list.
     *
     * @param id thr desirable object index
     * @return element or null if the parameter i is invalid
     */
    public E get(int id) {
        if (id <= 0 || id > seq) return null;
        return fetchToList(id);
    }

    public int getSeq() {
        return seq;
    }

    E add() {
        seq++;
        return getLast();
    }

    public void remove() {
        data.remove(data.size()-1);
        seq--;
    }
}

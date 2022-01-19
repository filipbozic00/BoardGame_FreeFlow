package com.mygdx.game.freeflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListHolder {
    public List<Float> results= new ArrayList<>();
    public ListHolder(){
        Collections.sort(results, new Comparator<Float>() {
            @Override
            public int compare(Float o1, Float o2) {
                return Float.compare(-o2,-o1);
            }
        });
    }
    public void sort(){
        Collections.sort(results, new Comparator<Float>() {
            @Override
            public int compare(Float o1, Float o2) {
                return Float.compare(o2,o1);
            }
        });
        Collections.reverse(results);
    }
}
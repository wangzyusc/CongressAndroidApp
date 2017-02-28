package edu.wangzhiyusc.hw9test;

import java.util.HashMap;

/**
 * Created by wangz on 2016/12/1.
 */

public class ComData extends HashMap<String, Object> implements Comparable<ComData> {
    public ComData(){
        super();
    }

    public int compareTo(ComData another){
        return ((String) this.get("name")).compareTo((String) another.get("name"));
    }
}

package edu.wangzhiyusc.hw9test;

import java.util.HashMap;

/**
 * Created by wangz on 2016/12/1.
 */

public class LegByNamePersonData extends HashMap<String, Object> implements Comparable<LegByNamePersonData> {
    public LegByNamePersonData(){
        super();
    }
    public int compareTo(LegByNamePersonData another){
        return ((String) this.get("last_name")).compareTo((String) another.get("last_name"));
    }
}

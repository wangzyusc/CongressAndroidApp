package edu.wangzhiyusc.hw9test;

import java.util.HashMap;

/**
 * Created by wangz on 2016/11/30.
 */

public class LegByStatePersonData extends HashMap<String, Object> implements Comparable<LegByStatePersonData>{

    public LegByStatePersonData(){
        super();
    }


    public int compareTo(LegByStatePersonData another){
        int stateres = ((String) this.get("state_name")).compareTo((String) another.get("state_name"));
        if(stateres != 0) return stateres;
        else{
            return ((String) this.get("last_name")).compareTo((String) another.get("last_name"));
        }
    }
}

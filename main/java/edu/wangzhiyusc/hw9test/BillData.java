package edu.wangzhiyusc.hw9test;

import java.util.HashMap;

/**
 * Created by wangz on 2016/12/1.
 */

public class BillData extends HashMap<String, Object> implements Comparable<BillData> {
    public BillData(){
        super();
    }

    public int compareTo(BillData another){
        return ((String) another.get("introduced_on")).compareTo((String) this.get("introduced_on"));
    }
}

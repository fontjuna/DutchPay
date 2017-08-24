package com.nohseunghwa.gallane.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by fontjuna on 2017-08-25.
 */

public class Members {
    private HashMap<String, Double> mMembers;

    public Members(HashMap<String, Double> members) {
        mMembers = members;
    }

    public HashMap<String, Double> getMembers() {
        return mMembers;
    }

    public void setMembers(HashMap<String, Double> members) {
        mMembers = members;
    }
    public TreeMap<String, Double> getAscending() {
        TreeMap<String, Double> memberMap = new TreeMap<>(mMembers);
        return  memberMap;
    }

    public TreeMap<String, Double> getDescending() {
        TreeMap<String, Double> memberMap = new TreeMap<>(Collections.<String>reverseOrder());
        memberMap.putAll(mMembers);
        return memberMap;
    }
}

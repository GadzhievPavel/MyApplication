package com.example.myapplication.util;

import android.util.Log;

public class  IpCorect {
    public static boolean ipIsCorect(String ip) {
        String noMask = ip.substring(8);
        boolean result = false;
        int k = 0;
        Log.e("subIp", noMask);
        for (int i = 0; i < noMask.length(); i++) {

            if (noMask.charAt(i) == '.') {
                i++;
                if (i<noMask.length()) {
                    result = true;
                }
            }

        }
        return result;
    }
}

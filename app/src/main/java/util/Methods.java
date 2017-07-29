package util;

import android.util.Pair;

import java.util.List;

/**
 * Created by ASAF on 29/7/2017.
 */

public class Methods {

    public static String convertToString(List<Pair<String,String>> pairList){
        String ans = "";
        String prefix = "";
        for (Pair<String,String> pair : pairList) {
            ans += prefix + pair.first + " = " + pair.second;
            prefix = " AND ";
        }
        return ans;
    }
}

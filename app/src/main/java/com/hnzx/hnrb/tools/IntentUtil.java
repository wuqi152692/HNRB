package com.hnzx.hnrb.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.Map;

/**
 * @author: mingancai
 * @Time: 2017/4/12 0012.
 */

public class IntentUtil {
    /**
     * 无参跳转
     *
     * @param con
     * @param goClass
     */
    public static void startActivity(Context con, Class goClass) {
        Intent intent = new Intent(con, goClass);
        con.startActivity(intent);
    }

    public static void startActivityForResult(Activity con, Class goClass, int request) {
        Intent intent = new Intent(con, goClass);
        con.startActivityForResult(intent, request);
    }

    public static void startActivityForResult(Fragment con, Class goClass, int request) {
        Intent intent = new Intent(con.getActivity(), goClass);
        con.startActivityForResult(intent, request);
    }

    /**
     * 有参跳转
     *
     * @param con
     * @param goClass
     * @param params
     */
    public static void startActivityForResult(Activity con, Class goClass, Map<String, String> params, int request) {
        Intent intent = new Intent(con, goClass);
        if (params != null) {
            for (String key : params.keySet()) {
                intent.putExtra(key, params.get(key));
            }
        }
        con.startActivityForResult(intent, request);
    }

    /**
     * 有参跳转
     *
     * @param con
     * @param goClass
     * @param params
     */
    public static void startActivityForResult(Fragment con, Class goClass, Map<String, String> params, int request) {
        Intent intent = new Intent(con.getActivity(), goClass);
        if (params != null) {
            for (String key : params.keySet()) {
                intent.putExtra(key, params.get(key));
            }
        }
        con.startActivityForResult(intent, request);
    }

    /**
     * 有参跳转
     *
     * @param con
     * @param goClass
     * @param params
     */
    public static void startActivity(Context con, Class goClass, Map<String, String> params) {
        Intent intent = new Intent(con, goClass);
        if (params != null) {
            for (String key : params.keySet()) {
                intent.putExtra(key, params.get(key));
            }
        }
        con.startActivity(intent);
    }
}

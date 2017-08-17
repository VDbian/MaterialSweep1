package com.example.fei.materialsweep.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by fei on 2017/7/18.
 */

public class WcfUtils {

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    //md5加密
    public static String md5(String hospital_id, String method_code) {
        if (TextUtils.isEmpty(hospital_id) && TextUtils.isEmpty(method_code)) {
            return "";
        }
        String str = hospital_id + method_code;
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(str.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getRequestString(String hospital_id, String method_code, String input) {
        String password = md5(hospital_id, method_code);
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
                "<request> " +
                "<hospital_id>" + hospital_id + "</hospital_id> " +
                "<password>" + password + "</password> " +
                "<fun_id>" + method_code + "</fun_id> " +
                "<transaction_id>000001</transaction_id> " +
                "<input>" + input + " </input> " +
                "</request>";
    }

}

package com.example.fei.materialsweep.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.example.fei.materialsweep.application.MyApplication;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * Created by fei on 2017/7/24.
 */

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候
 *
 * @author user 注意修改文件的路径和文件名，在Manifest中添加文件读写权限；
 *
 */
public class CrashHander implements Thread.UncaughtExceptionHandler {

        // 错误日志文件夹的位置
        public static String mCrashLogDirPath = "";
        // 系统默认的UncaughtException处理类
        private Thread.UncaughtExceptionHandler mDefaultHandler;
        // CrashHandler实例
        private static CrashHander INSTANCE = new CrashHander();
        private Context mContext;
        // 用来存储设备信息和异常信息
        private Map<String, String> infos = new HashMap<String, String>();
        // 用于格式化日期,作为日志文件名的一部分
        private DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss",
                Locale.CHINA);
    //用于保存错误信息
    private String contents = "";

        private CrashHander() {
        }

        public static CrashHander getInstance() {
            return INSTANCE;
        }

        public void init(Context context) {
            mContext = context;
            mCrashLogDirPath = context.getExternalCacheDir() + File.separator
                    + MyApplication.Error_DIR_NAME + File.separator;
            // 获取系统默认的UncaughtException处理器
            mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            // 设置该CrashHandler为程序的默认处理器
            Thread.setDefaultUncaughtExceptionHandler(this);
        }

        /**
         * 当UncaughtException发生时会转入该函数来处理 如果导入项目@Override报错，请修改project编译的jdk版本到1.5以上
         */
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            if (!handleException(ex) && mDefaultHandler != null) {
                // 如果用户没有处理则让系统默认的异常处理器来处理
                mDefaultHandler.uncaughtException(thread, ex);
            } else {
                // 退出程序
                Toast.makeText(mContext,"很抱歉，程序崩溃了，请重启",Toast.LENGTH_SHORT).show();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }

        }

        /**
         * 自定义错误处理,收集错误信息
         *
         * @param ex
         * @return true:如果处理了该异常信息;否则返回false.
         */
        private boolean handleException(Throwable ex) {
            if (ex == null) {
                return false;
            }
            // 收集设备参数信息
            collectDeviceInfo(mContext);
           contents = saveCrashInfo2File(ex);
            // 向配置文件中写入标识位
            flagCrash();
            return true;
        }

        public void flagCrash() {
            SharedPreferences.Editor editor = PreferenceManager
                    .getDefaultSharedPreferences(mContext).edit();
            editor.putBoolean(MyApplication.TAG_OCCURRED_ERROR, true);
            editor.putString("userId",MyApplication.USER_ID);
            editor.putString("userName",MyApplication.USER_NAME);
            editor.putString("contents",contents);
            editor.commit();
        }

        /**
         * 收集设备参数信息
         *
         * @param ctx
         */
        public void collectDeviceInfo(Context ctx) {
            try {
                PackageManager pm = ctx.getPackageManager();
                PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                        PackageManager.GET_ACTIVITIES);
                if (pi != null) {
                    String versionName = pi.versionName == null ? "null"
                            : pi.versionName;
                    String versionCode = pi.versionCode + "";
                    infos.put("versionName", versionName);
                    infos.put("versionCode", versionCode);
                    long timestamp = System.currentTimeMillis();
                    String time = formatter.format(new Date(timestamp));
                    infos.put("Time",time);
                }
            } catch (PackageManager.NameNotFoundException e) {
            }
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    infos.put(field.getName(), field.get(null).toString());

                } catch (Exception e) {

                }
            }
        }


        private String saveCrashInfo2File(Throwable ex) {
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> entry : infos.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key + "=" + value + "\n");
            }

            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.close();
            String result = writer.toString();
            sb.append(result);
            String con = sb.toString();
            con = con.replace("<","'").replace(">","'");
            return con;
        }
}

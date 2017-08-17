package com.example.fei.materialsweep.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;

import com.example.fei.materialsweep.bean.Versions;
import com.example.fei.materialsweep.bean.VersionsBean;
import com.example.fei.materialsweep.dialog.DialogFragmentConfirmCancel;
import com.example.fei.materialsweep.dialog.DialogFragmentDownLoad;
import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * @author coolszy
 * @date 2012-4-26
 * @blog http://blog.92coding.com
 */

public class UpdateManager implements DialogCallBack {
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    public static boolean cancelUpdate = false;
    /* 获取服务器上的版本信息*/
    private VersionsBean versionsBean = new VersionsBean();


    private Context mContext;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private DialogFragmentDownLoad mDownloadDialog;
    private DialogFragmentConfirmCancel dialog;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //获取服务器版本信息
                case 1001:
                    int versionCode = getVersionCode(mContext);
                    versionsBean = (VersionsBean) msg.obj;
                    if (versionsBean.getOutput().getVersions() > versionCode) {
                        checkUpdate();
                    }
                    break;
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
//                    mProgress.setProgress(progress);
                    mDownloadDialog.setProgressBarNum(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk();
                    break;
                default:
                    break;
            }
        }
    };

    public UpdateManager(Context context) {
        this.mContext = context;
        dialog = new DialogFragmentConfirmCancel();
        dialog.setDcb(this);
    }

    /**
     * 检测软件更新
     */
    private void checkUpdate() {
        // 显示提示对话框
        if (versionsBean.getOutput().getUpdateForce().equals("是")) {
            showDownloadDialog();
        } else {
            showNoticeDialog();
        }
    }

    /**
     * 检查软件是否有更新版本
     *
     * @return
     */
    public void isUpdate() {
        // 获取最新软件版本
        new Thread(new Runnable() {
            @Override
            public void run() {
                String request = WcfUtils.getRequestString("gdkq", "001_002", "");
                SoapService soapService = new SoapService(request);
                String res = soapService.LoadResult();
//                Log.e("VD",res);
                XStream xstream = new XStream();
                xstream.alias("response", VersionsBean.class);
                xstream.alias("output", Versions.class);
                VersionsBean versionsBean = (VersionsBean) xstream.fromXML(res);
                if (versionsBean.getCode().equals("1")) {
                    Message message = Message.obtain(mHandler, 1001, versionsBean);
                    mHandler.sendMessage(message);
                }
            }
        }).start();

    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    private int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
//			versionCode = context.getPackageManager().getPackageInfo("com.szy.update", 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog() {
        // 构造对话框
        dialog.show(((Activity) mContext).getFragmentManager(), "");
    }

    /**
     * 显示软件下载对话框
     */
    public void showDownloadDialog() {
        // 构造软件下载对话框
        mDownloadDialog = new DialogFragmentDownLoad();
        mDownloadDialog.show(((Activity) mContext).getFragmentManager(), "");
        // 下载文件
        downloadApk();
    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    @Override
    public void confirm() {
        showDownloadDialog();
    }
//
//    @Override
//    public void cancel() {
//        cancelUpdate = true;
//    }

    /**
     * 下载文件线程
     *
     * @author coolszy
     * @date 2012-4-26
     * @blog http://blog.92coding.com
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
//                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // 获得存储卡的路径
                String sdpath = Environment.getExternalStorageDirectory() + "/";
                mSavePath = sdpath + "download";
                URL url = new URL(versionsBean.getOutput().getFileSrc());
                // 创建连接
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                // 获取文件大小
                int length = conn.getContentLength();
                // 创建输入流
                InputStream is = conn.getInputStream();
                File file = new File(mSavePath);
                // 判断文件目录是否存在
                if (!file.exists()) {
                    file.mkdir();
                }
                File apkFile = new File(mSavePath, "materialsweep.apk");
                FileOutputStream fos = new FileOutputStream(apkFile);
                int count = 0;
                // 缓存
                byte buf[] = new byte[1024];
                // 写入到文件中

                do {
                    int numread = is.read(buf);
                    count += numread;
                    // 计算进度条位置
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWNLOAD);
                    if (numread <= 0) {
                        // 下载完成
                        mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                        break;
                    }
                    // 写入文件
                    fos.write(buf, 0, numread);
                } while (!cancelUpdate);// 点击取消就停止下载.
                fos.close();
                is.close();
//                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    }

    /**
     * 安装APK文件
     */
    private void installApk() {
//        Log.e("VD", mSavePath + "****installApk");
        File apkfile = new File(mSavePath, "materialsweep.apk");
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}

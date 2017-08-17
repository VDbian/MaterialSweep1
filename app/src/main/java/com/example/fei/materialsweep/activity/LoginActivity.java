package com.example.fei.materialsweep.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.fei.materialsweep.R;
import com.example.fei.materialsweep.application.MyApplication;
import com.example.fei.materialsweep.bean.ReturnUserBean;
import com.example.fei.materialsweep.bean.UserBean;
import com.example.fei.materialsweep.bean.bean;
import com.example.fei.materialsweep.utils.KeyBoardUtils;
import com.example.fei.materialsweep.utils.SoapService;
import com.example.fei.materialsweep.utils.UpdateManager;
import com.example.fei.materialsweep.utils.WcfUtils;
import com.thoughtworks.xstream.XStream;

import static com.example.fei.materialsweep.R.id.relativeLayout_title;
import static com.example.fei.materialsweep.application.MyApplication.TAG_OCCURRED_ERROR;

/**
 * Created by fei on 2017/7/11.
 */

public class LoginActivity extends BaseActivity {

    private EditText editTextUsername;
    private RelativeLayout relativeLayoutPasswordImage;
    private EditText editTextPassword;
    private Button buttonLogin;
    private RelativeLayout relativeLayoutLogin;
    private RelativeLayout relativeLayoutTitle;
    private CheckBox checkboxRememberPassword;
    private View viewButtonLine;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transparent();
        setContentView(R.layout.activity_login);
        initView();
        isRememberInfo();
        if (WcfUtils.isNetworkAvailable(this)) {
            //判断是否有更新
            UpdateManager updateManager = new UpdateManager(this);
            updateManager.isUpdate();
//            //判断是否有错误信息上传
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            if (sharedPreferences.getBoolean(TAG_OCCURRED_ERROR, false)) {
                uploadErrorDialog();
            }
        }
    }

    /**
     * 判断是否记住过密码
     */
    private void isRememberInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        boolean bool = sharedPreferences.getBoolean("isChecked", false);
        String password = sharedPreferences.getString("password", "");
        editTextUsername.setText(username);
        editTextPassword.setText(password);
        checkboxRememberPassword.setChecked(bool);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:
                    Toast.makeText(LoginActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private void initView() {
        editTextUsername = (EditText) findViewById(R.id.editText_username);
        relativeLayoutPasswordImage = (RelativeLayout) findViewById(R.id.relativeLayout_password_image);
        editTextPassword = (EditText) findViewById(R.id.editText_password);
        buttonLogin = (Button) findViewById(R.id.button_login);
        relativeLayoutPasswordImage.setVisibility(View.INVISIBLE);
        relativeLayoutLogin = (RelativeLayout) findViewById(R.id.relativeLayout_login);
        relativeLayoutTitle = (RelativeLayout) findViewById(relativeLayout_title);
        checkboxRememberPassword = (CheckBox) findViewById(R.id.checkbox_remember_password);
        viewButtonLine = findViewById(R.id.button_line);
        setListener();
    }

    private void setListener() {
        relativeLayoutPasswordImage.setOnClickListener(onClickListener);
        buttonLogin.setOnClickListener(onClickListener);
        editTextPassword.addTextChangedListener(textWatcher);
//        addLayoutListener(relativeLayoutLogin, buttonLogin);
    }

    private String xmlString(String username, String password) {
        return "<account>" + username + "</account> <pwd>" + password + "</pwd>";
    }

    /**
     * 登录
     */
    private void login() {
        if (KeyBoardUtils.isFastClick()) {
            final String username = editTextUsername.getText().toString().trim();
            final String password = editTextPassword.getText().toString().trim();
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            } else {
                if (WcfUtils.isNetworkAvailable(this)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String request = WcfUtils.getRequestString("gdkq", "001_001", xmlString(username, password));
//                        Log.e("VD", request);
                            SoapService soapService = new SoapService(request);
                            String res = soapService.LoadResult();
                            XStream xstream = new XStream();
                            xstream.alias("response", ReturnUserBean.class);
                            xstream.alias("output", UserBean.class);
                            ReturnUserBean returnUserBean = (ReturnUserBean) xstream.fromXML(res);
//                        XmlBean<UserBean> returnUserBean = (XmlBean<UserBean>) xstream.fromXML(res);
                            if (returnUserBean.getCode().equals("1")) {
                                MyApplication.USER_ID = returnUserBean.getOutput().getUserid();
                                MyApplication.USER_NAME = returnUserBean.getOutput().getUsername();
                                SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", username);
                                if (checkboxRememberPassword.isChecked()) {
                                    editor.putString("password", password);
                                    editor.putBoolean("isChecked", true);
                                } else {
                                    editor.putString("password", "");
                                    editor.putBoolean("isChecked", false);
                                }
                                editor.apply();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Message message = new Message();
                                message.what = 1001;
                                message.obj = returnUserBean.getMsg();
                                handler.sendMessage(message);
                            }
                        }
                    }).start();
                } else {
                    Toast.makeText(this, "无网络连接", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.relativeLayout_password_image:
                    editTextPassword.setText("");
                    relativeLayoutPasswordImage.setVisibility(View.INVISIBLE);
                    break;
                case R.id.button_login:
                    login();
                    break;
                default:
                    break;
            }
        }
    };

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(s.toString())) {
                relativeLayoutPasswordImage.setVisibility(View.INVISIBLE);
                addLayoutListener(relativeLayoutLogin, buttonLogin);
            } else {
                relativeLayoutPasswordImage.setVisibility(View.VISIBLE);
                addLayoutListener(relativeLayoutLogin, buttonLogin);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
//        addLayoutListener(relativeLayoutLogin, buttonLogin);
    }

    /**
     * 设置布局在软键盘弹出时向上滑动，便于登录按钮不被遮挡
     *
     * @param main   根布局
     * @param scroll 需要显示的最下方View
     */
    public void addLayoutListener(final View main, final View scroll) {
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //1、获取main在窗体的可视区域
                main.getWindowVisibleDisplayFrame(rect);
                //2、获取main在窗体的不可视区域高度，在键盘没有弹起时，main.getRootView().getHeight()调节度应该和rect.bottom高度一样
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
                int screenHeight = main.getRootView().getHeight();//屏幕高度
                //3、不可见区域大于屏幕本身高度的1/4：说明键盘弹起了
                if (mainInvisibleHeight > screenHeight / 4) {
                    int[] location = new int[2];
                    scroll.getLocationOnScreen(location);
                    // 4､获取Scroll的窗体坐标，算出main需要滚动的高度
                    int srollHeight = (location[1] + scroll.getHeight()) - rect.bottom + 16;
                    //5､让界面整体上移键盘的高度
                    main.scrollTo(0, srollHeight);
                } else {
                    //3、不可见区域小于屏幕高度1/4时,说明键盘隐藏了，把界面下移，移回到原有高度
                    main.scrollTo(0, 0);
                }
            }
        });
    }

    // 第一上传错误日志的对话框样式
    protected void uploadErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("上次程序异常退出，\n上传错误日志？");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                sendError();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteError();
                dialog.dismiss();
            }
        });
        Dialog upErrorDialog = builder.create();
        upErrorDialog.show();
    }

    public void deleteError() {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(this).edit();
        editor.remove(TAG_OCCURRED_ERROR);
        editor.remove("userID");
        editor.remove("userName");
        editor.remove("contents");
        editor.commit();
    }

    public void sendError() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userId = preferences.getString("userId", "userId");
//        userId = TextUtils.isEmpty(userId) ? "userId" : userId;
        String userName = preferences.getString("userName", "userName");
//        userName = TextUtils.isEmpty(userName) ? "userName" : userName;
        String contents = preferences.getString("contents", "contents");
//        contents = TextUtils.isEmpty(contents) ? "contents" : contents;
        contents = contents.replace("<", "'").replace(">", "'");
//        Log.e("VD", userId + "***\n" + userName + "***\n" + contents);

        final String input = " <Title>pda崩溃信息</Title>\n" +
                " <UserId>" + userId + "</UserId>\n" +
                " <UserName>" + userName + "</UserName>\n" +
                " <Contents>" + contents + "</Contents>";
        if (WcfUtils.isNetworkAvailable(this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String request = WcfUtils.getRequestString("gdkq", "001_003", input);
//                        Log.e("VD", request);
                    SoapService soapService = new SoapService(request);
                    String res = soapService.LoadResult();
//                    Log.e("VD",res);
                    XStream xstream = new XStream();
                    xstream.alias("response", bean.class);
//                    xstream.alias("output", UserBean.class);
                    bean bean = (bean) xstream.fromXML(res);
                    if (bean.getCode() == "1") {
                        deleteError();
                    }
//                        XmlBean<UserBean> returnUserBean = (XmlBean<UserBean>) xstream.fromXML(res);
                    Message message = new Message();
                    message.what = 1001;
                    message.obj = bean.getMsg();
                    handler.sendMessage(message);
                }
            }).start();
        } else {
            Toast.makeText(this, "无网络连接", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.example.fei.materialsweep.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.fei.materialsweep.R;
import com.example.fei.materialsweep.adapter.ListViewOutboundAdapter;
import com.example.fei.materialsweep.application.MyApplication;
import com.example.fei.materialsweep.bean.DocBean;
import com.example.fei.materialsweep.bean.OrgBean;
import com.example.fei.materialsweep.bean.ReturnTarrBean;
import com.example.fei.materialsweep.bean.TarrBean;
import com.example.fei.materialsweep.bean.XmlBean;
import com.example.fei.materialsweep.utils.KeyBoardUtils;
import com.example.fei.materialsweep.utils.SoapService;
import com.example.fei.materialsweep.utils.WcfUtils;
import com.thoughtworks.xstream.XStream;
import com.zltd.decoder.Constants;
import com.zltd.industry.ScannerManager;

import java.util.ArrayList;
import java.util.List;

import static com.zltd.industry.ScannerManager.SCAN_CONTINUOUS_MODE;

/**
 * Created by fei on 2017/7/11.
 */

public class OutboundActivity extends BaseActivity implements ScannerManager.IScannerStatusListener {

    private final String ERROR = "Decode is interruptted or timeout ...";
    private RelativeLayout relativeLayoutBack;
    private Button buttonSubmit;
    private RelativeLayout relativeLayoutOutbound;
    //    private ListView listViewOut;
    private SwipeMenuListView swipeMenuListViewOut;
    private RadioGroup mRadioGroup;
    private RadioButton mSingleButton;
    private RadioButton mContinuousButton;
    private RadioButton mKeyHoldButton;

    private Spinner spinnerShop;
    private Spinner spinnerDoctor;


//    private TextView textView;

    private ListViewOutboundAdapter listViewOutboundAdapter;
    private List<TarrBean> tarrBeanList = new ArrayList<>();
    private List<ApplicationInfo> applicationInfoList;
    private XmlBean<OrgBean> xmlOrg = new XmlBean<>();
    private XmlBean<DocBean> xmlDoc = new XmlBean<>();
    private List<OrgBean> orgList = new ArrayList<>();
    private List<DocBean> docList = new ArrayList<>();

    protected ScannerManager mScannerManager;
    //    protected SoundUtils mSoundUtils;
    protected int decoderType = 0;
    private boolean mIsScanKeyDown = false;
    private boolean inContinuousShoot = false;

    private ArrayAdapter<DocBean> adapterDoc;
    private ArrayAdapter<OrgBean> adapterOrg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transparent();
        setContentView(R.layout.activity_outbound);
        initView();
    }

    private void initView() {
        relativeLayoutOutbound = (RelativeLayout) findViewById(R.id.relativeLayout_outbound);
        relativeLayoutBack = (RelativeLayout) findViewById(R.id.relativeLayout_back);
        buttonSubmit = (Button) findViewById(R.id.button_submit);
        swipeMenuListViewOut = (SwipeMenuListView) findViewById(R.id.swipeMenuListView_out);
        spinnerDoctor = (Spinner) findViewById(R.id.spinner_doctor);
        spinnerShop = (Spinner) findViewById(R.id.spinner_shop);
        mScannerManager = ScannerManager.getInstance();
        decoderType = mScannerManager.getDecoderType();

        mSingleButton = (RadioButton) findViewById(R.id.single_radioButton);
        mContinuousButton = (RadioButton) findViewById(R.id.continuous_radioButton);
        mKeyHoldButton = (RadioButton) findViewById(R.id.key_radioButton);
        mRadioGroup = (RadioGroup) findViewById(R.id.rg);

        setListener();
    }

    private void setListener() {
        mRadioGroup.setOnCheckedChangeListener(onCheckedChangeListener);

        listViewOutboundAdapter = new ListViewOutboundAdapter(tarrBeanList, this);
        swipeMenuListViewOut.setAdapter(listViewOutboundAdapter);
        swipeMenuListViewOut.setOnItemClickListener(onItemClickListener);
        swipeMenuListViewOut.setOnItemLongClickListener(onItemLongClickListener);
        swipeMenuListViewOut.setMenuCreator(creator);
        swipeMenuListViewOut.setOnMenuItemClickListener(onMenuItemClickListener);

        relativeLayoutOutbound.setOnClickListener(onClickListener);
        relativeLayoutBack.setOnClickListener(onClickListener);
        buttonSubmit.setOnClickListener(onClickListener);

        spinnerShop.setOnItemSelectedListener(onItemSelectedListener);
        if (WcfUtils.isNetworkAvailable(this)) {
            getOrgInfo();
        } else {
            Toast.makeText(this, "无网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
//                    swipeMenuListViewOut.setAdapter(listViewOutboundAdapter);
                    listViewOutboundAdapter.notifyDataSetChanged();
                    swipeMenuListViewOut.setSelection(listViewOutboundAdapter.getCount());
                    break;
                case 1002:
                    Toast.makeText(OutboundActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 1004:
//                    Log.e("VD", "1004----  " + msg.obj.toString());
                    XmlBean<OrgBean> org = (XmlBean<OrgBean>) msg.obj;
//                    Log.e("VD", "1004-org---  " + org);
                    if (org != null && org.getOutput() != null) {
                        orgList = org.getOutput();
                    }
//                    Log.e("VD", "1004--size--" + orgList.size());
                    adapterOrg = new ArrayAdapter<OrgBean>(OutboundActivity.this, android.R.layout.simple_spinner_item, orgList);
                    adapterOrg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerShop.setAdapter(adapterOrg);
//                    adapterOrg.notifyDataSetChanged();
                    break;
                case 1005:
//                    Log.e("VD", "1005----  " + msg.obj.toString());
                    XmlBean<DocBean> doc = (XmlBean<DocBean>) msg.obj;
                    if (doc != null && doc.getOutput() != null) {
                        DocBean docBean = new DocBean();
                        docBean.setDoc_id("");
                        docBean.setDoc_name("");
                        docList.clear();
                        docList.add(docBean);
                        docList.addAll(doc.getOutput());
//                        for (int i=0;i<docList.size();i++){
//                            Log.e("VD",i+"***"+docList.get(i).toString());
//                        }
                    }
                    adapterDoc = new ArrayAdapter<DocBean>(OutboundActivity.this, android.R.layout.simple_spinner_item, docList);
                    adapterDoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerDoctor.setAdapter(adapterDoc);
                    break;
                case 1006:
                    if (mScannerManager.getScanMode() != SCAN_CONTINUOUS_MODE) {
                        Toast.makeText(OutboundActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
                    }
                    break;

                default:
                    break;
            }
        }
    };

    private String xmlTarr(String tarrId) {
        return "<tarr_id>" + tarrId + "</tarr_id>";
    }

    private String xmlSave(String out_orgid, String out_docid, List<TarrBean> list) {
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            str += "<item>" +
                    "<tarr_id>" + list.get(i).getTarr_id() + "</tarr_id>" +
                    "<batch>" + list.get(i).getBatch() + "</batch>" +
                    "<tarr_type>" + list.get(i).getTarr_type() + "</tarr_type>" +
                    "<tarr_attr>" + list.get(i).getTarr_attr() + "</tarr_attr>" +
                    "<out_count>" + list.get(i).getNum() + "</out_count>" +
                    "<price>" + list.get(i).getPrice() + "</price>" +
                    "</item>";
        }
        return "<out_userid>" + MyApplication.USER_ID + "</out_userid>" +
                "<out_username>" + MyApplication.USER_NAME + "</out_username>" +
                "<out_orgid>" + out_orgid + "</out_orgid>" +
                "<out_docid>" + out_docid + "</out_docid>" +
                "<items>" + str + "</items>";
    }

    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            switch (menu.getViewType()) {
                case 0:
                    createMenu(menu);
                    break;
                default:
                    break;
            }
        }

        //设置滑出菜单的属性
        private void createMenu(SwipeMenu menu) {
            SwipeMenuItem item = new SwipeMenuItem(
                    getApplicationContext());
            item.setBackground(R.color.colorAccent);
            item.setWidth(dp2px(80));
            item.setTitle("删除");
            item.setTitleColor(R.color.colorPrimary);
            item.setTitleSize(15);
            menu.addMenuItem(item);
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //点击整体布局，隐藏软键盘同时刷新listview
                case R.id.relativeLayout_outbound:
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    listViewOutboundAdapter.notifyDataSetChanged();
                    break;
                case R.id.relativeLayout_back:
                    finish();
                    break;
                case R.id.button_submit:
                    if (tarrBeanList.size() > 0) {
                        new AlertDialog.Builder(OutboundActivity.this).setTitle("确认提示框").setMessage("确认提交出库信息？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (WcfUtils.isNetworkAvailable(OutboundActivity.this)) {
                                            saveInformation();
                                        } else {
                                            Toast.makeText(OutboundActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                    } else {
                        Toast.makeText(OutboundActivity.this, "没有出库数据", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.single_radioButton:
                    mScannerManager.setScanMode(ScannerManager.SCAN_SINGLE_MODE);
                    break;
                case R.id.continuous_radioButton:
                    mScannerManager.setScanMode(SCAN_CONTINUOUS_MODE);
                    break;
                case R.id.key_radioButton:
                    mScannerManager.setScanMode(ScannerManager.SCAN_KEY_HOLD_MODE);
                    break;
                default:
                    break;
            }
        }
    };

    SwipeMenuListView.OnMenuItemClickListener onMenuItemClickListener = new SwipeMenuListView.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
            tarrBeanList.remove(position);
            hander.sendEmptyMessage(1001);
//                listViewOutboundAdapter.notifyDataSetChanged();
//            Toast.makeText(OutboundActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
            return false;
        }
    };

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Log.e("VD", "position=" + position);
            //隐藏软键盘同时刷新listview
            KeyBoardUtils.hideInputForce(OutboundActivity.this);
            listViewOutboundAdapter.notifyDataSetChanged();
        }
    };

    AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            TarrBean tarrBean = tarrBeanList.get(position);
            View popView = LayoutInflater.from(OutboundActivity.this).inflate(R.layout.pop_show_info, null);
//        第一个参数是显示的视图，第二个参数是宽，第三个是高，第四个是否有焦点
            PopupWindow popupWindow = new PopupWindow(popView, 450, 500, true);
//        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            LinearLayout linearLayoutBatch = (LinearLayout) popView.findViewById(R.id.linearLayout_batch);
            linearLayoutBatch.setVisibility(View.GONE);
            ((TextView) popView.findViewById(R.id.textView_tarr_name_show)).setText(tarrBean.getTarr_name());
            ((TextView) popView.findViewById(R.id.textView_unit_show)).setText(tarrBean.getUnit());
            ((TextView) popView.findViewById(R.id.textView_spec_show)).setText(tarrBean.getSpec());
            ((TextView) popView.findViewById(R.id.textView_price_show)).setText(String.valueOf(tarrBean.getPrice()));
            ((TextView) popView.findViewById(R.id.textView_num_show)).setText(String.valueOf(tarrBean.getNum()));

            popupWindow.showAtLocation(OutboundActivity.this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            return false;
        }
    };

    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.spinner_shop:
                    if (WcfUtils.isNetworkAvailable(OutboundActivity.this)) {
                        getDocInfo();
                    } else {
                        Toast.makeText(OutboundActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.spinner_doctor:
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    //dp和px的转换
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public void onResume() {
        super.onResume();
        int res = mScannerManager.connectDecoderSRV();
        mScannerManager.addScannerStatusListener(this);
        if (decoderType == Constants.DECODER_ONED_SCAN) {
            if (!mScannerManager.getScannerEnable()) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.action_settings)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setMessage(R.string.scan_message)
                        .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        }
        switch (mScannerManager.getScanMode()) {
            case SCAN_CONTINUOUS_MODE:
                mContinuousButton.setChecked(true);
                break;
            case ScannerManager.SCAN_KEY_HOLD_MODE:
                mKeyHoldButton.setChecked(true);
                break;
            case ScannerManager.SCAN_SINGLE_MODE:
            default:
                mSingleButton.setChecked(true);
                break;
        }
    }

    public void onPause() {
        mScannerManager.removeScannerStatusListener(this);
        mScannerManager.disconnectDecoderSRV();
        super.onPause();
    }


    @Override
    public void onScannerStatusChanage(int i) {

    }

    //扫描返回的值处理
    @Override
    public void onScannerResultChanage(byte[] bytes) {
        final String data = new String(bytes);
//        Log.e("VD",data);
        if (!data.equals(ERROR)) {
            boolean bool = isRepetition(data);
            if (!bool) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String request = WcfUtils.getRequestString("gdkq", "003_001", xmlTarr(data));
//                        Log.e("VD",request);
                        SoapService soapService = new SoapService(request);
                        String res = soapService.LoadResult();
//                        Log.e("VD", res);
                        XStream xstream = new XStream();
                        xstream.alias("response", ReturnTarrBean.class);
                        xstream.alias("output", TarrBean.class);
                        ReturnTarrBean returnTarrBean = (ReturnTarrBean) xstream.fromXML(res);
                        if (returnTarrBean.getCode().equals("1")) {
                            TarrBean tarrBean = returnTarrBean.getOutput();
                            tarrBean.setTarr_code(data);
                            tarrBeanList.add(tarrBean);
                            hander.sendEmptyMessage(1001);
                        } else {
                            Message message = hander.obtainMessage(1006, returnTarrBean.getMsg());
                            hander.sendMessage(message);
                        }
                    }
                }).start();
            }
//        } else {
////            Toast.makeText(this, "请重新扫描", Toast.LENGTH_SHORT).show();
//            Message message = hander.obtainMessage(1006, "请重新扫描");
//            hander.sendMessage(message);
        }
    }

    /**
     * 判断是否再次扫描，是就直接将数量加1，否就直接请求数据
     *
     * @param data
     * @return true 再次扫描   false请求数据
     */
    private boolean isRepetition(String data) {
        for (int i = 0; i < tarrBeanList.size(); i++) {
            if (data.equals(tarrBeanList.get(i).getTarr_code())) {
                int num = tarrBeanList.get(i).getNum() + 1;
                tarrBeanList.get(i).setNum(num);
                hander.sendEmptyMessage(1001);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BUTTON_A: {
                if (!mIsScanKeyDown) {
                    mScannerManager.dispatchScanKeyEvent(event);
                    // mScannerManager.triggerLevel(ScannerManager.LOW_LEVEL);
                    mIsScanKeyDown = true;
                    if (!inContinuousShoot) {
                        inContinuousShoot = true;
                        mScannerManager.startContinuousScan();
                    } else {
                        inContinuousShoot = false;
                        mScannerManager.stopContinuousScan();
                    }
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BUTTON_A: {
                if (mIsScanKeyDown) {
                    mIsScanKeyDown = false;
                    mScannerManager.dispatchScanKeyEvent(event);
                    // mScannerManager.triggerLevel(ScannerManager.HIGH_LEVEL);
                }
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private void getOrgInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String request = WcfUtils.getRequestString("gdkq", "002_001", "");
                SoapService soapService = new SoapService(request);
                String res = soapService.LoadResult();
                XStream xstream = new XStream();
                xstream.alias("response", XmlBean.class);
                xstream.alias("row", OrgBean.class);
                xmlOrg = (XmlBean<OrgBean>) xstream.fromXML(res);
                Message message = hander.obtainMessage(1004, xmlOrg);
                hander.sendMessage(message);
                if (xmlOrg != null && xmlOrg.getOutput() != null) {
//                    String org_id = ((OrgBean)(spinnerShop.getSelectedItem())).getOrg_id();
                    String org_id = xmlOrg.getOutput().get(0).getOrg_id();
                    String str = "<org_id>" + org_id + "</org_id>";
                    String requestDoc = WcfUtils.getRequestString("gdkq", "002_002", str);
                    SoapService soapServiceDoc = new SoapService(requestDoc);
                    String resDoc = soapServiceDoc.LoadResult();
                    XStream xstreamDoc = new XStream();
                    xstreamDoc.alias("response", XmlBean.class);
                    xstreamDoc.alias("row", DocBean.class);
                    xmlDoc = (XmlBean<DocBean>) xstreamDoc.fromXML(resDoc);
                    Message messageDoc = hander.obtainMessage(1005, xmlDoc);
                    hander.sendMessage(messageDoc);
                }
            }
        }).start();
    }

    private void getDocInfo() {
        String org_id = ((OrgBean) (spinnerShop.getSelectedItem())).getOrg_id();
        final String str = "<org_id>" + org_id + "</org_id>";
        new Thread(new Runnable() {
            @Override
            public void run() {
                String request = WcfUtils.getRequestString("gdkq", "002_002", str);
                SoapService soapService = new SoapService(request);
                String res = soapService.LoadResult();
//                Log.e("VD",res);
                XStream xstreamDoc = new XStream();
                xstreamDoc.alias("response", XmlBean.class);
                xstreamDoc.alias("row", DocBean.class);
                xmlDoc = (XmlBean<DocBean>) xstreamDoc.fromXML(res);
                Message messageDoc = hander.obtainMessage(1005, xmlDoc);
                hander.sendMessage(messageDoc);
            }
        }).start();
    }

    private void saveInformation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String org_id = ((OrgBean) (spinnerShop.getSelectedItem())).getOrg_id();
                String doc_id = ((DocBean) (spinnerDoctor.getSelectedItem())).getDoc_id();
                String input = xmlSave(org_id, doc_id, tarrBeanList);
                String request = WcfUtils.getRequestString("gdkq", "003_002", input);
//                Log.e("VD", request);
                SoapService soapService = new SoapService(request);
                String res = soapService.LoadResult();
                XStream xstream = new XStream();
                xstream.alias("response", XmlBean.class);
                XmlBean<String> strBean = (XmlBean<String>) xstream.fromXML(res);
                if (strBean.getCode().equals("1")) {
                    Message message = hander.obtainMessage(1002, "保存成功");
                    hander.sendMessage(message);
                    tarrBeanList.clear();
                    Message message1 = hander.obtainMessage(1001);
                    hander.sendMessage(message1);
                } else {
                    Message message = hander.obtainMessage(1002, strBean.getMsg());
                    hander.sendMessage(message);
                }
            }
        }).start();
    }
}



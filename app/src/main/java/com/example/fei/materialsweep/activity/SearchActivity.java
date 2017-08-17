package com.example.fei.materialsweep.activity;

import android.app.DatePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fei.materialsweep.R;
import com.example.fei.materialsweep.adapter.ExpandableAdapter;
import com.example.fei.materialsweep.bean.OutBean;
import com.example.fei.materialsweep.bean.OutChildBean;
import com.example.fei.materialsweep.bean.XmlBean;
import com.example.fei.materialsweep.utils.SoapService;
import com.example.fei.materialsweep.utils.WcfUtils;
import com.thoughtworks.xstream.XStream;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by fei on 2017/7/12.
 */

public class SearchActivity extends BaseActivity implements AbsListView.OnScrollListener{

    private RelativeLayout relativeLayoutBack;
    private RelativeLayout relativeLayoutTitle;
    private TextView textViewBeginDateShow;
    private TextView textViewEndDateShow;
//    private PinnedHeaderExpandableListView expandableListViewSearch;
    private ExpandableListView expandableListViewSearch;
    private LayoutInflater mInflater;
    private FrameLayout frameLayoutGroup;
    private TextView textViewShowTime;
    private RelativeLayout relativeLayoutDate;
    private int indicatorGroupId = -1;
    private int indicatorGroupHeight;


    private ExpandableAdapter expandableAdapter;

    private List<OutBean> outBeanList = new ArrayList<>();
    private XmlBean<OutBean> xmlOutBean = new XmlBean<>();

    private int year = 0;
    private int month = 0;
    private int day = 0;

    private PopupWindow popupWindow;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transparent();
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {

        relativeLayoutTitle = (RelativeLayout) findViewById(R.id.relativeLayout_title);
        relativeLayoutBack = (RelativeLayout) findViewById(R.id.relativeLayout_back);
        relativeLayoutDate = (RelativeLayout) findViewById(R.id.relativeLayout_date);
        expandableListViewSearch = (ExpandableListView) findViewById(R.id.expandableListView_search);
        textViewShowTime = (TextView) findViewById(R.id.textView_show_time);
        frameLayoutGroup = (FrameLayout) findViewById(R.id.frameLayout_group);
        frameLayoutGroup.setVisibility(View.GONE);

        this.getLayoutInflater().inflate(R.layout.item_expandablelistview_group,frameLayoutGroup,true);
//        expandableListViewSearch.setVisibility(View.INVISIBLE);


        setListener();
    }

    private void setListener() {

        expandableAdapter = new ExpandableAdapter(outBeanList, this);
        expandableListViewSearch.setAdapter(expandableAdapter);
        expandableListViewSearch.setOnChildClickListener(onChildClickListener);
        expandableListViewSearch.setOnScrollListener(this);
        expandableListViewSearch.setGroupIndicator(null);

        relativeLayoutDate.setOnClickListener(onClickListener);
        relativeLayoutBack.setOnClickListener(onClickListener);
//        buttonChooseBegin.setOnClickListener(onClickListener);
//        buttonChooseEnd.setOnClickListener(onClickListener);
    }

    ExpandableListView.OnChildClickListener onChildClickListener = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            if (childPosition > 0) {
                OutChildBean outChildBean = outBeanList.get(groupPosition).getItems().get(childPosition);
                View popView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.pop_show_info, null);
//        第一个参数是显示的视图，第二个参数是宽，第三个是高，第四个是否有焦点
                PopupWindow popupWindow = new PopupWindow(popView, 450, 500, true);
//        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                LinearLayout linearLayoutPrice = (LinearLayout) popView.findViewById(R.id.linearLayout_price);
                linearLayoutPrice.setVisibility(View.GONE);
                ((TextView) popView.findViewById(R.id.textView_tarr_name_show)).setText(outChildBean.getTarr_name());
                ((TextView) popView.findViewById(R.id.textView_unit_show)).setText(outChildBean.getUnit());
                ((TextView) popView.findViewById(R.id.textView_spec_show)).setText(outChildBean.getSpec());
                ((TextView) popView.findViewById(R.id.textView_batch_show)).setText(outChildBean.getBatch());
                ((TextView) popView.findViewById(R.id.textView_num_show)).setText(String.valueOf(outChildBean.getOut_count()));

                popupWindow.showAtLocation(SearchActivity.this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            }
            return false;
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:
                    xmlOutBean = (XmlBean<OutBean>) msg.obj;
                    if (xmlOutBean != null) {
                        if (xmlOutBean.getOutput() != null) {
                            outBeanList = xmlOutBean.getOutput();
                        }
                    }
//                    Log.e("VD", outBeanList.size() + "");
                    expandableAdapter = new ExpandableAdapter(outBeanList, SearchActivity.this);
                    expandableListViewSearch.setAdapter(expandableAdapter);
                    expandableListViewSearch.setOnChildClickListener(onChildClickListener);
                    expandableListViewSearch.setOnScrollListener(SearchActivity.this);
                    expandableListViewSearch.setGroupIndicator(null);
                    expandableAdapter.notifyDataSetChanged();
                    break;
                case 1002:
                    Toast.makeText(SearchActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 1003:
                    Toast.makeText(SearchActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    outBeanList.clear();
                    expandableAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.relativeLayout_back:
                    finish();
                    break;
                case R.id.relativeLayout_date:
                    showPopupWindow();
                    break;
                default:
                    break;
            }
        }
    };

    private void showDateDialog(final int index) {
        Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
//        Log.e("VD",dateAndTime.toString());
        int v_year = dateAndTime.get(Calendar.YEAR);
        int v_month = dateAndTime.get(Calendar.MONTH);
        int v_day = dateAndTime.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                SearchActivity.this.year = year;
                month = monthOfYear + 1;
                day = dayOfMonth;
                setDate(index);
            }
        }, v_year, v_month, v_day).show();
    }

    private void setDate(int index) {
        String mm = String.valueOf(month);
        String dd = String.valueOf(day);
        if (month < 10) {
            mm = "0" + mm;
        }
        if (day < 10) {
            dd = "0" + dd;
        }
        switch (index) {
            case 1:
                textViewBeginDateShow.setText(year + "-" + mm + "-" + dd);
                break;
            case 2:
                textViewEndDateShow.setText(year + "-" + mm + "-" + dd);
                break;
            default:
                break;
        }
    }

    private void showPopupWindow() {
//        final List<String> strings = getWorkTeam();
//		Log.e("VD", strings.size() + "");
        View view = LayoutInflater.from(this).inflate(R.layout.activity_search_choose_time, null);
//        第一个参数是显示的视图，第二个参数是宽，第三个是高，第四个是否有焦点
        popupWindow = new PopupWindow(view, 450, 500, true);
//        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        //popupWindow.setBackgroundDrawable(new BitmapDrawable())由于过时了，用下面代码设置透明背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        textViewBeginDateShow = (TextView) view.findViewById(R.id.textView_begin_date_show);
        textViewEndDateShow = (TextView) view.findViewById(R.id.textView_end_date_show);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        final Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
//        Log.e("VD", str);
        textViewBeginDateShow.setText(str);
        textViewEndDateShow.setText(str);

        textViewBeginDateShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(1);
            }
        });
        textViewEndDateShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(2);
            }
        });

        Button buttonSure = (Button) view.findViewById(R.id.button_submit);
        Button buttonCancel = (Button) view.findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        buttonSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String beginDate = textViewBeginDateShow.getText().toString().trim();
                String endDate = textViewEndDateShow.getText().toString().trim();
                popupWindow.dismiss();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date dateBegin = sdf.parse(beginDate); //转换为util.date
                    Date dateEnd = sdf.parse(endDate);
                    if (dateBegin.compareTo(dateEnd) <= 0 && dateEnd.compareTo(curDate) <= 0) {
                        textViewShowTime.setText("从" + beginDate + "到" + endDate + "的出库信息");
                        if (WcfUtils.isNetworkAvailable(SearchActivity.this)) {
                            getInfo(beginDate, endDate);
                        } else {
                            Toast.makeText(SearchActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        textViewShowTime.setText("请选择正确的时间区间");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                expandableListViewSearch.setVisibility(View.VISIBLE);
            }
        });
        popupWindow.showAtLocation(SearchActivity.this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return false;
        }
        return super.dispatchTouchEvent(event);
    }

    private void getInfo(final String begin, final String end) {
//        String begin = textViewBeginDateShow.getText().toString().trim();
//        String end = textViewEndDateShow.getText().toString().trim();
        final String str = xmlInput(begin, end);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String request = WcfUtils.getRequestString("gdkq", "003_003", str);
//                Log.e("VD", "request=" + request);
                SoapService soapService = new SoapService(request);
                String res = soapService.LoadResult();
//                Log.e("VD", "res=" + res);
                XStream xstream = new XStream();
                xstream.alias("response", XmlBean.class);
                xstream.alias("row", OutBean.class);
                xstream.alias("item", OutChildBean.class);
                xmlOutBean = (XmlBean<OutBean>) xstream.fromXML(res);
                if (xmlOutBean.getCode().equals("1")) {
                    Message message = handler.obtainMessage(1001, xmlOutBean);
                    handler.sendMessage(message);
                } else if (xmlOutBean.getCode().equals("0")) {
                    Message message = handler.obtainMessage(1003, xmlOutBean.getMsg());
                    handler.sendMessage(message);
                } else {
                    Message message = handler.obtainMessage(1002, xmlOutBean.getMsg());
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    private String xmlInput(String begin, String end) {
        return "<start_time>" + begin + "</start_time>" +
                "<end_time>" + end + "</end_time>";
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        final ExpandableListView listView = (ExpandableListView) view;
        /**
         * calculate point (0,0)
         */
        int npos = view.pointToPosition(0, 0);// 其实就是firstVisibleItem
//        Log.e("VD","npos="+npos);
        if (npos == AdapterView.INVALID_POSITION)// 如果第一个位置值无效
            return;

        long pos = listView.getExpandableListPosition(npos);
        int childPos = ExpandableListView.getPackedPositionChild(pos);// 获取第一行child的id
        int groupPos = ExpandableListView.getPackedPositionGroup(pos);// 获取第一行group的id
//        Log.e("vd","childPos="+childPos+"---------groupPos"+groupPos);
        if (childPos == AdapterView.INVALID_POSITION) {// 第一行不是显示child,就是group,此时没必要显示指示器
            View groupView = listView.getChildAt(npos - listView.getFirstVisiblePosition());// 第一行的view
            indicatorGroupHeight = groupView.getHeight();// 获取group的高度
            frameLayoutGroup.setVisibility(View.GONE);// 隐藏指示器
        } else{
            frameLayoutGroup.setVisibility(View.VISIBLE);// 滚动到第一行是child，就显示指示器
        }
        // get an error data, so return now
        if (indicatorGroupHeight == 0) {
            return;
        }
        // update the data of indicator group view
        if (groupPos != indicatorGroupId) {// 如果指示器显示的不是当前group
            expandableAdapter.getGroupView(groupPos, listView.isGroupExpanded(groupPos), frameLayoutGroup.getChildAt(0), null);// 将指示器更新为当前group
            indicatorGroupId = groupPos;
            // mAdapter.hideGroup(indicatorGroupId); // we set this group view
            // to be hided
            // 为此指示器增加点击事件
            frameLayoutGroup.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    listView.collapseGroup(indicatorGroupId);
                }
            });
        }
        if (indicatorGroupId == -1) // 如果此时grop的id无效，则返回
            return;

        /**
         * calculate point (0,indicatorGroupHeight) 下面是形成往上推出的效果
         */
        int showHeight = indicatorGroupHeight;
        int nEndPos = listView.pointToPosition(0, indicatorGroupHeight);// 第二个item的位置
        if (nEndPos == AdapterView.INVALID_POSITION)//如果无效直接返回
            return;
        long pos2 = listView.getExpandableListPosition(nEndPos);
        int groupPos2 = ExpandableListView.getPackedPositionGroup(pos2);//获取第二个group的id
        if (groupPos2 != indicatorGroupId) {//如果不等于指示器当前的group
            View viewNext = listView.getChildAt(nEndPos
                    - listView.getFirstVisiblePosition());
            showHeight = viewNext.getTop();
        }

        // update group position
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) frameLayoutGroup.getLayoutParams();
        layoutParams.topMargin = -(indicatorGroupHeight - showHeight);
        frameLayoutGroup.setLayoutParams(layoutParams);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}

package com.example.fei.materialsweep.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fei.materialsweep.R;
import com.example.fei.materialsweep.bean.TarrBean;

import java.util.List;

public class ListViewOutboundAdapter extends BaseAdapter implements View.OnTouchListener {
    private List<TarrBean> list;
    private Context context;
    private LayoutInflater inflater;
    private int before;
    private int index = -1;

    public ListViewOutboundAdapter(List<TarrBean> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_view_outbound, null);
            viewHolder.textViewTarrName = (TextView) convertView.findViewById(R.id.textView_tarr_name);
            viewHolder.textViewUnit = (TextView) convertView.findViewById(R.id.textView_unit);
            viewHolder.textViewSpec = (TextView) convertView.findViewById(R.id.textView_spec);
            viewHolder.textViewPrice = (TextView) convertView.findViewById(R.id.textView_price);
//            viewHolder.textViewTarrAttr = (TextView) convertView.findViewById(R.id.textView_attr);
            viewHolder.editTextNum = (EditText) convertView.findViewById(R.id.editText_num_show);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final TarrBean tarrBean = list.get(position);
        viewHolder.textViewTarrName.setText(tarrBean.getTarr_name());
//        viewHolder.textViewTarrAttr.setText(tarrBean.getTarr_attr() == 1 ? "材料" : "药品");
        viewHolder.textViewPrice.setText(String.valueOf(tarrBean.getPrice()));
        viewHolder.textViewSpec.setText(tarrBean.getSpec());
        viewHolder.textViewUnit.setText(tarrBean.getUnit());

        //设置editTextNum的Tag为position，再textwatch中获取
        viewHolder.editTextNum.setTag(position);
        viewHolder.editTextNum.clearFocus();

        final ViewHolder finalViewHolder = viewHolder;
        //设置editTextNum的输入监听事件
        viewHolder.editTextNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int pos = (int) finalViewHolder.editTextNum.getTag();
                TarrBean b = list.get(pos);
                if (!TextUtils.isEmpty(s)) {
                    b.setNum(Integer.parseInt(s + ""));
                } else {
                    b.setNum(1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        viewHolder.editTextNum.setText(String.valueOf(tarrBean.getNum()));

        //设置editTextNum和convertView的OnTouchListener，解决点击冲突问题
        viewHolder.editTextNum.setOnTouchListener(this);
        convertView.setOnTouchListener(this);

        //设置editTextNum的OnEditorActionListener，点击软键盘的完成键，关闭软键盘同时判断editTextNum的显示是否为空
        //若为空则设置为1
        viewHolder.editTextNum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(finalViewHolder.editTextNum.getWindowToken(), 0);
                    if (TextUtils.isEmpty(finalViewHolder.editTextNum.getText())) {
                        finalViewHolder.editTextNum.setText("1");
                    }
                }
                return false;
            }
        });
        return convertView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v instanceof EditText) {
            EditText et = (EditText) v;
            et.setFocusable(true);
            et.setFocusableInTouchMode(true);
        } else {
            ViewHolder holder = (ViewHolder) v.getTag();
            holder.editTextNum.setFocusable(false);
            holder.editTextNum.setFocusableInTouchMode(false);
//            KeyBoardUtils.closeKeybord(holder.editTextNum, context);
        }
        return false;
    }

    private class ViewHolder {
        TextView textViewTarrName;
        TextView textViewUnit;
        TextView textViewSpec;
        TextView textViewPrice;
//        TextView textViewTarrAttr;
        EditText editTextNum;
    }
}


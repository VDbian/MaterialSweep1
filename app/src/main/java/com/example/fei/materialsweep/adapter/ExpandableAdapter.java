package com.example.fei.materialsweep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fei.materialsweep.R;
import com.example.fei.materialsweep.bean.OutBean;
import com.example.fei.materialsweep.bean.OutChildBean;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by fei on 2017/8/17.
 */

public class ExpandableAdapter extends BaseExpandableListAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<OutBean> outBeanList;


    public ExpandableAdapter(List<OutBean> outBeanList, Context context) {
        List<OutBean> mList = new ArrayList<>();
        for (int i = 0; i < outBeanList.size(); i++) {
            OutBean outBean = outBeanList.get(i);
            OutChildBean outChildBean = new OutChildBean("材料名称", "单位", "规格", "批次", "数量");
            List<OutChildBean> outChildBeanList = new ArrayList<>();
            outChildBeanList.add(outChildBean);
            outChildBeanList.addAll(outBean.getItems());
            outBean.setItems(outChildBeanList);
            mList.add(outBean);
        }
        this.outBeanList = mList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<OutChildBean> outChildBeanList = outBeanList.get(groupPosition).getItems();
        return outChildBeanList == null ? null : outChildBeanList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//        if (childPosition==0){
//            convertView = inflater.inflate(R.layout.item_expandablelistview_child, parent, false);
//            return convertView;
//        }
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_expandablelistview_child, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.textViewBatch = (TextView) convertView.findViewById(R.id.textView_batch);
            childViewHolder.textViewOutCount = (TextView) convertView.findViewById(R.id.textView_out_count);
            childViewHolder.textViewSpec = (TextView) convertView.findViewById(R.id.textView_spec);
            childViewHolder.textViewTarrName = (TextView) convertView.findViewById(R.id.textView_tarr_name);
            childViewHolder.textViewUnit = (TextView) convertView.findViewById(R.id.textView_unit);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        OutChildBean outChildBean = outBeanList.get(groupPosition).getItems().get(childPosition);
        childViewHolder.textViewUnit.setText(outChildBean.getUnit());
        childViewHolder.textViewTarrName.setText(outChildBean.getTarr_name());
        childViewHolder.textViewBatch.setText(outChildBean.getBatch());
        childViewHolder.textViewSpec.setText(outChildBean.getSpec());
        childViewHolder.textViewOutCount.setText(String.valueOf(outChildBean.getOut_count()));
        return convertView;
    }

    @Override
    public int getGroupCount() {
        return outBeanList == null ? 0 : outBeanList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
//        Log.e("VD",outBeanList.toString());
        if (outBeanList.size() <= 0) {
            return 0;
        } else {
            if (outBeanList.get(groupPosition) == null) {
                return 0;
            } else {
                if (outBeanList.get(groupPosition).getItems() == null) {
                    return 0;
                } else {
                    return outBeanList.get(groupPosition).getItems().size();
                }
            }
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return outBeanList == null ? null : outBeanList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder = null;
//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.item_expandablelistview_group, parent, false);
//            groupViewHolder = new GroupViewHolder();
//            groupViewHolder.textViewOrgNameShow = (TextView) convertView.findViewById(R.id.textView_org_name_show);
//            groupViewHolder.textViewDocNameShow = (TextView) convertView.findViewById(R.id.textView_doc_name_show);
//            groupViewHolder.textViewOutUsernameShow = (TextView) convertView.findViewById(R.id.textView_out_username_show);
//            groupViewHolder.textViewOutTimeShow = (TextView) convertView.findViewById(R.id.textView_out_time_show);
//            convertView.setTag(groupViewHolder);
//        } else {
//            groupViewHolder = (GroupViewHolder) convertView.getTag();
//        }

        View v;
        if (convertView == null) {
            v = inflater.inflate(R.layout.item_expandablelistview_group, parent, false);
        } else {
            v = convertView;
        }
        groupViewHolder = new GroupViewHolder();
        groupViewHolder.textViewOrgNameShow = (TextView) v.findViewById(R.id.textView_org_name_show);
        groupViewHolder.textViewDocNameShow = (TextView) v.findViewById(R.id.textView_doc_name_show);
        groupViewHolder.textViewOutUsernameShow = (TextView) v.findViewById(R.id.textView_out_username_show);
        groupViewHolder.textViewOutTimeShow = (TextView) v.findViewById(R.id.textView_out_time_show);

        OutBean outBean = outBeanList.get(groupPosition);
        groupViewHolder.textViewOrgNameShow.setText(outBean.getOrg_name());
        groupViewHolder.textViewDocNameShow.setText(outBean.getDoc_name());
        groupViewHolder.textViewOutUsernameShow.setText(outBean.getOut_username());
        groupViewHolder.textViewOutTimeShow.setText(outBean.getOut_time());
        return v;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class GroupViewHolder {
        private LinearLayout linearLayoutChildHeader;
        private TextView textViewOrgNameShow;
        private TextView textViewDocNameShow;
        private TextView textViewOutUsernameShow;
        private TextView textViewOutTimeShow;
    }

    public class ChildViewHolder {
        private TextView textViewTarrName;
        private TextView textViewUnit;
        private TextView textViewSpec;
        private TextView textViewBatch;
        private TextView textViewOutCount;
    }

}

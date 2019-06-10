package com.duke.random.randommachine;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends BaseQuickAdapter<BaseModel, HomeAdapter.ViewHolder> {

    private View mEmptyView;

    public HomeAdapter(Context context) {
        super(R.layout.item_home_list, getDataList(MMKV.defaultMMKV().decodeString(Constants.KEY_DATA)));
        mContext = context;
    }

    private static List<BaseModel> getDataList(String dataString) {
        if (!TextUtils.isEmpty(dataString)) {
            return JSONArray.parseArray(dataString, BaseModel.class);
        }
        return new ArrayList<>();
    }

    @Override
    protected void convert(ViewHolder helper, BaseModel item) {
        helper.textView.setText(item.text);
        helper.checkBox.setChecked(item.code == 1);
    }

    public void toggle(int position) {
        if (mData != null) {
            int old = mData.get(position).code;
            mData.get(position).code = old == 0 ? 1 : 0;
            notifyDataSetChanged();
        }
    }

    public void onPause() {
        //save data
        if (mData != null && !mData.isEmpty()) {
            JSONArray jsonArray = (JSONArray) JSONArray.toJSON(mData);
            MMKV.defaultMMKV().encode(Constants.KEY_DATA, jsonArray.toJSONString());
        }
    }

    public void toggle() {
        if (mData != null && mData.size() > 0) {
            if (isCheckAll()) {
                for (int i = 0; i < mData.size(); i++) {
                    mData.get(i).code = 0;
                }
            } else {
                for (int i = 0; i < mData.size(); i++) {
                    mData.get(i).code = 1;
                }
            }
            notifyDataSetChanged();
        }
    }

    public boolean isCheckAll() {
        int count = 0;
        if (mData != null) {
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).code != 0) {
                    count ++;
                }
            }
            return count == mData.size();
        }
        return false;
    }

    public void setMyEmptyView(View view) {
        mEmptyView = view;
        checkEmpty();
    }

    private void checkEmpty() {
        if (mEmptyView != null) {
            if (mData != null && mData.size() > 0) {
                getRecyclerView().setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
            } else {
                getRecyclerView().setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
            }
        }
    }

    public ArrayList<String> getCheckedData() {
        ArrayList<String> checked = new ArrayList<>();
        for (int i = 0; i < mData.size(); i ++) {
            BaseModel model = mData.get(i);
            if (model != null && model.code == 1) {
                checked.add(model.text);
            }
        }
        return checked;
    }

    public int checkedCount() {
        if (mData != null) {
            int count = 0;
            for (int i = 0; i < mData.size(); i ++) {
                BaseModel model = mData.get(i);
                if (model != null && model.code == 1) {
                    count ++;
                }
            }
            return count;
        }
        return 0;
    }

    public class ViewHolder extends BaseViewHolder {

        TextView textView;
        CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.text);
            checkBox = view.findViewById(R.id.checkbox);
        }
    }
}

package com.jerry.nsis.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.jerry.nsis.R;
import com.jerry.nsis.adapter.OfficeAdapter;
import com.jerry.nsis.entity.Office;

import java.util.List;

/**
 * Created by Jerry on 2016/4/11.
 */
public class OfficeFragment extends Fragment {


    private View mView;

    private GridView mGridView;

    private OfficeAdapter mAdapter;

    private List<Office> mOfficeList;

    private int mSelectdIndex = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_office, container, false);
        mGridView = (GridView) mView.findViewById(R.id.gridview);
        return mView;
    }

    public void setOfficeData(List<Office> officeList) {
        mOfficeList = officeList;
        mAdapter = new OfficeAdapter(getActivity(), mOfficeList, R.layout.item_office);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectdIndex = position;
                for(Office o:mOfficeList) {
                    o.setCheck(false);
                }
                mOfficeList.get(position).setCheck(true);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 获取选择的科室
     * @return
     */
    public Office getSelectedOffice() {
        if (mSelectdIndex == -1) {
            return null;
        } else {
            return mOfficeList.get(mSelectdIndex);
        }
    }
}

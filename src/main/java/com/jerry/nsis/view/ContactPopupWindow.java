package com.jerry.nsis.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.adapter.SortAdapter;
import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.common.ServiceConstant;
import com.jerry.nsis.entity.SortModel;
import com.jerry.nsis.utils.CharacterParser;
import com.jerry.nsis.utils.DensityUtils;
import com.jerry.nsis.utils.HttpGetUtil;
import com.jerry.nsis.utils.JsonUtil;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.utils.PinyinComparator;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jerry on 2016/2/23.
 */
public class ContactPopupWindow extends PopupWindow {

    private Context mContext;
    private View view;
    private TextView mTextTitle;
    private ImageView mImage;
    private ImageView mClose;
    private ListView mListView;
    private SideBar mSideBar;
    private String type;
    private Drawable drawable;
    private SortAdapter mAdapter;
    private TextView mTextDialog;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> mContactList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    public ContactPopupWindow(Context context) {
        super(context);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_contact, null);
        setContentView(view);
        initView();
        // 设置SelectPicPopupWindow弹出窗体的宽
        setWidth(DensityUtils.dp2px(context, 280));
        // 设置SelectPicPopupWindow弹出窗体的高
        setHeight(DensityUtils.dp2px(context, 600));
        // 设置SelectPicPopupWindow弹出窗体可点击
        setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        setBackgroundDrawable(dw);


    }

    private void initView() {
        mListView = (ListView) view.findViewById(R.id.listview);
        mTextDialog = (TextView) view.findViewById(R.id.dialog);
        mSideBar = (SideBar) view.findViewById(R.id.sidebar);
        mTextTitle = (TextView) view.findViewById(R.id.tv_title);
        mImage = (ImageView) view.findViewById(R.id.iv_img);
        mClose = (ImageView) view.findViewById(R.id.iv_close);
        mSideBar.setTextView(mTextDialog);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        //设置右侧触摸监听
        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }

            }
        });

    }

    /**
     * 为ListView填充数据
     *
     * @return
     */
    private List<SortModel> filledData(List<SortModel> contactList) {
        for (SortModel sortModel : contactList) {
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(sortModel.getName());
            L.i("拼音是：" + pinyin);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setLetter(sortString.toUpperCase());
            } else {
                sortModel.setLetter("#");
            }
        }
        return contactList;

    }

    public void showWindow(View parent, String type, Drawable drawable) {
        this.type = type;
        this.drawable = drawable;
        loadContactDatal(parent, LoginInfo.OFFICE_ID, type);
    }

    private void show(View parent) {
        if (!isShowing()) {
            showAtLocation(parent, Gravity.RIGHT, DensityUtils.dp2px(mContext, 300), 0);
            mImage.setImageDrawable(drawable);
            mTextTitle.setText(type);

            filledData(mContactList);
            // 根据a-z进行排序源数据
            Collections.sort(mContactList, pinyinComparator);
            mAdapter = new SortAdapter(mContext, mContactList);
            mListView.setAdapter(mAdapter);

        } else {
            dismiss();
        }
    }

    /**
     * 读取护理信息明细
     */
    private void loadContactDatal(final View parent, String officeId, String type) {
        try {
            String url = ServiceConstant.SERVICE_IP + ServiceConstant.CONTACT_SERVICE + ServiceConstant.FIND_TELEPHONE_BY_OFFICEI_D + "?officeid=" + officeId + "&type=" + URLEncoder.encode(type, "UTF-8");
            HttpGetUtil getUtil = new HttpGetUtil() {
                @Override
                public void success(String json) {
                    mContactList = JsonUtil.getSortModelFromJson(json);
                    L.i("共读取到联系人数量：" + mContactList.size());
                    show(parent);
                }
            };
            getUtil.doGet(mContext, url, "联系人");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

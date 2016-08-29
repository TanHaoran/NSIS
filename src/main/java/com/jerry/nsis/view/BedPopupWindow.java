package com.jerry.nsis.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.common.CommonValues;
import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.common.ServiceConstant;
import com.jerry.nsis.entity.BedInfo;
import com.jerry.nsis.entity.Doctor;
import com.jerry.nsis.entity.NursingNote;
import com.jerry.nsis.entity.Patient;
import com.jerry.nsis.utils.DateUtil;
import com.jerry.nsis.utils.DensityUtils;
import com.jerry.nsis.utils.HttpGetUtil;
import com.jerry.nsis.utils.JsonUtil;
import com.jerry.nsis.utils.L;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Jerry on 2016/3/4.
 */
public class BedPopupWindow extends PopupWindow {

    private View view;
    private Context mContext;
    private Button button;

    private TextView bedText;
    private TextView hosIdText;
    private TextView docText;
    private TextView nameText;
    private TextView sexText;
    private TextView ageText;
    private TextView dateText;
    private TextView diagnosisText;
    private TextView levelText;
    private TextView noteText;
    private EditText noteEdit;

    private View parent;

    private BedInfo info;

    private List<NursingNote> nursingNoteList;

    public BedInfo getInfo() {
        return info;
    }

    public void setInfo(BedInfo info) {
        this.info = info;
    }

    public BedPopupWindow(Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_main_bed, null);
        // 设置SelectPicPopupWindow的View
        setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        setWidth(DensityUtils.dp2px(context, 280));
        // 设置SelectPicPopupWindow弹出窗体的高
        setHeight(DensityUtils.dp2px(context, 380));
        // 设置SelectPicPopupWindow弹出窗体可点击
        setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景，设置后点击外部可以消失
//        setBackgroundDrawable(dw);

        bedText = (TextView) view.findViewById(R.id.tv_bed);
        hosIdText = (TextView) view.findViewById(R.id.tv_hosid);
        docText = (TextView) view.findViewById(R.id.tv_doc);
        nameText = (TextView) view.findViewById(R.id.tv_name);
        sexText = (TextView) view.findViewById(R.id.tv_sex);
        ageText = (TextView) view.findViewById(R.id.tv_age);
        dateText = (TextView) view.findViewById(R.id.tv_date);
        diagnosisText = (TextView) view.findViewById(R.id.tv_diagnosis);
        levelText = (TextView) view.findViewById(R.id.tv_level);
        noteText = (TextView) view.findViewById(R.id.tv_note);
        noteEdit = (EditText) view.findViewById(R.id.et_note);

        noteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 暂时屏蔽
                if (info.getNursing().getItemType().equals(CommonValues.TYPE_CUSTOM)) {
                    String note = noteText.getText().toString();
                    noteEdit.setText(note);
                    noteText.setVisibility(View.INVISIBLE);
                    noteEdit.setVisibility(View.VISIBLE);
                    button.setText("保存");
                }
                */
            }
        });


        button = (Button) view.findViewById(R.id.btn_close);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noteEdit.getVisibility() == View.VISIBLE) {
                    saveToServer(LoginInfo.OFFICE_ID, info.getNursing().getItemId(), info.getNursingDetail().getFrequencyId(),
                            info.getHosId(), info.getInTimes(), info.getNo(), noteEdit.getText().toString(), false);
                } else {
                    dismiss();
                }
            }
        });
    }

    /**
     * 保存新的护理项目明细
     */
    private void saveToServer(String officeId, String itemId, String frequencyId,
                              final String hosId, final int inTimes, final String bedNo,
                              final String itemContent, boolean isUpdate) {
        try {
            String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.SAVE_NURSE_ITEM + "?officeid=" + officeId
                    + "&ItemID=" + itemId + "&PeriodID=" + frequencyId + "&InhosId=" + hosId
                    + "&InhosTimes=" + inTimes + "&BedID=" + bedNo + "&ItemContent=" + URLEncoder.encode(itemContent, "UTF-8");
            HttpGetUtil getUtil = new HttpGetUtil() {
                @Override
                public void success(String json) {
                    L.i("保存返回值：" + json);
                    noteText.setVisibility(View.VISIBLE);
                    noteEdit.setVisibility(View.INVISIBLE);
                    noteText.setText(itemContent);
                    button.setText("关闭");
                }
            };
            getUtil.doGet(mContext, url, isUpdate, "保存护理项目");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent, BedInfo info) {
        this.parent = parent;
        this.info = info;
        if (info.getNursing().getItemType().equals(CommonValues.TYPE_MESSAGE)) {
            return;
        }
        if (!info.getNursing().getItemType().equals(CommonValues.TYPE_TEMPER)) {
            loadNursingDetail(LoginInfo.OFFICE_ID, info.getNursing().getItemId());
        } else {
            showWindow();
        }
    }

    private void showWindow() {
        if (!isShowing()) {
            // 以下拉方式显示popupwindow
            showAsDropDown(parent, parent.getLayoutParams().width / 2, -DensityUtils.dp2px(mContext, 1));
            boolean isFind = false;
            for (Patient p : LoginInfo.mPatientList) {
                if (p.getHosId().equals(info.getHosId()) && p.getInTimes() == info.getInTimes()) {
                    isFind = true;
                    setPopupWindowInfo(p);
                }
            }
            // 如果从所有病患中查询不到信息，就从出院人员中查询
            if (!isFind) {
                loadOutPatient(info.getHosId(), info.getInTimes());
            }
            if (!info.getNursing().getItemType().equals(CommonValues.TYPE_TEMPER)) {
                for (NursingNote note : nursingNoteList) {
                    if (note.getId().equals(info.getNursingDetail().getDetailId())) {
                        if (!TextUtils.isEmpty(note.getContent())) {
                            noteText.setText(note.getContent().replace("rn", "\n"));
                        }
                    }
                }
            }

        } else {
            this.dismiss();
        }
    }

    private void setPopupWindowInfo(Patient p) {
        if (!TextUtils.isEmpty(p.getBedNo())) {
            bedText.setText(p.getBedNo());
        } else {
            bedText.setText("空");
        }
        hosIdText.setText(p.getHosId());
        if (!TextUtils.isEmpty(p.getDoc())) {
            docText.setText("责医 " + getNameByDocId(p.getDoc()));
        }
        nameText.setText(p.getName());
        sexText.setText(p.getSex());
        ageText.setText(String.valueOf(DateUtil.getAge(p.getBirthday())));
        if (!TextUtils.isEmpty(p.getInDate())) {
            dateText.setText(p.getInDate() + "(入院)");
        }
        diagnosisText.setText(p.getDiagnosis());
        levelText.setText(p.getLevel());
    }

    /**
     * 获取出院病人信息
     */
    private void loadOutPatient(String inHosId, int inTimes) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE +
                ServiceConstant.FIND_PATIENT_INFO_BY_INHOS_ID_LEAVE_HOSPITAL +
                "?InhosID=" + inHosId + "&InhosTimes=" + inTimes;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                L.i("查询结果" + json);
                Patient p = JsonUtil.getOutPatientFromJson(json);
                if (p != null) {
                    setPopupWindowInfo(p);
                }
            }

        };
        getUtil.doGet(mContext, url, "查询出院病人");
    }

    /**
     * 读取popupwindow备注信息
     *
     * @param officeId
     * @param itemId
     */
    private void loadNursingDetail(String officeId, String itemId) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE +
                ServiceConstant.FIND_NURSING_DETAIL_BY_ITEM_ID
                + "?officeid=" + officeId + "&ItemID=" + itemId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                nursingNoteList = JsonUtil.getNursingNoteFromJson(json);
                L.i("共读取到备注信息数量：" + nursingNoteList.size());
                showWindow();
            }
        };
        getUtil.doGet(mContext, url, "备注信息");
    }


    /**
     * 通过医生id查到医生姓名
     *
     * @param docId
     * @return
     */
    private String getNameByDocId(String docId) {
        for (Doctor doctor : LoginInfo.mDocList) {
            if (docId.equals(doctor.getId())) {
                return doctor.getName();
            }
        }
        return "";
    }

}

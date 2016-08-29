package com.jerry.nsis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jerry.nsis.R;
import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.common.ServiceConstant;
import com.jerry.nsis.entity.Note;
import com.jerry.nsis.utils.ActivityUtil;
import com.jerry.nsis.utils.DateUtil;
import com.jerry.nsis.utils.HttpGetUtil;
import com.jerry.nsis.utils.JsonUtil;
import com.jerry.nsis.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

public class NoteActivity extends Activity {

    private ImageView mClose;
    private EditText mContent;
    private Button mSave;

    public static final int NOTE_UPDATE = 102;

    private Note mNote;
    // 是否是编辑便签
    private boolean mEdit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NsisApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_note);
        ActivityUtil.makeActivity2Dialog(this);

        mNote = (Note) getIntent().getSerializableExtra("note");
        if (mNote != null) {
            mEdit = true;
        }

        initView();
        setListener();
    }

    private void setListener() {
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mContent.getText().toString())) {
                    if (mEdit) {
                        editNote(LoginInfo.OFFICE_ID, mContent.getText().toString());
                    } else {
                        insertNote(LoginInfo.OFFICE_ID, mContent.getText().toString());
                    }
                }
            }
        });
    }

    private void initView() {
        mContent = (EditText) findViewById(R.id.et_content);
        mSave = (Button) findViewById(R.id.btn_save);
        mClose = (ImageView) findViewById(R.id.iv_close);
        if (mEdit) {
            mContent.setText(mNote.getContent());
        }
    }


    /**
     * 新增便签
     */
    private void insertNote(String officeId, String content) {
        try {
            String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.INSERT_MEMORANDUM
                    + "?officeID=" + officeId + "&MemoContent=" + URLEncoder.encode(content, "UTF-8") + "&FontSize=16";
            HttpGetUtil getUtil = new HttpGetUtil() {
                @Override
                public void success(String json) {
                    json = JsonUtil.filterJson(json);
                    L.i("新增一条便签返回值：" + json);

                    sendUpdateBroadcast();
                }
            };
            getUtil.doGet(this, url, false, "新增便签");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 编辑便签
     */
    private void editNote(String officeId, String content) {
        try {
            String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.UPDATE_MEMORANDUM_INFO
                    + "?officeID=" + officeId + "&MemoId=" + mNote.getId() + "&MemoContent=" + URLEncoder.encode(content, "UTF-8") + "&FontSize=16";
            HttpGetUtil getUtil = new HttpGetUtil() {
                @Override
                public void success(String json) {
                    json = JsonUtil.filterJson(json);
                    L.i("编辑一条便签返回值：" + json);
                    sendUpdateBroadcast();
                }
            };
            getUtil.doGet(this, url, false, "编辑便签");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发出广播，结束窗口
     */
    private void sendUpdateBroadcast() {
        Intent intent = new Intent();
        intent.setAction(MainActivity.NOTE_UPDATE);
        sendBroadcast(intent);
        finish();
    }

}

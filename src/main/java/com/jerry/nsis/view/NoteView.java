package com.jerry.nsis.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.activity.MainActivity;
import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.common.ServiceConstant;
import com.jerry.nsis.entity.Note;
import com.jerry.nsis.utils.HttpGetUtil;
import com.jerry.nsis.utils.JsonUtil;
import com.jerry.nsis.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jerry on 2016/2/17.
 */
public class NoteView extends RelativeLayout {

    private TextView textContent;
    private TextView textTime;

    private RelativeLayout mClose;

    private Context mContext;

    private Note note;


    public NoteView(Context context) {
        this(context, null);
    }

    public NoteView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_note, this);
        textContent = (TextView) findViewById(R.id.tv_content);
        textTime = (TextView) findViewById(R.id.tv_time);
        mClose = (RelativeLayout) findViewById(R.id.rl_close);

        mClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote(note.getId());
            }
        });

    }


    public void setDeleteEnable(boolean enable) {
//        if (enable) {
//            mClose.setVisibility(VISIBLE);
//        } else {
//            mClose.setVisibility(INVISIBLE);
//        }
    }


    private void deleteNote(String noteId) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.DELETE_MEMORANDUM
                + "?MemoID=" + noteId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                L.i("删除便签返回值：" + json);
                Intent intent = new Intent();
                intent.setAction(MainActivity.NOTE_UPDATE);
                mContext.sendBroadcast(intent);
            }
        };
        getUtil.doGet(mContext, url, false, "删除便签");
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
        textContent.setText(note.getContent());
        textTime.setText(note.getTime());
    }

    public void makeTimeRed() {
        textTime.setTextColor(Color.parseColor("#fd7871"));
    }

    public void makeTimeNormal() {
        textTime.setTextColor(Color.parseColor("#666666"));
    }
}

package com.jerry.nsis.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.jerry.nsis.R;
import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.entity.Education;
import com.jerry.nsis.utils.DensityUtils;
import com.jerry.nsis.utils.L;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.activity_video)
public class VideoActivity extends Activity {


    @ViewInject(R.id.rl_video)
    RelativeLayout mLayout;
    @ViewInject(R.id.video_view)
    private VideoView mVideoView;

    @ViewInject(R.id.tv_title)
    private TextView mTextTitle;
    @ViewInject(R.id.tv_time)
    private TextView mTextTime;

    @ViewInject(R.id.iv_full)
    private ImageView mButtonFull;

    @ViewInject(R.id.iv_close)
    private ImageView mClose;

    private int mCount;

    private enum STATUS {
        FULL, WINDOW
    }

    private STATUS mStatus = STATUS.WINDOW;

    private long firstClick;
    private long secondClick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NsisApplication.getInstance().addActivity(this);

        Education video = (Education) getIntent().getSerializableExtra("video");
        if (video != null) {
            mTextTitle.setText(video.getTitle());
            mTextTime.setText(video.getTime());
            String urlString = LoginInfo.mFileUrl.getUrl() + "/" + video.getUrl();
            L.i("视频地址：" + urlString);
            Uri uri = Uri.parse(urlString);
            mVideoView.setMediaController(new MediaController(this));
            mVideoView.setVideoURI(uri);
            mVideoView.start();
            mVideoView.requestFocus();
        }
    }


    private class onDoubleClick implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (MotionEvent.ACTION_DOWN == event.getAction()) {
                mCount++;
                if (mCount == 1) {
                    firstClick = System.currentTimeMillis();

                } else if (mCount == 2) {
                    secondClick = System.currentTimeMillis();
                    if (secondClick - firstClick < 1000) {
                        toggleVideoView();
                    }
                    mCount = 0;
                    firstClick = 0;
                    secondClick = 0;
                }
            }
            return true;
        }

    }

    /**
     * 切换播放窗口的全屏和窗口显示
     */
    private void toggleVideoView() {


    }
    @OnClick(R.id.iv_full)
    public void onWindow(View v) {

        RelativeLayout.LayoutParams lpFull = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        lpFull.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpFull.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lpFull.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lpFull.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        RelativeLayout.LayoutParams lpWindow = new RelativeLayout.LayoutParams(DensityUtils.dp2px(this, 1000), DensityUtils.dp2px(this, 600));
        lpWindow.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);


        if (mStatus == STATUS.FULL) {
            mLayout.setLayoutParams(lpWindow);
            mStatus = STATUS.WINDOW;
            mButtonFull.setBackgroundResource(R.drawable.video_qp);
            mClose.setVisibility(View.VISIBLE);
        } else {
            mLayout.setLayoutParams(lpFull);
            mStatus = STATUS.FULL;
            mButtonFull.setBackgroundResource(R.drawable.video_sx);
            mClose.setVisibility(View.INVISIBLE);
        }

    }

    @OnClick((R.id.iv_close))
    public void onClick(View v) {
        finish();
    }


}

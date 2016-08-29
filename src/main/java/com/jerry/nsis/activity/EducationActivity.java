package com.jerry.nsis.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jerry.nsis.R;
import com.jerry.nsis.adapter.EducationAdapter;
import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.common.ServiceConstant;
import com.jerry.nsis.entity.Education;
import com.jerry.nsis.utils.AnimationUtil;
import com.jerry.nsis.utils.HttpGetUtil;
import com.jerry.nsis.utils.JsonUtil;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.utils.T;
import com.jerry.nsis.view.EducationTag;
import com.jerry.nsis.view.MyProgressDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 护士教育界面
 */
@ContentView(R.layout.activity_education)
public class EducationActivity extends Activity {

    @ViewInject(R.id.iv_video)
    private ImageView mVideo;
    @ViewInject(R.id.iv_file)
    private ImageView mFile;

    /**
     * 左侧显示分类的布局
     */
    @ViewInject(R.id.ll_detail)
    private LinearLayout mLayout;

    /**
     * 初始化标签设置在视频上
     */
    private TAG mTag = TAG.VIDEO;

    /**
     * 表明TAG标签的两个状态：视频和文件
     */
    public enum TAG {
        VIDEO, FILE
    }

    @ViewInject(R.id.gv_file)
    private GridView mGridView;

    private EducationAdapter mAdapter;
    /**
     * 视频集合
     */
    private List<Education> mVideoList = new ArrayList<>();
    /**
     * 文件集合
     */
    private List<Education> mFileList = new ArrayList<>();


    public static final String DIR_PATH = Environment.getExternalStorageDirectory().getPath();


    private static final String DIR_NSIS = "NSIS_File";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NsisApplication.getInstance().addActivity(this);

        loadVideo(LoginInfo.OFFICE_ID);
        loadFile(LoginInfo.OFFICE_ID, false);
    }


    /**
     * 读取文档
     *
     * @param officeId 科室id
     * @param isShow   是否是显示状态
     */
    private void loadFile(String officeId, final boolean isShow) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.FIND_NURSING_EDUCATION_BY_FILE
                + "?officeid=" + officeId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mFileList = JsonUtil.getEducationFromJson(json);
                L.i("共读取到文档数量：" + mFileList.size());
                if (isShow) {
                    mAdapter.setDatas(mFileList);
                }

            }

        };
        getUtil.doGet(this, url, "读取文档");
    }


    /**
     * 读取教育视频
     *
     * @param officeId 科室id
     */
    private void loadVideo(String officeId) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.FIND_NURSING_EDUCATION_BY_VIDEO + "?officeid=" + officeId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mVideoList = JsonUtil.getEducationFromJson(json);
                L.i("共读取到视频数量：" + mVideoList.size());
                if (!JsonUtil.isEmpty(mVideoList)) {
                    setVideoData();
                }
            }

        };
        getUtil.doGet(this, url, "读取视频");
    }

    /**
     * 设置视频的数据
     */
    private void setVideoData() {
        mAdapter = new EducationAdapter(this, mVideoList, R.layout.item_education);
        mGridView.setAdapter(mAdapter);
        setLeftLayout(mVideoList);
    }

    /**
     * 添加左侧的分类布局
     *
     * @param mEduList
     */
    private void setLeftLayout(List<Education> mEduList) {
        // 清除掉所有之前的子View
        mLayout.removeAllViews();
        Map<String, Integer> map = new HashMap<>();
        // 通过遍历教育内容，将不存在的添加到map中，存在的加一
        for (Education e : mEduList) {
            String tag = e.getType();
            if (!map.containsKey(tag)) {
                map.put(tag, 1);
            } else {
                int num = map.get(tag);
                map.put(tag, num + 1);
            }
        }
        // 将map中的数据取出来设置到左侧
        Set<Map.Entry<String, Integer>> set = map.entrySet();
        for (Map.Entry<String, Integer> entry : set) {
            String key = entry.getKey();
            int value = entry.getValue();
            EducationTag eTag = new EducationTag(this);
            eTag.setTag(key);
            eTag.setNumber(value);
            // 设置好数据和对应的适配器
            if (mTag == TAG.VIDEO) {
                eTag.setEduList(mVideoList);
            } else {
                eTag.setEduList(mFileList);
            }
            eTag.setAdapter(mAdapter);
            mLayout.addView(eTag);
        }
    }


    @OnItemClick(R.id.gv_file)
    public void onVideo(AdapterView<?> parent, View view, int position, long id) {
        if (mTag == TAG.VIDEO) {
            L.i("点击的是：" + mAdapter.getDatas().get(position));
            Education video = mAdapter.getDatas().get(position);
            Intent intent = new Intent(this, VideoActivity.class);
            intent.putExtra("video", video);
            startActivity(intent);
        } else {
            L.i("点击的是：" + mAdapter.getDatas().get(position));
            Education edu = mAdapter.getDatas().get(position);
            // 首先判断文件是否存在
            if (checkFileExists(edu)) {
                L.i("文件已存在！");
                startViewFile(edu);
            } else {
                L.i("文件不存在！");
                // 如果url地址不为空就开始下载，下载完毕后打开预览
                if (!TextUtils.isEmpty(edu.getUrl())) {
                    downloadFile(edu);
                }
            }

        }
    }

    /**
     * 预览教育文件内容，使用第三方app打开
     *
     * @param edu 教育文件
     */
    private void startViewFile(Education edu) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File file = new File(DIR_PATH + "/" + DIR_NSIS, edu.getName());
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/msword");
        startActivity(intent);
    }

    /**
     * 下载文件
     *
     * @param edu
     */
    private void downloadFile(final Education edu) {
        final MyProgressDialog dialog = new MyProgressDialog(this);
        File d = new File(DIR_PATH + "/" + DIR_NSIS);
        if (!d.exists()) {
            d.mkdir();
        }
        String path = LoginInfo.mFileUrl.getUrl() + "/" + edu.getUrl();
        String target = DIR_PATH + "/" + DIR_NSIS + "/" + edu.getName();
        HttpUtils http = new com.lidroid.xutils.HttpUtils(10000);
        http.configCurrentHttpCacheExpiry(1000);
        http.download(path, target, new RequestCallBack<File>() {

            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                L.i("current:" + current + "/" + total);
                dialog.setContent("正在加载：" + (int) (current / (float) total * 100) + "%");
            }

            @Override
            public void onSuccess(ResponseInfo<File> arg0) {
                L.i("下载成功");
                dialog.dismiss();
                startViewFile(edu);
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                L.i("下载失败");
                T.showShort(EducationActivity.this, "加载失败");
                dialog.dismiss();
            }
        });
    }

    /**
     * 检测本地是否有该文档
     *
     * @param edu
     * @return
     */
    private boolean checkFileExists(Education edu) {
        String fileName = edu.getName();
        File file = new File(DIR_PATH + "/" + DIR_NSIS, fileName);
        return file.exists();
    }

    @OnClick({R.id.iv_video, R.id.iv_file})
    public void onTag(View v) {
        switch (v.getId()) {
            case R.id.iv_video:
                if (mTag != TAG.VIDEO) {
                    showVideoTag();
                }
                break;
            case R.id.iv_file:
                if (mTag != TAG.FILE) {
                    showFileTag();
                }
                break;
        }
    }

    /**
     * 显示文件标签内容
     */
    private void showFileTag() {
        if (mTag == TAG.FILE) {
            return;
        } else {
            mTag = TAG.FILE;
            AnimationUtil.eduZoomAnim(mFile, mVideo);
            if (mAdapter != null) {
                mAdapter.setDatas(mFileList);
                setLeftLayout(mFileList);
            } else {
                mAdapter = new EducationAdapter(this, mFileList, R.layout.item_education);
                mGridView.setAdapter(mAdapter);
                setLeftLayout(mFileList);
            }
        }
    }

    /**
     * 显示视频标签内容
     */
    private void showVideoTag() {
        if (mTag == TAG.VIDEO) {
            return;
        } else {
            mTag = TAG.VIDEO;
            AnimationUtil.eduZoomAnim(mVideo, mFile);
            mAdapter.setDatas(mVideoList);
            setLeftLayout(mVideoList);
        }
    }

    @OnClick(R.id.iv_close)
    public void onClose(View v) {
        finish();
    }


    @OnClick(R.id.ll_big_tag)
    public void onBigTag(View v) {
        if (mTag == TAG.VIDEO) {
            mAdapter.setDatas(mVideoList);
        } else {
            mAdapter.setDatas(mFileList);
        }
    }
}

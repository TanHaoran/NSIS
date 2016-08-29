package com.jerry.nsis.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.entity.Education;
import com.jerry.nsis.view.MyProgressDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.File;

/**
 * Created by Jerry on 2016/3/2.
 */
public abstract class HttpGetUtil {

    private MyProgressDialog dialog;


    public static final String DIR_PATH = Environment.getExternalStorageDirectory().getPath();


    private static final String DIR_NSIS = "NSIS_FILE";

    /**
     * 使用get请求读取数据
     *
     * @param url
     */
    public void doGet(final Context context, final String url, final boolean isUpdate) {
        if (!InternetUtil.isNetworkAvailable(context)) {
            T.showLong(context, "网络未连接，请检查网络！");
            L.i("网络未连接，请检查网络！");
            return;
        }
        HttpUtils http = new HttpUtils(10000);
        http.configCurrentHttpCacheExpiry(1000);
        dialog = new MyProgressDialog(context);
        L.i("请求地址：" + url);
        http.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {

            @Override
            public void onStart() {
                if (!isUpdate) {
                    dialog.show();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (!isUpdate) {
                    dialog.dismiss();
                }
                String json = responseInfo.result;
                success(json);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (!isUpdate) {
                    dialog.dismiss();
                }
                T.showLong(context, url + "方法读取失败");
            }
        });
    }


    /**
     * 使用get请求读取数据
     *
     * @param url
     */
    public void doGet(final Context context, final String url) {
        this.doGet(context, url, false);
    }

    /**
     * 使用get请求读取数据
     *
     * @param context
     * @param url
     * @param message 用来调试的的信息
     */
    public void doGet(final Context context, final String url, final boolean isUpdate, final String message) {
        if (!InternetUtil.isNetworkAvailable(context)) {
            T.showLong(context, "网络未连接，请检查网络！");
            L.i("网络未连接，请检查网络！");
            return;
        }
        HttpUtils http = new HttpUtils(10000);
        http.configCurrentHttpCacheExpiry(1000);
        dialog = new MyProgressDialog(context);
        L.i("请求地址(" + message + ")：" + url);
        http.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {

            @Override
            public void onStart() {
                if (!isUpdate) {
                    dialog.show();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (!isUpdate) {
                    dialog.dismiss();
                }
                String json = responseInfo.result;
                success(json);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (!isUpdate) {
                    dialog.dismiss();
                }
                T.showLong(context, message + "方法读取失败");
            }
        });
    }

    /**
     * 使用get请求读取数据
     *
     * @param context
     * @param url
     * @param message 用来调试的的信息
     */
    public void doGet(final Context context, final String url, final String message) {
        doGet(context, url, false, message);
    }

    /**
     * 成功的方法
     *
     * @param json
     */
    public abstract void success(String json);

    public static void doDonwload(final Context context, String requestUrl, final String fileName) {
        if (!InternetUtil.isNetworkAvailable(context)) {
            T.showLong(context, "网络未连接，请检查网络！");
            L.i("网络未连接，请检查网络！");
            return;
        }
        final MyProgressDialog dialog = new MyProgressDialog(context);
        File d = new File(DIR_PATH + "/" + DIR_NSIS);
        if (!d.exists()) {
            d.mkdir();
        }
        String path = requestUrl;
        String target = DIR_PATH + "/" + DIR_NSIS + "/" + fileName;
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
                startViewFile(context, fileName);
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                L.i("下载失败");
                T.showLong(context, "加载失败");
                dialog.dismiss();
            }
        });
    }

    /**
     * 预览教育文件内容，使用第三方app打开
     *
     * @param fileName 文件名
     */
    private static void startViewFile(Context context, String fileName) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File file = new File(DIR_PATH + "/" + DIR_NSIS, fileName);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/msword");
        context.startActivity(intent);
    }
}

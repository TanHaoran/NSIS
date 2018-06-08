package com.jerry.nsis.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.jerry.nsis.R;
import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.common.ServiceConstant;
import com.jerry.nsis.entity.Office;
import com.jerry.nsis.fragment.HospitalFragment;
import com.jerry.nsis.fragment.OfficeFragment;
import com.jerry.nsis.fragment.RegisterFragment;
import com.jerry.nsis.fragment.ServiceFragment;
import com.jerry.nsis.fragment.WeChatFragment;
import com.jerry.nsis.fragment.WelcomeFragment;
import com.jerry.nsis.trans.ZoomOutPageTransformer;
import com.jerry.nsis.utils.HttpGetUtil;
import com.jerry.nsis.utils.JsonUtil;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.utils.T;
import com.jerry.nsis.view.NoScrollViewPager;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_register)
public class RegisterActivity extends FragmentActivity {

    public static final String SETTING_NAME = "setting";

    @ViewInject(R.id.viewpager)
    private NoScrollViewPager mViewPager;

    @ViewInject(R.id.btn_previous)
    private Button mPrevious;

    @ViewInject(R.id.btn_next)
    private Button mNext;

    private List<Fragment> mFragmentList;
    private FragmentPagerAdapter mAdapter;

    private WelcomeFragment mWelcomeFragment;
    private ServiceFragment mServiceFragment;
    private HospitalFragment mHospitalFragment;
    private OfficeFragment mOfficeFragment;
    private WeChatFragment mWeChatFragment;
    private RegisterFragment mRegisterFragment;

    private static int PAGE_WELCOME = 0;
    private static int PAGE_SERVICE = 1;
    private static int PAGE_HOSPITAL = 2;
    private static int PAGE_OFFICE = 3;
    private static int PAGE_WECHAT = 4;
    private static int PAGE_REGISTER = 5;

    private String mHospitalName;
    private List<Office> mOfficeList;
    private Office mOffice;
    private String mQrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NsisApplication.getInstance().addActivity(this);
        loadDeviceInfo();
        initView();
    }


    private void initView() {
        mFragmentList = new ArrayList<>();

        mWelcomeFragment = new WelcomeFragment();
        mServiceFragment = new ServiceFragment();
        mHospitalFragment = new HospitalFragment();
        mOfficeFragment = new OfficeFragment();
        mWeChatFragment = new WeChatFragment();
        mRegisterFragment = new RegisterFragment();


        mFragmentList.add(mWelcomeFragment);
        mFragmentList.add(mServiceFragment);
        mFragmentList.add(mHospitalFragment);
        mFragmentList.add(mOfficeFragment);
        mFragmentList.add(mWeChatFragment);
        mFragmentList.add(mRegisterFragment);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        };

        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == PAGE_WELCOME) {
                    mPrevious.setVisibility(View.INVISIBLE);
                    mNext.setText("继续");
                } else {
                    mNext.setText("下一步");
                    mPrevious.setVisibility(View.VISIBLE);
                    mNext.setVisibility(View.VISIBLE);
                }
                if (position == PAGE_HOSPITAL) {
                    loadHospitalData(true);
                } else if (position == PAGE_OFFICE) {
                    loadOfficeData(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setAdapter(mAdapter);
        mViewPager.setNoScroll(true);
    }

    @OnClick(R.id.btn_previous)
    public void onPrevious(View v) {
        if (mViewPager.getCurrentItem() > PAGE_WELCOME) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }

    @OnClick(R.id.btn_next)
    public void onNext(View v) {
        if (mViewPager.getCurrentItem() < mFragmentList.size() - 1) {
            if (mViewPager.getCurrentItem() == PAGE_SERVICE) {
                if (TextUtils.isEmpty(mServiceFragment.getServiceIp())) {
                    T.showShort(this, "请输入IP地址！");
                    return;
                }
                saveService();
                loadHospitalData(false);
            } else if (mViewPager.getCurrentItem() == PAGE_HOSPITAL) {
                loadOfficeData(false);
            } else if (mViewPager.getCurrentItem() == PAGE_OFFICE) {
                mOffice = mOfficeFragment.getSelectedOffice();
                if (mOffice != null) {
                    nextPage();
                } else {
                    T.showShort(this, "请选择一个科室！");
                    return;
                }
            } else if (mViewPager.getCurrentItem() == PAGE_WECHAT) {
                loadQrCodeData(serialNumber, androidVersion, androidId, cupId,
                        systemName, mHospitalName, mOffice.getName(), false);
            } else {
                L.i("当前页码：" + mViewPager.getCurrentItem());
                nextPage();
            }
        } else {
            String code = mRegisterFragment.getCode();
            checkCodeCorrect(code, serialNumber);
        }
    }

    private String ip;

    private void saveService() {
        SharedPreferences sp = getSharedPreferences(SETTING_NAME, MODE_PRIVATE);
        ip = "http://" + mServiceFragment.getServiceIp();
        ServiceConstant.SERVICE_IP = ip;
        sp.edit().putString("ip", ip).apply();

    }


    /**
     * 获取当前医院名称
     */
    private void loadHospitalData(final boolean isBack) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE +
                ServiceConstant.GET_HOSPITAL_NAME;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mHospitalName = JsonUtil.getHospitalName(json);
                LoginInfo.HOSPITAL_NAME = mHospitalName;
                mHospitalFragment.setHospitalName(mHospitalName);
                if (!isBack) {
                    nextPage();
                }
            }

        };
        getUtil.doGet(this, url, false, "读取医院名称");
    }

    /**
     * 获取所有科室
     */
    private void loadOfficeData(final boolean isBack) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE +
                ServiceConstant.FIND_OFFICE_ALL;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mOfficeList = JsonUtil.getOfficeFromJson(json);
                mOfficeFragment.setOfficeData(mOfficeList);
                if (!isBack) {
                    nextPage();
                }
            }

        };
        getUtil.doGet(this, url, false, "获取所有科室");
    }


    /**
     * 获取二维码信息
     */
    private void loadQrCodeData(String harddiskinfo, String monitorinfo, String adversion,
                                String cupid, String systemName, String hospitalName,
                                String officeNawme, final boolean isBack) {
        try {
            String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE +
                    ServiceConstant.GET_CHECK_CODE + "?harddiskinfo=" + "50026B7287A29C" +
                    "&monitorinfo=" + monitorinfo + "&adversion=" + adversion +
                    "&cpuid=" + cupid + "&systemName=" + systemName +
                    "&hospitalName=" + URLEncoder.encode(hospitalName, "UTF-8") + "&officeName=" + URLEncoder.encode(officeNawme, "UTF-8");
            HttpGetUtil getUtil = new HttpGetUtil() {
                @Override
                public void success(String json) {
                    mQrCode = JsonUtil.getQrCodeFromJson(json);
                    L.i("二维码信息：" + mQrCode);
                    if (createQrCode() != null) {
                        mRegisterFragment.setQrCodeBitmap(createQrCode());
                        if (!isBack) {
                            nextPage();
                        }
                    }
                }
            };
            getUtil.doGet(this, url, false, "获取二维码信息");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检测激活码是否正确
     *
     * @param code   序列号
     * @param number 机器码
     */
    private void checkCodeCorrect(final String code, String number) {
//        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE +
//                ServiceConstant.VERITY_CHECK_CODE + "?serialnumber=" +
//                code + "&harddiskinfo=" + "50026B7287A29C";
//        HttpGetUtil getUtil = new HttpGetUtil() {
//            @Override
//            public void success(String json) {
//                String result = JsonUtil.getRegisterInfoJson(json);
//                L.i("返回值：" + result);
//                if (result != null && result.equals("True")) {
//                    // 保存有关医院、科室、机器码等重要信息。
//                    saveImportantData(code);
//                    Intent intent = new Intent();
//                    intent.setClass(RegisterActivity.this, SuccessActivity.class);
//                    startActivity(intent);
//                } else {
//                    T.showShort(RegisterActivity.this, "注册失败");
//                }
//            }
//
//
//        };
//        getUtil.doGet(this, url, false, "检测激活码");

        Intent intent = new Intent();
        intent.setClass(RegisterActivity.this, SuccessActivity.class);
        startActivity(intent);
    }

    /**
     * 保存医院、科室、机器码等重要信息
     */
    private void saveImportantData(String code) {
        SharedPreferences sp = getSharedPreferences(RegisterActivity.SETTING_NAME, MODE_PRIVATE);
        sp.edit().putString("hospital", mHospitalName).apply();
        sp.edit().putString("office_name", mOffice.getName()).apply();
        sp.edit().putString("office_id", mOffice.getId()).apply();
        sp.edit().putString("serial", serialNumber).apply();
        sp.edit().putString("code", code).apply();
    }

    /**
     * 根据字符串创建一个二维码
     *
     * @return
     */
    private Bitmap createQrCode() {
        if (TextUtils.isEmpty(mQrCode)) {
            T.showLong(this, "二维码生成失败！");
            return null;
        }
        BitMatrix matrix = null;
        try {
            // 生成二维码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
            matrix = new MultiFormatWriter().encode(mQrCode, BarcodeFormat.QR_CODE, 300, 300);
            L.i("二维码的字符串：" + mQrCode);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        //矩阵的宽度
        int width = matrix.getWidth();
        //矩阵的高度
        int height = matrix.getHeight();
        //矩阵像素数组
        int[] pixels = new int[width * height];
        //双重循环遍历每一个矩阵点
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    //设置矩阵像素点的值
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }
        //根据颜色数组来创建位图
        /**
         * 此函数创建位图的过程可以简单概括为为:更加width和height创建空位图，
         * 然后用指定的颜色数组colors来从左到右从上至下一次填充颜色。
         * config是一个枚举，可以用它来指定位图“质量”。
         */
        Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bm.setPixels(pixels, 0, width, 0, 0, width, height);
        //将生成的条形码返回给调用者
        return bm;
    }

    private void nextPage() {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
    }


    private String serialNumber;
    private String deviceId;
    private String androidId;
    private String androidVersion;
    private String cupId;
    private String systemName = "NSIS";

    /**
     * 获取设备唯一序列号
     *
     * @return
     */
    private String loadDeviceInfo() {
        serialNumber = android.os.Build.SERIAL;
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        deviceId = tm.getDeviceId();
        androidId = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
        androidVersion = android.os.Build.VERSION.RELEASE;
        cupId = Build.CPU_ABI;

        L.i("SerialNumber：" + serialNumber);
        L.i("DeviceId：" + deviceId);
        L.i("AndroidID：" + androidId);
        L.i("AndroidVersion：" + androidVersion);
        L.i("CupId：" + cupId);
        L.i("Device：" + android.os.Build.DEVICE);
        L.i("DISPLAY：" + Build.DISPLAY);
        L.i("HARDWARE：" + Build.HARDWARE);
        return serialNumber;
    }

    @Override
    public void onBackPressed() {
    }
}

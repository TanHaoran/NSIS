package com.jerry.nsis.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.jerry.nsis.R;

/**
 * Created by Jerry on 2016/4/11.
 */
public class RegisterFragment extends Fragment {


    private View mView;
    private ImageView mQrCodde;
    private EditText mCode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_register, container, false);
        mQrCodde = (ImageView) mView.findViewById(R.id.iv_qrcode);
        mCode = (EditText) mView.findViewById(R.id.et_code);
        return mView;
    }

    public void setQrCodeBitmap(Bitmap bitmap) {
        mQrCodde.setImageBitmap(bitmap);
    }

    public String getCode() {
        return mCode.getText().toString().replace("-", "");
    }
}

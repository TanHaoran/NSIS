package com.jerry.nsis.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.entity.BedInfo;

/**
 * Created by Jerry on 2016/2/17.
 */
public class ContactView extends RelativeLayout {


    private TextView textName;
    private TextView textPhone;


    public ContactView(Context context) {
        this(context, null);
    }

    public ContactView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContactView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.item_contact, this);

        textName = (TextView) findViewById(R.id.tv_name);
        textPhone = (TextView) findViewById(R.id.tv_phone);

    }

    public void setName(String name) {
        textName.setText(name);
    }

    public void setPhone(String phone) {
        textPhone.setText(phone);
    }

}

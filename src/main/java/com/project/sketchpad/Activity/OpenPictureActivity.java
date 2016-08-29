package com.project.sketchpad.Activity;

import com.jerry.nsis.R;
import com.jerry.nsis.adapter.CommonAdapter;
import com.jerry.nsis.utils.ActivityUtil;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.utils.ViewHolder;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.project.sketchpad.Contral.FileOpenHelper;
import com.project.sketchpad.entity.Picture;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OpenPictureActivity extends Activity {

    private ImageView mClose;
    private GridView mGridView;

    private List<Picture> mPictureList = new ArrayList<>();

    private BitmapUtils bitmapUtils;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_picture);
        ActivityUtil.makeActivity2Dialog(this);

        mPictureList = FileOpenHelper.getPictures();

        mGridView = (GridView) findViewById(R.id.grid);
        mClose = (ImageView) findViewById(R.id.iv_close);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bitmapUtils = new BitmapUtils(this);

        mGridView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils, false, true));


        mGridView.setAdapter(new CommonAdapter<Picture>(this, mPictureList, R.layout.item_picture) {
            @Override
            public void convert(ViewHolder helper,final Picture item) {
                helper.setText(R.id.tv_name, item.getName());
                bitmapUtils.display(helper.getView(R.id.iv_picture), item.getPath());
                helper.getView(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File file = new File(item.getPath());
                        file.delete();
                        mPictureList.remove(item);
                        notifyDataSetChanged();;
                        L.i("删除图片：" + item.getPath());
                    }
                });
            }
        });


        mGridView.setOnItemClickListener(new OnItemClickListener() {


            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = mPictureList.get(position).getPath();
                if (path  != null) {
                    Intent data = new Intent();
                    data.putExtra("path", path);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });

    }

}

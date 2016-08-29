package com.project.sketchpad.Activity;

import com.jerry.nsis.R;
import com.jerry.nsis.adapter.CommonAdapter;
import com.jerry.nsis.utils.ActivityUtil;
import com.jerry.nsis.utils.DateUtil;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.utils.T;
import com.jerry.nsis.utils.ViewHolder;
import com.project.sketchpad.Contral.FileOpenHelper;
import com.project.sketchpad.entity.Picture;
import com.project.sketchpad.view.BoardView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class SaveBoardActivity extends Activity {

    private BoardView canvasView = null;
    private EditText mTextFileName;
    private Button saveButton;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_dialog);
        canvasView = (BoardView) findViewById(R.id.boardview);
        mTextFileName = (EditText) findViewById(R.id.fileName);
        saveButton = (Button) findViewById(R.id.save);

        mTextFileName.setText(DateUtil.getYMDHMS().replace(" ", "_").replace(":", "_").replace("-", "_"));



        saveButton.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {

                String filename = mTextFileName.getText().toString().trim();
                /*文件名不能为*/
                if (TextUtils.isEmpty(filename)) {
                    T.showLong(SaveBoardActivity.this, "文件名不能为空");
                    return;
                } else {
                    filename = filename + ".jpg";
                }
                /*返回文件路径，关闭当前对话框*/
                String filePath = FileOpenHelper.getPicturePath() + filename;
                Intent data = new Intent();
                data.putExtra("file_path", filePath);
                L.i("放进去的文件路径是：" + filePath);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

}

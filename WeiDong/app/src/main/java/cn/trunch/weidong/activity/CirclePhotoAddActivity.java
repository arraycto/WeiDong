package cn.trunch.weidong.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;
import com.suke.widget.SwitchButton;
import com.zhihu.matisse.Matisse;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.adapter.ImageAddAdapter;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.http.UploadApi;
import cn.trunch.weidong.util.PathUtil;

public class CirclePhotoAddActivity extends AppCompatActivity {

    private ImageView photoAddExitBtn;
    private TextView photoAddSubmitBtn;
    private EditText photoAddContent;
    private SwitchButton photoAddAnonymous;
    private RecyclerView photoAddList;
    private static final int REQUEST_IMAGE_CODE = 1;
    private List<Uri> uriSelected;
    private StringBuffer sb = new StringBuffer();
    private ImageAddAdapter imageAddAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_photo_add);

        init();
        initListener();
        DialogUIUtils.init(this);
    }

    private void init() {
        photoAddExitBtn = findViewById(R.id.photoAddExitBtn);
        photoAddSubmitBtn = findViewById(R.id.photoAddSubmitBtn);
        photoAddContent = findViewById(R.id.photoAddContent);
        photoAddAnonymous = findViewById(R.id.photoAddAnonymous);
        photoAddList = findViewById(R.id.photoAddList);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager (new GridLayoutManager (getActivity(),3,GridLayoutManager.VERTICAL,false));
        photoAddList.setLayoutManager(gridLayoutManager);
        imageAddAdapter = new ImageAddAdapter(this);
        photoAddList.setAdapter(imageAddAdapter);
    }

    private void initListener() {
        photoAddExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        photoAddSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageAddAdapter.getData().size() == 0
                        && photoAddContent.getText().toString().replaceAll(" ", "").replaceAll("\n", "").length() == 0) {
                    DialogUIUtils.showToastCenter("内容不能为空");
                } else {
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("token", ApiUtil.USER_TOKEN);
                    hm.put("type", ApiUtil.POST_TYPE.toString());
                    hm.put("content", photoAddContent.getText().toString());
                    if (!photoAddAnonymous.isChecked())
                        hm.put("ano", "1");
                    hm.put("annex", sb.toString());
                    new UniteApi(ApiUtil.DIARY_ADD, hm).post(new ApiListener() {
                        @Override
                        public void success(Api api) {
                            finish();
                        }

                        @Override
                        public void failure(Api api) {
                            DialogUIUtils.showToastCenter("网络波动，请重试");
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK) {
            assert data != null;
            uriSelected = Matisse.obtainResult(data);
            imageAddAdapter.updateData(uriSelected);
            for (int i = 0; i < uriSelected.size(); i++) {
                String path = PathUtil.getFilePath(this, uriSelected.get(i));
                new UploadApi(ApiUtil.FILE_UPLOAD_DIARY, path).upload(new ApiListener() {
                    @Override
                    public void success(Api api) {
                        UploadApi u = (UploadApi) api;
                        Gson gson = new Gson();
                        try {
                            String name = gson.fromJson(u.getJsonData().getString(0), String.class);
                            sb.append(name).append(",");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(Api api) {
                        DialogUIUtils.showToastCenter("网络波动了一下");
                    }
                });
            }
            uriSelected.clear();
        }
    }
}

package cn.trunch.weidong.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.suke.widget.SwitchButton;
import com.zhihu.matisse.Matisse;

import java.util.ArrayList;
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
import cn.trunch.weidong.util.StatusBarUtil;

public class SquareConsultAddActivity extends AppCompatActivity {

    private final static int CONSULT_COMMENT_ADD_CODE = 22;
    private int type;
    private String did;
    private int comType;
    private String uid;
    private ImageView consultAddExitBtn;
    private TextView consultAddSubmitBtn;

    private final static int contentMinNum = 15;
    private final static int contentMaxNum = 300;
    private EditText consultAddContent;
    private TextView consultAddContentNum;
    private SwitchButton consultAddAnonymous;
    private RecyclerView consultAddImgList;

    private static final int REQUEST_IMAGE_CODE = 1;
    private List<Uri> uriSelected;
    private List<String> imagePaths = new ArrayList<>();
    private ImageAddAdapter imageAddAdapter;
    private String imgs;

    //数据字段部分
    private String consultUserToken = ApiUtil.USER_TOKEN;
    private String consultUserId = ApiUtil.USER_ID;
    private String consultExpertId = "";
    private String consultContent = "";
    private String consultAnonymous = "";

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_consult_add);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);
        Bundle extras = getIntent().getExtras();
        type = extras.getInt("type", 0);
        comType = extras.getInt("comType", 1);
        did = extras.getString("did", "default");
        uid = extras.getString("uid", "default");
        init();
        initListener();
        DialogUIUtils.init(this);
    }

    private void init() {
        consultAddExitBtn = findViewById(R.id.consultAddExitBtn);
        consultAddSubmitBtn = findViewById(R.id.consultAddSubmitBtn);
        consultAddContent = findViewById(R.id.consultAddContent);
        consultAddContentNum = findViewById(R.id.consultAddContentNum);
        consultAddAnonymous = findViewById(R.id.consultAddAnonymous);
        consultAddImgList = findViewById(R.id.consultAddImgList);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        consultAddImgList.setLayoutManager(gridLayoutManager);
        imageAddAdapter = new ImageAddAdapter(this);
        imageAddAdapter.setImageNum(3);
        consultAddImgList.setAdapter(imageAddAdapter);
        imgs = ",";
    }

    private void initListener() {
        consultAddExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        consultAddSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == CONSULT_COMMENT_ADD_CODE) {
                    // TODO 添加咨询评论
                    addCom();
                } else {
                    // TODO 添加咨询
                    addConsult();
                }
            }
        });

        consultAddContent.addTextChangedListener(new TextWatcher() {
            private CharSequence tempContent;
            private int editStart;
            private int editEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tempContent = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = consultAddContent.getSelectionStart();
                editEnd = consultAddContent.getSelectionEnd();
                consultAddContentNum.setText(String.valueOf(tempContent.length()));
                if (tempContent.length() > contentMaxNum) {
                    s.delete(editStart - 1, editEnd);
                    consultAddContent.setText(s);
                    consultAddContent.setSelection(contentMaxNum);
                    consultAddSubmitBtn.setTextColor(getResources().getColor(R.color.colorDefaultText));
                    consultAddSubmitBtn.setEnabled(false);
                } else if (tempContent.length() >= contentMinNum) {
                    consultAddSubmitBtn.setTextColor(getResources().getColor(R.color.colorTheme));
                    consultAddSubmitBtn.setEnabled(true);
                } else {
                    consultAddSubmitBtn.setTextColor(getResources().getColor(R.color.colorDefaultText));
                    consultAddSubmitBtn.setEnabled(false);
                }
            }
        });

        consultAddAnonymous.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                consultAnonymous = isChecked ? "1" : "0";
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK) {
            assert data != null;
            uriSelected = Matisse.obtainResult(data);
            for (int i = 0; i < uriSelected.size(); i++) {
                imagePaths.add(PathUtil.getFilePath(this, uriSelected.get(i)));
            }
            dialog = DialogUIUtils.showLoading(this, "正在上传...", false, false, false, true).show();
            new UploadApi(ApiUtil.MULTFILE_UPLOAD, imagePaths, ApiUtil.MULT_POST).uploads(new ApiListener() {
                @Override
                public void success(Api api) {
                    UploadApi u = (UploadApi) api;
                    Gson gson = new Gson();
                    List<String> ls;
                    ls = gson.fromJson(u.getJsonData().toString(), new TypeToken<List<String>>() {
                    }.getType());
                    imgs = ls.get(0);
                    DialogUIUtils.dismiss(dialog);
                    DialogUIUtils.showToastCenter("图片预传成功");
                }

                @Override
                public void failure(Api api) {
                    DialogUIUtils.dismiss(dialog);
                    DialogUIUtils.showToastCenter("请检查网络配置");
                }
            });
            imageAddAdapter.updateData(uriSelected);
            uriSelected.clear();
        }
    }

    private void addCom() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("did", did);
        hm.put("content", consultAddContent.getText().toString());
        hm.put("type", String.valueOf(comType));
        hm.put("imgs", imgs);
        System.out.println(hm.toString());
        new UniteApi(ApiUtil.COM_ADD, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                DialogUIUtils.showToastCenter("添加成功");
                finish();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络波动，请重试");
            }
        });
        setResult(CONSULT_COMMENT_ADD_CODE);
        finish();
    }

    private void addConsult() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("buid", uid);
        hm.put("content", consultAddContent.getText().toString());
        hm.put("type", String.valueOf(ApiUtil.CONSULT_TYPE));
        hm.put("annex", imgs);
        System.out.println(hm.toString());
        new UniteApi(ApiUtil.DIARY_ADD, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                DialogUIUtils.showToastCenter("咨询已提交，等待回答");
                finish();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络波动，请重试");
            }
        });
        setResult(CONSULT_COMMENT_ADD_CODE);
        finish();
    }
}

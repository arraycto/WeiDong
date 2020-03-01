package cn.trunch.weidong.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.dialog.AnonymousAddDialog;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.http.UploadApi;
import cn.trunch.weidong.util.Glide4Engine;
import cn.trunch.weidong.util.HtmlUtil;
import cn.trunch.weidong.util.PathUtil;
import cn.trunch.weidong.util.StatusBarUtil;
import jp.wasabeef.richeditor.RichEditor;

public class SportDiaryAddActivity extends AppCompatActivity {

    private final static int REQUEST_PHOTO_CODE = 20202;
    private final static String TAG = "Testing";

    private ImageView programAddCloseBtn;
    private TextView programAddReleaseBtn;
    private TextView programAddContentNum;

    private EditText programAddTitle; //标题
    private RichEditor editor;
    private LinearLayout editorToolBar;
    private ImageView editorImageBtn;
    private ImageView editorBoldBtn;
    private ImageView editorItalicBtn;
    private ImageView editorTitleBtn;
    private ImageView editorOlBtn;
    private ImageView editorUlBtn;
    private ImageView editorLinkBtn;
    private ImageView editorUndoBtn;
    private ImageView editorRedoBtn;

    private AnonymousAddDialog anonymousAddDialog;
    private boolean isAnonymous = true;
    private List<Uri> imageSelected = new ArrayList<>(); //存放选中照片的Uri
    private String programContent = "内容异常"; //内容
    private String programPreview = "";

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_diary_add);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);

        init();
        initListener();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_PHOTO_CODE && resultCode == RESULT_OK) {
            imageSelected.clear();
            imageSelected = Matisse.obtainResult(data);
            insertImage(PathUtil.getFilePath(this, imageSelected.get(0)));
            imageSelected.clear();
        }
    }

    public void init() {
        programAddCloseBtn = findViewById(R.id.issueProgramAddCloseBtn);
        programAddReleaseBtn = findViewById(R.id.issueProgramAddReleaseBtn);
        programAddContentNum = findViewById(R.id.issueProgramAddContentNum);
        programAddTitle = findViewById(R.id.issueProgramAddTitle);
        //富文本编辑器及其工具栏
        editor = findViewById(R.id.issueProgramAddEditor);
        editor.setPlaceholder("在这里编辑日记哦");
        editor.setBackgroundColor(getResources().getColor(R.color.colorBack));
        editor.setEditorFontSize(18);
        editor.setPadding(15, 15, 15, 15);
        editor.loadCSS("file:////android_asset/editor.css");
        editorToolBar = findViewById(R.id.editorToolBar);
        editorToolBar.setVisibility(View.INVISIBLE); // 一开始不可见
        editorImageBtn = findViewById(R.id.editorImageBtn);
        editorBoldBtn = findViewById(R.id.editorBoldBtn);
        editorItalicBtn = findViewById(R.id.editorItalicBtn);
        editorUlBtn = findViewById(R.id.editorUlBtn);
        editorTitleBtn = findViewById(R.id.editorTitleBtn);
        editorLinkBtn = findViewById(R.id.editorLinkBtn);
        editorOlBtn = findViewById(R.id.editorOlBtn);
        editorUndoBtn = findViewById(R.id.editorUndoBtn);
        editorRedoBtn = findViewById(R.id.editorRedoBtn);

        anonymousAddDialog = new AnonymousAddDialog(this, "是否公开");
        anonymousAddDialog.setOnanonymousChangeListener(new AnonymousAddDialog.OnAnonymousChangeListener() {
            @Override
            public void onChange(boolean anonymous) {
                isAnonymous = anonymous;
            }
        });
    }

    public void initListener() {
        programAddCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.anim_static, R.anim.bottom_out);
                finish();
            }
        });
        programAddReleaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (programAddTitle.getText().toString().replace(" ", "").length() == 0) {
                    DialogUIUtils.showToastCenter("标题不能为空哦");
                    return;
                }
                if (editor.getHtml() == null) {
                    DialogUIUtils.showToastCenter("内容不能为空哦");
                    return;
                }

                dialog = DialogUIUtils.showLoading(SportDiaryAddActivity.this, "正在发布...",
                        false, false, false, true).show();

                try {
                    programPreview = URLEncoder.encode(programPreview, "utf-8");
                    programContent = URLEncoder.encode("<html><head><style>"
                            + "body {margin: 0;padding: 0;color: #2F2F2F;letter-spacing: 1px;ont-size: 16px;line-height:22px;}"
                            + "img {display: block;max-width: 100%;margin: auto;}"
                            + "</style></head><body>"
                            + editor.getHtml()
                            + "</body></html>", "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("token", ApiUtil.USER_TOKEN);
                hashMap.put("type", String.valueOf(ApiUtil.DIARY_TYPE));
                hashMap.put("title", programAddTitle.getText().toString());
                hashMap.put("content", programContent);
                hashMap.put("pre_content", HtmlUtil.getText(editor.getHtml()));// TODO
                hashMap.put("ano", isAnonymous ? "0" : "1");
                hashMap.put("img", programPreview);
                Log.d(TAG, "onClick 内容: " + programContent);
                Log.d(TAG, "onClick 览图: " + programPreview);
                new UniteApi(ApiUtil.DIARY_ADD, hashMap).post(new ApiListener() {
                    @Override
                    public void success(Api api) {
                        programAddReleaseBtn.post(new Runnable() {
                            @Override
                            public void run() {
                                DialogUIUtils.dismiss(dialog);
                                DialogUIUtils.showToastCenter("发布成功");
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void failure(Api api) {
                        programAddReleaseBtn.post(new Runnable() {
                            @Override
                            public void run() {
                                DialogUIUtils.dismiss(dialog);
                                DialogUIUtils.showToastCenter("发布失败");
                            }
                        });
                    }
                });
            }
        });
        editorImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndPermission.with(SportDiaryAddActivity.this)
                        .runtime()
                        .permission(Permission.Group.STORAGE)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                Matisse.from(SportDiaryAddActivity.this)
                                        .choose(MimeType.ofImage()) // 选择 mime 的类型
                                        .countable(true)
                                        .maxSelectable(1) // 图片选择的最多数量
                                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                        .thumbnailScale(0.85f) // 缩略图的比例
                                        .imageEngine(new Glide4Engine()) // 使用的图片加载引擎
                                        .forResult(REQUEST_PHOTO_CODE); // 设置作为标记的请求码
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                DialogUIUtils.showToastCenter("请前往设置打开权限");
                            }
                        })
                        .start();
            }
        });
        editorBoldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.setBold();
            }
        });
        editorItalicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.setItalic();
            }
        });
        editorTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.setHeading(3);
            }
        });
        editorOlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.setNumbers();
            }
        });
        editorUlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.setBullets();
            }
        });
        editorLinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anonymousAddDialog.show();
            }
        });
        editorUndoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.focusEditor();
                editor.undo();
            }
        });
        editorRedoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.focusEditor();
                editor.redo();
            }
        });

        editor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    editorToolBar.setVisibility(View.INVISIBLE);
                } else {
                    editorToolBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void insertImage(String imagePath) {
        new UploadApi(ApiUtil.FILE_UPLOAD_DIARY, imagePath).upload(new ApiListener() {
            @Override
            public void success(Api api) {
                UploadApi uploadApi = (UploadApi) api;
                Gson gson = new Gson();
                List<String> imgList = gson.fromJson(uploadApi.getJsonData().toString(), new TypeToken<List<String>>() {
                }.getType());
                editor.insertImage("http://www.two2two.xyz/werunImg/post/" + imgList.get(0), "图片");
                if ("".equals(programPreview))
                    programPreview = imgList.get(0);
                DialogUIUtils.dismiss(dialog);
                DialogUIUtils.showToastCenter("图片插入成功");
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.dismiss(dialog);
                DialogUIUtils.showToastCenter("图片插入失败");
            }
        });
    }
}

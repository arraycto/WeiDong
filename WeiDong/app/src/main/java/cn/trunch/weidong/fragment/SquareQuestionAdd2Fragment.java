package cn.trunch.weidong.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.dou361.dialogui.DialogUIUtils;
import com.suke.widget.SwitchButton;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.trunch.weidong.R;
import cn.trunch.weidong.activity.SquareQuestionAddActivity;
import cn.trunch.weidong.dialog.ImageAddDialog;
import cn.trunch.weidong.dialog.LabelAddDialog;
import cn.trunch.weidong.dialog.PayAddDialog;

public class SquareQuestionAdd2Fragment extends Fragment {

    private final static String TAG = "Testing";
    private View view;
    private SquareQuestionAddActivity activity;

    private EditText questionAddContent;
    private ImageView questionAddBounty;
    private ImageView questionAddImage;
    private ImageView questionAddLabel;
    private SwitchButton questionAddAnonymous;

    private PayAddDialog payAddDialog;
    private LabelAddDialog labelAddDialog;
    private ImageAddDialog imageAddDialog;
    private String questionBounty;
    private String questionImages;
    private String questionLabels;
    private StringBuffer label;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_square_question_add2, container, false);
        activity = (SquareQuestionAddActivity) getActivity();

        init();
        initListener();

        return view;
    }

    private void init() {
        questionAddContent = view.findViewById(R.id.questionAddContent);
        questionAddBounty = view.findViewById(R.id.questionAddBounty);
        questionAddImage = view.findViewById(R.id.questionAddPhoto);
        questionAddLabel = view.findViewById(R.id.questionAddLabel);
        questionAddAnonymous = view.findViewById(R.id.questionAddAnonymous);

        //功能弹框
        payAddDialog = new PayAddDialog(activity, "设置赏金");
        imageAddDialog = new ImageAddDialog(this, "添加图片");
        labelAddDialog = new LabelAddDialog(activity, "问题标签");
        label=new StringBuffer();
    }

    private void initListener() {
        //设置赏金
        questionAddBounty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payAddDialog.show();
            }
        });
        //支付按钮
        payAddDialog.setOnPayListener(new PayAddDialog.OnPayListener() {
            @Override
            public void onPay(String amount) {
                questionBounty = amount;
            }
        });
        //添加图片按钮
        questionAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageAddDialog.show();
            }
        });
        //
        imageAddDialog.setOnImageSelectListener(new ImageAddDialog.OnImageSelectListener() {
            @Override
            public void onSelect(String imageUrls) {
                try {
                    questionImages = imageUrls;
                } catch (Exception e) {
                    questionImages = "";
                }
            }
        });
        questionAddLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                labelAddDialog.show();
            }
        });
        labelAddDialog.setOnLabelChangeListener(new LabelAddDialog.OnLabelChangeListener() {
            @Override
            public void onChange(String labels) {
                questionLabels = labels;
                label.append(labels.substring(labels.indexOf("#"),labels.lastIndexOf("#")));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageAddDialog.setOnResult(requestCode, resultCode, data);
    }

    public String getQuestionContent() {
        return questionAddContent.getText().toString();
    }

    public String getQuestionBounty() {
        return questionBounty;
    }

    public String getQuestionImages() {
        return questionImages;
    }

    public String getQuestionLabels() {
        label.append("#");
        return label.toString();
    }

    public String getQuestionAnonymous() {
        return questionAddAnonymous.isChecked()?"1":"0";
    }
}

package cn.trunch.weidong.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.dou361.dialogui.DialogUIUtils;

import cn.trunch.weidong.R;

public class PayAddDialog extends BottomSheetDialog {

    private View view;
    private Context context;
    private String title;

    private TextView payAddTitle;
    private EditText payAddAmount;
    private Button payAddBtn;
    private String amount;

    private OnPayListener onPayListener;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Dialog dialog;

    public PayAddDialog(@NonNull Context context, String title) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.dialog_pay_add, null, false);
        this.context = context;
        this.title = title;
        this.setContentView(view);
        this.getWindow().findViewById(R.id.design_bottom_sheet)
                .setBackgroundResource(android.R.color.transparent);

        init();
        initListener();
        DialogUIUtils.init(context);
    }

    public void setOnPayListener(OnPayListener onPayListener) {
        this.onPayListener = onPayListener;
    }

    private void init() {
        payAddTitle = view.findViewById(R.id.payAddTitle);
        payAddTitle.setText(title);
        payAddAmount = view.findViewById(R.id.payAddAmount);
        payAddBtn = view.findViewById(R.id.payAddBtn);
    }

    private void initListener() {
        payAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayAddDialog.this.dismiss();
                dialog = DialogUIUtils.showLoading(context, "正在发起支付",
                        false, false, false, true).show();
                amount = payAddAmount.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(800);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    DialogUIUtils.dismiss(dialog);
                                    DialogUIUtils.showToastCenter("已成功支付"+amount+".00元");
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                onPayListener.onPay(amount);
            }
        });
    }

    public interface OnPayListener {
        void onPay(String amount);
    }
}

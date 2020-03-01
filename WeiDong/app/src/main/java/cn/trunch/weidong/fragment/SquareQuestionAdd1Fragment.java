package cn.trunch.weidong.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.trunch.weidong.R;
import cn.trunch.weidong.activity.SquareQuestionAddActivity;

public class SquareQuestionAdd1Fragment extends Fragment {

    private View view;
    private SquareQuestionAddActivity activity;

    private EditText problemAddTitle;
    private TextView problemAddTitleNum;
    private LinearLayout problemAddAdjust;

    private final static int titleMinNum = 5;
    private final static int titleMaxNum = 50;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_square_question_add1, container, false);
        activity = (SquareQuestionAddActivity) getActivity();

        init();
        initListener();

        return view;
    }

    private void init() {
        problemAddTitle = view.findViewById(R.id.questionAddTitle);
        problemAddTitleNum = view.findViewById(R.id.questionAddContentNum);
        problemAddAdjust = view.findViewById(R.id.questionAddAdjust);
        problemAddAdjust.setVisibility(View.INVISIBLE);
    }

    private void initListener() {
        problemAddTitle.addTextChangedListener(new TextWatcher() {
            private CharSequence tempTitle;
            private int editStart;
            private int editEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tempTitle = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = problemAddTitle.getSelectionStart();
                editEnd = problemAddTitle.getSelectionEnd();
                problemAddTitleNum.setText(String.valueOf(tempTitle.length()));
                if (tempTitle.length() > titleMaxNum) {
                    s.delete(editStart-1, editEnd);
                    problemAddTitle.setText(s);
                    problemAddTitle.setSelection(50);
                } else if (tempTitle.length() >= titleMinNum) {
                    activity.setNextAble(true);
                } else {
                    activity.setNextAble(false);
                }

                if (tempTitle.length() < 2)
                    problemAddAdjust.setVisibility(View.INVISIBLE);
                else
                    problemAddAdjust.setVisibility(View.VISIBLE);
            }
        });
    }

    public String getProblemTitle() {
        return problemAddTitle.getText().toString();
    }
}

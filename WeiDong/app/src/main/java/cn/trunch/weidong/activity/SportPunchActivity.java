package cn.trunch.weidong.activity;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dou361.dialogui.DialogUIUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.suke.widget.SwitchButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.trunch.weidong.R;
import cn.trunch.weidong.adapter.SportHistoryAdapter;
import cn.trunch.weidong.adapter.SportPunchAdapter;
import cn.trunch.weidong.step.bean.StepData;
import cn.trunch.weidong.step.utils.DbUtils;
import cn.trunch.weidong.step.utils.SharedPreferencesUtils;
import cn.trunch.weidong.util.SPUtil;
import cn.trunch.weidong.util.StatusBarUtil;
import cn.trunch.weidong.util.TimeUtil;

public class SportPunchActivity extends AppCompatActivity {

    private ImageView sportHisBackBtn;
    private XRecyclerView sportPunchList;
    private SportPunchAdapter sportPunchAdapter;
    // header
    private SharedPreferencesUtils sp;
    private EditText tv_step_number;
    private SwitchButton cb_remind;
    private TextView tv_remind_time;
    private TextView btn_save;
    private String walk_qty;
    private String remind;
    private String achieveTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_punch);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);

        init();
        initListener();
        initHeadData();
        initData();

        DialogUIUtils.init(this);
    }

    private void init() {
        sportPunchList = findViewById(R.id.sportPunchList);
        sportHisBackBtn = findViewById(R.id.sportPunchBackBtn);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        sportPunchList.setLayoutManager(linearLayoutManager);
        sportPunchAdapter = new SportPunchAdapter(this);
        sportPunchList.setAdapter(sportPunchAdapter);
        sportPunchList.setLoadingMoreEnabled(false);
        //header
        View imgHeaderView = LayoutInflater.from(this).inflate(R.layout.view_sport_punch_header
                , (ViewGroup) findViewById(android.R.id.content), false);
        tv_step_number = imgHeaderView.findViewById(R.id.tv_step_number);
        cb_remind = imgHeaderView.findViewById(R.id.cb_remind);
        tv_remind_time = imgHeaderView.findViewById(R.id.tv_remind_time);
        btn_save = findViewById(R.id.btn_save);

        sportPunchList.addHeaderView(imgHeaderView);
    }

    private void initListener() {
        sportHisBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        sportPunchList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initData();
            }

            @Override
            public void onLoadMore() {

            }
        });
        // header
        tv_remind_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog1();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    private void initHeadData() {
        sp = new SharedPreferencesUtils(this);
        String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
        String remind = (String) sp.getParam("remind", "1");
        String achieveTime = (String) sp.getParam("achieveTime", "20:00");
        if (!planWalk_QTY.isEmpty()) {
            if ("0".equals(planWalk_QTY)) {
                tv_step_number.setText("7000");
            } else {
                tv_step_number.setText(planWalk_QTY);
            }
        }
        if (!remind.isEmpty()) {
            if ("0".equals(remind)) {
                cb_remind.setChecked(false);
            } else if ("1".equals(remind)) {
                cb_remind.setChecked(true);
            }
        }

        if (!achieveTime.isEmpty()) {
            tv_remind_time.setText(achieveTime);
        }
    }

    private void initData() {
        if (DbUtils.getLiteOrm() == null) {
            DbUtils.createDb(this, "jingzhi");
        }
        List<StepData> stepDatas = DbUtils.getQueryAll(StepData.class);
        sportPunchAdapter.initData(stepDatas);
        sportPunchList.refreshComplete();
    }

    //header
    private void save() {
        walk_qty = tv_step_number.getText().toString().trim();
//        remind = "";
        if (cb_remind.isChecked()) {
            remind = "1";
        } else {
            remind = "0";
        }
        achieveTime = tv_remind_time.getText().toString().trim();
        if (walk_qty.isEmpty() || "0".equals(walk_qty)) {
            sp.setParam("planWalk_QTY", "7000");
        } else {
            sp.setParam("planWalk_QTY", walk_qty);
        }
        sp.setParam("remind", remind);

        if (achieveTime.isEmpty()) {
            sp.setParam("achieveTime", "21:00");
            this.achieveTime = "21:00";
        } else {
            sp.setParam("achieveTime", achieveTime);
        }
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        DialogUIUtils.showToastCenter("运动计划保存成功");
    }

    private void showTimeDialog1() {
        final Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        final DateFormat df = new SimpleDateFormat("HH:mm");
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                String remaintime = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
                Date date = null;
                try {
                    date = df.parse(remaintime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (null != date) {
                    calendar.setTime(date);
                }
                tv_remind_time.setText(df.format(date));
            }
        }, hour, minute, true).show();
    }
}

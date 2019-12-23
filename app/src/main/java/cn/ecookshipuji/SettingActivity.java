package cn.ecookshipuji;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author ciba
 * @description 描述
 * @date 2019/2/25
 */
public class SettingActivity extends Activity {
    private EditText etInfoIndex;
    private CheckBox cbDrawVod;
    private CheckBox cbCustomDrawVod;
    private CheckBox cbBannerGdt2;
    private Button btnConfirm;

    public static void jumpHere(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        etInfoIndex = findViewById(R.id.etInfoIndex);
        cbDrawVod = findViewById(R.id.cbDrawVod);
        cbCustomDrawVod = findViewById(R.id.cbCustomDrawVod);
        cbBannerGdt2 = findViewById(R.id.cbBannerGdt2);
        btnConfirm = findViewById(R.id.btnConfirm);
    }

    private void initListener() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changedSetting();
            }
        });
    }

    private void initData() {
        if (MyApplication.adIndex > 0) {
            etInfoIndex.setText(MyApplication.adIndex + "");
        }
        cbDrawVod.setChecked(MyApplication.horDrawVod);
        cbCustomDrawVod.setChecked(MyApplication.customDrawVod);
        cbBannerGdt2.setChecked(MyApplication.gdtBanner2);
    }

    private void changedSetting() {
        String indexStr = etInfoIndex.getText().toString();
        int index = -1;
        if (!TextUtils.isEmpty(indexStr)) {
            try {
                index = Integer.parseInt(indexStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            index = 0;
        }
        if (index < 0) {
            Toast.makeText(this, "Index必须是大于等于0的整数，请检查", Toast.LENGTH_SHORT).show();
            return;
        }
        MyApplication.adIndex = index;
        MyApplication.horDrawVod = cbDrawVod.isChecked();
        MyApplication.customDrawVod = cbCustomDrawVod.isChecked();
        MyApplication.gdtBanner2 = cbBannerGdt2.isChecked();
        Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
    }
}

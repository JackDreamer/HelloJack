package com.guochao.lockview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private LockView lockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lockView = (LockView) findViewById(R.id.lv_view);

        lockView.setmOnLockListener(new LockView.onUnLockListener() {
            @Override
            public void onUnlock() {
                finish();
            }
        });
    }
}

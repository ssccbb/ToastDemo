package com.sung.toastdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_click:
                HkRotationToast.create(this,"暂时无录制",HkRotationToast.Duration.SHORT).show();
                break;
        }
    }
}

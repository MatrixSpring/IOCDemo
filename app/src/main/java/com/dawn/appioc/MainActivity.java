package com.dawn.appioc;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dawn.libioc.annotation.BindView;
import com.dawn.libioc.annotation.OnClick;
import com.dawn.libioc.annotation.SetContentView;
import com.dawn.libioc.annotation.EventBase;
import com.dawn.libioc.base.BaseActivity;

@SetContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.tv_hello)
    TextView tv_hello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_content.setText("11122223333");
    }


    @OnClick({R.id.tv_content, R.id.tv_hello})
   private void OnClickMethod(View view){
        switch (view.getId()){
            case R.id.tv_content:
                Toast.makeText(MainActivity.this, "tv_content", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_hello:
                Toast.makeText(MainActivity.this, "tv_hello", Toast.LENGTH_SHORT).show();
                break;
        }
   }
}

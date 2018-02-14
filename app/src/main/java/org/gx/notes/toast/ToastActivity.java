package org.gx.notes.toast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.gx.notes.R;
import org.gx.notes.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ToastActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast);
        ButterKnife.bind(this);
    }


    @OnClick({
            R.id.btn_top_sheet_message
    })
    public void onClick(View view){

        switch (view.getId()){
            case R.id.btn_top_sheet_message:
                showTopSheetMessageToast();
                break;
        }

    }

    private void showTopSheetMessageToast(){
//        new TopToastView.Builder(this)
//                .bgColor(R.color.colorAccent)
//                .message("恭喜您获得成就3星！")
//                .statusBarColor("#00000000")
//                .textColor("#ffffff")
//                .autoDismiss(true)
//                .build()
//                .showToast();
        new MessageSheetToastView.Builder(this)
                .message("恭喜您获得成就3星！")
                .textColor(android.R.color.white)
                .bgColor(android.R.color.holo_red_light)
                .statusBarColor(R.color.colorPrimaryDark)
                .autoDismissTime(3000)
                .build().show(getSupportFragmentManager(),"");
    }
}

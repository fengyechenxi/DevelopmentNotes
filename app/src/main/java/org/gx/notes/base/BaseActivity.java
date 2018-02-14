package org.gx.notes.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.Window;

/**
 * Created by Administrator on 2018/2/11 0011.
 */

public class BaseActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setActivityTransition();
        super.onCreate(savedInstanceState);
    }

    /**
     * 转场动画设置
     */
    private void setActivityTransition(){
        requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if(Build.VERSION.SDK_INT >= 21){//这里使用系统的，可以自定义
            getWindow().setEnterTransition(new Fade());
            getWindow().setExitTransition(new Fade());
        }
    }

    /**
     * 用设置好的转场动画跳转Activity
     * @param intent
     */
    protected void goActivity(Intent intent){
        ActivityCompat.startActivity(this,intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
    }

}

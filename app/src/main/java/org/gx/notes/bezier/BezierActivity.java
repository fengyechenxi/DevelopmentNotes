package org.gx.notes.bezier;

import android.animation.AnimatorInflater;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.gx.notes.R;
import org.gx.notes.base.BaseActivity;
import org.gx.notes.bezier.animation.LoginBgAnimation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BezierActivity extends BaseActivity {
    @BindView(R.id.iv_bg)
    ImageView mIvBg;
    @BindView(R.id.btn_login)
    TextView mBtnLogin;
    @BindView(R.id.btn_wechat)
    ImageButton mIbWeChat;
    @BindView(R.id.btn_qq)
    ImageButton mIbQQ;
    @BindView(R.id.btn_weibo)
    ImageButton mIbWeiBo;
    private LoginBgAnimation loginBgAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);
        ButterKnife.bind(this);
        initAnimation();
    }
    private void initAnimation() {
        loginBgAnimation = new LoginBgAnimation();
        loginBgAnimation.setDuration(3500);
        loginBgAnimation.setRepeatCount(android.view.animation.Animation.INFINITE);
        loginBgAnimation.setRepeatMode(android.view.animation.Animation.REVERSE);
        mIvBg.startAnimation(loginBgAnimation);


        if(Build.VERSION.SDK_INT >= 21){
            mBtnLogin.setStateListAnimator(AnimatorInflater.loadStateListAnimator(this,R.animator.login_btn_scale));
            mIbWeChat.setStateListAnimator(AnimatorInflater.loadStateListAnimator(this,R.animator.login_auth_btn_scale));
            mIbQQ.setStateListAnimator(AnimatorInflater.loadStateListAnimator(this,R.animator.login_auth_btn_scale));
            mIbWeiBo.setStateListAnimator(AnimatorInflater.loadStateListAnimator(this,R.animator.login_auth_btn_scale));
        }

    }

    @Override
    public void onBackPressed() {
        if(loginBgAnimation != null){//退出Activity时必须停止动画，否则会与转场动画冲突
            loginBgAnimation.cancel();
        }
        super.onBackPressed();
    }

    @OnClick({R.id.btn_login,R.id.btn_wechat,R.id.btn_qq,R.id.btn_weibo})
    public void buttonClick(View view){

    }


}

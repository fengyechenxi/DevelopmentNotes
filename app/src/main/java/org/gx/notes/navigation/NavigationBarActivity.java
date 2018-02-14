package org.gx.notes.navigation;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.gx.notes.R;
import org.gx.notes.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NavigationBarActivity extends BaseActivity {


    @BindView(R.id.navigation_bar)
    NavigationBar navigationBar;
    @BindView(R.id.tabbar)
    TabBar tabbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_bar);
        ButterKnife.bind(this);

        ImageView imageViewLeft = new ImageView(this);
        imageViewLeft.setImageResource(android.R.drawable.ic_menu_camera);
        ImageView imageViewRight = new ImageView(this);
        imageViewRight.setImageResource(android.R.drawable.ic_menu_day);
        navigationBar.addItemInLeft(imageViewLeft)
                .addItemInRight(imageViewRight)
                .titleText(R.string.app_name)
                .titleTextColor(android.R.color.white)
                .titleTextSize(TypedValue.COMPLEX_UNIT_SP,20);

        TabBar.Tab tabQQ =
                TabBar.newTab(this)
                        .tabPadding(10)
                        .title("QQ")
                        .titleColor(R.color.selector_tab)
                        .icon(R.drawable.ic_qq)
                        .titleTextSize(12);
        TabBar.Tab tabWeChat =
                TabBar.newTab(this)
                        .tabPadding(10)
                        .title("WeChat")
                        .titleColor(R.color.selector_tab)
                        .icon(R.drawable.ic_wechat)
                        .titleTextSize(12);
        TabBar.Tab tabWeiBo =
                TabBar.newTab(this)
                        .tabPadding(10)
                        .title("WeiBo")
                        .titleColor(R.color.selector_tab)
                        .icon(R.drawable.ic_weibo)
                        .titleTextSize(12);
        tabbar.addTab(tabQQ,0)
                .addTab(tabWeChat,1)
                .addTab(tabWeiBo,2)
                .setOnTabSelectListener(new TabBar.OnTabSelectListener() {
                    @Override
                    public void onTabSelected(TextView tab, int index) {

                    }
                })
                .setOnTabRepeatClickListener(new TabBar.OnTabRepeatClickListener() {
                    @Override
                    public void onTabRepeatClick(TextView tab, int index) {

                    }
                }).commit();
        tabbar.checked(0);

    }

    @OnClick({R.id.btn_android_style,R.id.btn_ios_style})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.btn_android_style:
                navigationBar.changeNavigationType(NavigationBar.ANDROID);
                break;
            case R.id.btn_ios_style:
                navigationBar.changeNavigationType(NavigationBar.IOS);
                break;
            default:
        }

    }
}

package org.gx.notes.load;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.gx.notes.R;
import org.gx.notes.base.BaseActivity;
import org.gx.notes.load.loading.Loader;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * loading模块功能的展示界面
 */
public class LoadingActivity extends BaseActivity{
    private Loader loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ButterKnife.bind(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.btn_ui_loading_style1,R.id.btn_ui_loading_style2,R.id.btn_empty_loading,R.id.btn_view_loading})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_ui_loading_style1:
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.loading_container,new LoadingUIStyle1Fragment(),LoadingUIStyle1Fragment.class.getSimpleName())
                        .addToBackStack(LoadingUIStyle1Fragment.class.getSimpleName())
                        .commit();
                break;
            case R.id.btn_ui_loading_style2:
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.loading_container,new LoadingUIStyle2Fragment(),LoadingUIStyle2Fragment.class.getSimpleName())
                        .addToBackStack(LoadingUIStyle2Fragment.class.getSimpleName())
                        .commit();
                break;
            case R.id.btn_empty_loading:
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.loading_container,new LoadingEmptyFragment(),LoadingEmptyFragment.class.getSimpleName())
                        .addToBackStack(LoadingEmptyFragment.class.getSimpleName())
                        .commit();
                break;
            case R.id.btn_view_loading:
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.loading_container,new LoadingViewFragment(),LoadingViewFragment.class.getSimpleName())
                        .addToBackStack(LoadingViewFragment.class.getSimpleName())
                        .commit();
                break;
                default:
        }
    }


}

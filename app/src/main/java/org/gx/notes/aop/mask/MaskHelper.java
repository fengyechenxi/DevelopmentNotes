package org.gx.notes.aop.mask;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

class MaskHelper {
    private static String PREFERENCE_NAME = "MASK";
    /**
     * 是否需要MaskLayer
     * @param context
     * @param key
     * @return
     */
    public static boolean isShowMaskLayer(Context context, String key){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(key,true);
    }


    /**
     * 保存，不需要再现实MaskLayer
     * @param context
     * @param key
     */
    public static void saveAlreadyShowMaskLayer(Context context, String key){

        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        try {
            editor.putBoolean(key,false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.apply();
    }
}

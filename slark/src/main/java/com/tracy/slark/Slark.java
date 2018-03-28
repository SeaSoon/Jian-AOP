package com.tracy.slark;

import android.util.Log;
import android.view.View;

import com.tracy.slark.model.ILogCollector;
import com.tracy.slark.model.action.ClickAction;
import com.tracy.slark.model.action.PageAction;
import com.tracy.slark.model.Constant;
import com.tracy.slark.utils.TraceUtils;
import com.tracy.slark.view.EventPopup;

import java.util.HashMap;

/**
 * Created by cuishijie on 2018/1/28.
 */

public final class Slark {

    private static HashMap<String, HashMap<String, String>> configMap = new HashMap<>();

    /**
     * @param debug 开启debug模式，会在LogCat输出event的log日志，并会写入手机本地的Slark文件夹中，default关闭。
     *              在Slark的gradle配置中配置，无需手动调用
     * @pluiginInject
     */
    public final static void setDebug(boolean debug) {
        Constant.isDebug = debug;
    }

    /**
     * 设置日志接收器，灵活起见，暂未集成，需要自定义,并在应用初始化时调用
     *
     * @param collector
     */
    public final static void setLogCollector(ILogCollector collector) {
        SlarkService.getInstance().setLogCollector(collector);
    }

    /**
     * track click events
     *
     * @param view
     * @pluginInject
     */
    public final static void trackClickEvent(View view) {
        if (view != null) {
//            SlarkService.getInstance().addAction(new ClickAction(view));
        }
    }


    /**
     * @param pageRef   this (Activity or Fragment)
     * @param pageStart
     * @pluginInject
     */
    public final static void trackPageEvent(Object pageRef, boolean pageStart) {
        SlarkService.getInstance().addAction(new PageAction(pageRef, pageStart));
    }

    public static final int VIEW_TAG = 0x0000ffff;

    public static boolean hasEventConfig(View view) {
        if (view.getContext() != null) {
            String pageKey = view.getContext().getClass().getSimpleName();
            if (configMap.containsKey(pageKey)) {
                HashMap<String, String> pageMap = configMap.get(pageKey);
                if (view.getTag(VIEW_TAG) == null) {
                    view.setTag(TraceUtils.generateViewTree(view));
                }
                String eventKey = (String) view.getTag(VIEW_TAG);
                if (pageMap.containsKey(eventKey)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void showEventDialog(View view) {
        if (view.getContext() != null) {
            EventPopup popup = new EventPopup(view, pop -> {
                String pageKey = view.getContext().getClass().getSimpleName();
                HashMap<String, String> pageMap;
                if (configMap.containsKey(pageKey)) {
                    pageMap = configMap.get(pageKey);
                } else {
                    pageMap = new HashMap<>();
                    configMap.put(pageKey, pageMap);
                }
                if (view.getTag(VIEW_TAG) == null) {
                    view.setTag(TraceUtils.generateViewTree(view));
                }
                String eventKey = (String) view.getTag(VIEW_TAG);
                String eventValue = pop.getEventId();
                pageMap.put(eventKey, eventValue);
                Log.e("TraceUtils", "pageKey = " + pageKey + " | eventKey = " + eventKey + " | eventValue = " + eventValue);
            });
            popup.show();
        }
    }
}

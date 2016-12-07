package com.lilin.test.phototest;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

/**
 * 通用工具类
 */
public class CommonUtils {

    /**
     * 动态设置gridView的高度
     *
     * @param gridView
     */
    public static void setGideViewHeightBasedOnChildren(GridView gridView, int num) {
        if (gridView == null) return;
        int itemSpacing = 0;
        int heightNum = 0;
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        View listItem = listAdapter.getView(0, null, gridView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            itemSpacing = gridView.getVerticalSpacing();
        }
        listItem.measure(0, 0);
        heightNum = listAdapter.getCount() / (num + 1) + 1;
        totalHeight = listItem.getMeasuredHeight() * heightNum + itemSpacing + (heightNum - 1);
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }


}

package com.prayxiang.support.recyclerview;

import android.util.SparseArray;

import java.util.Map;
import java.util.Set;


public class TypeProvider {
    private SparseArray<ViewBinder<?, ? extends ViewHolder>> mCaches = new SparseArray<>();

    public ViewBinder<?, ? extends ViewHolder> findViewBinder(Integer integer) {
        return mCaches.get(integer);
    }

    public void register(int integer, ViewBinder<?, ? extends ViewHolder> binder) {
        mCaches.put(integer, binder);
    }

    public void registerAll(Map<Integer, ViewBinder<?, ? extends ViewHolder>> map) {
        Set<Integer> keys = map.keySet();
        for (Integer key : keys
                ) {
            mCaches.put(key, map.get(key));
        }

    }
}

package com.prayxiang.support.recyclerview;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by prayxiang on 2017/10/17.
 *
 * @description TODO
 */
@SuppressWarnings("all")
public class ViewBinder<T, V extends ViewHolder> {

    ListAdapter adapter;

    ListPresenter presenter;
    public ListAdapter getAdapter() {
        return adapter;
    }

    public ListPresenter getPresenter() {
        return presenter;
    }

    public V onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return null;
    }

   public void onBindViewHolder(V holder, List<Object> payloads) {

    }




    public void onViewDetachedFromWindow(V viewHolder) {

    }

    public void onViewAttachedToWindow(V viewHolder) {
    }
}

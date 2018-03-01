package com.prayxiang.support.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by prayxiang on 2017/10/17.
 *
 * @description TODO
 */
public class ViewHolder extends RecyclerView.ViewHolder {
    private Object object;

    @SuppressWarnings("unchecked")
    public <T> T getItem() {
        return (T) object;
    }

    public void setItem(Object object) {
        this.object = object;
    }

    @SuppressWarnings("all")
    public ViewHolder(View itemView) {
        super(itemView);
    }

    public Context getContext() {
        return itemView.getContext();
    }
}

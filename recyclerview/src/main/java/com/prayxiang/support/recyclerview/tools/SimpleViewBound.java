package com.prayxiang.support.recyclerview.tools;

import android.databinding.DataBindingComponent;

import com.prayxiang.support.recyclerview.DataBoundViewHolder;
import com.prayxiang.support.recyclerview.ViewBound;

/**
 * Created by xianggaofeng on 2018/3/1.
 */

public class SimpleViewBound extends ViewBound {
    private int br;
    public SimpleViewBound(int br, int layoutId) {
        super(layoutId);
        this.br = br;
    }

    public SimpleViewBound(int br,int layoutId, DataBindingComponent component) {
        super(layoutId, component);
        this.br = br;
    }

    @Override
    public void bindItem(DataBoundViewHolder holder, Object item) {
        holder.binding.setVariable(br,item);
    }
}

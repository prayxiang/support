/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.prayxiang.support.recyclerview;


import android.support.annotation.CallSuper;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;


public class ListAdapter extends RecyclerView.Adapter<ViewHolder> {


    private ListPresenter mPresenter;
    private ListUpdateCallback callback;

    private boolean attached;

    public ListAdapter(ListPresenter presenter) {
        mPresenter = presenter;
        callback = new ListChangeCallback(this);
    }


    private LayoutInflater inflater;


    @Override
    @CallSuper
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        ViewBinder binder = mPresenter.findViewBinder(viewType);
        binder.adapter = this;
        binder.presenter = mPresenter;
        assert inflater != null;
        return binder.onCreateViewHolder(inflater, parent);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mPresenter.addOnListChangeCallback(callback);
        attached = true;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mPresenter.removeListChangeCallback(callback);
        attached = false;
    }

    @Override
    public final void onBindViewHolder(ViewHolder holder, int position) {
        throw new IllegalArgumentException("just overridden to make final.");
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        ViewBinder binder = mPresenter.findViewBinder(holder.getItemViewType());
        holder.setItem(getItem(position));
        binder.onBindViewHolder(holder, payloads);
    }


    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewBinder binder = (ViewBinder) mPresenter.findViewBinder(holder.getItemViewType());
        binder.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ViewBinder binder = (ViewBinder) mPresenter.findViewBinder(holder.getItemViewType());
        binder.onViewDetachedFromWindow(holder);
    }


    @Override
    public int getItemViewType(int position) {
        return mPresenter.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mPresenter.getItemCount();
    }

    public Object getItem(int position) {
        return mPresenter.getItem(position);
    }

    public <T> void setListPresenter(ListPresenter<T> listPresenter) {
        if(attached){
            mPresenter.removeListChangeCallback(callback);
        }
        listPresenter.removeListChangeCallback(callback);
        listPresenter.addOnListChangeCallback(callback);
        this.mPresenter = listPresenter;
    }

    public static class ListChangeCallback implements ListUpdateCallback {
        RecyclerView.Adapter adapter;

        public ListChangeCallback(RecyclerView.Adapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onInserted(int position, int count) {
            if (adapter != null) {
                adapter.notifyItemRangeInserted(position, count);
            }
        }

        @Override
        public void onRemoved(int position, int count) {
            if (adapter != null) {
                adapter.notifyItemRangeRemoved(position,count);
            }
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            if (adapter != null) {
                adapter.notifyItemMoved(fromPosition,toPosition);
            }
        }

        @Override
        public void onChanged(int position, int count, Object payload) {
            if (adapter != null) {
                adapter.notifyItemRangeChanged(position, count, payload);
            }
        }
    }

}

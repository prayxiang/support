package com.prayxiang.support.recyclerview;

import android.os.AsyncTask;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by xianggaofeng on 2018/3/1.
 */

public class ListPresenter<T>  implements ListUpdateCallback{
    TypeProvider mPool = new TypeProvider();

    final  static TypeStrategy sDefaultTypeStrategy = new TypeStrategy() {
        @Override
        public int getItemViewType(Object item) {
            return item.getClass().hashCode();
        }
    };
    List<Object> items = Collections.emptyList();
    TypeStrategy mTypeStrategy = sDefaultTypeStrategy;
    List<ListUpdateCallback> callbacks;
    DiffCallback<T> mDiffCallback;

    public ViewBinder findViewBinder(int viewType) {
        return mPool.findViewBinder(viewType);
    }

    public int getItemViewType(int  position) {
       return mTypeStrategy.getItemViewType(items.get(position));
    }

    public int getItemCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return items.get(position);
    }

    public void addOnListChangeCallback(ListUpdateCallback callback) {
        if(callbacks==null){
            callbacks = new ArrayList<>();
        }
        callbacks.add(callback);
    }

    public void removeListChangeCallback(ListUpdateCallback callback) {
        if(callbacks!=null){
            callbacks.remove(callback);
        }
    }


    public boolean areItemsTheSame(T oldItem, T newItem) {
        if(mDiffCallback !=null){
            return mDiffCallback.areItemsTheSame(oldItem,newItem);
        }
        return false;
    }

    public boolean areContentsTheSame(T oldItem, T newItem) {
        if(mDiffCallback !=null) {

            return mDiffCallback.areContentsTheSame(oldItem,newItem);
        }
            return false;
    }


    public void display(List<T> list) {
        List<T> oldItems = getItems();
        if (list == null) {
            list = Collections.emptyList();
        }

        setItems(list);
        if(oldItems.size()==0){
            onInserted(0,list.size());
        }else if(list.size()==0){
            onRemoved(0,oldItems.size());
        }else {
            new DiffAsyncTask<>(this).execute(oldItems, list);
        }
    }

    public void setItems(List<T> items) {
        this.items = (List<Object>) items;
    }
    public List<T> getItems() {
        return (List<T>) items;
    }

    @Override
    public void onInserted(int position, int count) {
        if(callbacks!=null){
            for (ListUpdateCallback callback:
                    callbacks
                    ) {
                callback.onInserted(position,count);
            }
        }
    }

    @Override
    public void onRemoved(int position, int count) {
        if(callbacks!=null){
            for (ListUpdateCallback callback:
                    callbacks
                    ) {
                callback.onRemoved(position,count);
            }
        }
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        if(callbacks!=null){
            for (ListUpdateCallback callback:
                    callbacks
                    ) {
                callback.onMoved(fromPosition,toPosition);
            }
        }
    }

    @Override
    public void onChanged(int position, int count, Object payload) {
        if(callbacks!=null){
            for (ListUpdateCallback callback:
                    callbacks
                    ) {
                callback.onChanged(position,count,payload);
            }
        }
    }

    public void attach(RecyclerView recyclerView) {
        recyclerView.setAdapter(new ListAdapter(this));
    }

    public void attachWithBound(RecyclerView recyclerView) {
        recyclerView.setAdapter(new DataBoundAdapter(this));
    }
    public void attach(ListAdapter adapter,RecyclerView recyclerView){
        adapter.setListPresenter(this);
        recyclerView.setAdapter(adapter);
    }

    public void addViewBinder(int type, ViewBinder viewBinder){
        mPool.register(type,viewBinder);
    }

    public  interface DiffCallback<T>{
        boolean areItemsTheSame(T oldItem, T newItem);

        boolean areContentsTheSame(T oldItem, T newItem);

    }

    public void setTypeStrategy(TypeStrategy typeStrategy) {
        this.mTypeStrategy = typeStrategy;
    }

    public void setDiffCallback(DiffCallback<T> diffCallback) {
        this.mDiffCallback = diffCallback;
    }

    public static <T>Builder<T> create(){
        return new Builder<T>();
    }

    public interface TypeStrategy<T> {
        int getItemViewType(T item);
    }

    public static class DiffAsyncTask<T> extends AsyncTask<List<T>,Integer,DiffUtil.DiffResult> {
        WeakReference<ListPresenter<T>> presenterRef;

        public DiffAsyncTask(ListPresenter<T> presenter) {
            this.presenterRef = new WeakReference<>(presenter);
        }

        @Override
        protected DiffUtil.DiffResult doInBackground(List<T>[] lists) {
            final ListPresenter<T> presenter = presenterRef.get();
            if(presenter==null){
                return null;
            }
            final List<T> oldItems = lists[0];
            final List<T> newItems = lists[1];
            return DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return oldItems.size();
                }

                @Override
                public int getNewListSize() {
                    return newItems.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return presenter.areItemsTheSame(oldItems.get(oldItemPosition),newItems.get(newItemPosition));
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return presenter.areContentsTheSame(oldItems.get(oldItemPosition),newItems.get(newItemPosition));
                }
            });
        }

        @Override
        protected void onPostExecute(DiffUtil.DiffResult diffResult) {
            final ListPresenter<T> presenter = presenterRef.get();
            if(presenter==null||diffResult==null){
                return ;
            }
            diffResult.dispatchUpdatesTo(presenter);
        }
    }






    public static class Builder<T>{

        private ListPresenter<T> presenter;
        private ListAdapter adapter;

        public Builder(){
            this.presenter = new ListPresenter<>();
        }
        public Builder<T> addViewBinder(int type, ViewBinder viewBinder){
            presenter.addViewBinder(type,viewBinder);
            return this;
        }

        public Builder<T>  typeStrategy(TypeStrategy typeStrategy){
            presenter.setTypeStrategy(typeStrategy);
            return this;
        }

        public Builder<T>  diffCallback(DiffCallback<T> diffCallback){
            presenter.setDiffCallback(diffCallback);
            return this;
        }
        public ListPresenter<T> attach(RecyclerView recyclerView){
            if(adapter == null){
                adapter = new ListAdapter(presenter);
            }else {
                adapter.setListPresenter(presenter);
            }
            recyclerView.setAdapter(adapter);
            return presenter;
        }


        public ListPresenter attachWithBound(RecyclerView recyclerView){
            if(adapter == null){
                adapter = new DataBoundAdapter(presenter);
            }else {
                adapter.setListPresenter(presenter);
            }
            recyclerView.setAdapter(adapter);
            return presenter;
        }



    }
}

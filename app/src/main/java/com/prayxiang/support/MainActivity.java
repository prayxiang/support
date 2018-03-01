package com.prayxiang.support;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.prayxiang.support.recyclerview.DataBoundViewHolder;
import com.prayxiang.support.recyclerview.ListPresenter;
import com.prayxiang.support.recyclerview.tools.SimpleViewBound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListPresenter presenter = new ListPresenter();
    List list = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        presenter = ListPresenter.<String>create()
                .addViewBinder(String.class.hashCode(), new SimpleViewBound(BR.data, R.layout.item_main))
                .diffCallback(new ListPresenter.DiffCallback<String>() {

                    @Override
                    public boolean areItemsTheSame(String s, String t1) {
                        return s.equals(t1);
                    }

                    @Override
                    public boolean areContentsTheSame(String s, String t1) {
                        return s.equals(t1);
                    }
                })
                .attachWithBound(recyclerView);

        presenter.display(list);




//        presenter.display(list);


        for (int i = 0; i < 10; i++) {
//            list.add(i);

            final List tmp = new ArrayList(list);
            tmp.add(i + " string");
            tmp.add(i);
            list = tmp;
           // presenter.display(tmp);
            if(i <5)
            recyclerView.postDelayed(new Runnable() {
                                         @Override
                                         public void run() {

                                             Log.d("xgf","xx"+tmp.size());
                                             Toast.makeText(recyclerView.getContext(),""+tmp.size(),Toast.LENGTH_LONG).show();
                                             presenter.display(tmp);

                                         }},3000*i);

else
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Log.d("xgf","xx"+tmp.size());
                    Toast.makeText(recyclerView.getContext(),""+tmp.size(),Toast.LENGTH_LONG).show();
                    presenter.display(presenter.getItems()  .subList(1,presenter.getItemCount()));

                }},3000*i);
        }




    }
}

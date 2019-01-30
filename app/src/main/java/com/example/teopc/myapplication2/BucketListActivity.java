package com.example.teopc.myapplication2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Vector;

import io.realm.Realm;
import model.Product;

public class BucketListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter mAdapter;
    private Realm realm;
    private Vector<Product> myDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_list);
        realm = Realm.getDefaultInstance();
        myDataset = new Vector<>();
        myDataset.addAll(realm.where(Product.class).findAll());

        mRecyclerView = (RecyclerView) findViewById(R.id.bucketRV);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);
        mAdapter.mode = 1;
        mRecyclerView.setAdapter(mAdapter);

    }
}

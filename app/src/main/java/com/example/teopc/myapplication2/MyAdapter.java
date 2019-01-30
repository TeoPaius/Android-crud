package com.example.teopc.myapplication2;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Vector;

import model.Product;
import model.Run;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Vector<Product> mDataset;
    public static Integer selectedIdx = -1;
    public Integer mode = 0;


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener{
        // each data item is just a string in this case
        public LinearLayout lay;
        public Integer myID = 0;
        public MyViewHolder(LinearLayout v) {
            super(v);
            v.setOnClickListener(this);
            lay = v;
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getPosition();
            MyAdapter.selectedIdx = itemPosition;
            Log.d("tg", Integer.toString(this.myID));
        }
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Vector<Product> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);


        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Product p = mDataset.elementAt(position);
        TextView nameText = (TextView) holder.lay.findViewById(R.id.nameText);
        TextView priceText = (TextView) holder.lay.findViewById(R.id.priceText);
        TextView quantityText = (TextView) holder.lay.findViewById(R.id.quantityText);
        TextView statusText = (TextView) holder.lay.findViewById(R.id.statusText);
        TextView descriptionText = (TextView) holder.lay.findViewById(R.id.descriptionText);


        if(mode == 0) {
            statusText.setVisibility(View.GONE);
            descriptionText.setVisibility(View.GONE);
        }
        else
        {
            statusText.setVisibility(View.VISIBLE);
            descriptionText.setVisibility(View.VISIBLE);
        }

        nameText.setText("name: " + p.name);
        priceText.setText(p.price.toString() + " lei");
        quantityText.setText(p.quantity.toString());
        descriptionText.setText(p.description);
        statusText.setText("status: " + p.status);
        holder.myID = p.id;


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
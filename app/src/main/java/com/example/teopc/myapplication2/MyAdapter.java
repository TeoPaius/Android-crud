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

import model.Run;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Vector<Run> mDataset;
    public static Integer selectedIdx = -1;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener{
        // each data item is just a string in this case
        public LinearLayout lay;
        public MyViewHolder(LinearLayout v) {
            super(v);
            v.setOnClickListener(this);
            lay = v;
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getPosition();
            Log.d("tg", Integer.toString(itemPosition));
            MyAdapter.selectedIdx = itemPosition;
            Toast.makeText( v.getContext(), Integer.toString(itemPosition), Toast.LENGTH_LONG).show();
        }
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Vector<Run> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.run_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);


        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        TextView lengthText = (TextView) holder.lay.findViewById(R.id.lengthText);
        TextView timeText = (TextView) holder.lay.findViewById(R.id.timeText);
        TextView dateText = (TextView) holder.lay.findViewById(R.id.dateText);

        Run r = mDataset.elementAt(position);
        lengthText.setText(Float.toString(r.getLength()) + "km");
        timeText.setText(Integer.toString(r.getDuration()) + "mins");
        dateText.setText(r.getDate().toString());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
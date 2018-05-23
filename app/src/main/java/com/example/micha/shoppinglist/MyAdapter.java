package com.example.micha.shoppinglist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<String> mDataset;


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageButton minus;
        EditText singleItem;

        public ViewHolder(View itemView) {
            super(itemView);
            minus = (ImageButton) itemView.findViewById(R.id.minus);
            singleItem = (EditText) itemView.findViewById(R.id.item_content);

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    try{
                        mDataset.remove(position);
                        notifyItemRemoved(position);
                    }catch (IndexOutOfBoundsException e){e.printStackTrace();}
                }
            });

            singleItem.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mDataset.set(getAdapterPosition(), s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<String> myDataset) {
        mDataset = myDataset;

    }


    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int x = holder.getLayoutPosition();

        if(mDataset.get(x).length() > 0) {
            holder.singleItem.setText(mDataset.get(x));
        }
        else{
            holder.singleItem.setText(null);
            holder.singleItem.setHint("Next product");
            holder.singleItem.requestFocus();
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
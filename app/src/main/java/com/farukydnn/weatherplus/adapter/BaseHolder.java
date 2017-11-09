package com.farukydnn.weatherplus.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.farukydnn.weatherplus.interfaces.RecyclerViewClickListener;


class BaseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final RecyclerViewClickListener mListener;


    BaseHolder(View itemView, RecyclerViewClickListener listener) {
        super(itemView);

        mListener = listener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mListener.onClick(view, getAdapterPosition());
    }
}

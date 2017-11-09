package com.farukydnn.weatherplus.interfaces;

import android.support.v7.widget.RecyclerView;


public interface ItemTouchHelperListener {

    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);

    void onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                   RecyclerView.ViewHolder target);
}

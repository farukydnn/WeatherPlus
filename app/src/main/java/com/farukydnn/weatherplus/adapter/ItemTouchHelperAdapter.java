package com.farukydnn.weatherplus.adapter;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.farukydnn.weatherplus.interfaces.ItemTouchHelperListener;
import com.farukydnn.weatherplus.modelview.CityWeatherModelView;

import java.util.List;


public class ItemTouchHelperAdapter extends ItemTouchHelper.SimpleCallback {

    private static final float ALPHA_FULL = 1.0f;
    private boolean isEnabled;
    private ItemTouchHelperListener mCallBack;
    private List<CityWeatherModelView> items;

    public ItemTouchHelperAdapter(int dragDirs, int swipeDirs, ItemTouchHelperListener callBack,
                                  List<CityWeatherModelView> items, boolean isEnabled) {

        super(dragDirs, swipeDirs);
        this.mCallBack = callBack;
        this.items = items;
        this.isEnabled = isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current,
                               RecyclerView.ViewHolder target) {

        int targetPosition = target.getAdapterPosition();

        return targetPosition < items.size()
                && !items.get(targetPosition).isCurrentLocation()
                && super.canDropOver(recyclerView, current, target);
    }

    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        if (!isEnabled || items.get(viewHolder.getAdapterPosition()).isCurrentLocation())
            return 0;
        else
            return super.getSwipeDirs(recyclerView, viewHolder);
    }

    @Override
    public int getDragDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (!isEnabled || items.get(viewHolder.getAdapterPosition()).isCurrentLocation())
            return 0;
        else
            return super.getDragDirs(recyclerView, viewHolder);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        viewHolder.itemView.setAlpha(ALPHA_FULL);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {

        mCallBack.onMove(recyclerView, viewHolder, target);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        mCallBack.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }
}
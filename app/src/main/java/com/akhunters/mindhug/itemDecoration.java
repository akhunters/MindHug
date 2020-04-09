package com.akhunters.mindhug;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class itemDecoration extends RecyclerView.ItemDecoration {

    private final static int verticalOverlap = -180;

    @Override
    public void getItemOffsets (Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.set(0, 0, 0, verticalOverlap);

    }
}

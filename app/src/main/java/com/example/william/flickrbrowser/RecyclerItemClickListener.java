package com.example.william.flickrbrowser;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by William on 23/11/2016.
 */

class RecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {
    private static final String TAG = "RecyclerItemClickListen";

    interface OnRecyclerClickListener {
        void onItemClick(View view, int position);
        void onLongItemClick(View view, int position);
    }

    private final OnRecyclerClickListener mListener;
    private final GestureDetectorCompat mGestureDetector;

    //constructor
    //we need a context for the gesture detector to work and we need a reference for the recylcer view that we're detecting the taps on
    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnRecyclerClickListener listener) {
        mListener = listener;
        //creating an anonymous class that extends SimpleOnGestureListener so we can override the methods we're interested in
        mGestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG, "onSingleTapUp: starts");
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && mListener != null){
                    Log.d(TAG, "onSingleTapUp: calling listener.onItemClick");
                    mListener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.d(TAG, "onLongPress: starts");
                View childView = recyclerView.findChildViewUnder(e.getX(),e.getY());
                if (childView != null && mListener != null){
                    Log.d(TAG, "onLongPress: calling listener.onLongItemClick");
                    //1st param: set the view
                    //2nd param: returns position of childview variable
                    mListener.onLongItemClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
            }
        });
    }

    //passing the touchEvent when intercepted to the gesture detector. its read by the MotionEvent parameter
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.d(TAG, "onInterceptTouchEvent: starts");
        if(mGestureDetector != null){
            boolean result = mGestureDetector.onTouchEvent(e);
            Log.d(TAG, "onInterceptTouchEvent(): returned " + result);
            return result;
        } else {
            //returns false if the gesturedetector doesnt deal with it
            Log.d(TAG, "onInterceptTouchEvent(): returned false");
            return false;
        }
    }
}

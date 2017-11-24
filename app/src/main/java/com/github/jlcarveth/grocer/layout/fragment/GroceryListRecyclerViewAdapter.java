package com.github.jlcarveth.grocer.layout.fragment;

import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jlcarveth.grocer.R;
import com.github.jlcarveth.grocer.layout.fragment.GroceryListFragment.OnListFragmentInteractionListener;
import com.github.jlcarveth.grocer.model.GroceryItem;
import com.github.jlcarveth.grocer.storage.DatabaseHandler;
import com.github.jlcarveth.grocer.util.recycler.ItemTouchHelperAdapter;
import com.github.jlcarveth.grocer.util.recycler.OnStartDragListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * {@link RecyclerView.Adapter} that can display a {@link GroceryItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class GroceryListRecyclerViewAdapter extends RecyclerView.Adapter<GroceryListRecyclerViewAdapter.ViewHolder>
    implements ItemTouchHelperAdapter {

    private final ArrayList<GroceryItem> mValues;
    private final OnStartDragListener dragListener;

    private DatabaseHandler dh;

    private final String TAG = "GRecyclerViewAdapter";

    public GroceryListRecyclerViewAdapter(ArrayList<GroceryItem> items,
                                          OnListFragmentInteractionListener listener,
                                          GroceryListFragment groceryListFragment) {
        mValues = items;
        dragListener = (OnStartDragListener) groceryListFragment;

        dh = new DatabaseHandler(groceryListFragment.getActivity());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grocery_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }

        GroceryItem item = mValues.get(position);
        System.out.println("onBindViewHolder pos: " + position);
        holder.mNameView.setText(item.getName());
        holder.mNoteView.setText(item.getNote());
        holder.mCheckbox.setChecked(item.getChecked());


        holder.mDragBars.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    Log.d(TAG, "Starting drag with " + holder);
                    dragListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public boolean onItemMove(int from, int to) {
        Collections.swap(mValues, from, to);
        notifyItemMoved(from, to);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        dh.deleteGroceryItem(mValues.get(position));
        mValues.clear();
        mValues.addAll(dh.getGroceries());
        //notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mNameView;
        public final TextView mNoteView;

        public final CheckBox mCheckbox;

        public final ImageView mDragBars;

        public GroceryItem mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;

            mNameView = (TextView) view.findViewById(R.id.gl_name);
            mNoteView = (TextView) view.findViewById(R.id.gl_note);
            mCheckbox = (CheckBox) view.findViewById(R.id.gl_checkbox);
            mDragBars = (ImageView) view.findViewById(R.id.gl_burger);

            mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    // Change the value in the list
                    dh.checkGroceryItem(mValues.get(getAdapterPosition()), isChecked);
                    mValues.get(getAdapterPosition()).setChecked(isChecked);
                }
            });

            mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return true;
                }
            });
        }
    }
}

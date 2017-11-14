package com.github.jlcarveth.grocer.layout.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jlcarveth.grocer.R;
import com.github.jlcarveth.grocer.layout.fragment.GroceryListFragment.OnListFragmentInteractionListener;
import com.github.jlcarveth.grocer.layout.fragment.dummy.DummyContent.DummyItem;
import com.github.jlcarveth.grocer.model.GroceryItem;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class GroceryListRecyclerViewAdapter extends RecyclerView.Adapter<GroceryListRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<GroceryItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public GroceryListRecyclerViewAdapter(ArrayList<GroceryItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_grocerylist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        GroceryItem item = mValues.get(position);
        System.out.println("onBindViewHolder pos: " + position);
        holder.mNameView.setText(item.getName());
        holder.mNoteView.setText(item.getNote());
        holder.mCheckbox.setChecked(item.getChecked());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mNameView;
        public final TextView mNoteView;

        public final CheckBox mCheckbox;

        public final ImageView mDragBars;

        public GroceryItem mItem;

        public Bundle args;

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
                    mValues.get(getAdapterPosition()).setChecked(isChecked);
                    //dh.checkEntry(mValues.get(getAdapterPosition()));
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

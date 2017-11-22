package com.github.jlcarveth.grocer.layout.fragment;

import android.app.Fragment;
import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jlcarveth.grocer.R;
import com.github.jlcarveth.grocer.layout.decoration.RecyclerViewDivider;
import com.github.jlcarveth.grocer.model.GroceryItem;
import com.github.jlcarveth.grocer.storage.DatabaseHandler;
import com.github.jlcarveth.grocer.storage.DatabaseObserver;
import com.github.jlcarveth.grocer.storage.DatabaseSubject;
import com.github.jlcarveth.grocer.util.recycler.OnStartDragListener;
import com.github.jlcarveth.grocer.util.recycler.SimpleTouchHelperCallback;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class GroceryListFragment extends Fragment implements OnStartDragListener, DatabaseObserver {

    private OnListFragmentInteractionListener mListener;

    private RecyclerView rv;

    private GroceryListRecyclerViewAdapter adapter;

    private ArrayList<GroceryItem> dataset;

    private ItemTouchHelper ith;

    private DatabaseHandler dh;

    private LinearLayout emptyView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GroceryListFragment() {}

    public static GroceryListFragment newInstance() {
        GroceryListFragment fragment = new GroceryListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grocerylist, container, false);

        rv = view.findViewById(R.id.grocery_list);
        emptyView = view.findViewById(R.id.empty_view);

        dh = new DatabaseHandler(view.getContext());

        dataset = dh.getGroceries();

        Context context = view.getContext();
        rv.setLayoutManager(new LinearLayoutManager(context));

        adapter = new GroceryListRecyclerViewAdapter(dataset,mListener,this);
        rv.setAdapter(adapter);


        ItemTouchHelper.Callback callback = new SimpleTouchHelperCallback(adapter);
        ith = new ItemTouchHelper(callback);
        ith.attachToRecyclerView(rv);

        Drawable divider = (Drawable) ContextCompat.getDrawable(getActivity(), R.drawable.divider);
        RecyclerViewDivider div = new RecyclerViewDivider(divider);
        rv.addItemDecoration(div);

        if (dataset.isEmpty()){
            rv.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            rv.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }

        DatabaseSubject.Companion.attach(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

        DatabaseSubject.Companion.detach(this);
    }

    @Override
    public void onStartDrag(@NotNull RecyclerView.ViewHolder viewHolder) {
        ith.startDrag(viewHolder);
    }

    /**
     * From our DatabaseObserver interface
     * Updates the data in the RecyclerView
     */
    @Override
    public void update() {
        dataset.clear();
        dataset.addAll(dh.getGroceries());
        adapter.notifyDataSetChanged();

        if (dataset.isEmpty()){
            rv.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            rv.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(GroceryItem item);
    }
}

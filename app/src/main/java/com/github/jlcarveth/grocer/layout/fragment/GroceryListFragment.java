package com.github.jlcarveth.grocer.layout.fragment;

import android.app.Fragment;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jlcarveth.grocer.R;
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

        dh = new DatabaseHandler(view.getContext());

        dataset = dh.getGroceries();

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            adapter = new GroceryListRecyclerViewAdapter(dataset,mListener,this);
            recyclerView.setAdapter(adapter);
        }

        ItemTouchHelper.Callback callback = new SimpleTouchHelperCallback(adapter);
        ith = new ItemTouchHelper(callback);
        ith.attachToRecyclerView(rv);

        DividerItemDecoration div = new DividerItemDecoration(rv.getContext(),
                DividerItemDecoration.VERTICAL);

        rv.addItemDecoration(div);

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

package com.github.jlcarveth.grocer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.jlcarveth.grocer.layout.fragment.AddGroceryDialog;
import com.github.jlcarveth.grocer.layout.fragment.AddRecipeFragment;
import com.github.jlcarveth.grocer.layout.fragment.DefaultFragment;
import com.github.jlcarveth.grocer.layout.fragment.GroceryListFragment;
import com.github.jlcarveth.grocer.layout.fragment.RecipeFragment;
import com.github.jlcarveth.grocer.model.GroceryItem;
import com.github.jlcarveth.grocer.storage.DatabaseHandler;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "GrocerMainActivity";
    private DatabaseHandler databaseHandler;

    private String currentFragmentTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = getFragmentManager().findFragmentByTag(GroceryListFragment.TAG);
                if (fragment != null && fragment.isVisible()) {
                    AddGroceryDialog agd = new AddGroceryDialog();
                    agd.show(getFragmentManager(), AddGroceryDialog.Companion.getTAG());
                }
                Log.d(TAG, "FAB Listener");
                fragment = getFragmentManager().findFragmentByTag(RecipeFragment.Companion.getTAG());
                if (fragment != null && fragment.isVisible()) {

                    AddRecipeFragment arf = new AddRecipeFragment();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.content, arf, AddRecipeFragment.Companion.getTAG())
                            .addToBackStack(null)
                            .commit();
                }

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        GroceryListFragment fragment = new GroceryListFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.content, fragment, fragment.TAG)
                .addToBackStack(null)
                .commit();

        databaseHandler = new DatabaseHandler(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_clear_all) {
            databaseHandler.clearGroceries();
        } else if (id == R.id.action_sort) {
            databaseHandler.sortGroceries();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String tag = "";
        Fragment fragment;

        switch (id) {
            case(R.id.nav_grocery):
                fragment = new GroceryListFragment();
                tag = GroceryListFragment.TAG;
                break;
            case(R.id.nav_recipe):
                fragment = new RecipeFragment();
                tag = RecipeFragment.Companion.getTAG();
                break;
            default:
                fragment = new DefaultFragment();
                tag = DefaultFragment.Companion.getTAG();
                break;
        }

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment, tag)
                .addToBackStack(null)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}

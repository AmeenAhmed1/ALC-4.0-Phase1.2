package com.ameen.travelmantics.ui;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ameen.travelmantics.R;
import com.ameen.travelmantics.adapter.RecyclerAdapter;
import com.ameen.travelmantics.util.FirebaseUtil;

public class DataActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
    }

    //init the view
    private void init() {

        FirebaseUtil.openFbReference("itemdeals", this);

        mRecyclerView = findViewById(R.id.travel_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //Setting the adapter
        mRecyclerAdapter = new RecyclerAdapter();

        //Setting the adapter to the recycler
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.insert_menu, menu);
        MenuItem insertMenu = menu.findItem(R.id.menu_insert);
        if (FirebaseUtil.isAdmin) {
            insertMenu.setVisible(true);
        } else {
            insertMenu.setVisible(false);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_insert:
                startActivity(
                        new Intent(DataActivity.this, InsertActivity.class));
                return true;

            case R.id.menu_logout:
                FirebaseUtil.logOut();
                FirebaseUtil.detachListener();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtil.detachListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
        FirebaseUtil.attachListener();
    }
}

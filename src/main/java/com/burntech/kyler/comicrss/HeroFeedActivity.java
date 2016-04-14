package com.burntech.kyler.comicrss;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class HeroFeedActivity extends Activity {

    private RecyclerView _recyclerView;
    private PublisherRecyclerAdapter _adapter;
    private RecyclerView.LayoutManager _layoutManager;

    private ArrayList<String> _publishers = new ArrayList<String>();
    private DBHelper _db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
        _db = new DBHelper(this);
        initRecyclerView();

        // Download the publishers and their respective id values.
        PublisherIdentifier pubIdentifier = PublisherIdentifier.getInstance();
        pubIdentifier.getPublishers(_db, _publishers, _adapter);
    }

    private void initRecyclerView() {
        _recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        _recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        _layoutManager = new LinearLayoutManager(this);
        _recyclerView.setLayoutManager(_layoutManager);

        // Add line dividers.
        _recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        // specify an adapter (see also next example)
        _adapter = new PublisherRecyclerAdapter(_publishers);
        _adapter.setOnItemClickListener(new PublisherRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent myIntent = new Intent(view.getContext(), ViewerActivity.class);
                myIntent.putExtra("Publisher", _publishers.get(position)); //Optional parameters
                view.getContext().startActivity(myIntent);
            }
        });
        _recyclerView.setAdapter(_adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_publishers, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }
}

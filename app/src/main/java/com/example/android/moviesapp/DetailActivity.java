package com.example.android.moviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.moviesapp.data.DataItem;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            if(MainActivity.mTwoPane){
                Bundle args = getIntent().getExtras();
                DetailActivityFragment fragment = new DetailActivityFragment();
                fragment.setArguments(args);
                DataItem dataItem = (DataItem) args.get("MOVIE");
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.detail_container_pane, fragment)
                        .commit();
            }
            else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.detail_container, new DetailActivityFragment())
                        .commit();

            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}

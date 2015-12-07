package com.oneupdog.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 *
 */
public class MainActivity extends AppCompatActivity {

    public static final String DETAILFRAGMENT_TAG = "DFTAG";

    private static final String TAG = MainActivity.class.getSimpleName();
    private String mSortby;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSortby = Utils.getCurrentSortValue(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 400) {
            Log.d(TAG, "smallestScreenWidth is greater than 600");
        } else {
            Log.d(TAG, "smallestScreenWidth is "+ config.smallestScreenWidthDp);
        }

        if (findViewById(R.id.fragment_details) != null) {
            Log.d(TAG, "dual pane layout found....");
            mTwoPane = true;
            MainActivityFragment fragment = (MainActivityFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
            if(fragment != null) {
                fragment.setDualPane(mTwoPane);
            }
            if (savedInstanceState == null) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_details_container, new DetailsActivityFragment(), DETAILFRAGMENT_TAG)
//                        .commit();
            }
        } else {
            Log.d(TAG, "dual pane layout is false...");
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
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

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume");
        String sortby = Utils.getCurrentSortValue(this);
        if (sortby != null && !sortby.equals(mSortby)) {
            Log.d(TAG, "sortby has changed");
            MainActivityFragment mf = (MainActivityFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
            if ( null != mf ) {
                mf.onSortKeyChange();
            }
            mSortby = sortby;
        }
    }
}

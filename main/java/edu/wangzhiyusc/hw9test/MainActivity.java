package edu.wangzhiyusc.hw9test;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.TabLayout;
//import android.support.v4.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.content.res.Configuration;
//import android.R;


public class MainActivity extends AppCompatActivity {
    private String[] mCongressTypeTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private ArrayAdapter<String> mAdapter;
    private FragmentLegislator mLegislator;
    private FragmentBill mBill;
    private FragmentCommittee mCommittee;
    private FragmentFavorite mFavorite;
    private FragmentAboutMe mAboutMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCongressTypeTitles = getResources().getStringArray(R.array.congress_types);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setDefaultFragment();
    }

    /** Add the Items on side bar */
    private void addDrawerItems() {
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mCongressTypeTitles);
        mDrawerList.setAdapter(mAdapter);

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String output = "You are clicking #" + position;//0 1 2 3
                //Toast.makeText(MainActivity.this, output, Toast.LENGTH_SHORT).show();
                selectItem(position);
            }
        });
    }

    /** Setup Drawer for the Toggle effect */
    private void setupDrawer() {

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setDefaultFragment()
    {
        //setToBill();
        setToLegislator();
    }

    /*private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }*/

    /** Swaps fragments in the main content view */

    private void selectItem(int position) {

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);

        // Create a new fragment and specify the planet to show based on position
        switch (position){
            case 0: setToLegislator(); break;
            case 1: setToBill(); break;
            case 2: setToCommittee(); break;
            case 3: setToFavorite(); break;
            case 4: AboutMe(); break;
            default: setToLegislator(); break;
        }

    }

    private void setToLegislator(){
        getSupportActionBar().setTitle(mCongressTypeTitles[0]);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if(mLegislator == null) mLegislator = new FragmentLegislator();
        transaction.replace(R.id.id_content_frame, mLegislator);
        transaction.commit();
    }

    private void setToBill(){
        getSupportActionBar().setTitle(mCongressTypeTitles[1]);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if(mBill == null) mBill = new FragmentBill();
        transaction.replace(R.id.id_content_frame, mBill);
        transaction.commit();
    }

    private void setToCommittee(){
        getSupportActionBar().setTitle(mCongressTypeTitles[2]);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if(mCommittee == null) mCommittee = new FragmentCommittee();
        transaction.replace(R.id.id_content_frame, mCommittee);
        transaction.commit();
    }

    private void setToFavorite(){
        getSupportActionBar().setTitle(mCongressTypeTitles[3]);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if(mFavorite == null) mFavorite = new FragmentFavorite();
        transaction.replace(R.id.id_content_frame, mFavorite);
        transaction.commit();
    }

    private void AboutMe(){
        getSupportActionBar().setTitle(mCongressTypeTitles[4]);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if(mAboutMe == null) mAboutMe = new FragmentAboutMe();
        transaction.replace(R.id.id_content_frame, mAboutMe);
        transaction.commit();
    }

    /*
    public void setTitle(String title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }*/


}

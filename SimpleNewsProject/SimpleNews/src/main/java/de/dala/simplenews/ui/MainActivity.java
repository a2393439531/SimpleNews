package de.dala.simplenews.ui;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.util.List;

import de.dala.simplenews.R;
import de.dala.simplenews.common.Entry;
import de.dala.simplenews.dialog.ChangeLogDialog;
import de.dala.simplenews.parser.XmlParser;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks{

    private static String TAG = "MainActivity";
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);
        XmlParser.Init(this);

        setContentView(R.layout.activity_main);
        setupDrawer();

        getSupportActionBar().setTitle(getString(R.string.simple_news_title));

        //opening transition animations
        overridePendingTransition(R.anim.open_translate,R.anim.close_scale);
        RateMyApp.appLaunched(this);

        if (savedInstanceState == null){
            if(getIntent().getDataString()!=null)
            {
                String path = getIntent().getDataString();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Fragment categoryModifierFrag = CategoryModifierFragment.getInstance(path);
                transaction.replace(R.id.container, categoryModifierFrag).commit();
            }else{
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Fragment newsFrag = NewsOverViewFragment.getInstance(ExpandableNewsFragment.ALL);
                transaction.replace(R.id.container, newsFrag).commit();
            }
        }
    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //closing transition animations
        overridePendingTransition(R.anim.open_scale,R.anim.close_translate);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    protected void setupDrawer(){
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    public void selectNavigationDrawerItem(int item, boolean check){
        mNavigationDrawerFragment.checkItem(item, check);
    }

    private PrefFragment prefFragment;

    @Override
    public void onNavigationDrawerItemSelected(int item) {
        if (prefFragment != null && item != NavigationDrawerFragment.CHANGELOG){
            if (Build.VERSION.SDK_INT > 11) {
                getFragmentManager().beginTransaction().remove(prefFragment).commit();
                prefFragment = null;
            }
        }
        switch (item){
            case NavigationDrawerFragment.HOME:
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, NewsOverViewFragment.getInstance(ExpandableNewsFragment.ALL)).addToBackStack(null).commit();
                break;
            case NavigationDrawerFragment.FAVORITE:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, NewsOverViewFragment.getInstance(ExpandableNewsFragment.FAV)).addToBackStack(null).commit();
                break;
            case NavigationDrawerFragment.RECENT:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, NewsOverViewFragment.getInstance(ExpandableNewsFragment.RECENT)).addToBackStack(null).commit();
                break;
            case NavigationDrawerFragment.CATEGORIES:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, CategoryModifierFragment.getInstance()).addToBackStack(null).commit();
                break;
            case NavigationDrawerFragment.SEARCH:
                break;
            case NavigationDrawerFragment.RATING:
                RateMyApp.showRateDialog(this);
                break;
            case NavigationDrawerFragment.SETTINGS:
                //if (Build.VERSION.SDK_INT < 11) {
                    startActivity(new Intent(this, PrefActivity.class));
                //} else {
                //    prefFragment = new PrefFragment();
                //    getFragmentManager().beginTransaction().replace(R.id.container, prefFragment).addToBackStack(null).commit();
                //}
                break;
            case NavigationDrawerFragment.CHANGELOG:
                DialogFragment dialog = new ChangeLogDialog();
                dialog.show(getSupportFragmentManager(), "ChangeLog");
                break;
        }
    }

    protected void changeDrawerColor(LayerDrawable ld, int newColor) {
        mNavigationDrawerFragment.changeColor(ld, newColor);
    }

    public void setNavDrawerInformation(List<Entry> favoriteEntries, List<Entry> visitedEntries){
        mNavigationDrawerFragment.setInformation(favoriteEntries, visitedEntries);
    }

}
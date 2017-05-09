/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rs.reviewer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import rs.reviewer.activities.ReviewerPreferenceActivity;
import rs.reviewer.adapters.DrawerListAdapter;
import rs.reviewer.fragments.AboutFragment;
import rs.reviewer.fragments.MyMapFragment;
import rs.reviewer.fragments.ProfileFragment;
import rs.reviewer.fragments.reviewobjects.ReviewObjectsListFragment;
import rs.reviewer.model.NavItem;
import rs.reviewer.sync.SyncReceiver;
import rs.reviewer.sync.auto.SyncService;
import rs.reviewer.tools.CurrentUser;
import rs.reviewer.tools.FragmentTransition;
import rs.reviewer.tools.ReviewerTools;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    
    //Sync stuff
    private PendingIntent pendingIntent;
	private AlarmManager manager;
	private SharedPreferences sharedPreferences;
	
	private SyncReceiver sync;
	public static String SYNC_DATA = "SYNC_DATA";
	public static String SYNC_TIME = "SYNC_TIME";
	public static String NEW_COMMENTS = "NEW_COMMENTS";
	
	private String synctime;
	private boolean allowSync;
	private String lookupRadius;
	
	private boolean allowReviewNotif;
	private boolean allowCommentedNotif;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        prepareMenu(mNavItems);
        
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerList = (ListView) findViewById(R.id.navList);
        
        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setAdapter(adapter);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setIcon(R.drawable.ic_launcher);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
            actionBar.setHomeButtonEnabled(true);
        }


//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon

        // OVO NE MORA DA SE KORISTI, UKOLIKO SE NE KORISTI
        // ONDA SE NE MENJA TEKST PRILIKOM OPEN CLOSE DRAWERA POGLEDATI JOS
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
//                getActionBar().setTitle(mTitle);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
//                getActionBar().setTitle(mDrawerTitle);
                getSupportActionBar().setTitle("iReviewer");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
//        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItemFromDrawer(0);
        }
        
        setUpReceiver();
        
    }
    
    private void setUpReceiver(){
    	sync = new SyncReceiver();
    	
    	// Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(this, SyncService.class);
        pendingIntent = PendingIntent.getService(this, 0, alarmIntent, 0);
        
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        
        consultPreferences();
    }

    private void consultPreferences(){
    	synctime = sharedPreferences.getString(getString(R.string.pref_sync_list), "1");//1min
    	allowSync = sharedPreferences.getBoolean(getString(R.string.pref_sync), false);
    	
    	lookupRadius = sharedPreferences.getString(getString(R.string.pref_radius), "1");//1km
    	
    	allowCommentedNotif = sharedPreferences.getBoolean(getString(R.string.notif_on_my_comment_key), false);
    	allowReviewNotif = sharedPreferences.getBoolean(getString(R.string.notif_on_my_review_key), false);
    	
    	//Toast.makeText(MainActivity.this, allowSync+" "+lookupRadius+" "+synctime, Toast.LENGTH_LONG).show();
    }
    
	private void setUpUserName(){
		String usernameContent = CurrentUser.getName(this);
		TextView userName = (TextView) findViewById(R.id.userName);
		userName.setText(usernameContent);
	}
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	
    	//da postavi naziv korisnika
    	setUpUserName();
    	
    	//Za slucaj da referenca nije postavljena da se izbegne problem sa androidom!
    	if (manager == null) {
    		setUpReceiver();
		}
    	
    	if(allowSync){
	    	int interval = ReviewerTools.calculateTimeTillNextSync(Integer.parseInt(synctime));
	    	manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
	        //Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    	}
    	
    	IntentFilter filter = new IntentFilter();
    	filter.addAction(NEW_COMMENTS);
    	filter.addAction(SYNC_DATA);
    	
    	registerReceiver(sync, filter);
    }
    
    private void prepareMenu(ArrayList<NavItem> mNavItems ){
    	mNavItems.add(new NavItem(getString(R.string.home), getString(R.string.home_long), R.drawable.ic_action_map));
        mNavItems.add(new NavItem(getString(R.string.places), getString(R.string.places_long), R.drawable.ic_action_place));
        mNavItems.add(new NavItem(getString(R.string.preferences), getString(R.string.preferences_long), R.drawable.ic_action_settings));
        mNavItems.add(new NavItem(getString(R.string.about), getString(R.string.about_long), R.drawable.ic_action_about));
        mNavItems.add(new NavItem(getString(R.string.sync_data), getString(R.string.sync_data_long), R.drawable.ic_action_refresh));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	// If the nav drawer is open, hide action items related to the content view
    	boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerPane);
    	menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
    	return super.onPrepareOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        /*switch(item.getItemId()) {
        case R.id.action_websearch:
            // create intent to perform web search for this planet
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }*/
        return super.onOptionsItemSelected(item);
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	selectItemFromDrawer(position);
        }
    }
    
    
    private void selectItemFromDrawer(int position) {
    	if(position == 0){
    		FragmentTransition.to(MyMapFragment.newInstance(), this, false);
        }else if(position == 1){
        	FragmentTransition.to(new ReviewObjectsListFragment(), this);
        }else if(position == 2){
        	Intent preference = new Intent(MainActivity.this,ReviewerPreferenceActivity.class);
        	startActivity(preference);
        }else if(position == 3){
        	FragmentTransition.to(new AboutFragment(), this);
        }else if(position == 4){
        	startService(new Intent(this, SyncService.class));
        }else{
        	Log.e("DRAWER", "Nesto van opsega!");
        }
        
        mDrawerList.setItemChecked(position, true);
        if(position != 5) // za sve osim za sync
        {
        	setTitle(mNavItems.get(position).getmTitle());
            /*getSupportActionBar().setTitle(mNavItems.get(position).getmTitle());
            mTitle = ;*/
        }
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void getProfile(View view){
    	//Toast.makeText(this, "User", Toast.LENGTH_LONG).show();
    	FragmentTransition.to(new ProfileFragment(), this);
    	mDrawerLayout.closeDrawer(mDrawerPane);
    }
    
    @Override
    protected void onPause() {
    	if (manager != null) {
			manager.cancel(pendingIntent);
	        //Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
		}

    	if(sync != null){
    		unregisterReceiver(sync);
    	}
    	
    	super.onPause();
    	
    }
}

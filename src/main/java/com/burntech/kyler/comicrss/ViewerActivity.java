package com.burntech.kyler.comicrss;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

/*
 * Created by Kyler J. Burnett on 3/25/2015.
 */
public class ViewerActivity extends FragmentActivity {
    SmartFragmentStatePagerAdapter adapterViewPager;

    // List of queried comics from the RSS Feed.
    private ArrayList<Comic> _comics = new ArrayList<Comic>();
    String _week;

    //private String theURL = "http://www.previewsworld.com/shipping/upcomingreleases.txt";
    //private String[] urls = {theURL};

    /*Deprecated!*/
    // URL Constants for specific publisher new-releases for testing purposes.
    //private final String DC_URL = "http://www.comicvine.com/new-comics/?company=10&startWeek=03%2F29%2F2015";
    //private final String MARVEL_URL = "http://www.comicvine.com/new-comics/?company=31&startWeek=03%2F29%2F2015";

    private RecyclerView _recyclerView;
    private ComicRecyclerAdapter _adapter;
    private RecyclerView.LayoutManager _layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comic_recycler_view);

        actionBarChanger();

        startup();

        // Create the image carousel.
        //initViewPager();

        initRecyclerView();
    }

    private void startup() {
        _week = Util.getMostRecentSunday();
        String publisher = getPublisher();
        setTitle(publisher);

        loadComicsFromPublisher(publisher);
    }

    private void loadComicsFromPublisher(String publisher) {
        ComicDownloader downloader = new ComicDownloader(getApplicationContext());
        DBHelper db = new DBHelper(this);

        // Downloads the necessary information to the SQLite database.
        _comics = db.getComics(publisher, _week);
        if (_comics.size() == 0) {
            System.out.println("No comics in database for '" + publisher + "'. Downloading!...");
            downloader.downloadComics(publisher, _week);
            _comics = db.getComics(publisher, _week);
            if (_comics.size() == 0) { // If size=0 again, then the publisher hasn't released anything yet. digress a week.
                System.out.println("No comics online for current week, digressing a week, checking database...");
                _week = Util.getSecondToLastSunday();
                _comics = db.getComics(publisher, _week);
                if (_comics.size() == 0) {
                    System.out.println("No comics from current week in database, grabbing last week's..." + _week);
                    downloader.downloadComics(publisher, _week);
                    _comics = db.getComics(publisher, _week);
                }
            }
        }
        System.out.println("comics size: " + _comics.size());
        setTitle(publisher + " " + _week); // adds the pub+week.
    }

    private void actionBarChanger() {
        ActionBar actionBar = getActionBar();
        String publisher = getPublisher();

        if (publisher.equals("Marvel"))
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.RED));
        else if (publisher.equals("DC Comics"))
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
        else if (publisher.equals("Dark Horse Comics"))
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    private String getPublisher() {
        Intent intent = getIntent();
        String publisher = intent.getStringExtra("Publisher");
        return publisher;
    }

    private void initRecyclerView() {
        _recyclerView = (RecyclerView) findViewById(R.id.comic_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        _recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        _layoutManager = new LinearLayoutManager(this);
        _recyclerView.setLayoutManager(_layoutManager);

        // Add line dividers.
        _recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        // specify an adapter (see also next example)
        _adapter = new ComicRecyclerAdapter(_comics);
        _recyclerView.setAdapter(_adapter);
    }

    private void initViewPager() {
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        // Customized view pager settings.
        vpPager.setOffscreenPageLimit(5);
        //vpPager.setClipToPadding(false);
        //vpPager.setPageMargin(12);

        // Creates the unique "lag" effect on image slide.
        vpPager.setPageTransformer(true, new ParallaxPageTransformer());

        vpPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                //Toast.makeText(MainActivity.this,
                //        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}// end MainActivity

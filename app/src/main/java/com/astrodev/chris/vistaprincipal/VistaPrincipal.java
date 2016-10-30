package com.astrodev.chris.vistaprincipal;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.astrodev.chris.vistaprincipal.principalFragments.FragmentCinco;
import com.astrodev.chris.vistaprincipal.principalFragments.FragmentCuatro;
import com.astrodev.chris.vistaprincipal.principalFragments.FragmentDos;
import com.astrodev.chris.vistaprincipal.principalFragments.FragmentTres;
import com.astrodev.chris.vistaprincipal.principalFragments.FragmentUno;

import java.util.ArrayList;
import java.util.List;

public class VistaPrincipal extends AppCompatActivity {

    public CollapsingToolbarLayout collapsingToolbarLayout;
    public Toolbar toolbar;
    public ViewPager viewPager;
    public TabLayout tablayout;

    private DrawerLayout drawerLayout;


    public int iconos[] = {
            R.mipmap.ic_local_cafe_white_24dp,
            R.mipmap.ic_restaurant_white_24dp,
            R.mipmap.ic_favorite_white_24dp,
            R.mipmap.ic_location_on_white_24dp,
            R.mipmap.ic_call_white_24dp};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_principal);

        ponerToolbar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_colapsing);

        viewPager = (ViewPager) findViewById(R.id.viewPager_principal);
        ponViewPager(viewPager);

        tablayout = (TabLayout) findViewById(R.id.tablayout_principal);
        tablayout.setupWithViewPager(viewPager);

        iconosTabs();

        collapsingToolbarLayout.setTitleEnabled(true);
        collapsingToolbarLayout.setTitle("Fragment 1");

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                    switch (tab.getPosition()) {
                        case 0:
                            collapsingToolbarLayout.setTitle("Fragment 1");

                            break;
                        case 1:
                            collapsingToolbarLayout.setTitle("Fragment 2");

                            break;
                        case 2:
                            collapsingToolbarLayout.setTitle("Fragment 3");

                            break;
                        case 3:
                            collapsingToolbarLayout.setTitle("Fragment 4");

                            break;
                        case 4:
                            collapsingToolbarLayout.setTitle("Fragment 5");
                            break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void ponerToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }



    class adaptadorViewPager extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public adaptadorViewPager(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }


        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //return mFragmentTitleList.get(position);
            return null;
        }

        public void addFragment(Fragment fragment, String titulo) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(titulo);

        }
    }

    private void ponViewPager(ViewPager viewPager) {
        adaptadorViewPager adaptador = new adaptadorViewPager(getSupportFragmentManager());
        adaptador.addFragment(new FragmentUno(), "Fragment 1");
        adaptador.addFragment(new FragmentDos(), "Fragment 2");
        adaptador.addFragment(new FragmentTres(), "Fragment 3");
        adaptador.addFragment(new FragmentCuatro(), "Fragment 4");
        adaptador.addFragment(new FragmentCinco(), "Fragment 5");
        viewPager.setAdapter(adaptador);

    }

    public void iconosTabs() {
        tablayout.getTabAt(0).setIcon(iconos[0]);
        tablayout.getTabAt(1).setIcon(iconos[1]);
        tablayout.getTabAt(2).setIcon(iconos[2]);
        tablayout.getTabAt(3).setIcon(iconos[3]);
        tablayout.getTabAt(4).setIcon(iconos[4]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vista_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);
    }

}

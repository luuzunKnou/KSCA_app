package com.luuzun.ksca;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.luuzun.ksca.domain.Permission;
import com.luuzun.ksca.fragment.AgencyFragment;
import com.luuzun.ksca.fragment.BranchFragment;
import com.luuzun.ksca.fragment.ProgramFragment;
import com.luuzun.ksca.fragment.SccFragment;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;

    BranchFragment branchFragment;
    AgencyFragment agencyFragment;
    ProgramFragment programFragment;
    SccFragment sccFragment;

    TextView mNavTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View nav_header_view = navigationView.getHeaderView(0);

        /*  */
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Permission permission = (Permission) intent.getSerializableExtra("permission");
        String areaCode = intent.getStringExtra("areaCode");
        String areaName = intent.getStringExtra("areaName");

        mNavTitleTextView = nav_header_view.findViewById(R.id.nav_title);
        mNavTitleTextView.setText("사단법인 대한노인회 ("+areaName+")");

        /*  */
        Bundle bundle = new Bundle();
        bundle.putString("areaCode",areaCode);
        bundle.putString("areaName",areaName);

        branchFragment = new BranchFragment();
        agencyFragment = new AgencyFragment();
        programFragment = new ProgramFragment();
        sccFragment = new SccFragment();

        branchFragment.setArguments(bundle);
        agencyFragment.setArguments(bundle);
        programFragment.setArguments(bundle);
        sccFragment.setArguments(bundle);

        /*  */
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container, branchFragment).commit();
        getSupportActionBar().setTitle("분회 관리");
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
        getMenuInflater().inflate(R.menu.menu, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_branch) {
            fragmentManager.beginTransaction().replace(R.id.container, branchFragment).commit();
            getSupportActionBar().setTitle("분회");
        } else if (id == R.id.nav_scc) {
            fragmentManager.beginTransaction().replace(R.id.container, sccFragment).commit();
            getSupportActionBar().setTitle("경로당");
        } else if (id == R.id.nav_agency) {
            fragmentManager.beginTransaction().replace(R.id.container, agencyFragment).commit();
            getSupportActionBar().setTitle("기관");
        } else if (id == R.id.nav_program) {
            fragmentManager.beginTransaction().replace(R.id.container, programFragment).commit();
            getSupportActionBar().setTitle("프로그램");
        } else if (id == R.id.nav_schedule) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

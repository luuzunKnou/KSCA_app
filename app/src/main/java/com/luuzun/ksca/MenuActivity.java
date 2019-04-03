package com.luuzun.ksca;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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
import com.luuzun.ksca.fragment.ScheduleFragment;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;

    BranchFragment branchFragment;
    AgencyFragment agencyFragment;
    ProgramFragment programFragment;
    SccFragment sccFragment;
    ScheduleFragment scheduleFragment;

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
        scheduleFragment = new ScheduleFragment();

        branchFragment.setArguments(bundle);
        agencyFragment.setArguments(bundle);
        programFragment.setArguments(bundle);
        sccFragment.setArguments(bundle);
        scheduleFragment.setArguments(bundle);

        /*  */
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container, scheduleFragment).commit();
        getSupportActionBar().setTitle("스케줄");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
            alert_confirm.setMessage("종료하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.finishAffinity(MenuActivity.this);
                        }
                    }).setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
            AlertDialog alert = alert_confirm.create();
            alert.show();
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
        if (id == R.id.action_log_out) {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
            alert_confirm.setMessage("로그아웃 하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        }
                    }).setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            return;
                        }
                    });
            AlertDialog alert = alert_confirm.create();
            alert.show();

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
            fragmentManager.beginTransaction().replace(R.id.container, scheduleFragment).commit();
            getSupportActionBar().setTitle("스케줄");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

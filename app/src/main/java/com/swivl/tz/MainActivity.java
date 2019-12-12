package com.swivl.tz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.swivl.tz.ui.OnChageActionBarTitle;
import com.swivl.tz.ui.list.UserListFragment;

public class MainActivity extends AppCompatActivity implements  OnChageActionBarTitle{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                if(getSupportFragmentManager().getBackStackEntryCount() > 0){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, UserListFragment.newInstance())
                    .commitNow();
        } else if(getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onChangeActionBarTitle(String title) {
        setActionBarTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}

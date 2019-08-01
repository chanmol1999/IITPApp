package com.grobo.notifications.services.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.grobo.notifications.R;
import com.grobo.notifications.utils.ImageViewerActivity;

public class LostAndFoundActivity extends AppCompatActivity implements LostAndFoundRecyclerAdapter.OnLostFoundSelectedListener {

    FloatingActionButton fab;
    FragmentManager manager;
    Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found);

        getSupportActionBar().setTitle("Lost and Found");

        manager = getSupportFragmentManager();

        fab = findViewById(R.id.new_lost_found_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment current = manager.findFragmentById(R.id.frame_lost_found);
                Fragment next = new NewLostAndFound();
                showFragmentWithTransition(current, next);
            }
        });

        setBaseFragment(savedInstanceState);
    }

    private void setBaseFragment(Bundle savedInstanceState) {
        if (findViewById(R.id.frame_lost_found) != null) {

            if (savedInstanceState != null) {
                return;
            }

            LostAndFoundFragment firstFragment = new LostAndFoundFragment();
            firstFragment.setArguments(getIntent().getExtras());
            manager.beginTransaction()
                    .add(R.id.frame_lost_found, firstFragment).commit();
        }
    }

    private void showFragmentWithTransition(Fragment current, Fragment newFragment) {
        current.setExitTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.no_transition));
        newFragment.setEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.slide_bottom));

        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_lost_found, newFragment);
        fragmentTransaction.addToBackStack("later_fragment");
        fragmentTransaction.commit();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        activeFragment = fragment;
        if (fragment instanceof NewLostAndFound) {
            fab.hide();
            activeFragment = fragment;
        }
    }

    @Override
    public void onBackPressed() {
        if (activeFragment instanceof NewLostAndFound) {
            if (fab.isOrWillBeHidden()) fab.show();
        }
        super.onBackPressed();
    }

    @Override
    public void onLostFoundSelected(String image) {
        Intent i = new Intent(LostAndFoundActivity.this, ImageViewerActivity.class);
        i.putExtra("image_url", image);
        startActivity(i);
    }
}
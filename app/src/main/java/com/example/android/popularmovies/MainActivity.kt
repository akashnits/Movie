package com.example.android.popularmovies

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager.OnBackStackChangedListener

import com.example.android.popularmovies.fragments.MoviesFragment

class MainActivity : AppCompatActivity(), OnBackStackChangedListener{

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    getSupportFragmentManager().addOnBackStackChangedListener(this);
    shouldDisplayHomeUp();

    if(savedInstanceState == null){
      supportFragmentManager
          .beginTransaction()
          .replace(R.id.container, MoviesFragment.newInstance(), "Movies")
          .commit()
    }
  }

  override fun onBackStackChanged() {
    shouldDisplayHomeUp()
  }

  fun shouldDisplayHomeUp() {
    val canback = supportFragmentManager.backStackEntryCount > 0
    supportActionBar!!.setDisplayHomeAsUpEnabled(canback)
  }

  override fun onSupportNavigateUp(): Boolean {
    supportFragmentManager.popBackStack()
    return true
  }
}

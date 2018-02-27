package io.github.izzyleung.zhihudailypurify.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import io.github.izzyleung.zhihudailypurify.R;

public class MainActivity extends BasicFeedActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    LayoutInflater inflater = LayoutInflater.from(this);
    FloatingActionButton fab = (FloatingActionButton) inflater
        .inflate(R.layout.floating_action_button, coordinatorLayout, false);
    fab.setOnClickListener(v -> prepareIntent(PickDateActivity.class));

    coordinatorLayout.addView(fab);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int itemId = item.getItemId();

    if (itemId == R.id.action_settings) {
      return prepareIntent(PreferenceActivity.class);
    } else if (itemId == R.id.action_go_to_search) {
      return prepareIntent(SearchActivity.class);
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  private boolean prepareIntent(Class clazz) {
    startActivity(new Intent(MainActivity.this, clazz));
    return true;
  }
}

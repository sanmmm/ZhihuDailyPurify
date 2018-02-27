package io.github.izzyleung.zhihudailypurify.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import io.github.izzyleung.zhihudailypurify.R;

public abstract class BaseActivity extends AppCompatActivity {

  protected CoordinatorLayout coordinatorLayout;
  protected Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(layoutResId());

    toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    coordinatorLayout = findViewById(R.id.coordinator_layout);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  public void showSnackbar(int resId) {
    Snackbar.make(coordinatorLayout, resId, Snackbar.LENGTH_SHORT).show();
  }

  protected abstract int layoutResId();
}

package io.github.izzyleung.zhihudailypurify.ui.activity;

import android.os.Bundle;

import io.github.izzyleung.zhihudailypurify.R;
import io.github.izzyleung.zhihudailypurify.ui.fragment.PrefsFragment;

public class PreferenceActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getFragmentManager()
        .beginTransaction()
        .replace(R.id.fragment_frame, new PrefsFragment())
        .commit();
  }

  @Override
  protected int layoutResId() {
    return R.layout.activity_preference;
  }
}
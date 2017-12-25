package io.github.izzyleung.zhihudailypurify.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import java.io.IOException;

import io.github.izzyleung.MySearchView;
import io.github.izzyleung.ZhihuDailyPurify;
import io.github.izzyleung.ZhihuDailyPurifyServer;
import io.github.izzyleung.zhihudailypurify.R;
import io.github.izzyleung.zhihudailypurify.ui.fragment.SearchFragment;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends BaseActivity implements SingleObserver<ZhihuDailyPurify.Feed> {
    private MySearchView searchView;
    private SearchFragment searchFragment;

    @SuppressWarnings("deprecation")
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initDialog();

        searchFragment = new SearchFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_frame, searchFragment)
                .commit();
    }

    @Override
    public void onDestroy() {
        searchFragment = null;

        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView() {
        searchView = new MySearchView(this);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(query -> {
            dialog.show();
            searchView.clearFocus();
            try {
                ZhihuDailyPurifyServer.searchForKeyword(query)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this);
            } catch (IOException e) {
                onError(e);
            }
            return true;
        });

        RelativeLayout relative = new RelativeLayout(this);
        relative.addView(searchView);

        mToolBar.addView(relative);

        setSupportActionBar(mToolBar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initDialog() {
        //noinspection deprecation
        dialog = new ProgressDialog(SearchActivity.this);
        dialog.setMessage(getString(R.string.searching));
        dialog.setCancelable(true);
    }

    @Override
    public void onSubscribe(Disposable disposable) {
        dialog.show();
    }

    @Override
    public void onSuccess(ZhihuDailyPurify.Feed feed) {
        dialog.dismiss();
        searchFragment.updateContent(feed);
    }

    @Override
    public void onError(Throwable e) {
        dialog.dismiss();
        showSnackbar(R.string.no_result_found);
    }
}

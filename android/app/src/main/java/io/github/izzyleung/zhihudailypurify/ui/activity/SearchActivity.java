package io.github.izzyleung.zhihudailypurify.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.eowise.recyclerview.stickyheaders.StickyHeadersBuilder;
import com.eowise.recyclerview.stickyheaders.StickyHeadersItemDecoration;

import java.util.Optional;

import io.github.izzyleung.MySearchView;
import io.github.izzyleung.ZhihuDailyPurify;
import io.github.izzyleung.ZhihuDailyPurifyServer;
import io.github.izzyleung.zhihudailypurify.R;
import io.github.izzyleung.zhihudailypurify.ui.adapter.DateHeaderAdapter;
import io.github.izzyleung.zhihudailypurify.ui.adapter.NewsAdapter;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends BaseActivity implements SingleObserver<ZhihuDailyPurify.Feed> {

  private MySearchView searchView;
  private NewsAdapter adapter;
  private DateHeaderAdapter headerAdapter;

  @SuppressWarnings("deprecation")
  private ProgressDialog dialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    initView();
    initDialog();
  }

  @Override
  protected int layoutResId() {
    return R.layout.activity_search;
  }

  private void initView() {
    searchView = new MySearchView(this);
    searchView.setQueryHint(getResources().getString(R.string.search_hint));
    searchView.setOnQueryTextListener(query -> {
      dialog.show();
      searchView.clearFocus();
      Single.defer(() -> ZhihuDailyPurifyServer.search(query))
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(this);
      return true;
    });

    RelativeLayout relative = new RelativeLayout(this);
    relative.addView(searchView);

    toolbar.addView(relative);

    setSupportActionBar(toolbar);
    Optional.ofNullable(getSupportActionBar())
        .ifPresent(ab -> ab.setDisplayHomeAsUpEnabled(true));

    RecyclerView recyclerView = findViewById(R.id.search_result_list);
    recyclerView.setHasFixedSize(true);
    LinearLayoutManager llm = new LinearLayoutManager(this);

    llm.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(llm);
    adapter = new NewsAdapter();
    headerAdapter = new DateHeaderAdapter();

    StickyHeadersItemDecoration header = new StickyHeadersBuilder()
        .setAdapter(adapter)
        .setRecyclerView(recyclerView)
        .setStickyHeadersAdapter(headerAdapter)
        .build();

    recyclerView.setAdapter(adapter);
    recyclerView.addItemDecoration(header);
  }

  private void initDialog() {
    //noinspection deprecation
    dialog = new ProgressDialog(this);
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

    adapter.updateFeed(feed);
    headerAdapter.updateFeed(feed);

    if (feed.getNewsCount() == 0) {
      showSnackbar(R.string.no_result_found);
    }
  }

  @Override
  public void onError(Throwable e) {
    dialog.dismiss();

    showSnackbar(R.string.network_error);
  }
}

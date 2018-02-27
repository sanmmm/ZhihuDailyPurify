package io.github.izzyleung.zhihudailypurify.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Optional;

import io.github.izzyleung.ZhihuDailyOfficial;
import io.github.izzyleung.ZhihuDailyPurify;
import io.github.izzyleung.ZhihuDailyPurifyServer;
import io.github.izzyleung.zhihudailypurify.R;
import io.github.izzyleung.zhihudailypurify.ZhihuDailyPurifyApplication;
import io.github.izzyleung.zhihudailypurify.db.Cached;
import io.github.izzyleung.zhihudailypurify.db.SaveToDBTask;
import io.github.izzyleung.zhihudailypurify.support.Constants;
import io.github.izzyleung.zhihudailypurify.support.LocalDate;
import io.github.izzyleung.zhihudailypurify.ui.adapter.NewsAdapter;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BasicFeedActivity extends BaseActivity
    implements SwipeRefreshLayout.OnRefreshListener, SingleObserver<ZhihuDailyPurify.Feed> {

  protected String date = LocalDate.now().plusDays(1).format();
  private boolean isToday = true;
  private boolean hasRefreshed = false;

  private NewsAdapter adapter;
  private SwipeRefreshLayout swipeRefreshLayout;

  // region Lifecycle

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    initView();

    Optional.ofNullable(getIntent().getExtras())
        .map(bundle -> bundle.getString(Constants.BundleKeys.DATE))
        .ifPresent(d -> {
          date = d;
          isToday = LocalDate.isToday(date);
        });
  }

  @Override
  protected void onStart() {
    super.onStart();

    Cached.of(date).feed()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this);

    if (!hasRefreshed && shouldAutoRefresh()) {
      refresh();
    }
  }

  // endregion

  // region BaseActivity

  @Override
  protected int layoutResId() {
    return R.layout.activity_feed;
  }

  // endregion

  // region Helper

  private void initView() {
    RecyclerView mRecyclerView = findViewById(R.id.feed);
    mRecyclerView.setHasFixedSize(!isToday);

    LinearLayoutManager llm = new LinearLayoutManager(this);
    llm.setOrientation(LinearLayoutManager.VERTICAL);
    mRecyclerView.setLayoutManager(llm);

    adapter = new NewsAdapter();
    mRecyclerView.setAdapter(adapter);

    swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
    swipeRefreshLayout.setOnRefreshListener(this);
    swipeRefreshLayout.setColorSchemeResources(R.color.color_primary);
  }

  private void refresh() {
    swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));

    feedSource()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this);
  }

  private boolean shouldUseAccelerateServer() {
    return ZhihuDailyPurifyApplication.getSharedPreferences()
        .getBoolean(Constants.SharedPreferencesKeys.SHOULD_USE_ACCELERATE_SERVER, false);
  }

  private boolean shouldAutoRefresh() {
    return ZhihuDailyPurifyApplication.getSharedPreferences()
        .getBoolean(Constants.SharedPreferencesKeys.SHOULD_AUTO_REFRESH, true);
  }

  private Single<ZhihuDailyPurify.Feed> feedSource() {
    if (isToday || !shouldUseAccelerateServer()) {
      return Single.defer(() -> ZhihuDailyOfficial.of(date).feed());
    } else {
      return Single.defer(() -> ZhihuDailyPurifyServer.of(date).feed());
    }
  }

  // endregion

  // region SwipeRefreshLayout.OnRefreshListener

  @Override
  public void onRefresh() {
    refresh();
  }

  // endregion

  // region SingleObserver

  @Override
  public void onSubscribe(Disposable disposable) {

  }

  @Override
  public void onSuccess(ZhihuDailyPurify.Feed feed) {
    hasRefreshed = true;

    swipeRefreshLayout.setRefreshing(false);
    adapter.updateFeed(feed);

    new SaveToDBTask(feed).execute();
  }

  @Override
  public void onError(Throwable e) {
    swipeRefreshLayout.setRefreshing(false);

    showSnackbar(R.string.network_error);
  }

  // endregion
}

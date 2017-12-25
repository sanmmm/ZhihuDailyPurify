package io.github.izzyleung.zhihudailypurify.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import io.github.izzyleung.ZhihuDailyOfficial;
import io.github.izzyleung.ZhihuDailyPurify;
import io.github.izzyleung.ZhihuDailyPurifyServer;
import io.github.izzyleung.zhihudailypurify.R;
import io.github.izzyleung.zhihudailypurify.ZhihuDailyPurifyApplication;
import io.github.izzyleung.zhihudailypurify.adapter.NewsAdapter;
import io.github.izzyleung.zhihudailypurify.db.FromDB;
import io.github.izzyleung.zhihudailypurify.support.Constants;
import io.github.izzyleung.zhihudailypurify.task.SaveFeedTask;
import io.github.izzyleung.zhihudailypurify.ui.activity.BaseActivity;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FeedFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, SingleObserver<ZhihuDailyPurify.Feed> {
    private ZhihuDailyPurify.Feed feed = ZhihuDailyPurify.Feed.getDefaultInstance();

    private String date;
    private NewsAdapter mAdapter;

    // Fragment is single in SingleDayNewsActivity
    private boolean isToday;
    private boolean isRefreshed = false;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle bundle = getArguments();
            assert bundle != null;
            date = bundle.getString(Constants.BundleKeys.DATE);
            isToday = bundle.getBoolean(Constants.BundleKeys.IS_FIRST_PAGE);

            setRetainInstance(true);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        assert view != null;
        RecyclerView mRecyclerView = view.findViewById(R.id.news_list);
        mRecyclerView.setHasFixedSize(!isToday);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mAdapter = new NewsAdapter(feed);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_primary);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        FromDB.feedForDate(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        refreshIf(shouldRefreshOnVisibilityChange(isVisibleToUser));
    }

    private void refreshIf(boolean prerequisite) {
        if (prerequisite) {
            doRefresh();
        }
    }

    private void doRefresh() {
        try {
            getFeedFlowable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this);
        } catch (IOException e) {
            onError(e);
            return;
        }

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    private Single<ZhihuDailyPurify.Feed> getFeedFlowable() throws IOException {
        Single<ZhihuDailyPurify.Feed> candidate;

        if (shouldSubscribeToZhihu()) {
            candidate = Single.defer(() -> ZhihuDailyOfficial.feedForDate(date));
        } else {
            candidate = Single.defer(() -> ZhihuDailyPurifyServer.feedForDate(date));
        }

        return candidate;
    }

    private boolean shouldSubscribeToZhihu() {
        return isToday || !shouldUseAccelerateServer();
    }

    private boolean shouldUseAccelerateServer() {
        return ZhihuDailyPurifyApplication.getSharedPreferences()
                .getBoolean(Constants.SharedPreferencesKeys.KEY_SHOULD_USE_ACCELERATE_SERVER, false);
    }

    private boolean shouldAutoRefresh() {
        return ZhihuDailyPurifyApplication.getSharedPreferences()
                .getBoolean(Constants.SharedPreferencesKeys.KEY_SHOULD_AUTO_REFRESH, true);
    }

    private boolean shouldRefreshOnVisibilityChange(boolean isVisibleToUser) {
        return isVisibleToUser && shouldAutoRefresh() && !isRefreshed;
    }

    @Override
    public void onRefresh() {
        doRefresh();
    }

    @Override
    public void onSubscribe(Disposable disposable) {

    }

    @Override
    public void onSuccess(ZhihuDailyPurify.Feed feed) {
        this.feed = feed;

        isRefreshed = true;

        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.updateFeed(feed);

        new SaveFeedTask(feed).execute();
    }

    @Override
    public void onError(Throwable e) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (isAdded()) {
            BaseActivity baseActivity = ((BaseActivity) getActivity());

            if (baseActivity != null) {
                baseActivity.showSnackbar(R.string.network_error);
            }
        }
    }
}
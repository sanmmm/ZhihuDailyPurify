package io.github.izzyleung.zhihudailypurify.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eowise.recyclerview.stickyheaders.StickyHeadersBuilder;
import com.eowise.recyclerview.stickyheaders.StickyHeadersItemDecoration;

import io.github.izzyleung.ZhihuDailyPurify;
import io.github.izzyleung.zhihudailypurify.R;
import io.github.izzyleung.zhihudailypurify.adapter.DateHeaderAdapter;
import io.github.izzyleung.zhihudailypurify.adapter.NewsAdapter;

public class SearchFragment extends Fragment {
    private ZhihuDailyPurify.Feed feed = ZhihuDailyPurify.Feed.getDefaultInstance();

    private NewsAdapter mAdapter;
    private DateHeaderAdapter mHeaderAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        assert view != null;

        RecyclerView mRecyclerView = view.findViewById(R.id.search_result_list);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mAdapter = new NewsAdapter(feed);
        mHeaderAdapter = new DateHeaderAdapter(feed);

        StickyHeadersItemDecoration header = new StickyHeadersBuilder()
                .setAdapter(mAdapter)
                .setRecyclerView(mRecyclerView)
                .setStickyHeadersAdapter(mHeaderAdapter)
                .build();

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(header);

        return view;
    }

    public void updateContent(ZhihuDailyPurify.Feed feed) {
        mHeaderAdapter.setFeed(feed);
        mAdapter.updateFeed(feed);
    }
}

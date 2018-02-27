package io.github.izzyleung.zhihudailypurify.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.StickyHeadersAdapter;

import java.text.DateFormat;
import java.util.Date;

import io.github.izzyleung.ZhihuDailyPurify;
import io.github.izzyleung.zhihudailypurify.R;
import io.github.izzyleung.zhihudailypurify.support.LocalDate;

public class DateHeaderAdapter implements StickyHeadersAdapter<DateHeaderAdapter.HeaderViewHolder> {

  private ZhihuDailyPurify.Feed feed = ZhihuDailyPurify.Feed.getDefaultInstance();
  private DateFormat dateFormat = DateFormat.getDateInstance();

  public void updateFeed(ZhihuDailyPurify.Feed feed) {
    this.feed = feed;
  }

  @Override
  public HeaderViewHolder onCreateViewHolder(ViewGroup parent) {
    Context context = parent.getContext();
    View itemView = LayoutInflater.from(context)
        .inflate(R.layout.date_sticky_header, parent, false);

    return new HeaderViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(HeaderViewHolder viewHolder, int position) {
    String dateFromNews = feed.getNews(position).getDate();
    Date date = LocalDate.of(dateFromNews).plusDays(-1).getTime();

    viewHolder.title.setText(dateFormat.format(date));
  }

  @Override
  public long getHeaderId(int position) {
    return feed.getNews(position).getDate().hashCode();
  }

  static class HeaderViewHolder extends RecyclerView.ViewHolder {

    TextView title;

    HeaderViewHolder(View itemView) {
      super(itemView);

      title = itemView.findViewById(R.id.date_text);
    }
  }
}

package io.github.izzyleung.zhihudailypurify.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.StickyHeadersAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import io.github.izzyleung.zhihudailypurify.R;
import io.github.izzyleung.zhihudailypurify.bean.DailyNews;
import io.github.izzyleung.zhihudailypurify.support.Constants;

public class DateHeaderAdapter implements StickyHeadersAdapter<DateHeaderAdapter.HeaderViewHolder> {
    private List<DailyNews> newsList;

    private DateFormat dateFormat = DateFormat.getDateInstance();

    public DateHeaderAdapter(List<DailyNews> newsList) {
        this.newsList = newsList;
    }

    public void setNewsList(List<DailyNews> newsList) {
        this.newsList = newsList;
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
        Calendar calendar = Calendar.getInstance();

        try {
            calendar.setTime(Constants.Dates.simpleDateFormat.parse(newsList.get(position).getDate()));
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        } catch (ParseException ignored) {

        }

        viewHolder.title.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    public long getHeaderId(int position) {
        return newsList.get(position).getDate().hashCode();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.date_text);
        }
    }
}

package io.github.izzyleung.zhihudailypurify.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import io.github.izzyleung.ZhihuDailyPurify;
import io.github.izzyleung.zhihudailypurify.R;
import io.github.izzyleung.zhihudailypurify.ZhihuDailyPurifyApplication;
import io.github.izzyleung.zhihudailypurify.support.Check;
import io.github.izzyleung.zhihudailypurify.support.Constants;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.CardViewHolder> {

  private ZhihuDailyPurify.Feed feed = ZhihuDailyPurify.Feed.getDefaultInstance();

  private ImageLoader imageLoader = ImageLoader.getInstance();
  private DisplayImageOptions options = new DisplayImageOptions.Builder()
      .showImageOnLoading(R.drawable.noimage)
      .showImageOnFail(R.drawable.noimage)
      .showImageForEmptyUri(R.drawable.lks_for_blank_url)
      .cacheInMemory(true)
      .cacheOnDisk(true)
      .considerExifParams(true)
      .build();
  private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

  public NewsAdapter() {
    setHasStableIds(true);
  }

  public void updateFeed(ZhihuDailyPurify.Feed feed) {
    if (!feed.equals(ZhihuDailyPurify.Feed.getDefaultInstance()) && !this.feed.equals(feed)) {
      this.feed = feed;
      notifyDataSetChanged();
    }
  }

  @Override
  public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final Context context = parent.getContext();

    View itemView = LayoutInflater
        .from(context)
        .inflate(R.layout.news_list_item, parent, false);

    return new CardViewHolder(itemView, new CardViewHolder.ClickResponseListener() {
      @Override
      public void onWholeClick(int position) {
        browse(context, position);
      }

      @Override
      public void onOverflowClick(View v, int position) {
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.contextual_news_list, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
          if (item.getItemId() == R.id.action_share_url) {
            share(context, position);
          }

          return true;
        });

        popup.show();
      }
    });
  }

  @Override
  public void onBindViewHolder(CardViewHolder holder, int position) {
    ZhihuDailyPurify.News news = feed.getNews(position);
    imageLoader
        .displayImage(news.getThumbnailUrl(), holder.newsImage, options, animateFirstListener);

    if (news.getQuestionsList().size() > 1) {
      holder.questionTitle.setText(news.getTitle());
      holder.dailyTitle.setText(MULTIPLE_DISCUSSION);
    } else {
      holder.questionTitle.setText(news.getQuestionsList().get(0).getTitle());
      holder.dailyTitle.setText(news.getTitle());
    }
  }

  @Override
  public int getItemCount() {
    return feed.getNewsCount();
  }

  @Override
  public long getItemId(int position) {
    return feed.getNews(position).hashCode();
  }

  private void browse(Context context, int position) {
    ZhihuDailyPurify.News news = feed.getNews(position);

    if (news.getQuestionsList().size() > 1) {
      AlertDialog dialog = createDialog(context, news,
          makeGoToZhihuDialogClickListener(context, news));
      dialog.show();
    } else {
      goToZhihu(context, news.getQuestionsList().get(0).getUrl());
    }
  }

  private void share(Context context, int position) {
    ZhihuDailyPurify.News news = feed.getNews(position);

    if (news.getQuestionsList().size() > 1) {
      AlertDialog dialog = createDialog(context, news,
          makeShareQuestionDialogClickListener(context, news));
      dialog.show();
    } else {
      shareQuestion(context,
          news.getQuestionsList().get(0).getTitle(),
          news.getQuestionsList().get(0).getUrl());
    }
  }

  private AlertDialog createDialog(Context context, ZhihuDailyPurify.News news,
      DialogInterface.OnClickListener listener) {
    String[] questionTitles = getQuestionTitlesAsStringArray(news);

    return new AlertDialog.Builder(context)
        .setTitle(news.getTitle())
        .setItems(questionTitles, listener)
        .create();
  }

  private DialogInterface.OnClickListener makeGoToZhihuDialogClickListener(Context context,
      ZhihuDailyPurify.News dailyNews) {
    return (dialog, which) -> {
      String questionUrl = dailyNews.getQuestionsList().get(which).getUrl();

      goToZhihu(context, questionUrl);
    };
  }

  private DialogInterface.OnClickListener makeShareQuestionDialogClickListener(Context context,
      ZhihuDailyPurify.News dailyNews) {
    return (dialog, which) -> {
      String questionTitle = dailyNews.getQuestionsList().get(which).getTitle(),
          questionUrl = dailyNews.getQuestionsList().get(which).getUrl();

      shareQuestion(context, questionTitle, questionUrl);
    };
  }

  private void goToZhihu(Context context, String url) {
    SharedPreferences preferences = ZhihuDailyPurifyApplication.getSharedPreferences();

    if (!preferences.getBoolean(Constants.SharedPreferencesKeys.SHOULD_USE_CLIENT, false)) {
      openUsingBrowser(context, url);
    } else if (Check.isZhihuInstalled()) {
      openUsingZhihuClient(context, url);
    } else {
      openUsingBrowser(context, url);
    }
  }

  private void openUsingBrowser(Context context, String url) {
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

    if (Check.isIntentSafe(browserIntent)) {
      context.startActivity(browserIntent);
    } else {
      Toast.makeText(context, context.getString(R.string.no_browser), Toast.LENGTH_SHORT).show();
    }
  }

  private void openUsingZhihuClient(Context context, String url) {
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    browserIntent.setPackage(Constants.PackageID.ZHIHU);
    context.startActivity(browserIntent);
  }

  private void shareQuestion(Context context, String questionTitle, String questionUrl) {
    Intent share = new Intent(android.content.Intent.ACTION_SEND);
    share.setType("text/plain");
    //noinspection deprecation
    share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
    share.putExtra(Intent.EXTRA_TEXT,
        questionTitle + " " + questionUrl + SHARE_FROM_ZHIHU);
    context.startActivity(Intent.createChooser(share, context.getString(R.string.share_to)));
  }

  private String[] getQuestionTitlesAsStringArray(ZhihuDailyPurify.News news) {
    String[] result = new String[news.getQuestionsList().size()];

    for (int i = 0; i < news.getQuestionsList().size(); i++) {
      result[i] = news.getQuestionsList().get(i).getTitle();
    }

    return result;
  }

  public static class CardViewHolder extends RecyclerView.ViewHolder implements
      View.OnClickListener {

    ImageView newsImage;
    TextView questionTitle;
    TextView dailyTitle;
    ImageView overflow;

    private ClickResponseListener mClickResponseListener;

    CardViewHolder(View v, ClickResponseListener clickResponseListener) {
      super(v);

      this.mClickResponseListener = clickResponseListener;

      newsImage = v.findViewById(R.id.thumbnail_image);
      questionTitle = v.findViewById(R.id.question_title);
      dailyTitle = v.findViewById(R.id.daily_title);
      overflow = v.findViewById(R.id.card_share_overflow);

      v.setOnClickListener(this);
      overflow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      if (v == overflow) {
        mClickResponseListener.onOverflowClick(v, getAdapterPosition());
      } else {
        mClickResponseListener.onWholeClick(getAdapterPosition());
      }
    }

    public interface ClickResponseListener {

      void onWholeClick(int position);

      void onOverflowClick(View v, int position);
    }
  }

  private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

    static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<>());

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
      if (loadedImage != null) {
        ImageView imageView = (ImageView) view;
        boolean firstDisplay = !displayedImages.contains(imageUri);
        if (firstDisplay) {
          FadeInBitmapDisplayer.animate(imageView, 500);
          displayedImages.add(imageUri);
        }
      }
    }
  }

  private static final String SHARE_FROM_ZHIHU = " 分享自知乎网";
  private static final String MULTIPLE_DISCUSSION = "这里包含多个知乎讨论，请点击后选择";
}

package com.nytimes.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import com.bumptech.glide.Glide;
import com.nytimes.R;
import com.nytimes.interfaces.OnLoadMoreListener;
import com.nytimes.model.MediaMetadatum;
import com.nytimes.model.Medium;
import com.nytimes.model.Result;
import com.nytimes.utalities.Constants;
import com.nytimes.utalities.GlideRequestOptions;
import com.nytimes.utalities.Util;

import de.hdodenhof.circleimageview.CircleImageView;


public class ArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private List<Result> list;
    private final int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private final ClickListener itemClickListener;
    private final Context mContext;

    public ArticlesAdapter(RecyclerView recyclerView, List<Result> list, Context context, ClickListener listener) {
        this.list = list;
        this.mContext = context;
        this.itemClickListener = listener;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            ViewHolder holder;
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_content, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            holder.mainItemView.setOnClickListener(this);
            return holder;
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder vHolder = (ViewHolder) holder;
            Result item = list.get(position);
            List<Medium> media = item.getMedia();
            String URL = "";
            if (!media.isEmpty()) {
                List<MediaMetadatum> metadata = media.get(0).getMediaMetadata();
                if (!metadata.isEmpty()) {
                    URL = metadata.get(0).getUrl();
                }
            }
            vHolder.tvTitle.setText(item.getTitle());
            vHolder.tvDescription.setText(item.getAbstract());
            vHolder.tvDate.setText(item.getPublishedDate());

            try {
                if (Util.isValidURL(URL)) {
                    Glide.with(mContext).load(URL).apply(GlideRequestOptions.defaultPlaceHolderOptions).into(vHolder.ivImage);
                }
            } catch (Exception e) {
                Log.e(Constants.EXCEPTION, e.toString());
            }
            vHolder.mainItemView.setTag(vHolder);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return null != list ? list.size() : 0;
    }


    public void setLoaded() {
        isLoading = false;
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public final ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.progressBar1);
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        public View mainItemView;
        public TextView tvDate;
        public TextView tvDescription;
        public TextView tvTitle;
        public CircleImageView ivImage;

        public ViewHolder(View view) {
            super(view);
            mainItemView = view;
            tvDate = view.findViewById(R.id.tvDate);
            tvDescription = view.findViewById(R.id.tvDescription);
            tvTitle = view.findViewById(R.id.tvTitle);
            ivImage = view.findViewById(R.id.ivImage);
        }
    }

    @Override
    public void onClick(View v) {
        ViewHolder holder = (ViewHolder) v.getTag();
        int position = holder.getAdapterPosition();

        if (holder.mainItemView == v) {
            itemClickListener.onItemClicked(position, list.get(position));
        }
    }

    public interface ClickListener {
        void onItemClicked(int position, Result item);
    }

}
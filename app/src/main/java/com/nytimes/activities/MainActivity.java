package com.nytimes.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.nytimes.AppController;
import com.nytimes.adapters.ArticlesAdapter;
import com.nytimes.apis.GetArticlesResponse;
import com.nytimes.fragments.DetailFragment;
import com.nytimes.R;
import com.nytimes.model.Result;
import com.nytimes.utalities.Constants;
import com.nytimes.utalities.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ArticlesAdapter.ClickListener {

    private boolean mTwoPane;
    private RecyclerView recyclerView;
    private ArticlesAdapter mAdapter;
    private List<Result> list = new ArrayList<>();
    private Dialog progressBar;
    private int PERIOD = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title));
        setSupportActionBar(toolbar);
        initialize();
        hitArticlesAPI();
    }

    private void initialize() {
        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }
        progressBar = Util.loadingDialog(this);
        recyclerView = findViewById(R.id.item_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter = new ArticlesAdapter(recyclerView, list, this, this);
        recyclerView.setAdapter(mAdapter);
    }

    private void hitArticlesAPI() {
        if (!Util.isConnectingToInternet(this)) {
            Util.showToastMsg(this, Constants.kStringNetworkConnectivityError);
            return;
        }
        progressBar.show();

        Call<GetArticlesResponse> call = AppController.getApiService().getArticlesListResponse(Constants.API_KEY, PERIOD);
        call.enqueue(new Callback<GetArticlesResponse>() {
            @Override
            public void onResponse(Call<GetArticlesResponse> call, Response<GetArticlesResponse> response) {
                progressBar.dismiss();
                try {
                    if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                        list.clear();
                        list.addAll(response.body().getResults());
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Log.e(Constants.EXCEPTION, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<GetArticlesResponse> call, Throwable t) {
                progressBar.dismiss();
                Log.e(Constants.EXCEPTION, t.getMessage());
            }
        });
    }

    @Override
    public void onItemClicked(int position, Result item) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.ARG_ITEM, item);
        if (mTwoPane) {
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(bundle);
            this.getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

}

package com.arctouch.codechallenge.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.details.DetailsAtivity;
import com.arctouch.codechallenge.model.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    private HomeAdapter homeAdapter;
    private  LinearLayoutManager layoutManager;
    private HomeViewModel homeViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        ButterKnife.bind(this);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.init();
        homeViewModel.getMovies().observeForever(new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if(homeAdapter == null) {
                    handleAdapter();
                }else{
                    homeAdapter.notifyDataSetChanged();
                }

            }
        });


        homeViewModel.getPage().observe(this, page -> {
            if(page != 1) {
                homeViewModel.addNewMovies();
            }
        });



    }

    private void handleAdapter(){
        homeAdapter = new HomeAdapter( homeViewModel.getMovies().getValue());
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        homeAdapter.setRVClick((view, position) -> callIntent(DetailsAtivity.class, homeAdapter.getItem(position).toJson()));
        setScrollToRV();
        recyclerView.setAdapter(homeAdapter);
        progressBar.setVisibility(View.GONE);

    }

    private void setScrollToRV() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!homeViewModel.isLoading().getValue()) {
                    if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == homeAdapter.getItemCount() - 4) {
                        //bottom of list!
                        homeViewModel.addPage();

                    }
                }
            }
        });
    }


    private void callIntent(Class intentClass, String data){
        Intent intent = new Intent();
        intent.setClass(this, intentClass);
        intent.putExtra("movie",data);
        startActivity(intent);
    }


}

package com.arctouch.codechallenge.home;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;
import android.view.View;

import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.base.BaseController;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.model.UpcomingMoviesResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieRepository {
    private static MovieRepository instance;

    public static MovieRepository getInstance(){
        if(instance  == null){
            instance = new MovieRepository();
        }
        return instance;
    }

    public void getGenres() {
        new BaseController().getApi().genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->  Cache.setGenres(response.genres));

    }


    public void getMovies(int page, HomeViewModel homeViewModel) {
        MutableLiveData<List<Movie>> data = new MutableLiveData<>();
        new BaseController().getApi().allMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, page, TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpcomingMoviesResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(UpcomingMoviesResponse upcomingMoviesResponse) {

                        for (Movie movie : upcomingMoviesResponse.results) {
                                            movie.genres = new ArrayList<>();
                                            for (Genre genre : Cache.getGenres()) {
                                                if (movie.genreIds.contains(genre.id)) {
                                                    movie.genres.add(genre);
                                                }
                                            }
                                        }
                        homeViewModel.setMovies(upcomingMoviesResponse.results);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}


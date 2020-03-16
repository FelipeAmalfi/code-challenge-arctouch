package com.arctouch.codechallenge.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.arctouch.codechallenge.model.Movie;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Movie>> mMovies;
    private MovieRepository movieRepository;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private MutableLiveData<Integer> page = new MutableLiveData<>();


    public void init() {
        if(mMovies != null){
            return;
        }
        page.setValue(1);
        movieRepository = MovieRepository.getInstance();
        movieRepository.getGenres();
        movieRepository.getMovies(1,this);
        isUpdating.setValue(false);

    }


    public LiveData<List<Movie>> getMovies() {
        if(mMovies == null){
            mMovies = new MutableLiveData<>();
        }
        return mMovies;
    }

    public void addNewMovies(){
        isUpdating.setValue(true);
        movieRepository = MovieRepository.getInstance();
        movieRepository.getMovies(page.getValue(), this);
        isUpdating.setValue(false);
    }

    public LiveData<Boolean> isLoading(){
        return isUpdating;
    }

    public LiveData<Integer> getPage(){

        return page;
    }

    public void setMovies(List<Movie> movies){
        if(mMovies.getValue() == null){
            mMovies.setValue(movies);
        }else{
            List<Movie> newList = mMovies.getValue();
            newList.addAll(movies);
            mMovies.setValue(newList);
        }

    }

    public  void addPage(){
        try {
            page.setValue(page.getValue() + 1);
        }catch(NullPointerException e){
            page.setValue(1);
        }
    }




}

package com.arctouch.codechallenge.details;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.home.HomeViewModel;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsAtivity extends AppCompatActivity {

    @BindView(R.id.background_image)
    ConstraintLayout backgroundImage;
    @BindView(R.id.poster_image) ImageView posterImage;
    @BindView(R.id.txt_details_title)
    TextView txtDetailsTitle;
    @BindView(R.id.txt_release_date) TextView txtReleaseDate;
    @BindView(R.id.txt_gender) TextView txtGender;
    @BindView(R.id.text_overview) TextView txtOverview;

    private final MovieImageUrlBuilder movieImageUrlBuilder = new MovieImageUrlBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_ativity);
        ButterKnife.bind(this);
        setFields(new Gson().fromJson(getIntent().getExtras().getString("movie"),Movie.class));
    }

    private void setFields(Movie movie) {
        txtDetailsTitle.setText(movie.title);
        txtOverview.setText(movie.overview);
        setImageView(posterImage, movie.posterPath);
        setBackdropImage(backgroundImage, movie.backdropPath);
        txtGender.setText(String.format(Locale.US,"Gêneros: %s",TextUtils.join(", ", movie.genres)));
        txtReleaseDate.setText(String.format(Locale.US,"Lançamento: %s",convertDate(movie.releaseDate)));

    }


    private void setImageView(ImageView view, String path ){
        Glide.with(this)
                .load(movieImageUrlBuilder.buildPosterUrl(path))
                .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(view);
    }

    private void setBackdropImage(ConstraintLayout layout,  String backdrop){
        Glide.with(this).load(movieImageUrlBuilder.buildPosterUrl(backdrop)).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    layout.setBackground(resource);
                }
            }
        });
    }

    private String convertDate(String date)  {
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            Date newDate = format.parse(date);
            format = new SimpleDateFormat("dd/mm/yyyy");
            return  format.format(newDate);
        }catch (ParseException e){
            return date;
        }

    }


}

package com.example.android.popularmovies.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.example.android.popularmovies.model.Trailer;

public class MovieDetailViewModel extends AndroidViewModel{

    private Trailer trailer;

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public Trailer getTrailer() {
        return trailer;
    }

    public void setTrailer(Trailer trailer) {
        this.trailer = trailer;
    }
}

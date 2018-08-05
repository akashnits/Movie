package com.example.android.popularmovies.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.example.android.popularmovies.model.Response;

public class MoviesViewModel extends AndroidViewModel {

    private Response response;

    public MoviesViewModel(@NonNull Application application) {
        super(application);
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}

package com.example.movieapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerResults {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Trailers> trailerresults = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Trailers> getResults() {
        return trailerresults;
    }

    public void setResults(List<Trailers> trailerresults) {
        this.trailerresults = trailerresults;
    }

}

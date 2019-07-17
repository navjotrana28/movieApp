package com.example.movieapp;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//import android.support.annotation.NonNull;


@Entity(tableName = "favouritetable")

public class movie implements Parcelable {
    public static final Creator<movie> CREATOR = new Creator<movie>() {
        @Override
        public movie createFromParcel(Parcel source) {
            return new movie(source);
        }

        @Override
        public movie[] newArray(int size) {
            return new movie[size];
        }
    };
    /**
     * vote_count : 869
     * id : 429617
     * video : false
     * vote_average : 7.8
     * title : Spider-Man: Far from Home
     * popularity : 563.627
     * poster_path : /rjbNpRMoVvqHmhmksbokcyCr7wn.jpg
     * original_language : en
     * original_title : Spider-Man: Far from Home
     * genre_ids : [28,12,878]
     * backdrop_path : /dihW2yTsvQlust7mSuAqJDtqW7k.jpg
     * adult : false
     * overview : Peter Parker and his friends go on a summer trip to Europe. However, they will hardly be able to rest - Peter will have to agree to help Nick Fury uncover the mystery of creatures that cause natural disasters and destruction throughout the continent.
     * release_date : 2019-06-28
     */

    private int vote_count;
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "posterpath")
    private String poster_path;
    @ColumnInfo(name = "Title")
    private String original_title;
    private boolean video;
    private double vote_average;
    private String title;
    private double popularity;
    private String original_language;
    private String backdrop_path;
    private boolean adult;
    private String overview;
    private String release_date;
/*
    private List<Integer> genre_ids;
*/


    public movie(){}

    protected movie(Parcel in) {
        this.vote_count = in.readInt();
        this.id = in.readInt();
        this.video = in.readByte() != 0;
        this.vote_average = in.readDouble();
        this.title = in.readString();
        this.popularity = in.readDouble();
        this.poster_path = in.readString();
        this.original_language = in.readString();
        this.original_title = in.readString();
        this.backdrop_path = in.readString();
        this.adult = in.readByte() != 0;
        this.overview = in.readString();
        this.release_date = in.readString();
/*
        this.genre_ids = new ArrayList<Integer>();
        in.readList(this.genre_ids, Integer.class.getClassLoader());
*/
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

/*
    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }
*/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.vote_count);
        dest.writeInt(this.id);
        dest.writeByte(this.video ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.vote_average);
        dest.writeString(this.title);
        dest.writeDouble(this.popularity);
        dest.writeString(this.poster_path);
        dest.writeString(this.original_language);
        dest.writeString(this.original_title);
        dest.writeString(this.backdrop_path);
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeString(this.overview);
        dest.writeString(this.release_date);
/*
        dest.writeList(this.genre_ids);
*/
    }
}

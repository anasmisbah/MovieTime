package com.example.movietime.model;

import com.google.gson.annotations.SerializedName;

public class Genre {
    @SerializedName("id")
    private String idgenre;

    @SerializedName("name")
    private String namegenre;

    public String getIdgenre() {
        return idgenre;
    }

    public void setIdgenre(String idgenre) {
        this.idgenre = idgenre;
    }

    public String getNamegenre() {
        return namegenre;
    }

    public void setNamegenre(String namegenre) {
        this.namegenre = namegenre;
    }
}

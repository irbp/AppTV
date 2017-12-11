package com.italo.apptv;

import java.io.Serializable;

/**
 * Created by italo on 10/12/2017.
 */

// Class with attributes and methods that represents a thumbnail
public class Thumbnail implements Serializable{
    private int id;
    private String name, imageUrl;

    public Thumbnail(int id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageLink) {
        this.imageUrl = imageUrl;
    }
}

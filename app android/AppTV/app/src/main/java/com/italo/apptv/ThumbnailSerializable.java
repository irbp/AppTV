package com.italo.apptv;

import java.io.Serializable;
import java.util.List;

/**
 * Created by italo on 10/12/2017.
 */

// Auxiliar class for saveInstanceState
public class ThumbnailSerializable implements Serializable{
    public List<Thumbnail> thumbnailList;
    public static final String KEY = "thumbs";

    public ThumbnailSerializable(List<Thumbnail> thumbnailList) {
        this.thumbnailList = thumbnailList;
    }
}

package com.italo.apptv;

import java.io.Serializable;

/**
 * Created by italo on 11/12/2017.
 */

public class ShowSerializable implements Serializable {

    public Show show;
    public static final String KEY = "shows";

    public ShowSerializable(Show show) {
        this.show = show;
    }
}

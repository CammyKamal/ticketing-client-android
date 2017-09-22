package com.example.theodhor.retrofit2;

/**
 * Created by Theodhor Pandeli on 2/11/2016.
 */

import com.squareup.otto.Bus;

public class BusProvider {

    private static final Bus BUS = new Bus();

    public static Bus getInstance(){
        return BUS;
    }

    public BusProvider(){}
}


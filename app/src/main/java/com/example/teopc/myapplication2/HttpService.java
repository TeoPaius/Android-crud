package com.example.teopc.myapplication2;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

import java.util.ArrayList;

import model.Run;

public class HttpService {
    String url = "http://192.168.1.186:1337/runs";
    RequestQueue rq;

    public HttpService(RequestQueue r)
    {
        rq = r;
    }

    public static void post()
    {

    }

    public static ArrayList<Run> getAll()
    {
        return null;
    }
}

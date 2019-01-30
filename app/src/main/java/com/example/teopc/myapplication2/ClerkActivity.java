package com.example.teopc.myapplication2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import io.realm.Realm;
import model.Product;
import model.Run;

//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import okhttp3.WebSocket;
//import okhttp3.WebSocketListener;
//import okio.ByteString;

public class ClerkActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter mAdapter;
    private Realm realm;
    private Vector<Product> myDataset;
    private RequestQueue mRequestQueue;
    private String baseUrl = "http://10.152.5.75:2024";
    private Button deleteBtn;
    private Button addBtn;
//    private OkHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk);
        myDataset = new Vector<>();
//        client = new OkHttpClient();

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
        deleteBtn = (Button) findViewById(R.id.deleteButton);
        addBtn = (Button) findViewById(R.id.addButton);

//        okhttp3.Request request = new okhttp3.Request.Builder().url(baseUrl).build();
//        MyWebSocketListener listener = new MyWebSocketListener();
//        WebSocket ws = client.newWebSocket(request, listener);


        mRecyclerView = (RecyclerView) findViewById(R.id.clerkRV);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);
        mAdapter.mode = 1;
        mRecyclerView.setAdapter(mAdapter);

        String url = baseUrl + "/all";
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(getApplicationContext(), "SENT", Toast.LENGTH_SHORT).show();
                        for(int i = 0 ; i< response.length(); ++i)
                        {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Product p = Product.fromJson(obj);
                                myDataset.add(p);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        mAdapter = new MyAdapter(myDataset);
                        mAdapter.mode=1;
                        mRecyclerView.setAdapter(mAdapter);
                        //runLayoutAnimation(mRecyclerView);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(),  Toast.LENGTH_SHORT).show();
                        Log.d("REQUEST", "volley error");
                    }
                });


        mRequestQueue.add(jsonObjectRequest);



        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Integer id = Integer.parseInt(((TextView) findViewById(R.id.idText)).getText().toString());
                String url = baseUrl + "/product/" + id.toString();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(getApplicationContext(), "SENT", Toast.LENGTH_LONG).show();
                                for(Integer i = 0; i < myDataset.size(); ++i)
                                {
                                    if(myDataset.get(i).id.equals(id))
                                    {
                                        myDataset.removeElementAt(i);
                                    }
                                }
                                mAdapter = new MyAdapter(myDataset);
                                mAdapter.mode=1;
                                mRecyclerView.setAdapter(mAdapter);

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                parseVolleyError(error);
                                Log.d("REQUEST", "volley error");
                            }
                        });


                mRequestQueue.add(jsonObjectRequest);

            }
        });


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> params = new HashMap();
                params.put("name", "aa");
                params.put("price", 10);
                params.put("quantity", 10);
                params.put("description", "ddd");
                params.put("stauts", "new");
                JSONObject parameters = new JSONObject(params);
                String url = baseUrl + "/product";

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response){
                                Product p = null;
                                try {
                                    p = Product.fromJson(response);
                                    myDataset.add(p);
                                    mAdapter = new MyAdapter(myDataset);
                                    mAdapter.mode=1;
                                    mRecyclerView.setAdapter(mAdapter);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("REQUEST", error.getMessage());

                            }


                        });
                mRequestQueue.add(jsonObjectRequest);
            }
        });
    }

    public void parseVolleyError(VolleyError error) {
        try {
            assert error.networkResponse != null;
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            String message = data.getString("text");
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        } catch (JSONException | UnsupportedEncodingException ignored) {
        }
    }






}


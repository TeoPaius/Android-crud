package com.example.teopc.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Debug;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
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
import com.bugsnag.android.Bugsnag;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import model.Product;
import model.Run;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity implements CreateDialog.CreateDialogListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter mAdapter;
    private Vector<Product> myDataset;
    private Button bucketButton;
    private Button createButton;
    private Button purchaseButton;
    private Button updateButton;
    private Button changeButton;
    private FloatingActionButton fabBtn;
    private Realm realm;
    private RequestQueue mRequestQueue;
    private AdView mAdView;
    private String baseUrl = "http://10.152.5.75:2024";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);


        realm = Realm.getInstance(config);


        setContentView(R.layout.activity_main);
        myDataset = new Vector<>();

        changeButton = (Button) findViewById(R.id.redirButton);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    Intent intent = new Intent(getApplicationContext(), ClerkActivity.class);
                    startActivity(intent);
                }
            }
        });

        bucketButton = (Button) findViewById(R.id.bucketButton);
        bucketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BucketListActivity.class);
                startActivity(intent);
            }
        });

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();

        if(!isNetworkAvailable()) {
            Toast.makeText(getApplicationContext(), "NO NET, try again", Toast.LENGTH_SHORT).show();
//            final RealmResults<Run> runs = realm.where(Run.class).findAll();
//            myDataset.addAll(runs);
//            runLayoutAnimation(mRecyclerView);
        }
        else
        {

//            for(Run r: myDataset)
//            {
//                realm.beginTransaction();
//                r.deleteFromRealm();
//                realm.commitTransaction();
//            }
            myDataset.clear();
            String url = baseUrl + "/products";
            Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
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
        }


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);
        mAdapter.mode = 0;
        mRecyclerView.setAdapter(mAdapter);

        fabBtn = (FloatingActionButton) findViewById(R.id.floatingActionButton);
//        fabBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isNetworkAvailable())
//                {
//                    String url = "http://192.168.1.186:1337/runs";
//                    for(Run r : myDataset)
//                    {
//                        if(r.getId().equals(""))
//                        {
//                            Map<String, String> params = new HashMap();
//                            params.put("length", r.getLength().toString());
//                            params.put("duration", r.getDuration().toString());
//                            params.put("date", r.getDate().toString());
//                            JSONObject parameters = new JSONObject(params);
//
//
//                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                                    (Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
//
//                                        @Override
//                                        public void onResponse(JSONObject response) {
//
//                                        }
//                                    }, new Response.ErrorListener() {
//
//                                        @Override
//                                        public void onErrorResponse(VolleyError error) {
//                                            Log.d("REQUEST", error.getMessage());
//
//                                        }
//
//
//                                    });
//                            mRequestQueue.add(jsonObjectRequest);
//                        }
//                    }
//                    for(Run r: myDataset)
//                    {
//                        realm.beginTransaction();
//                        r.deleteFromRealm();
//                        realm.commitTransaction();
//                    }
//                    myDataset.clear();
//                    JsonArrayRequest jsonObjectRequest2 = new JsonArrayRequest
//                            (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
//
//                                @Override
//                                public void onResponse(JSONArray response) {
//                                    Toast.makeText(getApplicationContext(), "SENT", Toast.LENGTH_SHORT).show();
//                                    myDataset.clear();
//                                    for(int i = 0 ; i< response.length(); ++i)
//                                    {
//                                        try {
//                                            JSONObject obj = response.getJSONObject(i);
//                                            Run r = new Run();
//                                            r.setId(obj.getString("id"));
//                                            r.setDate(stringToDate(obj.getString("date")));
//                                            r.setLength(obj.getInt("length"));
//                                            r.setDuration(obj.getInt("duration"));
//
//                                            realm.beginTransaction();
//                                            final Run managedRun = realm.copyToRealm(r); // Persist unmanaged objects
//                                            myDataset.add(managedRun);
//                                            realm.commitTransaction();
//
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        } catch (ParseException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                    }
//                                    mAdapter = new MyAdapter(myDataset);
//                                    mRecyclerView.setAdapter(mAdapter);
//                                }
//                            }, new Response.ErrorListener() {
//
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    Log.d("REQUEST", error.getMessage());
//
//                                }
//                            });
//
//                    mRequestQueue.add(jsonObjectRequest2);
//                }
//            }
//        });

//        createButton = (Button) findViewById(R.id.addButton);
//        createButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openCreateDialog();
//            }
//        });

        purchaseButton = (Button) findViewById(R.id.purchaseButton);
        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNetworkAvailable())
                {
                    Toast.makeText(getApplicationContext(), "NO INTERNET CONECTION", Toast.LENGTH_SHORT).show();
                }
                else {

                    final Product p = myDataset.elementAt(MyAdapter.selectedIdx);
                    if(p.quantity < 1)
                        return;
                    String url = baseUrl + "/buyProduct";
                    Map<String, String> params = new HashMap();
                    params.put("id", p.id.toString());
                    params.put("quantity", "1");
                    JSONObject parameters = new JSONObject(params);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    myDataset.elementAt(MyAdapter.selectedIdx).quantity-=1;
                                    realm.beginTransaction();
                                    Product obj = realm.createObject(Product.class);
                                    obj.id = p.id;
                                    obj.quantity = p.quantity+1;
                                    obj.description = p.description;
                                    obj.name = p.name;
                                    obj.price = p.price;
                                    obj.status = p.status;
                                    realm.commitTransaction();
                                    mAdapter = new MyAdapter(myDataset);
                                    mRecyclerView.setAdapter(mAdapter);
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("REQUEST", error.getMessage());

                                }
                            });
                    mRequestQueue.add(jsonObjectRequest);
                }
            }
        });

//        updateButton = (Button) findViewById(R.id.updateButton);
//        updateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!isNetworkAvailable())
//                {
//                    Toast.makeText(getApplicationContext(), "NO INTERNET CONECTION", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    CreateDialog dialog = new CreateDialog();
//                    Bundle args = new Bundle();
//                    args.putInt("idx", MyAdapter.selectedIdx);
//                    args.putSerializable("run", myDataset.elementAt(MyAdapter.selectedIdx));
//                    dialog.setArguments(args);
//                    dialog.show(getSupportFragmentManager(), "update");
//                }
//            }
//        });






    }

    private void openCreateDialog()
    {
        CreateDialog dialog = new CreateDialog();

        dialog.show(getSupportFragmentManager(), "create");
    }


    private Date stringToDate(String s) throws ParseException {
        String string = s;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = format.parse(string);
        return date;
    }

    private String dateToString(Date d)
    {
        Date date = d;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String s = format.format(date);
        return s;
    }


    @Override
    public void setTexts(String length, String duration, String date) {
//        try {
//            if(!isNetworkAvailable()) {
//                Run newRun = new Run(Float.parseFloat(length), stringToDate(date), Integer.parseInt(duration));
//                newRun.setId("");
//                realm.beginTransaction();
//                final Run managedRun = realm.copyToRealm(newRun); // Persist unmanaged objects
//                myDataset.add(managedRun);
//                realm.commitTransaction();
//            }
//            else
//            {
//                Map<String, String> params = new HashMap();
//                params.put("length", length);
//                params.put("duration", duration);
//                params.put("date", date);
//                JSONObject parameters = new JSONObject(params);
//                String url = "http://192.168.1.186:1337/runs";
//
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                        (Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
//
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                Run r = new Run();
//                                try {
//                                    r.setId(response.getString("id"));
//                                    r.setDuration(response.getInt("duration"));
//                                    r.setLength(response.getInt("length"));
//                                    r.setDate(stringToDate(response.getString("date")));
//                                    realm.beginTransaction();
//                                    final Run managedRun = realm.copyToRealm(r); // Persist unmanaged objects
//                                    myDataset.add(managedRun);
//                                    realm.commitTransaction();
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                                mAdapter = new MyAdapter(myDataset);
//                                mRecyclerView.setAdapter(mAdapter);
//                            }
//                        }, new Response.ErrorListener() {
//
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.d("REQUEST", error.getMessage());
//
//                            }
//
//
//                        });
//                mRequestQueue.add(jsonObjectRequest);
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void updateTexts(String length, String duration, String date, Integer positon) {

//        realm.beginTransaction();
//        myDataset.elementAt(MyAdapter.selectedIdx).setLength(Float.parseFloat(length));
//        myDataset.elementAt(MyAdapter.selectedIdx).setDuration(Integer.parseInt(duration));
//        myDataset.elementAt(MyAdapter.selectedIdx).setDate(new Date(date));
//
//        String url = "http://192.168.1.186:1337/runs/" + myDataset.elementAt(MyAdapter.selectedIdx).getId();
//        Map<String, String> params = new HashMap();
//        params.put("length", myDataset.elementAt(MyAdapter.selectedIdx).getLength().toString());
//        params.put("duration", myDataset.elementAt(MyAdapter.selectedIdx).getDuration().toString());
//        params.put("date", myDataset.elementAt(MyAdapter.selectedIdx).getDate().toString());
//        JSONObject parameters = new JSONObject(params);
//
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                (Request.Method.PUT, url, parameters, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("REQUEST", error.getMessage());
//
//                    }
//
//
//                });
//        mRequestQueue.add(jsonObjectRequest);
//        realm.commitTransaction();
//        mAdapter = new MyAdapter(myDataset);
//        mRecyclerView.setAdapter(mAdapter);

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {

        final Context context = recyclerView.getContext();

        final LayoutAnimationController controller =

                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);



        recyclerView.setLayoutAnimation(controller);

        recyclerView.getAdapter().notifyDataSetChanged();

        recyclerView.scheduleLayoutAnimation();

    }
}

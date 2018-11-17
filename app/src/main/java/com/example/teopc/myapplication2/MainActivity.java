package com.example.teopc.myapplication2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import io.realm.Realm;
import io.realm.RealmResults;
import model.Run;

public class MainActivity extends AppCompatActivity implements CreateDialog.CreateDialogListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter mAdapter;
    private Vector<Run> myDataset;
    private Button createButton;
    private Button deleteButton;
    private Button updateButton;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Realm.init(getApplicationContext());

        realm = Realm.getDefaultInstance();


        setContentView(R.layout.activity_main);
        myDataset = new Vector<>();
        final RealmResults<Run> runs = realm.where(Run.class).findAll();
        myDataset.addAll(runs);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);


        createButton = (Button) findViewById(R.id.addButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateDialog();
            }
        });

        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.beginTransaction();
                Run r = myDataset.elementAt(MyAdapter.selectedIdx);
                r.deleteFromRealm();

                myDataset.removeElementAt(MyAdapter.selectedIdx);
                realm.commitTransaction();
                mAdapter = new MyAdapter(myDataset);
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        updateButton = (Button) findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateDialog dialog = new CreateDialog();
                Bundle args = new Bundle();
                args.putInt("idx", MyAdapter.selectedIdx);
                args.putSerializable("run", myDataset.elementAt(MyAdapter.selectedIdx));
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "update");
            }
        });






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
        try {
            Run newRun = new Run(Float.parseFloat(length),stringToDate(date), Integer.parseInt(duration));


            realm.beginTransaction();
            final Run managedRun = realm.copyToRealm(newRun); // Persist unmanaged objects
            myDataset.add(managedRun);
            realm.commitTransaction();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTexts(String length, String duration, String date, Integer positon) {


        realm.beginTransaction();
        RealmResults<Run> r = realm.where(Run.class).findAll();
        for(Run run : r)
        {
            if(run.getHash().equals(myDataset.elementAt(MyAdapter.selectedIdx).getHash()))
            {
                run.setLength(Float.parseFloat(length));
                run.setDuration(Integer.parseInt(duration));
                run.setDate(new Date(date));
                //run.setHash(myDataset.elementAt(MyAdapter.selectedIdx).getHash());
                realm.insertOrUpdate(run);
            }
        }


        realm.commitTransaction();
        //myDataset.set(positon, new Run(Float.parseFloat(length), new Date(date), Integer.parseInt(duration)));
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

    }
}

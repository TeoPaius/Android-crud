package com.example.teopc.myapplication2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import model.Run;

public class CreateDialog extends AppCompatDialogFragment {
    private EditText lengthText;
    private EditText durationText;
    private EditText dateText;
    private Integer updateIdx;


    private CreateDialogListener createDialogListener;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.create_layout_dialog, null);

        lengthText = v.findViewById(R.id.lengthText);
        durationText = v.findViewById(R.id.durationText);
        dateText = v.findViewById(R.id.dateText);


        Bundle args = getArguments();
        if(args != null)
            if(args.containsKey("idx"))
            {
            updateIdx = args.getInt("idx");
            Run r = (Run) args.getSerializable("run");
            lengthText.setText(r.getLength().toString());
            durationText.setText(r.getDuration().toString());
            dateText.setText(r.getDate().toString());
            }
            else
                updateIdx = -1;
        else
            updateIdx = -1;

        if(updateIdx == -1) {
            builder.setView(v).setTitle("Add new run").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String length = lengthText.getText().toString();
                    String duration = durationText.getText().toString();
                    String date = dateText.getText().toString();
                    createDialogListener.setTexts(length, duration, date);
                }
            });
        }
        else
            builder.setView(v).setTitle("Update run").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String length = lengthText.getText().toString();
                    String duration = durationText.getText().toString();
                    String date = dateText.getText().toString();
                    createDialogListener.updateTexts(length, duration, date, updateIdx);
                }
            });


        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        createDialogListener = (CreateDialogListener) context;
    }

    public interface CreateDialogListener{
        void setTexts(String length, String duration, String date);
        void updateTexts(String length, String duration, String date, Integer positon);
    }

    private String dateToString(Date d)
    {
        Date date = d;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String s = format.format(date);
        return s;
    }
}

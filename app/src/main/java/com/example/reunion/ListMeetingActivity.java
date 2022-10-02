package com.example.reunion;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import model.Reunion;


public class ListMeetingActivity extends AppCompatActivity {

    public static ArrayList<Reunion> filteredlist;
    private static int mColor;
    public FloatingActionButton mAddButton;
    public RecyclerView recyclerView;
    public static RecyclerViewAdapter recyclerViewAdapter;
    RecyclerView.LayoutManager layoutManager;
    public String[] roomNumbers;
    public static ArrayList<Reunion> mMeetings;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_meeting_activity);

        mAddButton = findViewById(R.id.add_button);

        recyclerView = findViewById(R.id.meeting_view);
        recyclerView.setHasFixedSize(true);

        AddMeetingDialog myDialog=new AddMeetingDialog();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mMeetings = new ArrayList<Reunion>();
        recyclerViewAdapter = new RecyclerViewAdapter(mMeetings);
        recyclerView.setAdapter(recyclerViewAdapter);


        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = ListMeetingActivity.this;
                View view = getLayoutInflater().inflate(R.layout.dialog_design, null);

                roomNumbers = getResources().getStringArray(R.array.room_numbers);
                myDialog.showDialog(view, context, roomNumbers);

                setList(mMeetings);
            }
        });


    }



    public void setAutoCompleteTextView(AutoCompleteTextView room, Context context, String[] roomNumbers) {
        ArrayAdapter<String> roomAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, roomNumbers);
        room.setAdapter(roomAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.room_picker_actions:
                showRoomDialog();
                break;
            case R.id.date_picker_actions:
                showDateDialog();

                break;
        }

        return true;
    }


    // Room picker dialog
    public void showRoomDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ListMeetingActivity.this);
        String[] items = getResources().getStringArray(R.array.room_numbers);

        alertDialog.setTitle("Choose Room");
        alertDialog.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int checkedItem) {
                filterByRoom(items[checkedItem]);
                dialogInterface.dismiss();

            }
        });

        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    // add new meeting
    public int addMeeting(String meetingRoom, String meetingTopic, String meetingTime, List<String> participants, String meetingDate) {

        mMeetings.add(new Reunion(meetingRoom, meetingTime, meetingTopic, participants, meetingDate));
        recyclerViewAdapter.notifyDataSetChanged();

        return mMeetings.size();

    }

    //delete meeting
    public void deleteMeeting(RecyclerViewAdapter adapter, int position) {
        adapter.mMeetings.remove(position);
        adapter.notifyItemRemoved(position);
    }


    // Date filter dialog
    public void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yy",
                        java.util.Locale.getDefault());
                filterByDate(dateFormat.format(calendar.getTime()));

            }

        };

        new DatePickerDialog(this, date, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();


    }

    //Perform filter by room
    public ArrayList<Reunion> filterByRoom(String text) {
        filteredlist = new ArrayList<Reunion>();

        for (Reunion item : mMeetings) {
            if (item.getMeetingRoom().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);

            }
            if (filteredlist.isEmpty()){
                setList(filteredlist);
            }else {
                setList(filteredlist);
            }
        }

        return filteredlist  ;
    }


    //Perform filter by date
    public ArrayList<Reunion> filterByDate(String text) {

        filteredlist = new ArrayList<Reunion>();

        for (Reunion item : mMeetings) {
            if (item.getMeetingDate().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);

            }
        }
        if (filteredlist.isEmpty()) {

            setList(filteredlist);
        } else {

            setList(filteredlist);
        }
        return filteredlist;
    }




    //set filtered list
    public void setList(ArrayList<Reunion> choosedList) {
        recyclerViewAdapter = new RecyclerViewAdapter(choosedList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }



}
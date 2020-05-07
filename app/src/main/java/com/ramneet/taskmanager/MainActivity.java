package com.ramneet.taskmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ramneet.taskmanager.model.Status;
import com.ramneet.taskmanager.model.Task;
import com.ramneet.taskmanager.model.TaskManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Task> myTasks = TaskManager.getInstance();
    ArrayAdapter<Task> adapter;
    DataBaseHelper dbHelp = new DataBaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getData();
        populateListView();
        registerClickCallbackListView();
        setupFab();
    }


    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public void getData()
    {
        Cursor cursor = dbHelp.fetchData();
        //String[] taskData =  new String[]{dbHelp._ID,dbHelp.COLUMN_1,dbHelp.COLUMN_2, dbHelp.COLUMN_3, dbHelp.COLUMN_4};

        try {
            if (cursor.moveToFirst()) {
                do {
                    String task = cursor.getString(cursor.getColumnIndex(dbHelp.COLUMN_1));
                    String note = cursor.getString(cursor.getColumnIndex(dbHelp.COLUMN_2));

                    String dateString = cursor.getString(cursor.getColumnIndex(dbHelp.COLUMN_3));
                    Date date = convertToDate(dateString);
                    Status status = convertToStatus(cursor.getString(cursor.getColumnIndex(dbHelp.COLUMN_4)));
                    Task newTask = new Task(task, note, date,status);
                    Log.d("Main Activity", "Added task: " + newTask.getTask());
                    myTasks.add(newTask);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("Main Activity", "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    private Date convertToDate(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    private Status convertToStatus(String status) {
        if (status.equalsIgnoreCase("active")){
            return Status.ACTIVE;
        }
        else {
            return Status.COMPLETED;
        }
    }

    private void populateListView() {
        // Build Adapter
        adapter = new myListAdapter();

        // Configure the list view.
        ListView listView = findViewById(R.id.listViewMain);
        listView.setAdapter(adapter);
    }

    private void registerClickCallbackListView() {
        ListView list = findViewById(R.id.listViewMain);
//        list.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task clickedTask = myTasks.get(position);

                String message = "You clicked position " + position
                        + " which is task " + clickedTask.getTask();
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Add new task.", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent i = AddTask.makeLaunchIntent(MainActivity.this);
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class myListAdapter extends ArrayAdapter<Task> {
        public myListAdapter() {
            super(MainActivity.this, R.layout.tasks_item_view, myTasks);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            // make sure we have a view to work with, may have been given null
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.tasks_item_view, parent, false);
            }

            // Find the lens to work with.
            final Task currTask = myTasks.get(position);

            Typeface custom_title_font = Typeface.createFromAsset(getAssets(), "fonts/LEMONMILK-LightItalic.ttf");
            final Typeface custom_body_font = Typeface.createFromAsset(getAssets(),"fonts/aliquam.ttf");

            // Fill the view.
            final ImageButton imgBtn = itemView.findViewById(R.id.item_statusImg);

            final View finalItemView = itemView;
            imgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // setup the alert builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Set task status: ");

                    // add a radio button list
                    String[] choices = {getString(R.string.statusTxt_active), getString(R.string.statusTxt_completed)};
                    int checkedItem = 1;
                    if (currTask.getStatus() == Status.ACTIVE) {
                        checkedItem = 0;
                    }

                    builder.setSingleChoiceItems(choices, checkedItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TextView statusTxt = finalItemView.findViewById(R.id.item_status);

                            // user checked an item
                            if (which == 0) {
                                currTask.setStatus(Status.ACTIVE);
                                statusTxt.setText(R.string.statusTxt_active);
                                statusTxt.setTypeface(custom_body_font);

                                imgBtn.setImageDrawable(getDrawable(R.drawable.active_status_img));
                            } else {
                                currTask.setStatus(Status.COMPLETED);
                                statusTxt.setText(R.string.statusTxt_completed);
                                statusTxt.setTypeface(custom_body_font);
                                imgBtn.setImageDrawable(getDrawable(R.drawable.completed_status_img));
                            }
                        }
                    });

                    // add OK button
                    builder.setPositiveButton("OK", null);

                    // create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            if (currTask.getStatus() == Status.ACTIVE) {
                imgBtn.setImageDrawable(getDrawable(R.drawable.active_status_img));
            } else {
                imgBtn.setImageDrawable(getDrawable(R.drawable.completed_status_img));
            }

            // Task:
            TextView taskText = itemView.findViewById(R.id.item_task);
            taskText.setText(currTask.getTask());
            taskText.setTypeface(custom_title_font);
            //taskText.setPadding(5,5,5,5);
            taskText.setTextSize(17);

            // Note:
            TextView noteText = itemView.findViewById(R.id.item_note);
            noteText.setText(currTask.getNote());
            noteText.setTypeface(custom_body_font);
            noteText.setTextSize(14);

            // Status:
            TextView statusText = itemView.findViewById(R.id.item_status);
            String status = "";
            switch (currTask.getStatus()) {
                case ACTIVE:
                    status = status + "Active";
                    break;
                case COMPLETED:
                    status = status + "Completed";
                    break;
            }
            statusText.setText(status);
            statusText.setTypeface(custom_body_font);
            statusText.setTextSize(14);

            // Date:
            TextView dateText = itemView.findViewById(R.id.item_date);
            Date date = currTask.getDate();
            String formattedDate = new SimpleDateFormat("MM/dd/yyyy").format(date);
            dateText.setText(formattedDate);
            dateText.setTypeface(custom_body_font);
            dateText.setTextSize(14);

            return itemView;
        }



    }

}

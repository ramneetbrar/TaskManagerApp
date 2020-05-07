package com.ramneet.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.ramneet.taskmanager.model.Task;
import com.ramneet.taskmanager.model.TaskManager;

import java.util.Date;
import java.util.List;

public class AddTask extends AppCompatActivity {

    List<Task> myTasks = TaskManager.getInstance();
    DataBaseHelper dbHelp = new DataBaseHelper(this);

    public static Intent makeLaunchIntent(Context context) {
        Intent intent = new Intent(context, AddTask.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        setupSaveBtn();
        setupCancelBtn();
    }

    private void setupSaveBtn() {
        Button saveBtn = findViewById(R.id.addTask_saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText taskInput = findViewById(R.id.addTask_editTask);
                String taskInputVal = taskInput.getText().toString();

                EditText noteInput = findViewById(R.id.addTask_editNote);
                String noteInputVal = noteInput.getText().toString();

                Task task = new Task(taskInputVal, noteInputVal, new Date(System.currentTimeMillis()) );
                myTasks.add(task);

                Snackbar.make(v, "Task Created: " + taskInputVal, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                dbHelp.addData(taskInputVal, noteInputVal, new Date(System.currentTimeMillis()).toString(), "Active");

                finish();
            }
        });
    }

    private void setupCancelBtn() {
        Button cancelBtn = findViewById(R.id.addTask_cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

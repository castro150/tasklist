package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.app.MESSAGE";
    public static final String EXTRA_TASK_ID = "com.example.app.TASK_ID";
    public static final String EXTRA_TASK_NAME = "com.example.app.TASK_NAME";

    private static final String PENDING_TASKS_URL = "http://awesome-tasklist-server.herokuapp.com/api/tasks?pending=true";

    private HttpRequest http = new HttpRequest();
    private LinearLayout linearLayout;
    private ArrayList<Task> tasks = new ArrayList<>();

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linearLayout = this.linearLayout();
        setContentView(linearLayout);

        final Handler handler = new Handler();
        handler.postAtTime(new Runnable() {
            @Override
            public void run() {
                requestToUpdate();
                handler.postDelayed(this, 5000);
            }
        }, 5000);
    }

    private void requestToUpdate() {
        System.out.println("REQUESTING TO UPDATE");
        http.get(PENDING_TASKS_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    MainActivity.this.setTasks(Task.fromJsonArrayString(body));

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateViews();
                        }
                    });
                } else {
                    System.out.println("REQUEST ERROR");
                }
            }
        });
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    private LinearLayout linearLayout() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(16, 16, 16, 16);

        return linearLayout;
    }

    private void updateViews() {
        this.linearLayout.removeAllViews();
        for (Task task : this.tasks) {
            final String taskId = task.getId().toString();
            final String taskName = task.getName();

            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = vi.inflate(R.layout.task_layout, null);

            TextView textView = view.findViewById(R.id.taskName);
            textView.setText(task.getName());

            Button doButton = view.findViewById(R.id.doTask);
            doButton.setText(R.string.do_task);
            doButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, DoTaskActivity.class);
                    intent.putExtra(EXTRA_TASK_ID, taskId);
                    intent.putExtra(EXTRA_TASK_NAME, taskName);
                    startActivity(intent);
                }
            });

            this.linearLayout.addView(view, 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        setContentView(this.linearLayout);
    }
}

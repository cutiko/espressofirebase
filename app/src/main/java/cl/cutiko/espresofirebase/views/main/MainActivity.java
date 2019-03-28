package cl.cutiko.espresofirebase.views.main;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import cl.cutiko.espresofirebase.R;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Presenter(this).getUserTasks();
    }

    @Override
    public void setTasksNumber(int count) {
        TextView tv = findViewById(R.id.taskCountTv);
        String text = getResources().getQuantityString(R.plurals.tasks_plurals, count, count);
        tv.setText(text);
    }
}

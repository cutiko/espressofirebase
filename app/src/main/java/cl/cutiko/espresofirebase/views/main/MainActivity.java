package cl.cutiko.espresofirebase.views.main;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import cl.cutiko.espresofirebase.R;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.taskCountTv);
        new Presenter(this).getUserTasks();
    }

    @Override
    public void setTasksNumber(int count) {
        String text = getResources().getQuantityString(R.plurals.tasks_plurals, count, count);
        tv.setText(text);
    }

    @Override
    public void error() {
        tv.setText(R.string.error);
    }
}

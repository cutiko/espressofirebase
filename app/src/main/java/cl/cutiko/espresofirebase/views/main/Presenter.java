package cl.cutiko.espresofirebase.views.main;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import androidx.annotation.NonNull;
import cl.cutiko.espresofirebase.data.Nodes;

public class Presenter implements MainContract.Presenter, ValueEventListener {

    private final MainContract.View callback;

    public Presenter(MainContract.View callback) {
        this.callback = callback;
    }

    @Override
    public void getUserTasks() {
        new Nodes().userTasks().addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
            int count = 0;
            while (iterator.hasNext()) {
                count++;
                iterator.next();
            }
            callback.setTasksNumber(count);
            return;
        }
        callback.setTasksNumber(0);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}

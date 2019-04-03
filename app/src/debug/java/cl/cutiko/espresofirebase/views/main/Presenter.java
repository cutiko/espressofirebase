package cl.cutiko.espresofirebase.views.main;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import androidx.annotation.NonNull;
import androidx.test.espresso.idling.CountingIdlingResource;

public class Presenter extends MainPresenter {

    private static final String MAIN_PRESENTER = "cl.cutiko.espresofirebase.views.main.Presenter.key.MAIN_PRESENTER";
    public static final CountingIdlingResource idling = new CountingIdlingResource(MAIN_PRESENTER);

    public Presenter(MainContract.View callback) {
        super(callback);
    }

    @Override
    public void getUserTasks() {
        super.getUserTasks();
        idling.increment();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        super.onDataChange(dataSnapshot);
        idling.decrement();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        super.onCancelled(databaseError);
        idling.decrement();
    }
}

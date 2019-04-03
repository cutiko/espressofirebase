package cl.cutiko.espresofirebase.views.main;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import androidx.annotation.NonNull;
import cl.cutiko.espresofirebase.data.Nodes;

public class Presenter extends MainPresenter {

    public Presenter(MainContract.View callback) {
        super(callback);
    }
    
}

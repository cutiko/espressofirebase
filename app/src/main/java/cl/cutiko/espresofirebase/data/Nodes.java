package cl.cutiko.espresofirebase.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Nodes {

    private final DatabaseReference root = FirebaseDatabase.getInstance().getReference();


    public DatabaseReference userTasks() {
        return root.child("tasks").child(new CurrentUser().getUid());
    }

}

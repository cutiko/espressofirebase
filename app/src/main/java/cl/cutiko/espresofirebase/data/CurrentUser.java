package cl.cutiko.espresofirebase.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CurrentUser {

    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public boolean isLogged() {
        return user != null;
    }

    public String getUid() {
        return user.getUid();
    }

}

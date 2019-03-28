package cl.cutiko.espresofirebase.views.login;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;

import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cl.cutiko.espresofirebase.R;
import cl.cutiko.espresofirebase.views.main.MainActivity;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private static final int RC_LOGIN = 343;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        new Presenter(this).authValidation();

        /*Quick way to add some tasks, remove comments only after user is logged*/
        /*Map<String, String> map = new HashMap<>();
        DatabaseReference reference = new Nodes().userTasks();
        map.put(reference.push().getKey(), "one");
        map.put(reference.push().getKey(), "two");
        map.put(reference.push().getKey(), "three");
        reference.setValue(map);*/
    }

    @Override
    public void notLogged() {
        List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
                RC_LOGIN
        );
    }

    @Override
    public void logged() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RC_LOGIN == requestCode && RESULT_OK == resultCode) {
            logged();
        } else {
            notLogged();
        }
    }
}

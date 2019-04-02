package cl.cutiko.espresofirebase;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.runner.RunWith;

import androidx.annotation.NonNull;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import cl.cutiko.espresofirebase.data.CurrentUser;

import static androidx.test.espresso.Espresso.onIdle;
import static junit.framework.TestCase.fail;

@RunWith(AndroidJUnit4.class)
public abstract class FireBaseTest implements OnCompleteListener<AuthResult> {

    private static final String IDLING_NAME = "cl.cutiko.espresofirebase.FireBaseTest.key.IDLING_NAME";
    private static final CountingIdlingResource idlingResource = new CountingIdlingResource(IDLING_NAME);

    @Before
    public void prepare() {
        final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        int apps = FirebaseApp.getApps(context).size();
        if (apps == 0) {
            Log.d("CUTIKO_TAG", "FireBaseTest.java" + " prepare: inside");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setApiKey(BuildConfig.apiKey)
                    .setApplicationId(BuildConfig.applicationId)
                    .setDatabaseUrl(BuildConfig.databaseUrl)
                    .setProjectId(BuildConfig.projectId)
                    .build();
            FirebaseApp.initializeApp(context, options);
        }
        if (!new CurrentUser().isLogged()) {
            IdlingRegistry.getInstance().register(idlingResource);
            FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword("test@app.io", "12345678")
                    .addOnCompleteListener(this);
            idlingResource.increment();
            onIdle();
        }
    }


    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            idlingResource.decrement();
        } else {
            fail("The user was not logged successfully");
        }
    }
}

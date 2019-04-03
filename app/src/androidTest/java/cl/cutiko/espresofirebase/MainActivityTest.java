package cl.cutiko.espresofirebase;

import android.content.Context;
import android.content.res.Resources;

import org.junit.Test;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.platform.app.InstrumentationRegistry;
import cl.cutiko.espresofirebase.views.main.MainActivity;
import cl.cutiko.espresofirebase.views.main.Presenter;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class MainActivityTest extends FireBaseTest {

    @Test
    public void taskCountTvTextTest() {
        IdlingRegistry.getInstance().register(Presenter.idling);
        ActivityScenario.launch(MainActivity.class);
        final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Resources resources = context.getResources();
        String text = resources .getQuantityString(R.plurals.tasks_plurals, 3, 3);
        onView(withId(R.id.taskCountTv)).check(matches(withText(text)));
    }

}

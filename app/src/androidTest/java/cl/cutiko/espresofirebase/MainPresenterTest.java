package cl.cutiko.espresofirebase;

import org.junit.Test;

import androidx.test.espresso.IdlingRegistry;
import cl.cutiko.espresofirebase.views.main.MainContract;
import cl.cutiko.espresofirebase.views.main.Presenter;

import static androidx.test.espresso.Espresso.onIdle;
import static junit.framework.Assert.fail;
import static junit.framework.TestCase.assertEquals;

public class MainPresenterTest extends FireBaseTest implements MainContract.View {

    private final Presenter presenter = new Presenter(this);

    @Test
    public void testGetUserTasks() {
        IdlingRegistry.getInstance().register(Presenter.idling);
        presenter.getUserTasks();
        onIdle();
    }

    @Override
    public void setTasksNumber(int count) {
        assertEquals(3, count);
    }

    @Override
    public void error() {
        fail("Database Error");
    }
}

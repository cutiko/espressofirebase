package cl.cutiko.espresofirebase.views.main;

public interface MainContract {

    interface View {
        void setTasksNumber(int count);
        void error();
    }

    interface Presenter {
        void getUserTasks();
    }

}

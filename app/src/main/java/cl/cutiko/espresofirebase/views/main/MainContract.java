package cl.cutiko.espresofirebase.views.main;

public interface MainContract {

    interface View {
        void setTasksNumber(int count);
    }

    interface Presenter {
        void getUserTasks();
    }

}

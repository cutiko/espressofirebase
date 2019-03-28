package cl.cutiko.espresofirebase.views.login;

public interface LoginContract {

    interface View {
        void notLogged();
        void logged();
    }

    interface Presenter {
        void authValidation();
    }
}

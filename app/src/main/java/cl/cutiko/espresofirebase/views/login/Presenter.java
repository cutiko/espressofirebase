package cl.cutiko.espresofirebase.views.login;

import cl.cutiko.espresofirebase.data.CurrentUser;

public class Presenter implements LoginContract.Presenter {

    private final LoginContract.View callback;

    public Presenter(LoginContract.View callback) {
        this.callback = callback;
    }


    @Override
    public void authValidation() {
        if (new CurrentUser().isLogged()) {
            callback.logged();
        } else {
            callback.notLogged();
        }
    }
}

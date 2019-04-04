# Espresso and Firebase

This is an example repo that cover the basis for testing with Espresso a project using Firebase.
There are some steps that can be achieved in a different manner, those are indicated. This project,
covers specifically how to solve the Firebase problems and make it play along with Espresso.

In the following you'll find how to setup the project and the step by step process.

## Overview
This project is using `androidx` if your project has not migrated yet the general approach should still be valid, you will need to change the reference accordingly.

In this momement there is an [issue](https://github.com/firebase/FirebaseUI-Android/issues/1445) with Firebase dependencies and `google-services:4.1.0` so please make sure you are using at least:

 - [In the project gradle](https://github.com/cutiko/espressofirebase/blob/master/build.gradle) `classpath 'com.google.gms:google-services:4.2.0'`
 
 - [And in the app module gradle](https://github.com/cutiko/espressofirebase/blob/master/app/build.gradle)
 
```
implementation 'com.firebaseui:firebase-ui-auth:4.3.2'
implementation 'com.google.firebase:firebase-core:16.0.8'
implementation 'com.google.firebase:firebase-database:16.1.0'
```

If you want to look at the final result you can switch to the workshop branch.

The project uses firebase-ui-auth for creating a quick login using email and then will move forward to another screen where a query to the RTD is performed and the UI is updated. The project us the MVP pattern.

## Setup
This will guide you to run the basic app:

 1. Clone the project, fork it, however you feel more comfortable
 2. You have to link the project with a Firebase project of your own, you can use the Android Studio Assistant or do it manually. The file **google-services.json** is not provided and for public should be ignored from VCS.
 3. Make sure you have created the Real-Time Database for the Firebase project
 4. This are the database rules you need
 
 ```
 {
  "rules": {
    "tasks": {
      "$uid": {
        ".read": "auth.uid === $uid",
        ".write": "auth.uid === $uid"
      }
    }
  }
}
```

 5. Run the app
 6. Create a user with the email `test@app.io` and the password `12345678` (this is our test user)
 7. After the login in the `MainActivity` you should see 0 tasks
 8. Now go the Firebase web console and in the Authentication section copy your user UID
 9. Replace the UID in the following data, create a Json file with it, and upload it to the RTD
 
```
{
  "tasks": {
    "REPLACE_THIS_WITH_YOUR_UID": {
      "-Lb4zAYdzn53BIsG4XcX": "one",
      "-Lb4zAYdzn53BIsG4XcY": "two",
      "-Lb4zAYdzn53BIsG4XcZ": "three"
    }
  }
}
```

 10. Restart the app, the user should be already logged and you will see 3 tasks.

# Workshop
The following is the step by step to build your Espresso tests, you can switch to a new branch or work on master, whatever suits you.


### Dependencies

 1. Go to your `gradle app` and add the depencies

```
android {
    ...
    defaultConfig {
        ...
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
        ...
    }
    ...
}

dependencies {
   ...
   testImplementation 'junit:junit:4.12'
   androidTestImplementation 'androidx.test.ext:junit:1.1.0'
   androidTestImplementation 'androidx.test.espresso:espresso-core:3.1.1'
   debugImplementation 'androidx.test.espresso:espresso-idling-resource:3.1.1'
}
```

This are mostly the same instructions that you could fing in the [Android Documentatio](https://developer.android.com/training/testing/espresso/setup#add-espresso-dependencies) with a differences for our use case:

 - We are adding the `espresso-idling-resource` as a debug dependency because we don't want to carry it to the release version of the app but still be able to use in the our testing, we are gonna use build variants for this later (more info later as well)

### Firebase Credentials
**When we move to create the tests, we will find 2 issues, the initializing the FirebaseApp and login the user**, we have partially take care of the second by creating a test user (test@app.io) in the [6 step of the setup](https://github.com/cutiko/espressofirebase#setup). We now have to take care of what we need for initializing the FirebaseApp manually.

 1. In the root directory of the project create a file called `firebase.properties` if you are using the Android view you can change it to project or just do it using the explorer. **You need to ignore that file from version control if you are planning to plublish the repo**
 2. Open your `google-services.json`
 3. This is the content of the `firebase.properties` file:
 
```
applicationId="1:REPLACE_THIS:android:NUMBERS_AND_LETTERS"
apiKey="AIzaREPLACE_THIS_SOME_RANDOM_CHARACTERS"
projectId="REPLACE_THIS_IS_THE_NAME_OF_YOUR_PROJECT"
databaseUrl="REPLACE_THIS_IS_THE_URL_OF_YOUR_PROJECT"
```
 4. You have to replace every text with the equivalent in the `google-services.json` file, the names are mostly the same
 5. No we have to make this credentials available for our project, we will do this using the `buildConfigField` in  the module app gradle:
 
```
android {
    ...
    buildTypes {
       ...
       //You need to add this buildVariant it is always used for default, but now we needed explicitly
        debug {
            //This read the file we created
            def fireFile = rootProject.file("firebase.properties")
            def fireProperties = new Properties()
            fireProperties.load(new FileInputStream(fireFile))
        
            //This will get each key and make it available for the project
            buildConfigField "String", "applicationId", fireProperties['applicationId']
            buildConfigField "String", "apiKey", fireProperties['apiKey']
            buildConfigField "String", "projectId", fireProperties['projectId']
            buildConfigField "String", "databaseUrl", fireProperties['databaseUrl']
        }
        ...
    }
}
```

If you are confuse here is the the [workshop file](https://github.com/cutiko/espressofirebase/blob/workshop/app/build.gradle)

### Creating the base test
As it was said, when try to test a project using Firebase we will have to 2 problems:

 - Initializing the FirebaseApp
 - Login in an user

 1. Go to the `androidTest` directory, in this case is `cl.cutiko.espresofirebase (androidTest)`
 2. Create an abstract class called `FireBaseTest`
 3. Annotate your class with @RunWith(AndroidJUnit4.class) (this annotation is inherit so we can use it here)
 4. Implements `OnCompleteListener<AuthResult>`
 5. This is how the base test should look
 
 
 ```
    private static final String IDLING_NAME = "cl.cutiko.espresofirebase.FireBaseTest.key.IDLING_NAME";
    private static final CountingIdlingResource idlingResource = new CountingIdlingResource(IDLING_NAME);

    @Before
    public void prepare() {
        final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        int apps = FirebaseApp.getApps(context).size();
        if (apps == 0) {
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
 ```
 - We are using the `@Before` annotation for making sure everything we need in our Firebase test to be ready before the tests. I don't recommend do this in the `@BeforeClass` because that annotation force the method to be `static` wich can turn inconvenient.
 - Since we are using the `@Before` annotation, this would happen everytime before each `@Test` method, that is why there are 2 validations, in case our tests files have more than 1 test.
 - The first we are doing is to make sure the `FirebaseApp` is not initialized previously. In our case **it will be initialized**, so why are we adding that code? The `apply plugin: 'com.google.gms.google-services'` can only be in the module app gradle, it takes care of reading the `google-services.json` file and initialize the `FirebaseApp`, so no need for it as long as the project is a mono-module. Any multi-module project will have to solve this problem. As you can see the manual initializtion is using the credentials we exposed as the `buildConfigField` on the gradle.
 - The second is to check if the user is logged or not, if it is not logged, then we have to logged, and only move forward when the loggin is completed. So if the user is not logged in, we are signin with the email and password we create for our test user.

This is the first time we are introducing the `IdlingResource`, that is the Espresso library we add for `debugImplementation`, it'll be good to take some lines to explained it further

#### IdlingResource
You can find the official [Android Documentation here](https://developer.android.com/training/testing/espresso/idling-resource), every sample for [Espresso is here](https://github.com/googlesamples/android-testing) and [this is the specific sample](https://github.com/googlesamples/android-testing/tree/master/ui/espresso/IdlingResourceSample) for `IdlingResources`. **Is strongly recommended to see the IdlingResourceSample**.

IdlingResource works like a semaphore of work, Espresso use *magic* to know when there is work been done and has to wait and then when the work has completed to continue with the tests.

 1. Always register your IdlingResource
 2. Use the IdlingResource to notify there is work, Espresso will wait
 3. Use the IdlingResource to notify work is done, Espresso will continue the tests
 4. If there are no more test, make sure to use `onIdle`
 5. It is recommended to unregister your IdlingResource
 
In this case we are using a `CountingIdlingResource` when the work start we increase it and when the work is done we decrease it. You can do yout own IdlingResource by extending the Espresso classes, but for our purpouse this will work. The only warning is the `CountingIdlingResource` can be corrupted if the decreasing happens more times than the increasing (lower than zero).

This could be done by other means, but this is the minimal approach:

 - `CountDownLatch` having a framework to compare, is considered bad practice, take a look at this [SO answer](https://stackoverflow.com/a/3802487/4017501)
 - Using RxFirebase, look at this [SO answer](https://stackoverflow.com/a/49572189/4017501)
 - This could be done using Kotlin Coroutines, you can take a look at this [Google Lab](https://codelabs.developers.google.com/codelabs/kotlin-coroutines/#6)

##### Summary
The `@Before` method will be hold untill the login is succesfull or fails using the `CountingIdlingResource`, and after that our tests will run, no race conditions.

### Refactor Presenter to use IdlingResource
The presenter is in charge of the workload, so one way to use the IdlingResource is to use it there, indicating when the work start and when the work finished. The problem is we don't need to have the IdlingResource on the release apk, remember we add the dependency as `debugImplementation` [at the start](https://github.com/cutiko/espressofirebase#dependencies). So what we will do is to have a presenter for debug and another for release. The debug presenter will include the usage of IdlingResource while the release debug will not add any extra behaviour. Our current presenter is on the `main` directory, so we will have to refactor it to make it `abstract`, then extend it on the build variant versions, and fix the reference in the `MainActivity`.

 1. Open the `Presenter` refactor it to be called `MainPresenter`
 2. Make it abstract
 
 ```
 public abstract class MainPresenter implements MainContract.Presenter, ValueEventListener {

    public MainPresenter(MainContract.View callback) {
        this.callback = callback;
    }
}
```

 3. Now we have to create the presenters for each build variant, if the Android Studio directory tree confuses you, change it to project or use the file explorer of the OS, your directory should look like the example:

```
espressofirebase/app/src
├── androidTest
|
├── debug
|  └── java
|     └── cl
|        └── cutiko
|           └── espresofirebase
|              └── views
|                 └── main
|                    └── Presenter.java
|
├── main
|  ├── java
|     └── cl
|        └── cutiko
|           └── espresofirebase
|              ├── data
|              └── views
|                 ├── login
|                 └── main
|                    ├── MainActivity.java
|                    ├── MainContract.java
|                    └── MainPresenter.java
|
├── release
|  └── java
|     └── cl
|        └── cutiko
|           └── espresofirebase
|              └── views
|                 └── main
|                    └── Presenter.java
|
└── test
```

 4. The `Presenter.java` in the release version will only extend the `MainPresenter`
 
```
public class Presenter extends MainPresenter
//Don't forget the mandatory constructor
```
 
 5. The `Presenter.java` in the Ddebug version will extend the `MainPresenter` and will use the IdlingResource. We are gonna make the IdlingResource easily accesible to make the `IdlingRegistry` easier and we have to override the methods that start the work and end the work to add the IdlingResoruce correspondingly
 
```
public class Presenter extends MainPresenter {

    private static final String MAIN_PRESENTER = "cl.cutiko.espresofirebase.views.main.Presenter.key.MAIN_PRESENTER";
    public static final CountingIdlingResource idling = new CountingIdlingResource(MAIN_PRESENTER);

    public Presenter(MainContract.View callback) {
        super(callback);
    }

    @Override
    public void getUserTasks() {
        super.getUserTasks();
        idling.increment();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        super.onDataChange(dataSnapshot);
        idling.decrement();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        super.onCancelled(databaseError);
        idling.decrement();
    }
}
```

6. Now go to the `MainActivity.class` and change the reference to `Prensenter.java`

```
public class MainActivity extends AppCompatActivity implements MainContract.View {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.taskCountTv);
        //Here, fix this
        new Presenter(this).getUserTasks();
    }
    //More code below
}
```

### Creating the Presenter test
Now that we have all the parts we need to put them together. Go to the `androidTest` and create the following class and methods

```
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
```

 - Always remember to register the IdlingResource, in this case we can easily get it from the presenter
 - The presenter inner work will use the IdlingResource to indicate when the work starts and end
 - **We have to use onIdle() method**, after `presenter.getUserTasks()` there are no more Espreso test, so there we have to use `onIdle()` otherwise it won't work
 - Remember we have 3 tasks in our RTD, however the test could have been writen differently

### Creating the UI test
For creating the UI test of the `MainActivity` is pretty much the same, we do have to be aware of some nuances. We can't use `@Rule` annotation because the activity is starting the presenter on the `onCreate` and `@Rule` would happen before `@Before`. So we have to launch the Activity in the test

```
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
```

 - Please notice that we don't need to use `onIdle()` this time because after the async work has started there are more Espresso tests, so Espreso is aware of the IdlingResource nad know it has to wait.
 - As usual we **have to register the IdlingResource** again, we can easily obtained from the `Presenter`

### Firebase Test Lab
For using the Firebase Test Lab select the other tab when launching the tests. You should know a couple of things:

 - The configurations proposed there have plenty of unexistent devices, is better to create a device matrix direcly on the web console
 - The device Matrix created and your project can take some time to show up, don't know why, usually one day is enough

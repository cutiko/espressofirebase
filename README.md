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
 2. You have to link the project with a Firebase project of your own, you can use the Android Studio Assistant or do it manually. The file **google-services.json** is not provided.
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

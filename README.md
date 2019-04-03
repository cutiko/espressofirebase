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

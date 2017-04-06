package com.task.vasskob.firebase.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.task.vasskob.firebase.Constants;
import com.task.vasskob.firebase.model.Coordinates;
import com.task.vasskob.firebase.model.Session;
import com.task.vasskob.firebase.model.User;

public class FirebaseOperations {

    public static DatabaseReference getInstanceRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static void cleanDb(String uid) {
        getRefForCoordChild(uid).removeValue();
        getRefForSesChild(uid).removeValue();
    }

    public static void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    public static FirebaseAuth signInRef() {
       return FirebaseAuth.getInstance();
    }

    public static String getChildKey(String child) {
        return getInstanceRef().child(child).push().getKey();
    }

    private static DatabaseReference getRefForCoordChild(String child2) {
        return getInstanceRef().child(Constants.USERS_SESSIONS_COORDINATES).child(child2);
    }

    public static DatabaseReference getRefForCoordChild(String child2, String child3) {
        return getRefForCoordChild(child2).child(child3);
    }

    public static DatabaseReference getRefForSesChild(String child2) {
        return getInstanceRef().child(Constants.SESSIONS).child(child2);
    }

    public static void sendCoordinatesToDb(String child2, String child3, String child4, Coordinates coordinates) {
        getRefForCoordChild(child2, child3).child(child4).setValue(coordinates.toMap());
    }

    public static void sendSessionToDb(String child2, String child3, Session session) {
        getRefForSesChild(child2).child(child3).setValue(session.toMap());
    }

    public static void CreateNewUser(String tableName, String userId, User user) {
        getInstanceRef().child(tableName).child(userId).setValue(user);
    }
}


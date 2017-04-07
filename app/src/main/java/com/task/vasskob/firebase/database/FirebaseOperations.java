package com.task.vasskob.firebase.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.task.vasskob.firebase.Constants;
import com.task.vasskob.firebase.model.Coordinates;
import com.task.vasskob.firebase.model.Session;
import com.task.vasskob.firebase.model.User;

public class FirebaseOperations {

    String coordinateKey = FirebaseOperations.getChildKey(Constants.COORDINATES);

    public static DatabaseReference getInstanceRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static void cleanDb(String uid) {
        getRefForCoordChild(uid).removeValue();
        getRefForSesChild(uid).removeValue();
        getRefForUsersChild(uid).removeValue();
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

    public static DatabaseReference getRefForCoordChild(String userId, String sessionId) {
        return getRefForCoordChild(userId).child(sessionId);
    }

    private static DatabaseReference getRefForCoordChild(String userId) {
        return getInstanceRef().child(Constants.USERS_SESSIONS_COORDINATES).child(userId);
    }

    public static DatabaseReference getRefForSesChild(String userId) {
        return getInstanceRef().child(Constants.SESSIONS).child(userId);
    }

    public static DatabaseReference getRefForUsersChild(String userId) {
        return getInstanceRef().child(Constants.USERS).child(userId);
    }
    public static void sendCoordinatesToDb(String userId, String sessionId, String coordId, Coordinates coordinates) {
        getRefForCoordChild(userId, sessionId).child(coordId).setValue(coordinates.toMap());
    }

    public static void sendSessionToDb(String userId, String sessionId, Session session) {
        getRefForSesChild(userId).child(sessionId).setValue(session.toMap());
    }

    public static void CreateNewUser(String tableName, String userId, User user) {
        getInstanceRef().child(tableName).child(userId).setValue(user);
    }
}


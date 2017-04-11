package com.task.vasskob.firebase.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.task.vasskob.firebase.Constants;
import com.task.vasskob.firebase.model.Coordinates;
import com.task.vasskob.firebase.model.Session;
import com.task.vasskob.firebase.model.User;

public class FirebaseOperations {

    private static DatabaseReference getInstanceRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static void CreateNewUser(User user) {
        getInstanceRef().child(Constants.USERS).push().setValue(user);
    }

    public static void cleanDb(String uid) {
        getRefForCoordChild(uid).removeValue();
        getRefForSesChild(uid).removeValue();
        // TODO: 11/04/17 why you need to clear user data
        getRefForUsersChild(uid).removeValue();
    }

    public static void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    public static FirebaseAuth signInRef() {
        return FirebaseAuth.getInstance();
    }

    public static String getSessionKey() {
        return getInstanceRef().child(Constants.SESSIONS).push().getKey();
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

    private static DatabaseReference getRefForUsersChild(String userId) {
        return getInstanceRef().child(Constants.USERS).child(userId);
    }

    public static void sendCoordinatesToDb(String userId, String sessionId, Coordinates coordinates) {
        getRefForCoordChild(userId, sessionId).push().setValue(coordinates.toMap());
    }

    public static void sendSessionToDb(String userId, String sessionId, Session session) {
        getRefForSesChild(userId).child(sessionId).setValue(session.toMap());
    }

    public static void uploadFileToFirebase(String filePath) {


    }

    public static StorageReference getStorageRef(String child1, String child2) {
        return FirebaseStorage.getInstance().getReference().child(child1).child(child2);
    }


}


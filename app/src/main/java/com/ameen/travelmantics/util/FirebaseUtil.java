package com.ameen.travelmantics.util;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.Toast;

import com.ameen.travelmantics.model.ItemModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtil {

    private static final int RC_SIGN_IN = 123;
    private static FirebaseUtil firebaseUtil;
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseRef;
    public static FirebaseStorage mFirebaseStorage;
    public static StorageReference mFirebaseStorageReference;
    public static FirebaseAuth mFirebaseAuth;
    public static FirebaseAuth.AuthStateListener mAuthStateListener;
    public static List<ItemModel> items;
    private static Activity caller;
    public static boolean isAdmin;

    private FirebaseUtil() {
    }

    public static void openFbReference(String reference, final Activity callerActivity) {
        if (firebaseUtil == null) {
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseAuth = FirebaseAuth.getInstance();
            caller = callerActivity;
            mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    if (firebaseAuth.getCurrentUser() == null)
                        sign();

                    else {
                        String userId = firebaseAuth.getUid();
                        checkAdmin(userId);
                    }
                }
            };
            connectStorage();
        }

        items = new ArrayList<ItemModel>();
        mDatabaseRef = mFirebaseDatabase.getReference().child(reference);
    }

    private static void checkAdmin(String userId) {
        FirebaseUtil.isAdmin = false;
        DatabaseReference ref = mFirebaseDatabase.getReference().child("admin").child(userId);
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FirebaseUtil.isAdmin = true;
                caller.invalidateOptionsMenu();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addChildEventListener(listener);
    }

    private static void sign() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    public static void attachListener() {
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public static void detachListener() {
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    public static void connectStorage(){
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseStorageReference = mFirebaseStorage.getReference().child("item-images");
    }

    public static void logOut() {
        AuthUI.getInstance()
                .signOut(caller.getApplicationContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(caller.getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
                        attachListener();
                    }
                });
    }
}

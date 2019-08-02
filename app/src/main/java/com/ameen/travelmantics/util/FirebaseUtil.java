package com.ameen.travelmantics.util;

import com.ameen.travelmantics.model.ItemModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUtil {

    private static FirebaseUtil firebaseUtil;
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseRef;
    public static List<ItemModel> items;

    private FirebaseUtil(){}

    public static void openFbReference(String reference){
        if(firebaseUtil == null){
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
        }

        items = new ArrayList<ItemModel>();
        mDatabaseRef = mFirebaseDatabase.getReference().child(reference);
    }
}

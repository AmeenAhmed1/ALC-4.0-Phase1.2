package com.ameen.travelmantics.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ameen.travelmantics.R;
import com.ameen.travelmantics.model.ItemModel;
import com.ameen.travelmantics.util.FirebaseUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHolder> {

    List<ItemModel> itemsList;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;


    public RecyclerAdapter() {

        FirebaseUtil.openFbReference("itemdeals");
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseRef;
        itemsList = FirebaseUtil.items;
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ItemModel itemModel = dataSnapshot.getValue(ItemModel.class);
                itemModel.setId(dataSnapshot.getKey());

                itemsList.add(itemModel);
                notifyItemInserted(itemsList.size() - 1);
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
        mDatabaseReference.addChildEventListener(mChildEventListener);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        //Get the recycler item layout
        return
                new MyHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.recycler_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int position) {

        //Setting fetched data
        myHolder.bind(itemsList.get(position));
    }

    @Override
    public int getItemCount() {

        //Return the list size
        return itemsList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView textTitle, textDesc, textPrice;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //Attach the references
            textTitle = itemView.findViewById(R.id.item_title);
            textDesc = itemView.findViewById(R.id.item_desc);
            textPrice = itemView.findViewById(R.id.item_prise);
        }

        public void bind(ItemModel itemModel) {
            textTitle.setText(itemModel.getTitle());
            textDesc.setText(itemModel.getDesc());
            textPrice.setText(itemModel.getPrice());
        }
    }
}

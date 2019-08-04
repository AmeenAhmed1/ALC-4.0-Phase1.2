package com.ameen.travelmantics.adapter;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ameen.travelmantics.R;
import com.ameen.travelmantics.model.ItemModel;
import com.ameen.travelmantics.ui.InsertActivity;
import com.ameen.travelmantics.util.FirebaseUtil;
import com.bumptech.glide.Glide;
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

        //FirebaseUtil.openFbReference("itemdeals");
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

    public class MyHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView textTitle, textDesc, textPrice;
        ImageView imgView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //Attach the references
            textTitle = itemView.findViewById(R.id.item_title);
            textDesc = itemView.findViewById(R.id.item_desc);
            textPrice = itemView.findViewById(R.id.item_prise);
            imgView = itemView.findViewById(R.id.item_image);

            itemView.setOnClickListener(this);
        }

        public void bind(ItemModel itemModel) {
            textTitle.setText(itemModel.getTitle());
            textDesc.setText(itemModel.getDesc());
            textPrice.setText(itemModel.getPrice());

            Glide.with(imgView.getContext())
                    .load(itemModel.getImageUrl())
                    .override(260, 260)
                    .centerCrop()
                    .into(imgView);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            //Get the data and go to the edit activity
            ItemModel item = itemsList.get(position);
            Intent intent = new Intent(v.getContext(), InsertActivity.class);
            intent.putExtra("item", item);
            v.getContext().startActivity(intent);

        }
    }
}

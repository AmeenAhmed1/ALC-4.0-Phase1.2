package com.ameen.travelmantics.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ameen.travelmantics.R;
import com.ameen.travelmantics.model.ItemModel;
import com.ameen.travelmantics.util.FirebaseUtil;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class InsertActivity extends AppCompatActivity {

    private static final String TAG = "InsertActivity";

    private static final int PICTURE_REQUEST = 123;
    EditText txtTitle, txtDesc, txtPrice;

    // Write a message to the firebaseDatabase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseRef;

    ItemModel itemModel;

    Button mUploadButton;
    ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_menu, menu);

        if (FirebaseUtil.isAdmin) {
            menu.findItem(R.id.menu_save).setVisible(true);
            menu.findItem(R.id.menu_delete).setVisible(true);
            setEditTextEnabled(true);
        } else {
            menu.findItem(R.id.menu_save).setVisible(false);
            menu.findItem(R.id.menu_delete).setVisible(false);
            setEditTextEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_save:
                saveItem();
                Toast.makeText(this, "Item saved", Toast.LENGTH_SHORT).show();
                //backToList();
                finish();
                return true;

            case R.id.menu_delete:
                deleteItem();
                Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
                //backToList();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG, "onActivityResult: " + requestCode + " " + resultCode);

        if (requestCode == PICTURE_REQUEST && resultCode == RESULT_OK) {

            Log.i(TAG, "onActivityResult: ");

            Uri imageUri = data.getData();
            final StorageReference ref = FirebaseUtil.mFirebaseStorageReference.child(imageUri.getLastPathSegment());
            ref.putFile(imageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    itemModel.setImageName(taskSnapshot.getStorage().getPath());
                    Log.i(TAG, "onSuccess: " + taskSnapshot.getStorage().getPath());

                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            itemModel.setImageUrl(uri.toString());
                            setImage(uri.toString());
                            Log.i(TAG, "onSuccess: " + uri);
                        }
                    });
                }
            });
        }
    }


    private void initView() {
        txtTitle = findViewById(R.id.txtTitle);
        txtDesc = findViewById(R.id.txtDesc);
        txtPrice = findViewById(R.id.txtPrice);

        mImageView = findViewById(R.id.imageView);
        mUploadButton = findViewById(R.id.btnUploadImage);
        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent.createChooser(intent,
                        "Upload an image"), PICTURE_REQUEST);
            }
        });

        //Getting the intent if exist
        Intent intent = getIntent();
        ItemModel item = (ItemModel) intent.getSerializableExtra("item");
        if (item == null)
            itemModel = new ItemModel();

        else {
            this.itemModel = item;
            txtTitle.setText(itemModel.getTitle());
            txtDesc.setText(itemModel.getDesc());
            txtPrice.setText(itemModel.getPrice());
            setImage(itemModel.getImageUrl());
        }
        //init firebase
        //FirebaseUtil.openFbReference("itemdeals", this);
        firebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        databaseRef = FirebaseUtil.mDatabaseRef;
    }

    private void saveItem() {

        itemModel.setTitle(txtTitle.getText().toString());
        itemModel.setDesc(txtDesc.getText().toString());
        itemModel.setPrice(txtPrice.getText().toString());

//        ItemModel item = new ItemModel(
//                txtTitle.getText().toString(),
//                txtDesc.getText().toString(),
//                txtPrice.getText().toString(),
//                "");

        if (itemModel.getId() == null)
            databaseRef.push().setValue(itemModel);
        else
            databaseRef.child(itemModel.getId()).setValue(itemModel);
    }

    private void deleteItem() {

        if (itemModel == null) {

            Toast.makeText(this, "Save deal before deleting", Toast.LENGTH_SHORT).show();
            return;

        }
        databaseRef.child(itemModel.getId()).removeValue();

        Log.i(TAG, "deleteItem: " + itemModel.getImageName());

        if (itemModel.getImageName() != null && itemModel.getImageName().isEmpty() == false) {
            StorageReference ref = FirebaseUtil.mFirebaseStorage.getReference().child(itemModel.getImageName());
            ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i(TAG, "onSuccess: Deleted");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, "onFailure: ");
                }
            });
        }

    }

//    private void backToList() {
//        startActivity(
//                new Intent(InsertActivity.this, DataActivity.class));
//    }

    private void setEditTextEnabled(boolean isEnabled) {
        txtTitle.setEnabled(isEnabled);
        txtDesc.setEnabled(isEnabled);
        txtPrice.setEnabled(isEnabled);

        if (isEnabled == false)
            mUploadButton.setVisibility(View.GONE);
        else
            mUploadButton.setVisibility(View.VISIBLE);
    }

    private void setImage(String url) {
        if (url != null && url.isEmpty() == false) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;

            Glide.with(this)
                    .load(url)
                    .override(width, width * 2 / 3)
                    .centerCrop()
                    .into(mImageView);

        }
    }
}

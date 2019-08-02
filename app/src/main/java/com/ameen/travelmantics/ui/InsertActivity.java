package com.ameen.travelmantics.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.ameen.travelmantics.R;
import com.ameen.travelmantics.model.ItemModel;
import com.ameen.travelmantics.util.FirebaseUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertActivity extends AppCompatActivity {

    EditText txtTitle, txtDesc, txtPrice;

    // Write a message to the firebaseDatabase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseRef;

    ItemModel itemModel;

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_save:
                saveItem();
                Toast.makeText(this, "Item saved", Toast.LENGTH_SHORT).show();
                backToList();
                finish();
                return true;

            case R.id.menu_delete:
                deleteItem();
                Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
                backToList();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }


    private void initView() {
        txtTitle = findViewById(R.id.txtTitle);
        txtDesc = findViewById(R.id.txtDesc);
        txtPrice = findViewById(R.id.txtPrice);


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
        }
        //init firebase
        FirebaseUtil.openFbReference("itemdeals");
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

    }

    private void backToList() {
        startActivity(
                new Intent(InsertActivity.this, DataActivity.class));
    }
}

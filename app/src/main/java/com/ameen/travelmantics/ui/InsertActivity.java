package com.ameen.travelmantics.ui;

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


        //init firebase
        FirebaseUtil.openFbReference("itemdeals");
        firebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        databaseRef = FirebaseUtil.mDatabaseRef;
    }

    private void saveItem() {

        ItemModel item = new ItemModel(
                txtTitle.getText().toString(),
                txtDesc.getText().toString(),
                txtPrice.getText().toString(),
                "");

        databaseRef.push().setValue(item);
    }
}

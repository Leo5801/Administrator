package com.example.administrator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Button button, button2;
    Uri mImageUri;
    EditText editText;
    Uri uri;


    DatabaseReference databaseReference;
    StorageReference storageReference;

    Oxshagiy oxshagiy;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        editText = findViewById(R.id.edittext1);
        imageView = findViewById(R.id.imageview1);

        progressBar = findViewById(R.id.progressbar1);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Images");
        storageReference = FirebaseStorage.getInstance().getReference().child("Images");




        // Picasso.get().load("https://get.wallhere.com/photo/cat-face-eyes-lie-1064411.jpg").into(imageView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openFileChooser();

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(view.VISIBLE);
                uploadimage();

            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requsetCode, int resultCode, Intent data) {
        super.onActivityResult(requsetCode, resultCode, data);
        if (requsetCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(imageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void uploadimage(){
        if (mImageUri != null) {
            StorageReference filereferense = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            filereferense.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    oxshagiy = new Oxshagiy();
                    oxshagiy.setName(editText.getText().toString());
                    oxshagiy.setImageurl(uri.toString());
                    databaseReference.push().setValue(oxshagiy);
                    progressBar.setVisibility(View.INVISIBLE);

                }
            });
        }
    }
}
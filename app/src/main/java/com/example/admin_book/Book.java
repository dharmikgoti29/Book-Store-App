package com.example.admin_book;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Book extends AppCompatActivity {

    ImageView img;
    TextView bname,desc;
    Button read_book;

    String forname,fordesc;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        String documentid = getIntent().getStringExtra("Documentid");

        img=findViewById(R.id.book_image);
        desc=findViewById(R.id.desc_book);
        bname=findViewById(R.id.name_book);
        read_book=findViewById(R.id.read_book);

        db.collection("books").document(documentid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
               forname =  documentSnapshot.getString("bookname");
               bname.setText(forname);
               fordesc =  documentSnapshot.getString("discription");
               desc.setText(fordesc);
            }
        });
        String imgname = documentid+".jpg";
        String pdfname = documentid+".pdf";
        StorageReference storageReference=storage.getReference();
        StorageReference imgReference=storageReference.child(imgname);
        StorageReference pdfReference=storageReference.child(pdfname);
        imgReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(Book.this)
                        .load(uri)
                        .into(img);
            }
        });
        read_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdfReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                        viewIntent.setDataAndType(uri, "application/pdf");
                        viewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        // Add this line if targeting Android 7.0+ to use a File Provider
                        viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        try {
                            startActivity(viewIntent);
                        } catch (ActivityNotFoundException e) {
                            // Handle case where PDF viewer app is not available
                            Toast.makeText(Book.this, "No PDF viewer app available", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}
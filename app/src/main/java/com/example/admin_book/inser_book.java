package com.example.admin_book;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class inser_book extends AppCompatActivity {

    //xml elements
    ImageView img;
    Button  upload_pdf,insert_book;
    EditText book_name,author_name,description;
    Spinner catagary;

    //strings for xml elements
    String bookname,authorname,desc,cata;

    //firestore references
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageReference=storage.getReference();
    StorageReference pdfReference,imgReference;

    //image and pdf input
    Uri input_image,input_pdf;

    Integer pic=100;
    Integer pdf=200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inser_book);

        img=findViewById(R.id.image);
        upload_pdf=findViewById(R.id.upload_pdf);
        insert_book=findViewById(R.id.insert_book);
        book_name=findViewById(R.id.book_name);
        author_name=findViewById(R.id.author_name);
        description=findViewById(R.id.description);
        catagary=findViewById(R.id.catagary);



        ArrayList<String> cat_list = new ArrayList<>();
        ArrayAdapter<String> cat_adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,cat_list);
        catagary.setAdapter(cat_adapter);
        db.collection("subjects").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            cat_list.clear();
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                            {
                                cat_list.add(documentSnapshot.getString("subject"));
                            }
                            cat_adapter.notifyDataSetChanged();
                        }
                    }
                });

        catagary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cata=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,pic);
            }
        });
        upload_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(upload_pdf.getText().equals("upload pdf"))
               {
                   Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                   intent.setType("application/pdf");
                   startActivityForResult(intent, pdf);
               } else if (upload_pdf.getText().equals("view pdf")) {
                   Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                   viewIntent.setDataAndType(input_pdf, "application/pdf");
                   viewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                   // Add this line if targeting Android 7.0+ to use a File Provider
                   viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                   try {
                       startActivity(viewIntent);
                   } catch (ActivityNotFoundException e) {
                       // Handle case where PDF viewer app is not available
                       Toast.makeText(inser_book.this, "No PDF viewer app available", Toast.LENGTH_SHORT).show();
                   }
               }
            }

        });
        insert_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookname=book_name.getText().toString();
                authorname=author_name.getText().toString();
                desc=description.getText().toString();

                if(bookname.isEmpty() || authorname.isEmpty() || desc.isEmpty() || cata.isEmpty() || input_pdf==null || input_image==null)
                {
                    Toast.makeText(inser_book.this, "enter every require element", Toast.LENGTH_SHORT).show();
                }
                else{
                    Map<String,Object> book_detail = new HashMap<>();
                    book_detail.put("bookname",bookname);
                    book_detail.put("authorname",authorname);
                    book_detail.put("authorname",authorname);
                    book_detail.put("subject",cata);
                    book_detail.put("discription",desc);
                    db.collection("books").add(book_detail).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String documentid = documentReference.getId();
                            String pdfname = documentid+".pdf";
                            String imgname = documentid+".jpg";
                            pdfReference=storageReference.child(pdfname);
                            UploadTask uploadTask = pdfReference.putFile(input_pdf);
                            imgReference=storageReference.child(imgname);
                            UploadTask uploadTask1 = imgReference.putFile(input_image);
                            Toast.makeText(inser_book.this, "book uploaded", Toast.LENGTH_SHORT).show();

                        }
                    });
                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == pic)
        {
            input_image=data.getData();
            img.setImageURI(input_image);
        }
        else if (requestCode == pdf)
        {
            input_pdf=data.getData();
            upload_pdf.setText("view pdf");
        }
    }
}
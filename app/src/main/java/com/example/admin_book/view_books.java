package com.example.admin_book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class view_books extends AppCompatActivity {

    RecyclerView rw;
    ArrayList<model> arrayList = new ArrayList<>();
    modeladapter modeladapter = new modeladapter(this,arrayList);

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_books);

        rw=findViewById(R.id.rw);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        rw.setLayoutManager(gridLayoutManager);
        rw.setAdapter(modeladapter);

        db.collection("books").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                arrayList.clear();
                if(task.isSuccessful())
                {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                    {
                        String documentid = queryDocumentSnapshot.getId();
                        String imgname = documentid+".jpg";
                        String a = queryDocumentSnapshot.getString("bookname");
                        String b = queryDocumentSnapshot.getString("authorname");
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageReference=storage.getReference();
                        StorageReference  imgReference=storageReference.child(imgname);

                        imgReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String c =uri.toString();
                                model model1 = new model(a,b,c,documentid);
                                arrayList.add(model1);
                                modeladapter.notifyDataSetChanged();
                            }
                        });

                    }

                }

            }
        });

    }
}
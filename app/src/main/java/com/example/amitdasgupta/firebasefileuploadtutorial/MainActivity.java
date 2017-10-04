package com.example.amitdasgupta.firebasefileuploadtutorial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST =1234 ;
    Button bc,bu;
    ImageView iv;
    Uri fileup;
    StorageReference store;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bc=(Button)findViewById(R.id.button);
        bu=(Button)findViewById(R.id.button2);
        iv=(ImageView)findViewById(R.id.imageView);
        bu.setOnClickListener(this);
        bc.setOnClickListener(this);
        store= FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void onClick(View view) {
        if (view==bc)
            uploadFile();
        if(view==bu)
            uploadfile();

    }

    private void uploadfile() {
        final ProgressDialog pb=new ProgressDialog(this);
        pb.setTitle("Uploading.............");
        if(fileup!=null) {
            pb.show();
            StorageReference riversRef = store.child("images");

            riversRef.putFile(fileup)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                             pb.dismiss();
                            Toast.makeText(MainActivity.this,"File is Uploaded",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            pb.dismiss();
                            Toast.makeText(MainActivity.this,exception.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") double d = taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount();

                }
            });
        }
        else
            Toast.makeText(this,"No file is selected",Toast.LENGTH_SHORT).show();
    }

    private void uploadFile() {
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"select an image"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST&&resultCode==RESULT_OK)
        {
           fileup=data.getData();
            try {
                Bitmap map= MediaStore.Images.Media.getBitmap(getContentResolver(),fileup);
                iv.setImageBitmap(map);
            } catch (IOException e) {
               Toast.makeText(MainActivity.this,e+" ",Toast.LENGTH_SHORT).show();
            }
        }
    }
}

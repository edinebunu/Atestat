package com.example.atestat;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


public class PictureSetter {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReferenceFromUrl("gs://atestat-1545f.appspot.com/");

    public void setImageRound(String imgId, final CircleImageView view) throws IOException {
        StorageReference mRef = storageReference.child("profile-images").child(imgId+".jpeg");
        final File file = File.createTempFile("image","jpeg");
                mRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                view.setImageBitmap(bitmap);
            }
        });
    }


    public void setImage(String imgId, final ImageView view) throws IOException {
        StorageReference mRef = storageReference.child("Item Picture").child(imgId+".jpg");
        final File file = File.createTempFile("image","jpeg");
        mRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                view.setImageBitmap(bitmap);
                return;
            }
        });
        StorageReference mRefi = storageReference.child("Item Picture").child(imgId+".jpeg");
        final File filei = File.createTempFile("image","jpeg");
        mRefi.getFile(filei).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bitmap = BitmapFactory.decodeFile(filei.getAbsolutePath());
                view.setImageBitmap(bitmap);
            }
        });
    }
}

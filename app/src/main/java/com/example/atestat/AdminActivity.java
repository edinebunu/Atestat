package com.example.atestat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AdminActivity extends AppCompatActivity {

    private int PICK_IMAGE = 10101;

    Bitmap mPicture = null;
    ImageView profileImage;
    EditText name;
    EditText desc;
    EditText price;
    EditText cat;
    Button b;

    public void addButton(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"pick an image"), PICK_IMAGE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        profileImage = findViewById(R.id.imageView5);
        name = findViewById(R.id.editTextTextPersonName5);
        desc = findViewById(R.id.editTextTextPersonName6);
        price = findViewById(R.id.editTextTextPersonName7);
        cat = findViewById(R.id.editTextTextPersonName8);
        b= findViewById(R.id.button7);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleUpload(mPicture);
            }
        });

    }

    public void handleUpload(Bitmap bitmap){

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,25, baos);

        try {
            Map<String, String> user = new HashMap<>();
            user.put("Name", name.getText().toString());
            user.put("Description", desc.getText().toString());
            user.put("Price", price.getText().toString());

            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("Products")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            final String[] storageUid = new String[1];
                            db.collection("Products")
                                    .whereEqualTo("Name", name.getText().toString())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    storageUid[0] = document.getId();


                                                    Map<String, Object> city = new HashMap<>();
                                                    db.collection("Categories").document(cat.getText()
                                                            .toString())
                                                            .set(city)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                }
                                                            });

                                                    db.collection("Categories").document(cat.getText()
                                                            .toString()).collection("Produse").document(storageUid[0])
                                                            .set(city)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                }
                                                            });

                                                    final StorageReference reference = FirebaseStorage.getInstance()
                                                            .getReference()
                                                            .child("Item Picture")
                                                            .child(storageUid[0] +".jpeg");

                                                    reference.putBytes(baos.toByteArray())
                                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                @Override
                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                    getDownloadUrl(reference);
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(AdminActivity.this, "Profile image failed", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            }
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

        }
        catch(NullPointerException e){
            e.printStackTrace();
            Toast.makeText(this, "Error Firebase Unreached", Toast.LENGTH_SHORT).show();
        }
    }

    public void handle(View view){
        handleUpload(mPicture);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE)
        {
            try {
                InputStream inputStream = getContentResolver()
                        .openInputStream(Objects.requireNonNull(data.getData()));
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                profileImage.setImageBitmap(bitmap);
                mPicture = bitmap;
                //handleUpload(mPicture);
            }
            catch(FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void getDownloadUrl(StorageReference reference)
    {

        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                setProfileUrl(uri);
            }
        });
    }

    private void setProfileUrl(Uri uri)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri).build();

        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Toast.makeText(AdminActivity.this, "Profile image failed", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminActivity.this, "Profile image failed", Toast.LENGTH_SHORT).show();
                    }
                }
                );
    }

}
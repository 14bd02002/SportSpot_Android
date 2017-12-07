package dias.newsapphttp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class PostActivity extends AppCompatActivity {

    ImageButton mSelectImage;
    EditText mPostTitle;
    EditText mPostDesc;
    Button mSubmitBtn;
    EditText mPostDate;
    EditText mPostCategory;
    Uri mImageUri = null;
    public static final int GALLERY_REQUEST = 1;
    public StorageReference mStorage;
    DatabaseReference mDatabase;
    ProgressDialog mProgress;
    public static final int PERMISSIONS_REQUEST_READ_STORAGE = 100;
    //database for basket category
    DatabaseReference mDatabaseBasket;
    String basketString = "basket";
    //database for football category
    DatabaseReference mDatabaseFootball;
    String footballString = "football";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mStorage = FirebaseStorage.getInstance().getReference();
        mPostDate = (EditText) findViewById(R.id.dateField);
        mSelectImage = (ImageButton) findViewById(R.id.imageSelect);
        mPostTitle = (EditText) findViewById(R.id.titleField);
        mPostDesc = (EditText) findViewById(R.id.descField);
        mPostCategory = (EditText) findViewById(R.id.category);
        mSubmitBtn = (Button) findViewById(R.id.submitButton);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("News");
        mDatabaseBasket = FirebaseDatabase.getInstance().getReference().child("NewsBasket");
        mDatabaseFootball = FirebaseDatabase.getInstance().getReference().child("NewsFootball");

        mProgress = new ProgressDialog(this);
        //storage
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSIONS_REQUEST_READ_STORAGE);

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });
    }
    private void startPosting() {

        mProgress.setMessage("Posting...");
        
        final String title_val = mPostTitle.getText().toString().trim();
        final String desc_val = mPostDesc.getText().toString().trim();
        final String date_val = mPostDate.getText().toString().trim();
        final String category_val = mPostCategory.getText().toString().trim();
        //no empty values and image
        if(!TextUtils.isEmpty(title_val) &&  !TextUtils.isEmpty(desc_val) && !TextUtils.isEmpty(date_val)&& mImageUri != null){
            mProgress.show();
            StorageReference filepath = mStorage.child("News_Images").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost  = mDatabase.push();
                    newPost.child("title").setValue(title_val);
                    newPost.child("desc").setValue(desc_val);
                    newPost.child("image").setValue(downloadUrl.toString());
                    newPost.child("date").setValue(date_val);



                    mProgress.dismiss();
                    finish();
                    startActivity(new Intent(PostActivity.this, MainActivity.class));
                }
            });
        }
        //pushing info to basket database
        if(!TextUtils.isEmpty(title_val) &&  !TextUtils.isEmpty(desc_val) && !TextUtils.isEmpty(date_val)&& mImageUri != null && category_val.equals(basketString)){
            mProgress.show();
            StorageReference filepath = mStorage.child("News_Images").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost  = mDatabaseBasket.push();
                    newPost.child("title").setValue(title_val);
                    newPost.child("desc").setValue(desc_val);
                    newPost.child("image").setValue(downloadUrl.toString());
                    newPost.child("date").setValue(date_val);
                    newPost.child("category").setValue(date_val);
                    mProgress.dismiss();
                    finish();
                    startActivity(new Intent(PostActivity.this, MainActivity.class));
                }
            });
        }
        if(!TextUtils.isEmpty(title_val) &&  !TextUtils.isEmpty(desc_val) && !TextUtils.isEmpty(date_val)&& mImageUri != null && category_val.equals(footballString)){
            mProgress.show();
            StorageReference filepath = mStorage.child("News_Images").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost  = mDatabaseFootball.push();
                    newPost.child("title").setValue(title_val);
                    newPost.child("desc").setValue(desc_val);
                    newPost.child("image").setValue(downloadUrl.toString());
                    newPost.child("date").setValue(date_val);
                    newPost.child("category").setValue(date_val);
                    mProgress.dismiss();
                    finish();
                    startActivity(new Intent(PostActivity.this, MainActivity.class));
                }
            });
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST && resultCode == RESULT_OK){

            mImageUri = data.getData();
            mSelectImage.setImageURI(mImageUri);
        }
    }
}

package dias.newsapphttp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class NewsSingleActivity extends AppCompatActivity {


    //Activity for showing single news
    String mNews_key = null;

    DatabaseReference mDatabase;


    ImageView mNewsSingleImage;
    TextView mNewsSingleTitle;
    TextView mNewsSingleDesc;
    TextView mNewsSingleDate;
    Button mSingleRemoveBtn;
    FirebaseAuth firebaseAuth;
    String admin = "dias@gmail.com";


//    //Likes
//    ImageButton mNewsSingleLike;
//    DatabaseReference mDatabaseLike;
//    FirebaseAuth firebaseAuth;
//    boolean mProcessLike = false;
//    String news_key;
//
//    public void setLikeBtn(final String news_key){
//        mDatabaseLike.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
//                if(dataSnapshot.child(news_key).hasChild(firebaseAuth.getCurrentUser().getUid())){
//                    mNewsSingleLike.setImageResource(R.drawable.heart);
//                }
//                else {
//                    mNewsSingleLike.setImageResource(R.drawable.heart_gray);
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_single);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("News");
        //remove news
        firebaseAuth = FirebaseAuth.getInstance();
        mSingleRemoveBtn = (Button) findViewById(R.id.singleRemoveBtn);

        mNewsSingleDesc = (TextView) findViewById(R.id.singleNewsDesc);
        mNewsSingleImage = (ImageView) findViewById(R.id.singleNewsImage);
        mNewsSingleTitle = (TextView) findViewById(R.id.singleNewsTitle);
        mNewsSingleDate = (TextView) findViewById(R.id.singleNewsDate);
//        //Likes
//        mNewsSingleLike = (ImageButton) findViewById(R.id.singleLikeButton);
//        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
//        mDatabaseLike.keepSynced(true);
//
//        final FirebaseAuth firebaseAuth = null;
//
//        mNewsSingleLike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setLikeBtn(news_key);
//                mProcessLike = true;
//
//
//                mDatabaseLike.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
//                        if(mProcessLike) {
//
//                            if (dataSnapshot.child(news_key).hasChild(firebaseAuth.getCurrentUser().getUid())) {
//                                mDatabaseLike.child(news_key).child(firebaseAuth.getCurrentUser().getUid()).removeValue();
//                                mProcessLike = false;
//
//
//                            } else {
//                                mDatabaseLike.child(news_key).child(firebaseAuth.getCurrentUser().getUid()).setValue("RandomValue");
//                                mProcessLike = false;
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//            }
//        });
        //ADD BUTTON SHOW FOR ADMIN
        if(firebaseAuth.getCurrentUser().getEmail().equals(admin)){

            mSingleRemoveBtn.setVisibility(View.VISIBLE);
        }
//






        mNews_key = getIntent().getExtras().getString("news_id");
        // Toast.makeText(NewsSingleActivity.this, news_key, Toast.LENGTH_LONG).show();

        mDatabase.child(mNews_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String news_title = (String) dataSnapshot.child("title").getValue();
                String news_desc = (String ) dataSnapshot.child("desc").getValue();
                String news_image = (String ) dataSnapshot.child("image").getValue();
                String news_date = (String ) dataSnapshot.child("date").getValue();

                String news_uid = (String ) dataSnapshot.child("uid").getValue();

                mNewsSingleTitle.setText(news_title);
                mNewsSingleDesc.setText(news_desc);
                mNewsSingleDate.setText(news_date);
                Picasso.with(NewsSingleActivity.this).load(news_image).into(mNewsSingleImage);




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSingleRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child(mNews_key).removeValue();
                Intent mainIntent = new Intent(NewsSingleActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });

    }
}



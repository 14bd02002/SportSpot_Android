package dias.newsapphttp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class NewsSingleActivity extends AppCompatActivity {


    //Activity for showing single news
    String mNews_key = null;

    DatabaseReference mDatabase;
    DatabaseReference mDatabaseLikes;
    DatabaseReference mDatabaseComments;

    ImageView mNewsSingleImage;
    TextView mNewsSingleTitle;
    TextView mNewsSingleDesc;
    TextView mNewsSingleDate;
    FloatingActionButton mSingleRemoveBtn;
    FirebaseAuth firebaseAuth;
    String admin = "dias@gmail.com";
    TextView message_user;
    private FirebaseListAdapter<ChatMessage> adapter;
    //For users
    FirebaseUser firebaseUser;
    DatabaseReference mDatabaseUser;
    //Back button
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_single);

            //Back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //GET UID OF USER
            mDatabase = FirebaseDatabase.getInstance().getReference().child("News");
            //remove news
            firebaseAuth = FirebaseAuth.getInstance();
            mSingleRemoveBtn = (FloatingActionButton) findViewById(R.id.singleRemoveBtn);
            mNewsSingleDesc = (TextView) findViewById(R.id.singleNewsDesc);
            mNewsSingleImage = (ImageView) findViewById(R.id.singleNewsImage);
            mNewsSingleTitle = (TextView) findViewById(R.id.singleNewsTitle);
            mNewsSingleDate = (TextView) findViewById(R.id.singleNewsDate);

                //check login
            firebaseAuth = FirebaseAuth.getInstance();
            if(firebaseAuth.getCurrentUser() == null){
                finish();
                startActivity(new Intent(this, LoginActivity.class));
            }

//            //Get User
//            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//            TextView message_user = (TextView)findViewById(R.id.message_user);
//            message_user.setText(firebaseUser.getEmail());


            //chat comment

            mDatabaseComments = FirebaseDatabase.getInstance().getReference().child("News").child("Messages");
            displayChatMessages();
            FloatingActionButton fab =
                    (FloatingActionButton)findViewById(R.id.fab);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText input = (EditText)findViewById(R.id.input);
                    Toast.makeText(NewsSingleActivity.this, mNews_key, Toast.LENGTH_LONG).show();
                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    // Read the input field and push a new instance
                    // of ChatMessage to the Firebase database
                    mDatabase
                            .child(mNews_key)
                            .child("Messages")
                            .push()
                            .setValue(new ChatMessage(input.getText().toString(),
                                    user.getEmail())
                            );

                    // Clear the input
                    input.setText("");
                }
            });




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
                finish();
                startActivity(mainIntent);
            }
        });

    }
    private void displayChatMessages() {

        mNews_key = getIntent().getExtras().getString("news_id");
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);


        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference().child("News").child(mNews_key).child("Messages")) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);

    }
}



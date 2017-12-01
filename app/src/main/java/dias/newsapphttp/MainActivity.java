package dias.newsapphttp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.POST;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth firebaseAuth;
    TextView textViewUserEmail;
    TextView textLogout;
    RecyclerView mNewsList;
    DatabaseReference mDatabase;
    boolean mProcessLike = false;
    DatabaseReference mDatabaseLike;
    ImageButton actionAdd;
    String admin = "dias@gmail.com";
    //reverse order
    LinearLayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNewsList = (RecyclerView) findViewById(R.id.news_list);
        mNewsList.setHasFixedSize(true);
        mNewsList.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("News");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");

        mDatabaseLike.keepSynced(true);

        //FIREBASE LOGOUT + USER
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        //ADD BUTTON SHOW FOR ADMIN
        actionAdd = (ImageButton) findViewById(R.id.action_add);
        if(user.getEmail().equals(admin)){

            actionAdd.setVisibility(View.VISIBLE);
        }

        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        //КАК СДЕЛАТЬ ПО НЕЙМУ?
        textViewUserEmail.setText("Welcome " + " " + FirebaseAuth.getInstance()
                .getCurrentUser()
                .getDisplayName());
        textLogout = (TextView) findViewById(R.id.textLogout);

        textLogout.setOnClickListener(this);
        actionAdd.setOnClickListener(this);
        //REVERSE ORDER LAYOUT
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mNewsList.setLayoutManager(mLayoutManager);
        mNewsList.setAdapter(mAdapter);




    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<News, NewsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, NewsViewHolder>(
                News.class,
                R.layout.news_row,
                NewsViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(NewsViewHolder viewHolder, final News model, int position) {

                final String news_key = getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                //Likes number display
                //!!
                viewHolder.setLikes(model.getLikes());

                viewHolder.setLikeBtn(news_key);

                viewHolder.mView.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(MainActivity.this, news_key, Toast.LENGTH_LONG).show();
                        //Giving ID of news to new activity
                        Intent singleNewsIntent = new Intent(MainActivity.this, NewsSingleActivity.class);
                        singleNewsIntent.putExtra("news_id", news_key);
                        startActivity(singleNewsIntent);
                    }
                });




                viewHolder.mLikebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mProcessLike = true;

                            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                                    if(mProcessLike) {
                                        if (dataSnapshot.child(news_key).hasChild(firebaseAuth.getCurrentUser().getUid())) {

                                            mDatabase.child(news_key).child("likes").setValue(model.getLikes()-1);
                                            mDatabaseLike.child(news_key).child(firebaseAuth.getCurrentUser().getUid()).removeValue();
                                            mProcessLike = false;


                                        } else {
                                            mDatabase.child(news_key).child("likes").setValue(model.getLikes()+1);
                                            mDatabaseLike.child(news_key).child(firebaseAuth.getCurrentUser().getUid()).setValue(+1);
                                            mProcessLike = false;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                    }
                });

            }
        };

        mNewsList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        View mView;

        ImageButton mLikebtn;
        DatabaseReference mDatabase;
        DatabaseReference mDatabaseLike;
        FirebaseAuth firebaseAuth;
        TextView mNewsLikes;

        public NewsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mLikebtn = (ImageButton) mView.findViewById(R.id.like_btn);
            mDatabase = FirebaseDatabase.getInstance().getReference().child("News").child("likes");
            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
            firebaseAuth = FirebaseAuth.getInstance();
            mDatabaseLike.keepSynced(true);

        }
        public void setLikeBtn(final String news_key){
            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(news_key).hasChild(firebaseAuth.getCurrentUser().getUid())){
                        mLikebtn.setImageResource(R.drawable.heart);

                    }
                    else {
                        mLikebtn.setImageResource(R.drawable.heart_gray);


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        public void setTitle(String title){
            TextView news_title = (TextView) mView.findViewById(R.id.news_title);
            news_title.setText(title);

        }
        public void setDesc(String desc){
            TextView news_desc = (TextView) mView.findViewById(R.id.news_desc);
            news_desc.setText(desc);

        }
        public void setImage(Context ctx, String image){
            ImageView news_image = (ImageView )mView.findViewById(R.id.news_image);
            Picasso.with(ctx).load(image).into(news_image);
        }
        //LIKES SETLIKES
        //
        //!!!
        public void setLikes(int likes){
            TextView news_likes = (TextView) mView.findViewById(R.id.news_likes);
            news_likes.setText(String.valueOf(likes));
        }




    }
    public void onClick(View view) {
        if(view == textLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        if(view == actionAdd){

            startActivity( new Intent(this, PostActivity.class));
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);




    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.action_add){
            startActivity(new Intent(MainActivity.this, PostActivity.class));
        }
        if(item.getItemId()==R.id.action_logout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}



package dias.newsapphttp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CategoriesActivity extends AppCompatActivity implements View.OnClickListener {

    TextView football;
    TextView basket;
    TextView tennis;
    TextView hockey;
    DatabaseReference mDatabase;
    boolean mProcessLike = false;
    DatabaseReference mDatabaseLike;
    FirebaseAuth firebaseAuth;
    //reverse order
    LinearLayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    RecyclerView mNewsList;

    //database for basket category
    DatabaseReference mDatabaseBasket;
    String basketString = "basket";
    //database for football category
    DatabaseReference mDatabaseFootball;
    String footballString = "football";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);



        football = (TextView) findViewById(R.id.football);
        basket = (TextView) findViewById(R.id.basket);
        tennis = (TextView) findViewById(R.id.tennis);
        hockey = (TextView) findViewById(R.id.hockey);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("News");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
        mDatabaseBasket = FirebaseDatabase.getInstance().getReference().child("NewsBasket");
        mDatabaseFootball = FirebaseDatabase.getInstance().getReference().child("NewsFootball");


        mDatabaseLike.keepSynced(true);

        mNewsList = (RecyclerView) findViewById(R.id.news_list);
        mNewsList.setHasFixedSize(true);
        mNewsList.setLayoutManager(new LinearLayoutManager(this));


//       basketString = mDatabase.child("category").getKey();

        football.setOnClickListener(this);
        basket.setOnClickListener(this);
        tennis.setOnClickListener(this);
        hockey.setOnClickListener(this);

        //REVERSE ORDER LAYOUT
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mNewsList.setLayoutManager(mLayoutManager);
        mNewsList.setAdapter(mAdapter);













    }
    public void onClick(View view) {
        if(view == football) {



            mDatabaseFootball = FirebaseDatabase.getInstance().getReference().child("NewsFootball");
            FirebaseRecyclerAdapter<News, CategoriesActivity.NewsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, CategoriesActivity.NewsViewHolder>(
                    News.class,
                    R.layout.news_row,
                    CategoriesActivity.NewsViewHolder.class,
                    mDatabaseFootball

            ) {
                @Override
                protected void populateViewHolder(CategoriesActivity.NewsViewHolder viewHolder, final News model, int position) {



                        final String news_key = getRef(position).getKey();

                        viewHolder.setTitle(model.getTitle());
                        viewHolder.setDesc(model.getDesc());
                        viewHolder.setImage(getApplicationContext(), model.getImage());
                        //Likes number display
                        //!!
                        viewHolder.setLikes(model.getLikes());

                        viewHolder.setLikeBtn(news_key);

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                //Toast.makeText(MainActivity.this, news_key, Toast.LENGTH_LONG).show();
                                //Giving ID of news to new activity

                                Intent singleNewsIntent = new Intent(CategoriesActivity.this, NewsSingleActivity.class);
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

                                        if (mProcessLike) {
                                            if (dataSnapshot.child(news_key).hasChild(firebaseAuth.getCurrentUser().getUid())) {

                                                mDatabase.child(news_key).child("likes").setValue(model.getLikes() - 1);
                                                mDatabaseLike.child(news_key).child(firebaseAuth.getCurrentUser().getUid()).removeValue();
                                                mProcessLike = false;


                                            } else {
                                                mDatabase.child(news_key).child("likes").setValue(model.getLikes() + 1);
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
            //if we click football disable football button, enable others
            football.setEnabled(false);
            basket.setEnabled(true);
            tennis.setEnabled(true);
            hockey.setEnabled(true);

        }
        if(view == basket){

            mDatabaseBasket = FirebaseDatabase.getInstance().getReference().child("NewsBasket");
            FirebaseRecyclerAdapter<News, CategoriesActivity.NewsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, CategoriesActivity.NewsViewHolder>(
                    News.class,
                    R.layout.news_row,
                    CategoriesActivity.NewsViewHolder.class,
                    mDatabaseBasket

            ) {
                @Override
                protected void populateViewHolder(CategoriesActivity.NewsViewHolder viewHolder, final News model, int position) {



                    final String news_key = getRef(position).getKey();

                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setDesc(model.getDesc());
                    viewHolder.setImage(getApplicationContext(), model.getImage());
                    //Likes number display
                    //!!
                    viewHolder.setLikes(model.getLikes());

                    viewHolder.setLikeBtn(news_key);

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            //Toast.makeText(MainActivity.this, news_key, Toast.LENGTH_LONG).show();
                            //Giving ID of news to new activity

                            Intent singleNewsIntent = new Intent(CategoriesActivity.this, NewsSingleActivity.class);
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

                                    if (mProcessLike) {
                                        if (dataSnapshot.child(news_key).hasChild(firebaseAuth.getCurrentUser().getUid())) {

                                            mDatabase.child(news_key).child("likes").setValue(model.getLikes() - 1);
                                            mDatabaseLike.child(news_key).child(firebaseAuth.getCurrentUser().getUid()).removeValue();
                                            mProcessLike = false;


                                        } else {
                                            mDatabase.child(news_key).child("likes").setValue(model.getLikes() + 1);
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

            football.setEnabled(true);
            basket.setEnabled(false);
            tennis.setEnabled(true);
            hockey.setEnabled(true);
        }
        if(view == tennis){

            football.setEnabled(true);
            basket.setEnabled(true);
            tennis.setEnabled(false);
            hockey.setEnabled(true);
        }
        if(view == hockey){

            football.setEnabled(true);
            basket.setEnabled(true);
            tennis.setEnabled(true);
            hockey.setEnabled(false);
        }


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
}

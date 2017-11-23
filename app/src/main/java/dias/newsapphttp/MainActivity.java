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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.POST;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth firebaseAuth;
    TextView textViewUserEmail;
    TextView textLogout;
    RecyclerView mNewsList;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNewsList = (RecyclerView) findViewById(R.id.news_list);
        mNewsList.setHasFixedSize(true);
        mNewsList.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("News");


        //FIREBASE LOGOUT + USER
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();


        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);

        textViewUserEmail.setText("Welcome " + " " + user.getEmail());
        textLogout = (TextView) findViewById(R.id.textLogout);

        textLogout.setOnClickListener(this);




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
            protected void populateViewHolder(NewsViewHolder viewHolder, News model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(), model.getImage());

            }
        };

        mNewsList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public NewsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
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
            ImageView post_image = (ImageView )mView.findViewById(R.id.news_image);
            Picasso.with(ctx).load(image).into(post_image);
        }


    }
    public void onClick(View view) {
        if(view == textLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
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
        return super.onOptionsItemSelected(item);
    }
}



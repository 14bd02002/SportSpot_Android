package dias.newsapphttp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 1 on 21.11.2017.
 */

public class FragmentThree extends Fragment implements View.OnClickListener{


    FirebaseAuth firebaseAuth;
    TextView textViewUserEmail;
    Button buttonLogout;


    public FragmentThree() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_profile, container, false);


        return view;
    }


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//
//        firebaseAuth = FirebaseAuth.getInstance();
//
//        if(firebaseAuth.getCurrentUser() == null){
//            getActivity().finish();
//            startActivity(new Intent(FragmentThree.this.getActivity(), LoginActivity.class));
//        }
//
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//
//
//        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
//
//        textViewUserEmail.setText("Welcome " + " " + user.getEmail());
//        buttonLogout = (Button) findViewById(R.id.buttonLogout);
//
//        buttonLogout.setOnClickListener(this);
//    }
    @Override
    public void onClick(View view) {
        if(view == buttonLogout){
            firebaseAuth.signOut();
            getActivity().finish();
            startActivity(new Intent(FragmentThree.this.getActivity(), LoginActivity.class));
        }

    }







}

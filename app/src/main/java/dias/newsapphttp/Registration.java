package dias.newsapphttp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

/**
 * Created by 1 on 20.11.2017.
 */

public class Registration extends AppCompatActivity implements View.OnClickListener{

     Button buttonRegister;
     EditText editTextEmail;
     EditText editTextPassword;
     EditText editTextName;
     TextView textViewSignin;
     DatabaseReference mDatabase;

     ProgressDialog progressDialog;
     FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            //To main activity
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        progressDialog = new ProgressDialog(this);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextName = (EditText) findViewById(R.id.editTextName);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);



    }

    public void registerUser(){
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email empty
            Toast.makeText(this, "Enter Email!", Toast.LENGTH_SHORT).show();
            return;

        }
        if(TextUtils.isEmpty(password) ){
            //password empty
            Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show();
            return;

        }
        if(TextUtils.isEmpty(name) ){
            //password empty
            Toast.makeText(this, "Enter your name!", Toast.LENGTH_SHORT).show();
            return;


        }
        progressDialog.setMessage("Signing up...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //user logged in, start the profile activity here
                            //display toast
                            Toast.makeText(Registration.this, "Signed up!", Toast.LENGTH_SHORT).show();
                                String user_id = firebaseAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = mDatabase.child(user_id);
                                current_user_db.child("email").setValue(email);
                                current_user_db.child("name").setValue(name);

                            finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        }else {
                            Toast.makeText(Registration.this, "Cant not sign up! Try Again...", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }

    @Override
    public void onClick(View view) {
        if(view == buttonRegister){
            registerUser();
        }

        if(view == textViewSignin){
            //will open login activity
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
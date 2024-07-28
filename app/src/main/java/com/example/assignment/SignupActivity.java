package com.example.assignment; // Defines the package this class belongs to.

// Imports necessary classes for UI elements, database interaction, and user data
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import DB_Context.DBContext;
import DB_Context.UserModel;

public class SignupActivity extends AppCompatActivity {// Declares a new activity for user sign-up

    // UI elements
    TextView login_text;
    EditText username;
    EditText password;
    EditText confirm_password;
    Button signupbtn;
//// Creates a database context instance
    DBContext dbcontext=new DBContext(this);
    ArrayList<UserModel> userModelArrayList=new ArrayList<>();// List to store user data
    @Override
    protected void onCreate(Bundle savedInstanceState) { // Main entry point of the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup); // Sets the layout for the activity
        // Initialization of UI elements by finding them in the layout
        login_text=findViewById(R.id.logintext);
        username=findViewById(R.id.signup_username);
        password=findViewById(R.id.signup_password);
        confirm_password=findViewById(R.id.signup_confirm_password);
        signupbtn=findViewById(R.id.signup_button);

        login_text.setOnClickListener(new View.OnClickListener() {// When login text is clicked

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignupActivity.this,LoginActivity.class);//Create an intent to start LoginActivity
                startActivity(intent); // Start the LoginActivity

            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {// When signup button is clicked
            @Override
            public void onClick(View view) {
                String name=username.getText().toString();
                String pass=password.getText().toString();
                String con_pass=confirm_password.getText().toString();
//test to fill data
                if(name.isEmpty() )
                {
                    Toast.makeText(SignupActivity.this, "Username field is empty  ", Toast.LENGTH_SHORT).show();
                } else if (pass.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Password field is empty  ", Toast.LENGTH_SHORT).show();
                } else if (con_pass.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Confirm Password field is empty  ", Toast.LENGTH_SHORT).show();
                } else{
                    //check password with confirm password
                    if(pass.equals(con_pass)){
                        userModelArrayList=dbcontext.readUser();
                        UserModel user;
                        Boolean isUser=false;
                        //getting user
                        if(userModelArrayList!=null){
                            for (int i=0;i<userModelArrayList.size();i++)
                            {
                                user=userModelArrayList.get(i);
//checking if user exists in db
                                if(name.equals(user.getUsername())&& pass.equals(user.getPassword()))
                                {
                                    isUser=true;
                                    break;
                                }
                            }
                        }
//if exists error message and if not add user to table
                        if(isUser)
                        {
                            Toast.makeText(getApplicationContext(),"User already exists. Please login to continue",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            dbcontext.addUser(name,pass);
                            Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                    else
                    {
                        Toast.makeText(SignupActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}

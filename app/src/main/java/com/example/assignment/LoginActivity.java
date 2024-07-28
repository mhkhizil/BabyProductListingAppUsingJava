// Package declaration
package com.example.assignment;
//import packages
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import DB_Context.DBContext;
import DB_Context.UserModel;

public class LoginActivity extends AppCompatActivity {
    // Declare variables for the username and password input fields, the login button, and the sign-up text view
private EditText username;
private EditText password;
private Button login_btn;

TextView sign_up_text;
    // Initialize the database context and user model list
    DBContext dbcontext=new DBContext(this);
    ArrayList<UserModel> userModelArrayList=new ArrayList<>();
    // Define constants for shared preferences keys
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String UserIdKey = "user_id";
//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the layout defined in activity_login.xml
        setContentView(R.layout.activity_login);

        // Link the declared variables to the corresponding UI elements in the layout
        username=findViewById(R.id.username_login);
        password=findViewById(R.id.password_login);
        login_btn=findViewById(R.id.login_button);
        sign_up_text=findViewById(R.id.signuptext);
//        List<String> table_list=dbcontext.getTableList();
        // Set an onClickListener for the login button
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String name=username.getText().toString();
            String pass=password.getText().toString();

                // Check if the username or password fields are empty and show appropriate toast messages
                if (name.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Username field is empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Password field is empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Read the user data from the database
                userModelArrayList=dbcontext.readUser();
                UserModel user;
                int user_id = -1;
                int isUser_exist=0;
                // Loop through the user data to find a matching username and password
                for (int i=0;i< userModelArrayList.size();i++)
                {
                    user=userModelArrayList.get(i);
                    if(name.equals(user.getUsername())&&pass.equals(user.getPassword()))
                    {
                        isUser_exist++;
                        user_id = user.getId();
                        break;
                    }
                }
// If a matching user is found, save the user ID in shared preferences and start the dashboard activity
                if(isUser_exist!=0){
                    SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(UserIdKey, user_id);
                    editor.apply();
                    Intent intent=new Intent(LoginActivity.this,UI.dashboardActivity.class);
                    intent.putExtra("username",name);
                    intent.putExtra("password",pass);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Invalid user name and password.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Set an onClickListener for the sign-up text view to navigate to the sign-up activity
        sign_up_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}
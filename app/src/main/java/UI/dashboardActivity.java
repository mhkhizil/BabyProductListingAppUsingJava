package UI;// Declares the package (like a folder) this class belongs to
// Import necessary Android components and libraries
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.assignment.LoginActivity;
import com.example.assignment.R;
import com.google.android.material.navigation.NavigationView;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
// Main activity class for the dashboard
public class dashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    // Declare variables to hold UI elements and user data
    private DrawerLayout drawerLayout;
    private String username;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {// Set the layout for this activity
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dashboard);
        // Get username and password passed from the login screen
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        username=bundle.getString("username");
        password=bundle.getString("password");
        // Set up the toolbar (action bar)
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
// Set up the navigation drawer
        drawerLayout=findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
// Add a toggle button to the toolbar for the drawer
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();// Ensure toggle and drawer are in sync
        // Display username in the navigation drawer header
        View headerView = navigationView.getHeaderView(0);
        TextView nameTextView = headerView.findViewById(R.id.name_of_the_user);

        // Set the username from Intent extras to the TextView
        nameTextView.setText(username);
        // Load the Home_Fragment initially if no previous state exists
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home_Fragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);// Mark home as selected
        }
    }
    //Handle navigation item clicks
 @Override
 public boolean onNavigationItemSelected(@NonNull MenuItem item) {
     switch (item.getItemId()){
         case R.id.nav_home:
             // Show Home_Fragment and close drawer
             getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home_Fragment()).commit();
             drawerLayout.closeDrawer(Gravity.LEFT);
             break;
         case R.id.nav_property:
             // Show Product_Fragment, pass username/password, and close drawer
             Product_Fragment pfragment=new Product_Fragment();
             Bundle args = new Bundle();
             args.putString("username", username);
             args.putString("password", password);
             pfragment.setArguments(args);
             FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
             transaction.replace(R.id.fragment_container, pfragment); // or replace()
             transaction.commit();
             drawerLayout.closeDrawer(Gravity.LEFT);
             break;
         case R.id.nav_logout:
             // Go back to the LoginActivity on logout
             Intent intent=new Intent(dashboardActivity.this, LoginActivity.class);
             startActivity(intent);
             break;
     }
    return true;
 }
    // Handle back button press - close drawer if open, otherwise default behavior
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

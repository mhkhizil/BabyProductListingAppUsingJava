package UI;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.assignment.R;
import com.google.android.material.navigation.NavigationView;

public class Home_Fragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View home_view = inflater.inflate(R.layout.fragment_home, container, false);
        Button startButton = home_view.findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("mode", "add_mode");//have to deleted this line delete
                bundle.putString("username", "your_username");//have to delete this lineto delete

                Product_Fragment p_fragment = new Product_Fragment();
                p_fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, p_fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                if (getActivity() instanceof FragmentActivity) {
                    FragmentActivity activity = (FragmentActivity) getActivity();
                    // Check for null savedInstanceState within the FragmentActivity

                        NavigationView navigationView = activity.findViewById(R.id.nav_view);
                        if (navigationView != null) {
                            navigationView.setCheckedItem(R.id.nav_property);
                        }

                }
            }
        });

        return home_view;
    }

}
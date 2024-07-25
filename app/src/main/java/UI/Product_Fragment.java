package UI;




import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.provider.MediaStore;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.assignment.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import DB_Context.DBContext;
import DB_Context.ProductListModel;

public class Product_Fragment extends Fragment implements PropertyClickListener {

    RecyclerView recyclerView;
    ProductAdapter adapter;
    Button add_btn;
    List<ProductListModel> property_list;
    DBContext dbContext;
    String username="";
    String password="";
    EditText search_text;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View property_view=inflater.inflate(R.layout.fragment_product, container, false);
        Bundle bundle=getArguments();

        if(bundle!=null)
        {
             username=bundle.getString("username");
             password=bundle.getString("password");
        }

        recyclerView=property_view.findViewById(R.id.product_recyclerView);
        add_btn=property_view.findViewById(R.id.add_new_product);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        search_text=property_view.findViewById(R.id.product_search);
        dbContext=new DBContext(Product_Fragment.this.getActivity());
        property_list = new ArrayList<>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int user_id = sharedPreferences.getInt("user_id", -1); // Default value is -1

        property_list=dbContext.readProductListByUserId(user_id);
        adapter=new ProductAdapter(getContext(),property_list,this);
        recyclerView.setAdapter(adapter);

       // FragmentManager fragmentManager=getSupportFragmentManager();

//        for(int i=0;i<2;i++)
//        {
//            PropertyModel p=new PropertyModel(1,"ptype","proom","12-2-2023","122","furn","no remark","name"+i);
//            property_list.add(p);
//        }
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        adapter.notifyDataSetChanged();

        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is called when the text is changed.
                // You can get the text using the `s` parameter.
                String newText = s.toString();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                int user_id = sharedPreferences.getInt("user_id", -1);
                property_list= dbContext.searchProductByRefNo(newText,user_id);
                adapter=new ProductAdapter(getContext(),property_list, Product_Fragment.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is called after the text is changed.
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("mode", "add_mode");
                bundle.putString("username",username);
                Product_Form_Fragment p_form_fragment = new Product_Form_Fragment();
                p_form_fragment.setArguments(bundle);
                FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,p_form_fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        ImageView backButton = property_view.findViewById(R.id.back_icon_to_home_page);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Choose one of the following options based on your HomeScreen structure:

                // Option 1: HomeScreen is a Fragment
//               getActivity().getSupportFragmentManager().popBackStack();

                //Option 2: HomeScreen is an Activity
//                 Intent intent = new Intent(getActivity(), Home_Fragment.class);
//                startActivity(intent);
//                 getActivity().finish();
                // Option 1: Home Screen is a Fragment
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                Home_Fragment homeFragment = (Home_Fragment) fragmentManager.findFragmentByTag("HomeFragmentTag"); // Replace with your tag
//                if (homeFragment != null) {
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.fragment_container, homeFragment) // Replace with your container ID
//                            .commit();
//                }

                if (getActivity() instanceof FragmentActivity) {
                    FragmentActivity activity = (FragmentActivity) getActivity(); // Declare the activity variable here


                   activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new Home_Fragment()) // Replace with your container ID
                            .commit();
                    // Highlight the Home item in the navigation drawer
                    NavigationView navigationView = activity.findViewById(R.id.nav_view); // Get NavigationView from activity
                    if (navigationView != null) {
                        navigationView.setCheckedItem(R.id.nav_home);
                    }
                }
         }
        });

//        back_icon_to_home_page.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().getSupportFragmentManager().popBackStack();
//            }
//        });



        return property_view;
    }

    @Override
    public void onItemClick(int position){
        ProductListModel current_property=property_list.get(position);
        int ref_no=current_property.getRef_no();
        Bundle args=new Bundle();
        args.putString("ref_no",Integer.toString(ref_no));
        args.putString("mode", "detail_mode");
        args.putString("username",username);
        Product_Form_Fragment product_form_fragment =new Product_Form_Fragment();
        product_form_fragment.setArguments(args);
        FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, product_form_fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void onShareClick(int position) {
        ProductListModel product = property_list.get(position);

        String message = "Check out this product:\n" +
                "Name: " + product.getProductName() + "\n" +
                "Price: " + product.getPrice() + "\n" +
                "status: " + (product.isPurchased()?"Purchased": "Not Purchased")+ "\n" +
                "Description: " + product.getRemark();

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        try {
            startActivity(Intent.createChooser(sendIntent, "Share product via"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "No MMS app found.", Toast.LENGTH_SHORT).show();
        }
//        if (product.getImage() != null) {
//            Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length);
//
////            if (bitmap.getByteCount() > 300 * 1024) { // Roughly 300 KB limit
////                float scaleFactor = (float) Math.sqrt(300 * 1024 / (double) bitmap.getByteCount());
////                bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * scaleFactor),
////                        (int) (bitmap.getHeight() * scaleFactor), true);
////            }
//
//            // Save the image temporarily
//            try {
//                String path = MediaStore.Images.Media.insertImage(
//                        getContext().getContentResolver(), bitmap, "product_image", null);
//                Uri imageUri = Uri.parse(path);
//                sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//            } catch (Exception e) {
//                // Handle exceptions
//                e.printStackTrace();
//            }
//        }

        // Filter for MMS apps
        //PackageManager pm = getContext().getPackageManager();
//        List<ResolveInfo> activities = pm.queryIntentActivities(sendIntent, 0);
//        for (ResolveInfo info : activities) {
//            if (info.activityInfo.packageName.contains("mms") || info.activityInfo.name.contains("mms")) {
//                sendIntent.setPackage(info.activityInfo.packageName);
//                break; // Found an MMS app
//            }
//        }


    }

}
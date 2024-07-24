package UI;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.Manifest;

import com.example.assignment.R;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import DB_Context.DBContext;
import DB_Context.ProductListModel;


public class Product_Form_Fragment extends Fragment {
    EditText ref_no;
    EditText product_name;
    EditText price;
    EditText remark;
    ImageView upload_item_image;
    Button upload_image_button;
    Button camera_image_button;
    Button save_btn;
    Button delete_btn;
    Button cancel_btn;
    private CheckBox cbPurchased;
    public String current_mode;
    public String current_username;
    private static final int PICK_IMAGE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private Uri imageUri;
    private byte[] imageInBytes;
    DBContext dbContext;
    View ref_no_layout;
    List<ProductListModel> property_list;
    String reference_no;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle=getArguments();
        if(bundle!=null){
            current_mode =bundle.getString("mode");//check mode 'add_mode or 'edit_mode'
            current_username= bundle.getString("username");

        }

        View form_view=inflater.inflate(R.layout.product_form_fragment, container, false);

        dbContext=new DBContext(Product_Form_Fragment.this.getActivity());
        ref_no_layout=form_view.findViewById(R.id.reference_no_layout);
        ref_no=form_view.findViewById(R.id.reference_no);
        product_name = form_view.findViewById(R.id.product_name);
        price=form_view.findViewById(R.id.price);
        remark=form_view.findViewById(R.id.remark);
        cbPurchased =form_view.findViewById(R.id.cbPurchased );
        upload_item_image = form_view.findViewById(R.id.upload_item_image);
        upload_image_button = form_view.findViewById(R.id.upload_image_button);
        save_btn=form_view.findViewById(R.id.save_btn);
        delete_btn=form_view.findViewById(R.id.delete_btn);
        cancel_btn = form_view.findViewById(R.id.cancel_btn);
        camera_image_button = form_view.findViewById(R.id.camera_image_button);

        upload_item_image.setImageResource(R.drawable.placeholder_product); // Set your default image resource here
        if(current_mode=="add_mode"){
            ref_no_layout.setVisibility(View.INVISIBLE);
            save_btn.setText("Add");
            save_btn.setWidth(300);
            delete_btn.setVisibility(View.GONE);

        }
        if(current_mode=="detail_mode"){

           reference_no=bundle.getString("ref_no");
            ref_no.setText("Item number:"+reference_no);
            ref_no.setEnabled(false);
            property_list=dbContext.readProductByRefNumber(reference_no);
            ProductListModel p=property_list.get(0);
            product_name.setText(property_list.get(0).getProductName());
            price.setText(property_list.get(0).getPrice());
            remark.setText(property_list.get(0).getRemark());
            cbPurchased.setChecked(property_list.get(0).isPurchased());

            if (property_list.get(0).getImage() != null) {
                imageInBytes=property_list.get(0).getImage();
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageInBytes, 0, imageInBytes.length);
                upload_item_image.setImageBitmap(bitmap);
            }

        }
        upload_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        camera_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                        showPermissionRationale();
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                    }
                } else {
                    openCamera();
                }
            }
        });
//       reporter.setText(current_username);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(current_mode=="add_mode"){
                //should add trim later
                String pn=product_name.getText().toString();
                String pr=price.getText().toString();
                String rem=remark.getText().toString();
                boolean pc=cbPurchased.isChecked();

                if(  pn.isEmpty() || pr.isEmpty() || rem.isEmpty())
                {
                    Toast.makeText(Product_Form_Fragment.this.getActivity(), "Enter all data", Toast.LENGTH_SHORT).show();
                }
                else {
                        dbContext.addProductList(pn,pr,rem,pc,imageInBytes);
                      //  Toast.makeText(Property_Form_Fragment.this.getActivity(), "New property added successfully", Toast.LENGTH_SHORT).show();
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
            }
            if(current_mode=="detail_mode"){
                String pn=product_name.getText().toString();
                String pr=price.getText().toString();
                String rem=remark.getText().toString();
                boolean pc=cbPurchased.isChecked();
                dbContext.updateProductList(reference_no,Integer.parseInt(reference_no) ,pn,pr,rem,pc, imageInBytes);
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
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            dbContext.deleteProductList(reference_no);
            FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
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

        return form_view;
    }
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }
    private void showPermissionRationale() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Camera Permission Needed")
                .setMessage("This app needs the Camera permission to take pictures. Please allow this permission in App Settings.")
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode ==  Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            upload_item_image.setImageURI(imageUri);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imageInBytes = stream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (requestCode == CAMERA_REQUEST_CODE && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            upload_item_image.setImageBitmap(photo);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imageInBytes = stream.toByteArray();
        }
    }

}
package UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.assignment.R;

import java.util.Calendar;
import java.util.List;

import DB_Context.DBContext;
import DB_Context.ProductListModel;


public class Product_Form_Fragment extends Fragment {
    EditText date_time_picker;
    EditText ref_no;
    EditText product_name;
    EditText price;
    EditText remark;
    EditText reporter;
    Button save_btn;
    Button delete_btn;
    Button cancel_btn;
    private CheckBox cbPurchased;
    public String current_mode;
    public String current_username;
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
        reporter=form_view.findViewById(R.id.reporter_name);
        cbPurchased =form_view.findViewById(R.id.cbPurchased );
        date_time_picker=form_view.findViewById(R.id.date);
        save_btn=form_view.findViewById(R.id.save_btn);
        delete_btn=form_view.findViewById(R.id.delete_btn);
        cancel_btn = form_view.findViewById(R.id.cancel_btn);
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
            date_time_picker.setText(property_list.get(0).getDate());
            product_name.setText(property_list.get(0).getProductName());
            price.setText(property_list.get(0).getPrice());
            remark.setText(property_list.get(0).getRemark());
            cbPurchased.setChecked(property_list.get(0).isPurchased());
            reporter.setText(property_list.get(0).getUserName());
        }

        reporter.setText(current_username);
        final Calendar calendar=Calendar.getInstance();

        date_time_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SingleDateAndTimePickerDialog.Builder builder = new SingleDateAndTimePickerDialog.Builder(Property_Form_Fragment.this.getActivity())
//                        .setTitle("Select Date and Time")
//                        .setListener(new SingleDateAndTimePickerDialog.Listener() {
//                            @Override
//                            public void onDateSelected(Date date) {
//                                // Handle the selected date and time
//                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
//                                String formattedDateTime = sdf.format(date);
//                                date_time_picker.setText(formattedDateTime);
//                            }
//                        });
//
//                SingleDateAndTimePickerDialog dialog = builder.build();
//                dialog.display();
//                SingleDateAndTimePickerDialog.Builder builder=new SingleDateAndTimePickerDialog.Builder(Property_Form_Fragment.this.getActivity())
//                        .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
//                            @Override
//                            public void onDisplayed(SingleDateAndTimePicker picker) {
//                                // Retrieve the SingleDateAndTimePicker
//                            }
//
//                            @Override
//                            public void onClosed(SingleDateAndTimePicker picker) {
//                                picker.getDate();
//                            }
//                        })
//                        .title("Simple")
//                        .listener(new SingleDateAndTimePickerDialog.Listener() {
//                            @Override
//                            public void onDateSelected(Date date) {
//                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
//                                String formattedDateTime = sdf.format(date);
//                                date_time_picker.setText(formattedDateTime);
//                            }
//                        }).display();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(current_mode=="add_mode"){
                //should add trim later
                String add_date=date_time_picker.getText().toString();
                String pn=product_name.getText().toString();
                String pr=price.getText().toString();
                String rem=remark.getText().toString();
                String rp_name=reporter.getText().toString();
                boolean pc=cbPurchased.isChecked();

                if( add_date.isEmpty()|| pn.isEmpty() || pr.isEmpty() || rem.isEmpty())
                {
                    Toast.makeText(Product_Form_Fragment.this.getActivity(), "Enter all data", Toast.LENGTH_SHORT).show();
                }
                else {
                        dbContext.addProductList(add_date,pn,pr,rem,rp_name,pc);
                      //  Toast.makeText(Property_Form_Fragment.this.getActivity(), "New property added successfully", Toast.LENGTH_SHORT).show();
                    FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack();
                }
            }
            if(current_mode=="detail_mode"){
                String add_date=date_time_picker.getText().toString();
                String pn=product_name.getText().toString();
                String pr=price.getText().toString();
                String rem=remark.getText().toString();
                String rp_name=reporter.getText().toString();
                boolean pc=cbPurchased.isChecked();
                dbContext.updateProductList(reference_no,Integer.parseInt(reference_no) ,add_date,pn,pr,rem,rp_name,pc);
                FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
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
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        return form_view;
    }
}
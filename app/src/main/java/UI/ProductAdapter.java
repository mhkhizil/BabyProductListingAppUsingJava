package UI;// Declares the package
// Import necessary Android components and libraries
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.R;

import java.util.List;

import DB_Context.DBContext;
import DB_Context.ProductListModel;
// Adapter class for displaying a list of products in a RecyclerView
public class ProductAdapter extends RecyclerView.Adapter<MyViewHolder> {
    // Declare variables to hold the context, data list, and click listener
    private Context context;
    private List<ProductListModel> dataList;
    private PropertyClickListener propertyClickListener;
    // Constructor initializes the adapter with context, data, and click listener
    public ProductAdapter(Context context, List<ProductListModel> dataList, PropertyClickListener propertyClickListener) {
        this.context = context;
        this.dataList = dataList;
        this.propertyClickListener=propertyClickListener;
    }
    // Function to mark a product as purchased when swipedright
    public void markAsPurchased(int position) {
        ProductListModel product = dataList.get(position);
        product.setPurchased(true);
        notifyItemChanged(position);
        // Update the database
        DBContext dbContext = new DBContext(context);
        dbContext.updateProductList( String.valueOf(product.getRef_no()),product.getRef_no(), product.getProductName(), product.getPrice(), product.getRemark(), true, product.getImage());
        Toast.makeText(context, "Purchase status changed!", Toast.LENGTH_SHORT).show();
        // Update the list and notify adapter
        dataList.set(position, product);  // Update the item in the list

    }  // Function to delete a product  when swipedleft

    public void deleteItem(int position) {
        ProductListModel product = dataList.get(position);
        // Remove from the database
        DBContext dbContext = new DBContext(context);
        dbContext.deleteProductList(String.valueOf(product.getRef_no()));
        Toast.makeText(context, "Item deleted " , Toast.LENGTH_SHORT).show();
        // Remove from the list and notify adapter
        dataList.remove(position);
        notifyItemRemoved(position);
    }
    // Creates and returns a new ViewHolder for an item in the list
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list__item, parent, false);
        // Inflate the layout for a product item
        return new MyViewHolder(view,propertyClickListener);
    }
    // Binds data to a ViewHolder at a specific position
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
// Set the product name in the ViewHolder (truncated if too long)
        holder.property_title.setText(
                (dataList.get(position).getProductName().length() > 5)
                        ? dataList.get(position).getProductName().substring(0, 5) + "..."
                        : dataList.get(position).getProductName()
        );
        // Set the purchase status text and color based on whether the product is purchased
        if (dataList.get(position).isPurchased()) {  // Assuming your boolean field is named 'isPurchased'
            holder.purchase_status.setText("Purchased");
            holder.purchase_status.setTextColor(context.getResources().getColor(R.color.mid_green)); // Set to green
        } else {
            holder.purchase_status.setText("Not Purchased");
            holder.purchase_status.setTextColor(context.getResources().getColor(R.color.red));
        }
        // Set the product image in the ViewHolder
        byte[] imageData = dataList.get(position).getImage();

        if (imageData != null) {
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                holder.upload_item_image.setImageBitmap(bitmap);
            } catch (Exception e) {
                // Handle decoding errors here (e.g., set a placeholder image)
                holder.upload_item_image.setImageResource(R.drawable.placeholder_product);
            }
        } else {
            // Set placeholder if no image is available
            holder.upload_item_image.setImageResource(R.drawable.placeholder_product);
        }
    }



    // Returns the total number of items in the data list
    @Override
    public int getItemCount() {
        return dataList.size();
    }



}
// ViewHolder class for holding references to views within a product item layout
class MyViewHolder extends RecyclerView.ViewHolder{
    TextView purchase_status;
    TextView property_title;
    ImageView upload_item_image;
    CardView property_Card;
    ImageView share_icon;
    // Constructor initializes the ViewHolder with a view and a click listener
    public MyViewHolder(@NonNull View itemView,PropertyClickListener propertyClickListener) {
        super(itemView);
        // ... (Find and initialize the views within the itemView)
        property_Card = itemView.findViewById(R.id.property_Card);
        property_title = itemView.findViewById(R.id.property_Title);
        purchase_status=itemView.findViewById(R.id.purchase_status);
        upload_item_image=itemView.findViewById(R.id.product_Image);
        share_icon = itemView.findViewById(R.id.share_icon);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (propertyClickListener != null) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        propertyClickListener.onItemClick(pos);
                    }
                }
            }



        });
        // Set a click listener on the share_icon
        share_icon.setOnClickListener(view ->

        {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && propertyClickListener != null) {
                propertyClickListener.onShareClick(pos); // New method in interface
            }
        });




    }
}

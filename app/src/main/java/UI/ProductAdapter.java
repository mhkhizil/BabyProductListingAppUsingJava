package UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.R;

import java.util.List;

import DB_Context.ProductListModel;

public class ProductAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<ProductListModel> dataList;
    private PropertyClickListener propertyClickListener;

    public ProductAdapter(Context context, List<ProductListModel> dataList, PropertyClickListener propertyClickListener) {
        this.context = context;
        this.dataList = dataList;
        this.propertyClickListener=propertyClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list__item, parent, false);
        return new MyViewHolder(view,propertyClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.property_title.setText(
                (dataList.get(position).getProductName().length() > 5)
                        ? dataList.get(position).getProductName().substring(0, 5) + "..."
                        : dataList.get(position).getProductName()
        );

        if (dataList.get(position).isPurchased()) {  // Assuming your boolean field is named 'isPurchased'
            holder.purchase_status.setText("Purchased");
            holder.purchase_status.setTextColor(context.getResources().getColor(R.color.mid_green)); // Set to green
        } else {
            holder.purchase_status.setText("Not Purchased");
            holder.purchase_status.setTextColor(context.getResources().getColor(R.color.red));
        }
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




    @Override
    public int getItemCount() {
        return dataList.size();
    }



}
class MyViewHolder extends RecyclerView.ViewHolder{
    TextView purchase_status;
    TextView property_title;
    ImageView upload_item_image;
    CardView property_Card;
    public MyViewHolder(@NonNull View itemView,PropertyClickListener propertyClickListener) {
        super(itemView);
//        recImage = itemView.findViewById(R.id.recImage);
        property_Card = itemView.findViewById(R.id.property_Card);
//        recDesc = itemView.findViewById(R.id.recDesc);
//        recLang = itemView.findViewById(R.id.recLang);
        property_title = itemView.findViewById(R.id.property_Title);
        purchase_status=itemView.findViewById(R.id.purchase_status);
        upload_item_image=itemView.findViewById(R.id.product_Image);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(propertyClickListener!= null){
                    int pos=getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        propertyClickListener.onItemClick(pos);
                    }
                }
            }
        });
    }
}

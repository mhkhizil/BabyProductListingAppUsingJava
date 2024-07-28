package UI;// Declares the package
// Import necessary Android components and libraries for swipe-to-delete functionality
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
// Class that enables swipe-to-delete and swipe-to-mark-as-purchased functionality in a RecyclerView
public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    // Reference to the ProductAdapter that manages the data in the RecyclerView
    private ProductAdapter mAdapter;
    // Constructor initializes the SwipeToDeleteCallback with the adapter
    // and sets the allowed swipe directions (left and right)
    public SwipeToDeleteCallback(ProductAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
    }
    // This method is called when an item is moved within the RecyclerView
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }
    // This method is called when an item is swiped left or right
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.RIGHT) {
            mAdapter.markAsPurchased(position); // Handle right swipe
        } else if (direction == ItemTouchHelper.LEFT) {
            mAdapter.deleteItem(position); // Handle left swipe
        }

    }
}

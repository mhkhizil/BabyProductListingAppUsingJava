package DB_Context;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBContext extends SQLiteOpenHelper {
    //declaring database name
    private static String DB_NAME="BabyBuyBabyProductListing";

    //declaring table name
    private static String USERS_TABLE ="users_table";
    private static String PRODUCT_LIST_TABLE ="product_list_table";

    //declaring column names for user_table
    private static String USER_ID="id";
    private static String USER_NAME="name";
    //private static String USER_EMAIL="email";
    private static String USER_PASSWORD="password";
    //declaring column names for property_table
    private static String PROPERTY_REF_NO="ref_no";
    private static String PRODUCT_NAME="product_name";
    private static String PRICE= "product_price";
    private static String REMARK="remark";
    private static String PURCHASED = "purchased";
    private static String IMAGE = "image"; //
    private static String PRODUCT_USER_ID = "user_id";

    public DBContext(Context context){
        super(context,DB_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //creating table for user
        String user_create="CREATE TABLE "+ USERS_TABLE +"("+
                USER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                USER_NAME+" TEXT,"+USER_PASSWORD+" TEXT)";

        db.execSQL(user_create);

        //creating table for product
        String property_create="CREATE TABLE "+ PRODUCT_LIST_TABLE +"("+
                PROPERTY_REF_NO+" INTEGER PRIMARY KEY AUTOINCREMENT,"+PRODUCT_NAME+" TEXT,"+PRICE+" TEXT,"+REMARK+" TEXT,"+ PURCHASED + " INTEGER," + IMAGE + " BLOB,"+
                PRODUCT_USER_ID+" INTEGER," +
                "FOREIGN KEY(" + PRODUCT_USER_ID + ") REFERENCES " + USERS_TABLE + "(" + USER_ID + "))";

        db.execSQL(property_create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ USERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ PRODUCT_LIST_TABLE);

    }

    //adding a new user to user table
    public void addUser(String username,String password)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(USER_NAME,username);

        contentValues.put(USER_PASSWORD,password);

        database.insert(USERS_TABLE,null,contentValues);
        database.close();
    }

    //reading user data to arraylist
    public ArrayList<UserModel> readUser()
    {
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor user_cursor=database.rawQuery("SELECT * FROM "+ USERS_TABLE,null);

        ArrayList<UserModel> userModelArrayList=new ArrayList<>();

        if (user_cursor.moveToFirst())
        {
            do {
                userModelArrayList.add(new UserModel(user_cursor.getInt(0),
                        user_cursor.getString(1),
                        user_cursor.getString(2)));
            }while (user_cursor.moveToNext());
        }
        return userModelArrayList;
    }

    //adding a new product to product table
    public void addProductList( int user_id, String product_name, String product_price, String remark,boolean purchased,byte[] image)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(PRODUCT_USER_ID, user_id);
        contentValues.put(PRODUCT_NAME, product_name);
        contentValues.put(PRICE,product_price);
        contentValues.put(REMARK,remark);
        contentValues.put(PURCHASED, purchased ? 1 : 0);
        contentValues.put(IMAGE, image);
        database.insert(PRODUCT_LIST_TABLE,null,contentValues);
        database.close();
    }

    //reading data from product
    public ArrayList<ProductListModel> readProductList()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+ PRODUCT_LIST_TABLE,null);
        ArrayList<ProductListModel> productModelArrayList=new ArrayList<>();

        if(cursor.moveToFirst())
        {
            do{
                productModelArrayList.add(new ProductListModel(cursor.getInt(0),cursor.getString(1)
                        ,cursor.getString(2), cursor.getString(3),cursor.getInt(4) == 1,cursor.getBlob(5),cursor.getInt(6)));

            }while (cursor.moveToNext());
        }
        db.close();
        return productModelArrayList;
    }
    //reading data from product table by user ID
    public ArrayList<ProductListModel> readProductListByUserId(int user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PRODUCT_LIST_TABLE + " WHERE " + PRODUCT_USER_ID + "=?", new String[]{String.valueOf(user_id)});
        ArrayList<ProductListModel> productModelArrayList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                productModelArrayList.add(new ProductListModel(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getInt(4) == 1, cursor.getBlob(5), cursor.getInt(6)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productModelArrayList;
    }
    //read product by ref no
    public ArrayList<ProductListModel> readProductByRefNumber(String ref_no, int user_id){
        SQLiteDatabase db=this.getReadableDatabase();
        String selection = PROPERTY_REF_NO + "=? AND " + PRODUCT_USER_ID + "=?";
        String[] selectionArgs = { ref_no, String.valueOf(user_id) };
        Cursor cursor = db.query(PRODUCT_LIST_TABLE, null, selection, selectionArgs, null, null, null);
        ArrayList<ProductListModel> productModelArrayList=new ArrayList<>();

        if(cursor.moveToFirst())
        {
            do{
                productModelArrayList.add(new ProductListModel(cursor.getInt(0),cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3), cursor.getInt(4) == 1,cursor.getBlob(5),cursor.getInt(6)));

            }while (cursor.moveToNext());
        }
        db.close();
        return productModelArrayList;
    }
    //search product product name
    public ArrayList<ProductListModel> searchProductByProductName(String product_name, int user_id){
        SQLiteDatabase db=this.getReadableDatabase();
        String selection = PRODUCT_NAME + " LIKE ? AND " + PRODUCT_USER_ID + "=?";
        String[] selectionArgs = { "%" + product_name + "%", String.valueOf(user_id) };
        Cursor cursor = db.query(PRODUCT_LIST_TABLE, null, selection, selectionArgs, null, null, null);
        ArrayList<ProductListModel> productModelArrayList=new ArrayList<>();

        if(cursor.moveToFirst())
        {
            do{
                productModelArrayList.add(new ProductListModel(cursor.getInt(0),cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                     cursor.getInt(4) == 1,cursor.getBlob(5),cursor.getInt(6)));

            }while (cursor.moveToNext());
        }
        db.close();
        return productModelArrayList;
    }

    //delete property form product table
    public void deleteProductList(String ref_no)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(PRODUCT_LIST_TABLE,"ref_no=?",new String[]{ref_no});
        db.close();
    }
    //update info in product table

    public void updateProductList(String original_ref_no, int new_ref_no, String product_name, String product_price, String remark,boolean purchased,byte[] image)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(PROPERTY_REF_NO,new_ref_no);
        contentValues.put(PRODUCT_NAME,product_name);
        contentValues.put(PRICE,product_price);
        contentValues.put(REMARK,remark);
        contentValues.put(PURCHASED, purchased ? 1 : 0);
        contentValues.put(IMAGE, image);
        db.update(PRODUCT_LIST_TABLE,contentValues,"ref_no=?",new String[]{original_ref_no});

    }
    //get table list
    public List<String> getTableList() {
        List<String> tableList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        try {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String tableName = cursor.getString(0);
                    if (!tableName.equals("android_metadata")) {
                        tableList.add(tableName);
                        Log.d("TableList", tableName);
                    }

                    cursor.moveToNext();
                }
            }
        } finally {
            cursor.close();
        }

        return tableList;
    }
}

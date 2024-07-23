package DB_Context;

public class ProductListModel {


    private String date;
    private int ref_no;

    private String product_name;
    private String price;
    private String remark;
    private String user_name;
    private boolean purchased;





    public String getDate() { return date; }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getProductName() {
        return product_name;
    }

    public void setProductName(String product_name) {
        this.product_name = product_name;
    }

    public String getPrice()
    {
        return price;
    }

    public void
    setPrice(String price)
    {
        this.price = price;
    }

    public int getRef_no() { return ref_no; }

    public void setRef_no(int ref_no) { this.ref_no = ref_no; }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    // constructor
    public ProductListModel(int ref_no,
                            String date, String product_name,
                            String price, String remark, String user_name, boolean purchased)
    {
        this.ref_no=ref_no;
        this.date = date;
        this.product_name = product_name;
        this.price = price;
        this.remark = remark;
        this.user_name = user_name;
        this.purchased = purchased;
    }
}

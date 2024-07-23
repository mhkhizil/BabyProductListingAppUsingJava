package DB_Context;

public class ProductListModel {
    private int ref_no;
    private String product_name;
    private String price;
    private String remark;
    private boolean purchased;
    private byte[] image;
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

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }


    // constructor
    public ProductListModel(int ref_no, String product_name,
                            String price, String remark, boolean purchased,byte[] image)
    {
        this.ref_no=ref_no;
        this.product_name = product_name;
        this.price = price;
        this.remark = remark;
        this.purchased = purchased;
        this.image = image;
    }
}

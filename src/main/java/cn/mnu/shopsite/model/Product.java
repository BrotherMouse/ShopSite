package cn.mnu.shopsite.model;
import java.util.Date;
import java.util.List;

/**
 * 商品信息
 *
 * @author Yanghai
 */

public class Product {
    /**
     * 商品id
     */
    private int id;

    /**
     * 商品分类id，即ProductCategory中的id，如cellphone、computer
     */
    private String categoryId;

    /**
     * 商品品牌id，即ProductBrand中的id，如xiaomi、lenovo
     */
    private String brandId;

    /**
     * 商品名称，如iphone 10 plus
     */
    private String name;

    /**
     * 售价
     */
    private double price;

    /**
     * 促销价
     */
    private double salePrice;

    /**
     * 进货量
     */
    private int purchasedAmount;

    /**
     * 库存量
     */
    private int stockBalance;

    /**
     * 上市日期，商品的上市日期，用于index页面中【品牌分类】展示各品牌最新上市几款商品
     */
    private Date listingDate;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 备注
     */
    private String remark;

    /**
     * 封面图（1张）路径
     */
    private String coverPath;

    /**
     * 展示图（多张）路径
     */
    private List<String> exhibitPaths;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public int getPurchasedAmount() {
        return purchasedAmount;
    }

    public void setPurchasedAmount(int purchasedAmount) {
        this.purchasedAmount = purchasedAmount;
    }

    public int getStockBalance() {
        return stockBalance;
    }

    public void setStockBalance(int stockBalance) {
        this.stockBalance = stockBalance;
    }

    public Date getListingDate() {
        return listingDate;
    }

    public void setListingDate(Date listingDate) {
        this.listingDate = listingDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public List<String> getExhibitPaths() {
        return exhibitPaths;
    }

    public void setExhibitPaths(List<String> exhibitPaths) {
        this.exhibitPaths = exhibitPaths;
    }
}

package cn.mnu.shopsite.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderedProduct {
    /**
     * 商品id
     */
    private int id;

    /**
     * 商品种类id，即ProductCategory中的id，如Cellphone、Computer
     */
    private String categoryId;

    /**
     * 所属品牌id，即ProductBrand中的id，如Xiaomi、Lenovo
     */
    private String brandId;

    /**
     * 商品名称，如iphone 10 plus
     */
    private String name;

    /**
     * 成交价格
     */
    private double price;

    /**
     * 购买数量
     */
    private int amount;

    /**
     * 上市日期，商品的上市日期，用于index页面中【品牌分类】展示各品牌最新上市几款商品
     */
    private Date listingDate;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 缩略图（1张）路径
     */
    private String thumbnailPath;

    /**
     * 展示图（多张）路径
     */
    private List<String> exhibitPaths;

    public OrderedProduct(Product product, int amount) {
        this.id = product.getId();
        this.categoryId = product.getCategoryId();
        this.brandId = product.getBrandId();
        this.name = product.getName();
        this.price = product.getSalePrice();
        this.amount = amount;
        this.listingDate = product.getListingDate();
        this.description = product.getDescription();
        this.thumbnailPath = product.getThumbnailPath();
        this.exhibitPaths = product.getExhibitPaths();
    }
}

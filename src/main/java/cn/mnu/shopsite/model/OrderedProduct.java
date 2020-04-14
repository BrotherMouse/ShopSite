package cn.mnu.shopsite.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 已下单的商品信息bean
 *
 * @author Yanghai
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderedProduct {
    /**
     * 商品id，即Product中的id
     */
    private int id;

    /**
     * 商品种类id，即ProductCategory中的id，如cellphone、Computer
     */
    private String categoryId;

    /**
     * 所属品牌id，即ProductBrand中的id，如xiaomi、lenovo
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
     * 上市日期
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

    /**
     * 根据商品信息生成已下单商品信息
     *
     * @param product 商品信息
     * @param amount 下单数量
     */
    public OrderedProduct(Product product, int amount) {
        this(product, product.getSalePrice(), amount);
    }

    /**
     * 根据商品信息生成已下单商品信息
     *
     * @param product 商品信息
     * @param price 成交价格
     * @param amount 下单数量
     */
    public OrderedProduct(Product product, double price, int amount) {
        this.id = product.getId();
        this.categoryId = product.getCategoryId();
        this.brandId = product.getBrandId();
        this.name = product.getName();
        this.price = price;
        this.amount = amount;
        this.listingDate = product.getListingDate();
        this.description = product.getDescription();
        this.remark = product.getRemark();
        this.coverPath = product.getCoverPath();
        this.exhibitPaths = product.getExhibitPaths();
    }
}
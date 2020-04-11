package cn.mnu.shopsite.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 商品信息
 *
 * @author Yanghai
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
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
     * 售价
     */
    private double price;

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
     * 缩略图（1张）路径
     */
    private String thumbnailPath;

    /**
     * 展示图（多张）路径
     */
    private List<String> exhibitPaths;
}
package cn.mnu.shopsite.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分类-商品bean，承载某个分类、该分类的广告产品及该分类的若干个商品信息，比如【5个手机商品，以小米Note8为广告】
 *
 * @author Yanghai
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryProducts {
    /**
     * 商品分类信息
     */
    private ProductCategory category;

    /**
     * 该分类的若干个商品
     */
    private List<Product> products;

    /**
     * 该分类的广告商品
     */
    private Product adProduct;
}
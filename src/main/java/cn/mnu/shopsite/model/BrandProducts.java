package cn.mnu.shopsite.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 品牌-商品bean，承载某个品牌及该品牌的若干个商品信息，比如【5个小米商品】
 *
 * @author Yanghai
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandProducts {
    /**
     * 商品品牌信息
     */
    private ProductBrand brand;

    /**
     * 该品牌的若干个商品
     */
    private List<Product> products;
}
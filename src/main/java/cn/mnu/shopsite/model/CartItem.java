package cn.mnu.shopsite.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 购物车条目bean
 *
 * @author Yanghai
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    /**
     * 商品信息
     */
    private Product product;

    /**
     * 该商品在购物车中的数量
     */
    private int amount;
}
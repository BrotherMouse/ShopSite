package cn.mnu.shopsite.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 定单条目bean
 *
 * @author hyang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    /**
     * 定单id
     */
    private int id;

    /**
     * 该定单包含的已下单的商品信息
     */
    private List<OrderedProduct> products;

    /**
     * 定单提交日期
     */
    private Date commitDate;

    /**
     * 定单状态，ordered - 已下单未支付，payed - 已支持，done - 完成
     */
    private String status;
}

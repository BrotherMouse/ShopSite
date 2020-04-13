package cn.mnu.shopsite.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品种类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory {
    /**
     * 商品种类id，如computer、cellphone
     */
    private String id;

    /**
     * 种类名称，如电脑、手机
     */
    private String name;

    /**
     * 宣传语，如科技改变生活
     */
    private String slogan;

    /**
     * 展示顺序，数值越小，在页面中显示越靠前
     */
    private int displayOrder;

    /**
     * 备注
     */
    private String remark;
}
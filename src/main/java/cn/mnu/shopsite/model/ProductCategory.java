package cn.mnu.shopsite.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * 商品种类
 */
public class ProductCategory {
    /**
     * 商品种类id
     */
    private String id;

    /**
     * 种类名称
     */
    private String name;

    /**
     * 广告语
     */
    private String adWords;

    /**
     * 广告图片路径
     */
    private String adImage;

    /**
     * 展示顺序
     */
    private int displayOrder;

    /**
     * 备注
     */
    private String remark;
}
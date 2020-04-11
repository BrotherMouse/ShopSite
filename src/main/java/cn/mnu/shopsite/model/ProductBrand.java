package cn.mnu.shopsite.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 品牌信息，即各生产商的信息
 *
 * @author Yanghai
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductBrand {
    /**
     * 品牌id，如Xiaomi、Lenovo
     */
    private String id;

    /**
     * 品牌名称，如小米、联想
     */
    private String name;

    /**
     * 排名顺序，数值越小，排名越靠前，主要用于页面展示的顺序（即index页面中【品牌分类】的展示顺序）
     */
    private int rankOrder;

    /**
     * 备注
     */
    private String remark;
}
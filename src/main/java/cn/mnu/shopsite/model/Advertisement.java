package cn.mnu.shopsite.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 广告信息
 *
 * @author Yanghai
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Advertisement {
    /**
     * 广告大类，目前仅2种值：slide，表示这是轮播广告的信息；category，表示这是某个商品分类的广告信息
     */
    private String type;

    /**
     * 广告子类，type=slide时此字段无意义、固定为slide，type=category时为商品分类id
     */
    private String subtype;

    /**
     * 该类型广告的id，可表示顺序
     */
    private int id;

    /**
     * 商品id，要打广告的商品
     */
    private int productId;

    /**
     * 备注
     */
    private String remark;
}

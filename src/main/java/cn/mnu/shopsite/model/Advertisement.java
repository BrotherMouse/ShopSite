package cn.mnu.shopsite.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Advertisement {
    /**
     * 广告大类
     */
    private String type;

    private String subtype;

    private int id;

    private int productId;

    private String remark;
}

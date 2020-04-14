package cn.mnu.shopsite.dao;

import cn.mnu.shopsite.model.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 商品分类信息dao
 *
 * @author Yanghai
 */
@Repository
public class ProductCategoryDao {
    /**
     * 操作数据库的对象
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ProductCategoryRowMapper rowMapper = new ProductCategoryRowMapper();

    /**
     * 商品分类数据行映射类，从数据库获得一行数据后，如何将这些数据设置为java类对象的字段值，通过此类完成
     */
    private static class ProductCategoryRowMapper implements RowMapper<ProductCategory> {
        @Override
        public ProductCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductCategory brand = new ProductCategory();

            brand.setId(rs.getString("id"));
            brand.setName(rs.getString("name"));
            brand.setSlogan(rs.getString("slogan"));
            brand.setDisplayOrder(rs.getInt("display_order"));
            brand.setRemark(rs.getString("remark"));

            return brand;
        }
    }

    /**
     * 获得全部商品分类信息（按显示顺序排序）
     *
     * @return 全部商品分类信息（按显示顺序排序）
     */
    public List<ProductCategory> getAllCategoriesInOrder() {
        String sql = "select * from t_product_category order by display_order";

        return jdbcTemplate.query(sql, rowMapper);
    }

    /**
     * 查询商品分类信息
     *
     * @param id 分类id
     * @return null - 商品分类不存在，非null - 商品分类信息
     */
    public ProductCategory queryCategory(String id) {
        String sql = "select * from t_product_category where id = ?";
        List<ProductCategory> brands = jdbcTemplate.query(sql, rowMapper, id);

        return brands.isEmpty() ? null : brands.get(0);
    }

    /**
     * 添加商品分类信息
     *
     * @param category 商品分类信息
     * @return true - 成功，false - 失败（分类id已存在）
     */
    public boolean addCategory(ProductCategory category) {
        String insertOrderSql = "insert into t_product_category values (?, ?, ?, ?, ?)";

        try {
            jdbcTemplate.update(insertOrderSql, category.getId(), category.getName(), category.getSlogan(),
                    category.getDisplayOrder(), category.getRemark());
            return true;
        }
        catch(DataAccessException ex) {
            return false;
        }
    }

    /**
     * 删除商品分类信息
     *
     * @param id 分类id
     */
    public void deleteCategory(String id) {
        String insertOrderSql = "delete from t_product_category where id = ?";

        jdbcTemplate.update(insertOrderSql, id);
    }
}
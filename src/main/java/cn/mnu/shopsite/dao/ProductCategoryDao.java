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

@Repository
public class ProductCategoryDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ProductCategoryRowMapper rowMapper = new ProductCategoryRowMapper();

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

    public List<ProductCategory> getAllCategories() {
        String sql = "select * from t_product_category";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<ProductCategory> getAllCategoriesInOrder() {
        String sql = "select * from t_product_category order by display_order";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public ProductCategory queryCategory(String id) {
        String sql = "select * from t_product_category where id = ?";
        List<ProductCategory> brands = jdbcTemplate.query(sql, rowMapper, id);

        return brands.isEmpty() ? null : brands.get(0);
    }

    public String queryCategoryName(String id) {
        ProductCategory category = queryCategory(id);

        return category == null ? null : category.getName();
    }

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

    public void deleteCategory(ProductCategory category) {
        String insertOrderSql = "delete from t_product_category where id = ?";

        jdbcTemplate.update(insertOrderSql, category.getId());
    }
}
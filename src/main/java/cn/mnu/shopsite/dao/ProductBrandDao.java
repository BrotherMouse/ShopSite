package cn.mnu.shopsite.dao;

import cn.mnu.shopsite.model.ProductBrand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 商品品牌信息dao
 *
 * @author Yanghai
 */
@Repository
public class ProductBrandDao {
    /**
     * 操作数据库的对象
     */
    @Autowired
    JdbcTemplate jdbcTemplate;

    private ProductBrandRowMapper rowMapper = new ProductBrandRowMapper();

    /**
     * 商品品牌数据行映射类，从数据库获得一行数据后，如何将这些数据设置为java类对象的字段值，通过此类完成
     */
    private static class ProductBrandRowMapper implements RowMapper<ProductBrand> {
        @Override
        public ProductBrand mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductBrand brand = new ProductBrand();

            brand.setId(rs.getString("id"));
            brand.setName(rs.getString("name"));
            brand.setRankOrder(rs.getInt("rank_order"));
            brand.setRemark(rs.getString("remark"));

            return brand;
        }
    }

    /**
     * 获得全部品牌信息
     *
     * @return 全部品牌信息
     */
    public List<ProductBrand> getAllBrands() {
        String sql = "select * from t_product_brand";

        return jdbcTemplate.query(sql, rowMapper);
    }

    /**
     * 获得全部品牌信息（按排名顺序排序）
     *
     * @return 排序后的全部品牌信息
     */
    public List<ProductBrand> getAllBrandsRanking() {
        String sql = "select * from t_product_brand order by rank_order";

        return jdbcTemplate.query(sql, rowMapper);
    }

    /**
     * 查询品牌信息
     *
     * @param id 品牌id
     * @return null - 品牌不存在，非null - 品牌信息
     */
    public ProductBrand queryBrand(String id) {
        String sql = "select * from t_product_brand where id = ?";
        List<ProductBrand> brands = jdbcTemplate.query(sql, rowMapper, id);

        return brands.isEmpty() ? null : brands.get(0);
    }

    /**
     * 查询品牌名称
     *
     * @param id 品牌id
     * @return null - 品牌不存在，非null - 品牌名称
     */
    public String queryBrandName(String id) {
        ProductBrand brand = queryBrand(id);

        return brand == null ? null : brand.getName();
    }

    /**
     * 添加商品品牌信息
     *
     * @param brand 商品品牌信息
     * @return true - 成功，false - 失败（该商品品牌id已经存在）
     */
    public boolean addBrand(ProductBrand brand) {
        String insertOrderSql = "insert into t_product_brand values (?, ?, ?, ?)";

        try {
            jdbcTemplate.update(insertOrderSql, brand.getId(), brand.getName(), brand.getRankOrder(), brand.getRemark());
            return true;
        }
        catch(DataAccessException ex) {
            return false;
        }
    }

    /**
     * 删除商品品牌信息
     *
     * @param id 品牌id
     */
    public void deleteBrand(String id) {
        String insertOrderSql = "delete from t_product_brand where id = ?";

        jdbcTemplate.update(insertOrderSql, id);
    }
}
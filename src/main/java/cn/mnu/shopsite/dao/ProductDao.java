package cn.mnu.shopsite.dao;

import cn.mnu.shopsite.model.OrderedProduct;
import cn.mnu.shopsite.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductDao {
    private static final int TRY_TIMES = 10;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ProductRowMapper rowMapper = new ProductRowMapper();

    /**
     * 商品数据包装类，将从数据库获得的商品信息，封装为商品类对象，即完成数据库字段和类字段的映射
     */
    public class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();

            product.setId(rs.getInt("id"));
            product.setCategoryId(rs.getString("category_id"));
            product.setBrandId(rs.getString("brand_id"));
            product.setName(rs.getString("name"));
            product.setPrice(rs.getDouble("price"));
            product.setSalePrice(rs.getDouble("sale_price"));
            product.setPurchasedAmount(rs.getInt("purchase_amount"));
            product.setStockBalance(rs.getInt("stock_balance"));
            product.setListingDate(rs.getDate("listing_date"));
            product.setDescription(rs.getString("description"));

            setProductImagePath(product);
            return product;
        }
    }

    /**
     * 根据商品id查询商品信息
     *
     * @param id 商品id
     * @return null - 商品不存在，非null - 商品信息
     */
    public Product queryProduct(int id) {
        String sql = "select * from t_product where id = ?";
        List<Product> products = jdbcTemplate.query(sql, rowMapper, id);

        return products.isEmpty() ? null : products.get(0);
    }

    /**
     * 获得商品的图片路径并设置到相关字段
     *
     * @param product 商品信息
     */
    private void setProductImagePath(Product product) {
        String sql = "select * from t_product_images where id = ? order by sequence";
        List<String> exhibitPaths = new ArrayList<>();
        jdbcTemplate.query(sql, rs -> {
            String imagePath = rs.getString("path");
            //封面图
            if("cover".equals(rs.getString("type"))) {
                product.setCoverPath(imagePath);
            }
            //展示图
            else {
                exhibitPaths.add(imagePath);
            }
        }, product.getId());

        product.setExhibitPaths(exhibitPaths);
    }

    /**
     * 查询某个品牌的最新上架的n个商品信息
     *
     * @param brandId 品牌id，如Xiaomi、Lenovo等
     * @param n 取最新的n个
     * @return 该品牌最新上架的m个商品信息，m <= n
     */
    public List<Product> queryBrandNewProducts(String brandId, int n) {
        String sql = "select * from t_product where brand_id = ? order by listing_date desc limit ?";

        return jdbcTemplate.query(sql, rowMapper, brandId, n);
    }

    /**
     * 查询某个种类的最新上市的n个商品信息
     *
     * @param categoryId 品牌id，如Cellphone、Computer等
     * @param n 取最新上市的n个
     * @return 该种类最新上市的m个商品信息，m <= n
     */
    public List<Product> queryCategoryNewProducts(String categoryId, int n) {
        String sql = "select * from t_product where category_id = ? order by listing_date desc limit ?";

        return jdbcTemplate.query(sql, rowMapper, categoryId, n);
    }

    /**
     * 查询最具人气的n个商品信息
     *
     * @param n 取最具人气的n个
     * @return 最具人气的m个商品信息，m <= n
     */
    public List<Product> queryPopProducts(int n) {
        String sql = "select * from t_product order by (purchase_amount - stock_balance) desc limit ?";

        return jdbcTemplate.query(sql, rowMapper, n);
    }

    public boolean addProduct(Product product) {
        String maxIdString = "select IfNull(Max(id), 0) from t_product";
        String insertOrderSql = "insert into t_product values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int tryTimes = TRY_TIMES;
        while(tryTimes-- > 0) {
            Integer maxId = jdbcTemplate.queryForObject(maxIdString, Integer.class);
            int id = maxId == null ? 1 : maxId + 1;

            try {
                jdbcTemplate.update(insertOrderSql, id, product.getCategoryId(), product.getBrandId(),
                        product.getName(), product.getPrice(), product.getSalePrice(), product.getPurchasedAmount(),
                        product.getStockBalance(), product.getListingDate(), product.getDescription(), null);
                product.setId(id);
                return true;
            }
            catch(DataAccessException ex) {
                ;
            }
        }

        return false;
    }

    public boolean addProductImage(int productId, String type, String fileName, String originalName) {
        String maxSequenceString = "select IfNull(Max(sequence), 0) from t_product_images where id = ?";
        String insertOrderSql = "insert into t_product_images values (?, ?, ?, ?, ?, ?)";

        String path = "/pimages/" + fileName;

        int tryTimes = TRY_TIMES;
        while(tryTimes-- > 0) {
            Integer maxSequence = jdbcTemplate.queryForObject(maxSequenceString, Integer.class, productId);
            int sequence = maxSequence == null ? 1 : maxSequence + 1;

            try {
                jdbcTemplate.update(insertOrderSql, productId, sequence, type, path, originalName, null);
                return true;
            }
            catch(DataAccessException ex) {
                ;
            }
        }

        return false;
    }

    public List<Product> getAllProduct() {
        String sql = "select * from t_product";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public void deleteProduct(Product product) {
        String deleteImagesSql = "delete from t_product_images where id = ?";
        String deleteProductSql = "delete from t_product where id = ?";

        jdbcTemplate.update(deleteImagesSql, product.getId());
        jdbcTemplate.update(deleteProductSql, product.getId());
    }
}
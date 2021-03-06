package cn.mnu.shopsite.dao;

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

/**
 * 商品信息dao
 */
@Repository
public class ProductDao {
    /**
     * 往数据库插数据时最多尝试的次数
     */
    private static final int TRY_TIMES = 10;

    /**
     * 操作数据库的对象
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ProductRowMapper rowMapper = new ProductRowMapper();

    /**
     * 商品信息数据行映射类，从数据库获得一行数据后，如何将这些数据设置为java类对象的字段值，通过此类完成
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
            product.setPurchasedAmount(rs.getInt("purchased_amount"));
            product.setStockBalance(rs.getInt("stock_balance"));
            product.setListingDate(rs.getDate("listing_date"));
            product.setDescription(rs.getString("description"));
            product.setRemark(rs.getString("remark"));

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
     * 根据商品名称或描述模糊查找商品信息
     *
     * @param name 商品名称或者描述中包含的关键字
     * @return 空List - 无相关商品，非空List - 相关商品信息
     */
    public List<Product> queryProduct(String name) {
        String sql = "select * from t_product where name like ? or description like ?";
        name = "%" + name + "%";
        List<Product> products = jdbcTemplate.query(sql, rowMapper, name, name);

        return products;
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
     * @param brandId 品牌id，如xiaomi、lenovo等
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
     * @param categoryId 品牌id，如cellphone、computer等
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
        String sql = "select * from t_product where purchased_amount <> 0 order by (purchased_amount - stock_balance) / purchased_amount desc limit ?";

        return jdbcTemplate.query(sql, rowMapper, n);
    }

    /**
     * 添加商品信息
     *
     * @param product 商品信息，其id不会被使用
     * @return true - 成功（其id被设置），false - 失败（尝试若干次均失败）
     */
    public boolean addProduct(Product product) {
        String maxIdString = "select IfNull(Max(id), 0) from t_product";
        String insertOrderSql = "insert into t_product values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        /*
         * 由于商品id是通过取当前最大的商品id加1，存在并发时id重复的可能，故而需要不断尝试，这里尝试TRY_TIMES次
         */
        int tryTimes = TRY_TIMES;
        while(tryTimes-- > 0) {
            Integer maxId = jdbcTemplate.queryForObject(maxIdString, Integer.class);
            int id = maxId == null ? 1 : maxId + 1;

            try {
                jdbcTemplate.update(insertOrderSql, id, product.getCategoryId(), product.getBrandId(),
                        product.getName(), product.getPrice(), product.getSalePrice(), product.getPurchasedAmount(),
                        product.getStockBalance(), product.getListingDate(), product.getDescription(),
                        product.getRemark());
                product.setId(id);
                return true;
            }
            catch(DataAccessException ex) {
                ;
            }
        }

        return false;
    }

    /**
     * 添加商品图片
     *
     * @param productId 商品id
     * @param type 图片类型，cover - 封面图片，exhibit - 展示图
     * @param fileName 文件名（不带路径信息）
     * @param originalName 文件原来的名字
     * @return true - 成功，false - 失败（尝试若干次均失败）
     */
    public boolean addProductImage(int productId, String type, String fileName, String originalName) {
        String maxSequenceString = "select IfNull(Max(sequence), 0) from t_product_images where id = ?";
        String insertOrderSql = "insert into t_product_images values (?, ?, ?, ?, ?, ?)";

        //添加路径信息
        String path = "/pimages/" + fileName;

        /*
         * 由于顺序号是通过取当前最大的顺序号加1，存在并发时顺序号重复的可能，故而需要不断尝试，这里尝试TRY_TIMES次
         */
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

    /**
     * 获得全部商品信息
     *
     * @return 全部商品信息
     */
    public List<Product> getAllProduct() {
        String sql = "select * from t_product";

        return jdbcTemplate.query(sql, rowMapper);
    }

    /**
     * 删除商品信息
     *
     * @param id 商品id
     */
    public void deleteProduct(int id) {
        String deleteImagesSql = "delete from t_product_images where id = ?";
        String deleteProductSql = "delete from t_product where id = ?";

        jdbcTemplate.update(deleteImagesSql, id);
        jdbcTemplate.update(deleteProductSql, id);
    }
}
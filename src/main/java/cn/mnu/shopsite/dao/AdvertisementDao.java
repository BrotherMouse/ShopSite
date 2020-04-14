package cn.mnu.shopsite.dao;

import cn.mnu.shopsite.model.Advertisement;
import cn.mnu.shopsite.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 广告信息dao
 *
 * @author Yanghai
 */
@Repository
public class AdvertisementDao {
    /**
     * 操作数据库的对象
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 商品信息dao，用于获得要打广告的商品的信息
     */
    @Autowired
    private ProductDao productDao;

    /**
     * 查询轮播广告的商品信息
     *
     * @param n 最多获得n个商品的信息
     * @return m个商品信息，m <= n
     */
    public List<Product> querySlideAdvertisement(int n) {
        String sql = "select product_id from t_advertisement where type = 'slide'";
        List<Integer> productIds = jdbcTemplate.queryForList(sql, Integer.class);

        //根据商品id，获得商品信息
        List<Product> products = new ArrayList<>();
        for(Integer productId : productIds) {
            Product product = productDao.queryProduct(productId);
            if(product != null) {
                products.add(product);
                if(products.size() >= n) {
                    break;
                }
            }
        }

        return products;
    }

    /**
     * 查询分类广告的商品信息
     *
     * @param categoryId 分类id
     * @return 该分类下需要打广告的商品，一般不超过1个
     */
    public List<Product> queryCategoryAdvertisement(String categoryId) {
        String sql = "select product_id from t_advertisement where type = 'category' and subtype = ?";
        List<Integer> productIds = jdbcTemplate.queryForList(sql, Integer.class, categoryId);

        //根据商品id，获得商品信息
        List<Product> products = new ArrayList<>();
        for(Integer productId : productIds) {
            Product product = productDao.queryProduct(productId);
            if(product != null) {
                products.add(product);
            }
        }

        return products;
    }

    /**
     * 获得全部广告信息
     *
     * @return 全部广告信息
     */
    public List<Advertisement> getAllAdvertisements() {
        String sql = "select * from t_advertisement";
        List<Advertisement> advertisements = jdbcTemplate.query(sql, (rs, rowNum) -> {
            String type = rs.getString("type");
            String subtype = rs.getString("subtype");
            int id = rs.getInt("id");
            int productId = rs.getInt("product_id");
            String remark = rs.getString("remark");

            return new Advertisement(type, subtype, id, productId, remark);
        });

        return advertisements;
    }

    /**
     * 添加广告信息
     *
     * @param advertisement 广告信息，其id不会被使用
     * @return true - 成功，false - 失败（该类广告下已有该商品）
     */
    public boolean addAdvertisement(Advertisement advertisement) {
        String duplicatedSql = "select Count(*) from t_advertisement where type = ? and subtype = ? and product_id = ?";
        String maxIdSql = "select IfNull(Max(id), 0) from t_advertisement where type = ? and subtype = ?";
        String insertSql = "insert into t_advertisement values (?, ?, ?, ?, ?)";

        //轮播广播不需要subtype，且设置为slide
        if("slide".equals(advertisement.getType())) {
            advertisement.setSubtype("slide");
        }

        //检查该类广告下是否已存在该商品
        Integer count = jdbcTemplate.queryForObject(duplicatedSql, Integer.class, advertisement.getType(),
                advertisement.getSubtype(), advertisement.getProductId());
        if(count == null || count > 0) {
            return false;
        }

        //获得下一个广告id
        Integer maxId = jdbcTemplate.queryForObject(maxIdSql, Integer.class, advertisement.getType(),
                advertisement.getSubtype());
        advertisement.setId(maxId == null ? 1 : maxId + 1);
        try {
            jdbcTemplate.update(insertSql, advertisement.getType(), advertisement.getSubtype(), advertisement.getId(),
                    advertisement.getProductId(), advertisement.getRemark());
            return true;
        }
        catch(DataAccessException ex) {
            return false;
        }
    }

    /**
     * 删除广告信息
     *
     * @param advertisement 广告信息，只有其id、type和subtype会被使用
     */
    public void deleteAdvertisement(Advertisement advertisement) {
        String sql = "delete from t_advertisement where type = ? and subtype = ? and id = ?";

        jdbcTemplate.update(sql, advertisement.getType(), advertisement.getSubtype(), advertisement.getId());
    }
}
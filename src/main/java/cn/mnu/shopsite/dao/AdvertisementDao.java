package cn.mnu.shopsite.dao;

import cn.mnu.shopsite.model.Advertisement;
import cn.mnu.shopsite.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AdvertisementDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductDao productDao;

    public List<Product> querySlideAdvertisement(int n) {
        String sql = "select product_id from t_advertisement where type = 'slide'";
        List<Integer> productIds = jdbcTemplate.queryForList(sql, Integer.class);

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

    public List<Product> queryCategoryAdvertisement(String categoryId) {
        String sql = "select product_id from t_advertisement where type = 'category' and subtype = ?";
        List<Integer> productIds = jdbcTemplate.queryForList(sql, Integer.class, categoryId);

        List<Product> products = new ArrayList<>();
        for(Integer productId : productIds) {
            Product product = productDao.queryProduct(productId);
            if(product != null) {
                products.add(product);
            }
        }

        return products;
    }

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

    public boolean addAdvertisement(Advertisement advertisement) {
        String duplicatedString = "select Count(*) from t_advertisement where type = ? and subtype = ? and product_id = ?";
        String maxIdString = "select IfNull(Max(id), 0) from t_advertisement where type = ? and subtype = ?";
        String insertSql = "insert into t_advertisement values (?, ?, ?, ?, ?)";

        //轮播广播不需要subtype
        if("slide".equals(advertisement.getType())) {
            advertisement.setSubtype("slide");
        }

        Integer count = jdbcTemplate.queryForObject(duplicatedString, Integer.class, advertisement.getType(),
                advertisement.getSubtype(), advertisement.getProductId());
        if(count == null || count > 0) {
            return false;
        }

        Integer maxId = jdbcTemplate.queryForObject(maxIdString, Integer.class, advertisement.getType(),
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

    public void deleteAdvertisement(Advertisement advertisement) {
        String sql = "delete from t_advertisement where type = ? and subtype = ? and id = ?";

        jdbcTemplate.update(sql, advertisement.getType(), advertisement.getSubtype(), advertisement.getId());
    }
}
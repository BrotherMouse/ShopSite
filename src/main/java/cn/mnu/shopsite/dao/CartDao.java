package cn.mnu.shopsite.dao;

import cn.mnu.shopsite.model.CartItem;
import cn.mnu.shopsite.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CartDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductDao productDao;

    public List<CartItem> queryCart(String account) {
        String sql = "select * from t_cart where account = ?";

        List<CartItem> items = new ArrayList<>();
        jdbcTemplate.query(sql, rch -> {
            int productId = rch.getInt("product_id");
            int amount = rch.getInt("amount");
            Product product = productDao.queryProduct(productId);

            if(product != null) {
                items.add(new CartItem(product, amount));
            }
        }, account);

        return items;
    }

    public void addProductToCart(String account, int productId, int amount) {
        String countSql = "select Count(*) from t_cart where account = ? and product_id = ?";
        String insertSql = "insert into t_cart values (?, ?, ?)";
        String updateSql = "update t_cart set amount = amount + ? where account = ? and product_id = ?";

        Integer count = jdbcTemplate.queryForObject(countSql, Integer.class, account, productId);
        if(count == null || count == 0) {
            jdbcTemplate.update(insertSql, account, productId, amount);
        }
        else {
            jdbcTemplate.update(updateSql, amount, account, productId);
        }
    }
}
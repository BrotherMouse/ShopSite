package cn.mnu.shopsite.dao;

import cn.mnu.shopsite.model.CartItem;
import cn.mnu.shopsite.model.Product;
import cn.mnu.shopsite.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车信息dao
 *
 * @author Yanghai
 */
@Repository
public class CartDao {
    /**
     * 操作数据库的对象
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 商品信息dao，用于获得商品信息
     */
    @Autowired
    private ProductDao productDao;

    /**
     * 查询购物车信息
     *
     * @param user 用户信息
     * @return 购物车条目
     */
    public List<CartItem> queryCart(User user) {
        String sql = "select * from t_cart where account = ?";

        List<CartItem> items = new ArrayList<>();
        jdbcTemplate.query(sql, rch -> {
            int productId = rch.getInt("product_id");
            int amount = rch.getInt("amount");
            Product product = productDao.queryProduct(productId);

            if(product != null) {
                items.add(new CartItem(product, amount));
            }
        }, user.getAccount());

        return items;
    }

    /**
     * 将商品加入购物车
     *
     * @param user 用户信息
     * @param productId 商品id
     * @param amount 数量
     */
    public void addProductToCart(User user, int productId, int amount) {
        String countSql = "select Count(*) from t_cart where account = ? and product_id = ?";
        String insertSql = "insert into t_cart values (?, ?, ?)";
        String updateSql = "update t_cart set amount = amount + ? where account = ? and product_id = ?";

        String account = user.getAccount();

        Integer count = jdbcTemplate.queryForObject(countSql, Integer.class, account, productId);
        //该商品此前未加入购物车，直接插入
        if(count == null || count == 0) {
            jdbcTemplate.update(insertSql, account, productId, amount);
        }
        //该商品此前已加入过购物车，累加数量
        else {
            jdbcTemplate.update(updateSql, amount, account, productId);
        }
    }
}
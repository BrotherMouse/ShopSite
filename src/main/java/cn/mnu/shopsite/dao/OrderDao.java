package cn.mnu.shopsite.dao;

import cn.mnu.shopsite.model.OrderItem;
import cn.mnu.shopsite.model.OrderedProduct;
import cn.mnu.shopsite.model.Product;
import cn.mnu.shopsite.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 定单信息dao
 *
 * @author Yanghai
 */
@Repository
public class OrderDao {
    /**
     * 往数据库插数据时最多尝试的次数
     */
    private static final int TRY_TIMES = 10;

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
     * 查询用户的全部定单
     *
     * @param user 用户信息
     * @return 全部定单
     */
    public List<OrderItem> queryAllOrders(User user) {
        String sql = "select * from t_order a, t_order_products b where a.id = b.id and a.account = ? order by a.commit_date desc";

        String account = user.getAccount();
        List<OrderItem> items = new ArrayList<>();
        List<Integer> lastOrderIdList = new ArrayList<>();
        lastOrderIdList.add(-1);

        //将从数据库查询到的数据转为已下单的商品信息bean，查询出来的数据已经按定单id排好序
        jdbcTemplate.query(sql, rch -> {
            int orderId = rch.getInt("a.id");
            OrderItem item;

            //新的订单
            int lastOrderId = lastOrderIdList.get(0);
            if(orderId != lastOrderId) {
                Date commitDate = rch.getDate("a.commit_date");
                String status = rch.getString("a.status");

                item = new OrderItem(orderId, new ArrayList<>(), commitDate, status);
                items.add(item);
                lastOrderIdList.set(0, orderId);
            }
            //还是上一个订单
            else {
                item = items.get(items.size() - 1);
            }

            //查询不到商品信息的跳过
            int productId = rch.getInt("b.product_id");
            Product productInfo = productDao.queryProduct(productId);
            if(productInfo == null) {
                return;
            }

            //将商品信息转为已下单商品信息bean
            double price = rch.getDouble("b.price");
            int amount = rch.getInt("b.amount");
            OrderedProduct product = new OrderedProduct(productInfo, price, amount);
            item.getProducts().add(product);
        }, account);

        return items;
    }

    /**
     * 添加定单
     * @param user 用户信息
     * @param order 定单条目，其定单id不会被使用
     * @return true - 成功（其id被设置）， false - 失败（尝试若干次均失败）
     */
    public boolean addOrder(User user, OrderItem order) {
        String maxIdString = "select IfNull(Max(id), 0) from t_order";
        String insertOrderSql = "insert into t_order values (?, ?, ?, ?)";
        String insertOrderProductsSql = "insert into t_order_products (?, ?, ?, ?)";

        /*
         * 先插定单信息
         * 由于定单id是通过取当前最大的定单id加1，存在并发时id重复的可能，故而需要不断尝试，这里尝试TRY_TIMES次
         */
        int tryTimes = TRY_TIMES;
        int id = 0;
        while(tryTimes-- > 0) {
            Integer maxId = jdbcTemplate.queryForObject(maxIdString, Integer.class);
            id = maxId == null ? 1 : maxId + 1;

            try {
                jdbcTemplate.update(insertOrderSql, id, user.getAccount(), order.getCommitDate(), order.getStatus());
                break;
            }
            catch(DataAccessException ex) {
                ;
            }
        }

        //尝试TRY_TIMES次仍失败，返回
        if(tryTimes <= 0) {
            return false;
        }

        //再插定单商品信息
        for(OrderedProduct product : order.getProducts()) {
            jdbcTemplate.update(insertOrderProductsSql, id, product.getId(), product.getPrice(), product.getAmount());
        }
        return true;
    }
}
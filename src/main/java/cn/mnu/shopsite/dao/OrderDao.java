package cn.mnu.shopsite.dao;

import cn.mnu.shopsite.model.OrderItem;
import cn.mnu.shopsite.model.OrderedProduct;
import cn.mnu.shopsite.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class OrderDao {
    private static final int TRY_TIMES = 10;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductDao productDao;

    public List<OrderItem> queryAllOrders(String account) {
        String sql = "select * from t_order a, t_order_products b where a.id = b.id and a.account = ? order by a.commit_date desc";

        List<OrderItem> items = new ArrayList<>();
        List<Integer> lastOrderIdList = new ArrayList<>();
        lastOrderIdList.add(-1);
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

            int productId = rch.getInt("b.product_id");
            Product productInfo = productDao.queryProduct(productId);
            if(productInfo == null) {
                return;
            }

            double price = rch.getDouble("b.price");
            int amount = rch.getInt("b.amount");
            OrderedProduct product = new OrderedProduct(productInfo.getId(), productInfo.getCategoryId(),
                    productInfo.getBrandId(), productInfo.getName(), price, amount, productInfo.getListingDate(),
                    productInfo.getDescription(), productInfo.getThumbnailPath(), productInfo.getExhibitPaths());
            item.getProducts().add(product);
        }, account);

        return items;
    }

    public void addOrder(String account, OrderItem order) {
        String maxIdString = "select IfNull(Max(id), 0) from t_order";
        String insertOrderSql = "insert into t_order values (?, ?, ?, ?)";
        String insertOrderProductsSql = "insert into t_order_products (?, ?, ?, ?)";

        int tryTimes = TRY_TIMES;
        int id = 0;
        while(tryTimes-- > 0) {
            Integer maxId = jdbcTemplate.queryForObject(maxIdString, Integer.class);
            id = maxId == null ? 1 : maxId + 1;

            try {
                jdbcTemplate.update(insertOrderSql, id, account, order.getCommitDate(), order.getStatus());
                break;
            }
            catch(DataAccessException ex) {
                ;
            }
        }

        if(tryTimes <= 0) {
            return;
        }

        for(OrderedProduct product : order.getProducts()) {
            jdbcTemplate.update(insertOrderProductsSql, id, product.getId(), product.getPrice(), product.getAmount());
        }
    }
}
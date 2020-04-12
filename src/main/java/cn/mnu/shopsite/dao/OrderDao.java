package cn.mnu.shopsite.dao;

import cn.mnu.shopsite.model.OrderItem;
import cn.mnu.shopsite.model.OrderedProduct;
import cn.mnu.shopsite.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class OrderDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductDao productDao;

    public List<OrderItem> queryAllOrders(String account) {
        String sql = "select * from t_order a, t_order_products b where a.id = b.id and a.account = ? order by a.commit_date desc";

        List<OrderItem> items = new ArrayList<>();
        int lastOrderId = -1;
        jdbcTemplate.query(sql, rch -> {
            int orderId = rch.getInt("a.id");
            OrderItem item;

            //新的订单
            if(orderId != lastOrderId) {
                Date commitDate = rch.getDate("a.commit_date");
                String status = rch.getString("a.status");

                item = new OrderItem(orderId, new ArrayList<>(), commitDate, status);
                items.add(item);
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

    //public void
}
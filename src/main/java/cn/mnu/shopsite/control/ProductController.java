package cn.mnu.shopsite.control;

import cn.mnu.shopsite.dao.CartDao;
import cn.mnu.shopsite.dao.OrderDao;
import cn.mnu.shopsite.dao.ProductDao;
import cn.mnu.shopsite.model.*;
import cn.mnu.shopsite.service.RecommendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 商品相关页面控制器
 *
 * @author Yanghai
 */
@Controller
@RequestMapping("/product")
public class ProductController {
    /**
     * 商品信息dao
     */
    @Autowired
    private ProductDao productDao;

    /**
     * 购物车信息dao
     */
    @Autowired
    private CartDao cartDao;

    /**
     * 定单信息dao
     */
    @Autowired
    private OrderDao orderDao;

    /**
     * 商品推荐服务
     */
    @Autowired
    private RecommendingService recommendingService;

    @RequestMapping("/{id}")
    public String productDetail(@PathVariable int id, Model model) {
        Product product = productDao.queryProduct(id);
        if(product == null) {
            return "redirect:/index";
        }
        else {
            model.addAttribute("product", product);
            addNavigateData(model);
            return "product/productDetail";
        }
    }

    @RequestMapping("/addToCart")
    @ResponseBody
    public Map<String, Object> addToCart(HttpSession session, String productId, String amount) {
        Map<String, Object> ret = new HashMap<>();

        User user = (User)session.getAttribute("user");
        if(user == null) {
            ret.put("result", "NotLogin");
            return ret;
        }

        int productIdInt = Integer.parseInt(productId);
        int amountInt = Integer.parseInt(amount);

        cartDao.addProductToCart(user, productIdInt, amountInt);

        ret.put("result", "Success");
        return ret;
    }

    @RequestMapping("/generateOrder")
    @ResponseBody
    public Map<String, Object> generateOrder(HttpSession session, @RequestBody List<OrderedProduct> orderProducts) {
        Map<String, Object> ret = new HashMap<>();

        User user = (User)session.getAttribute("user");
        if(user == null) {
            ret.put("result", "NotLogin");
            return ret;
        }

        //逐个检查商品id是否存在
        OrderItem order = new OrderItem(0, new ArrayList<>(), new Date(), "ordered");
        for(OrderedProduct orderProduct : orderProducts) {
            Product productInfo = productDao.queryProduct(orderProduct.getId());
            if(productInfo == null) {
                ret.put("result", "IllegalProductId");
                return ret;
            }

            OrderedProduct product = new OrderedProduct(productInfo, orderProduct.getAmount());
            order.getProducts().add(product);
        }

        orderDao.addOrder(user, order);

        ret.put("result", "Success");
        return ret;
    }

    @RequestMapping("/search")
    public String search(Model model, String name) {
        List<Product> products = productDao.queryProduct(name);

        model.addAttribute("products", products);

        return "product/searchProduct";
    }

    /**
     * 添加导航数据，即页面顶部各分类和品牌推荐商品的信息
     *
     * @param model model
     */
    private void addNavigateData(Model model) {
        List<BrandProducts> brandNewProducts = recommendingService.queryBrandNewProducts(5);
        List<CategoryProducts> categoryNewProductsLess = recommendingService.queryCategoryNewProducts(5);

        model.addAttribute("brandNewProducts", brandNewProducts);
        model.addAttribute("categoryNewProductsLess", categoryNewProductsLess);
    }
}
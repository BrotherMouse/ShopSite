package cn.mnu.shopsite.control;

import cn.mnu.shopsite.dao.CartDao;
import cn.mnu.shopsite.dao.OrderDao;
import cn.mnu.shopsite.dao.ProductDao;
import cn.mnu.shopsite.dao.UserDao;
import cn.mnu.shopsite.model.*;
import cn.mnu.shopsite.service.RecommendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 普通用户页面控制器
 *
 * @author Yanghai
 */
@Controller
@RequestMapping("/user")
public class UserController {
    /**
     * 用户信息dao
     */
    @Autowired
    private UserDao userDao;

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

    @RequestMapping("/login")
    public String login() {
        return "user/login";
    }

    @RequestMapping("/checkLogin")
    @ResponseBody
    public Map<String, Object> checkLogin(HttpSession session, User normalUser) {
        Map<String, Object> ret = new HashMap<>();

        User user = userDao.getNormalUser(normalUser.getAccount());
        if(user == null) {
            ret.put("result", "NotExist");
        }
        else {
            //检查密码是否正确
            String inputPassword = normalUser.getPassword();
            String correctPassword = user.getPassword();
            if(inputPassword != null && inputPassword.equals(correctPassword)) {
                session.setAttribute("user", user);
                ret.put("result", "Verified");
            }
            else {
                ret.put("result", "ErrorPassword");
            }
        }

        return ret;
    }

    @RequestMapping("/register")
    public String register() {
        return "user/register";
    }

    @RequestMapping("/registerUser")
    @ResponseBody
    public Map<String, Object> registerUser(User normalUser) {
        Map<String, Object> ret = new HashMap<>();

        if(userDao.normalAccountExists(normalUser.getAccount())) {
            ret.put("result", "AccountExists");
        }
        else {
            boolean success = userDao.addNormalUser(normalUser);
            if(success) {
                ret.put("result", "Success");
            }
            else {
                ret.put("result", "Fail");
            }
        }

        return ret;
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();

        return "redirect:/index";
    }

    @RequestMapping("/cart")
    public String cart(HttpSession session, Model model) {
        User user = (User)session.getAttribute("user");
        if(user == null) {
            return "redirect:login";
        }

        List<CartItem> cartItems = cartDao.queryCart(user);
        model.addAttribute("cart", cartItems);
        addNavigateData(model);

        return "user/cart";
    }

    @RequestMapping("/generateOrder")
    @ResponseBody
    public Map<String, Object> generateOrder(HttpSession session, @RequestBody List<Map<String, String>> productsOrder) {
        Map<String, Object> ret = new HashMap<>();

        User user = (User)session.getAttribute("user");
        if(user == null) {
            ret.put("result", "NotLogin");
            return ret;
        }

        //逐个检查商品id是否存在
        OrderItem order = new OrderItem(0, new ArrayList<>(), new Date(), "ordered");
        for(Map<String, String> productOrder : productsOrder) {
            int productId = Integer.parseInt(productOrder.get("productId"));
            int amount = Integer.parseInt(productOrder.get("amount"));

            Product productInfo = productDao.queryProduct(productId);
            if(productInfo == null) {
                ret.put("result", "IllegalProductId");
                return ret;
            }

            OrderedProduct product = new OrderedProduct(productInfo, amount);
            order.getProducts().add(product);
        }

        orderDao.addOrder(user, order);

        ret.put("result", "Success");
        return ret;
    }

    @RequestMapping("/order")
    public String order(HttpSession session, Model model) {
        User user = (User)session.getAttribute("user");
        if(user == null) {
            return "redirect:login";
        }

        List<OrderItem> orderItems = orderDao.queryAllOrders(user);
        model.addAttribute("order", orderItems);
        addNavigateData(model);
        return "user/order";
    }

    /**
     * 添加导航数据，即页面顶部各分类和品牌推荐商品、左侧人气商品的信息
     *
     * @param model model
     */
    private void addNavigateData(Model model) {
        List<BrandProducts> brandNewProducts = recommendingService.queryBrandNewProducts(5);
        List<CategoryProducts> categoryNewProductsLess = recommendingService.queryCategoryNewProducts(5);
        List<Product> popProducts = recommendingService.queryPopProducts(4);

        model.addAttribute("brandNewProducts", brandNewProducts);
        model.addAttribute("categoryNewProductsLess", categoryNewProductsLess);
        model.addAttribute("popProducts", popProducts);
    }
}
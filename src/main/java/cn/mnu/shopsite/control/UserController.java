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

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserDao userDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CartDao cartDao;

    @Autowired
    private OrderDao orderDao;

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

        List<CartItem> cartItems = cartDao.queryCart(user.getAccount());
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

        orderDao.addOrder(user.getAccount(), order);

        ret.put("result", "Success");
        return ret;
    }

    @RequestMapping("/order")
    public String order(HttpSession session, Model model) {
        User user = (User)session.getAttribute("user");
        if(user == null) {
            return "redirect:login";
        }

        List<OrderItem> orderItems = orderDao.queryAllOrders(user.getAccount());
        model.addAttribute("order", orderItems);
        addNavigateData(model);
        return "user/order";
    }

    private void addNavigateData(Model model) {
        List<BrandProducts> brandNewProducts = recommendingService.queryBrandNewProducts(5);
        List<CategoryProducts> categoryNewProductsLess = recommendingService.queryCategoryNewProducts(5);
        List<Product> popProducts = recommendingService.queryPopProducts(4);

        model.addAttribute("brandNewProducts", brandNewProducts);
        model.addAttribute("categoryNewProductsLess", categoryNewProductsLess);
        model.addAttribute("popProducts", popProducts);
    }
}
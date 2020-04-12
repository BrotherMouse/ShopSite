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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class RootController {
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

    @RequestMapping("")
    public String root() {
        return "redirect:index";
    }

    @RequestMapping("index")
    public String index(Model model) {
        addNaviData(model);
        return "index";
    }

    @RequestMapping("login")
    public String login() {
        return "login";
    }

    @RequestMapping("checklogin")
    @ResponseBody
    public Map<String, Object> checkLogin(HttpSession session, @RequestBody Map<String, String> userInfo) {
        Map<String, Object> ret = new HashMap<>();

        String account = userInfo.get("account");
        String password = userInfo.get("password");

        User user = userDao.getUser(account);
        if(user == null) {
            ret.put("result", "NotExist");
        }
        else {
            if(user.getPassword().equals(password)) {
                session.setAttribute("user", user);
                ret.put("result", "Verified");
            }
            else {
                ret.put("result", "ErrorPassword");
            }
        }

        return ret;
    }

    @RequestMapping("register")
    public String register() {
        return "register";
    }

    @RequestMapping("registeruser")
    @ResponseBody
    public Map<String, Object> registerUser(@RequestBody Map<String, String> userInfo) {
        Map<String, Object> ret = new HashMap<>();

        String account = userInfo.get("account");
        String password = userInfo.get("password");

        if(userDao.accountExists(account)) {
            ret.put("result", "AccountExists");
        }
        else {
            User user = new User();
            user.setAccount(account);
            user.setPassword(password);

            boolean success = userDao.addUser(user);
            if(success) {
                ret.put("result", "Success");
            }
            else {
                ret.put("result", "Fail");
            }
        }

        return ret;
    }

    @RequestMapping("logout")
    public String logout(HttpSession session) {
        session.invalidate();

        return "redirect:index";
    }

    @RequestMapping("product/{id}")
    public String productDetail(@PathVariable int id, Model model) {
        Product product = productDao.queryProduct(id);
        if(product == null) {
            return "redirect:index";
        }
        else {
            model.addAttribute("product", product);
            addNaviData(model);
            return "pro-desc";
        }
    }

    private void addNaviData(Model model) {
        List<BrandProducts> brandNewProducts = recommendingService.queryBrandNewProducts(5);
        List<CategoryProducts> categoryHotProducts = recommendingService.queryCategoryHotProducts(5);
        List<Product> popProducts = recommendingService.queryPopProducts(9);
        List<CategoryProducts> categoryNewProducts = recommendingService.queryCategoryNewProducts(8);

        model.addAttribute("newProducts", brandNewProducts);
        model.addAttribute("hotProducts", categoryHotProducts);
        model.addAttribute("popList", popProducts);
        model.addAttribute("allCategories", categoryNewProducts);
    }

    @RequestMapping("addtocart")
    @ResponseBody
    public Map<String, Object> addToCart(HttpSession session, @RequestBody Map<String, String> info) {
        Map<String, Object> ret = new HashMap<>();

        User user = (User)session.getAttribute("user");
        if(user == null) {
            ret.put("result", "NotLogin");
            return ret;
        }

        int productId = Integer.parseInt(info.get("productId"));
        int amount = Integer.parseInt(info.get("amount"));

        cartDao.addProductToCart(user.getAccount(), productId, amount);

        ret.put("result", "Success");
        return ret;
    }

    @RequestMapping("cart")
    public String cart(HttpSession session, Model model) {
        User user = (User)session.getAttribute("user");
        if(user == null) {
            return "redirect:login";
        }

        List<CartItem> cartItems = cartDao.queryCart(user.getAccount());
        model.addAttribute("cart", cartItems);
        addNaviData(model);

        return "cart";
    }

    @RequestMapping("generateorder")
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

    @RequestMapping("order")
    public String order(HttpSession session, Model model) {
        User user = (User)session.getAttribute("user");
        if(user == null) {
            return "redirect:login";
        }

        List<OrderItem> orderItems = orderDao.queryAllOrders(user.getAccount());
        model.addAttribute("order", orderItems);
        addNaviData(model);
        return "order";
    }

    @RequestMapping("upload")
    public Map<String, Object> uploadImages(HttpServletRequest request) throws IllegalStateException, IOException {
        Map<String, Object> ret = new HashMap<>();

        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());

        if(multipartResolver.isMultipart(request)) {
            ret.put("result", "NoFiles");
            return ret;
        }

        MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
        Iterator<String> iterator = multiRequest.getFileNames();
        while(iterator.hasNext()) {
            MultipartFile file=multiRequest.getFile(iterator.next());
            if(file == null) {
                continue;
            }

            String path = "D:/ProductImages/" + file.getOriginalFilename();
            file.transferTo(new File(path));
        }

        ret.put("result", "Success");
        return ret;
    }
}
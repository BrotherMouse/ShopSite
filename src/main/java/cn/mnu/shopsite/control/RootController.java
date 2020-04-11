package cn.mnu.shopsite.control;

import cn.mnu.shopsite.dao.ProductDao;
import cn.mnu.shopsite.dao.UserDao;
import cn.mnu.shopsite.model.*;
import cn.mnu.shopsite.service.RecommendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class RootController {
    @Autowired
    private UserDao userDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private RecommendingService recommendingService;

    @RequestMapping("")
    public String root() {
        return "redirect:index";
    }

    @RequestMapping("index")
    public String index(Model model) {
        List<BrandProducts> brandNewProducts = recommendingService.queryBrandNewProducts(5);
        List<CategoryProducts> categoryHotProducts = recommendingService.queryCategoryHotProducts(5);
        List<Product> popProducts = recommendingService.queryPopProducts(9);
        List<CategoryProducts> categoryNewProducts = recommendingService.queryCategoryNewProducts(8);

        model.addAttribute("newProducts", brandNewProducts);
        model.addAttribute("hotProducts", categoryHotProducts);
        model.addAttribute("popList", popProducts);
        model.addAttribute("allCategories", categoryNewProducts);
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
            return "pro-desc";
        }
    }
}
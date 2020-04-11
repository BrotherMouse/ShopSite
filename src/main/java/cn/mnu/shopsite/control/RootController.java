package cn.mnu.shopsite.control;

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
    public Map<String, Object> checkLogin(HttpSession session, @RequestBody Map<String, String> input) {
        Map<String, Object> ret = new HashMap<>();

        String account = input.get("account");
        String password = input.get("password");

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

    @RequestMapping("product/{id}")
    public String productDetail(@PathVariable int id, Model model) {
        return "product";
    }
}
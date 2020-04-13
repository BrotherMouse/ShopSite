package cn.mnu.shopsite.control;

import cn.mnu.shopsite.dao.CartDao;
import cn.mnu.shopsite.dao.ProductDao;
import cn.mnu.shopsite.model.BrandProducts;
import cn.mnu.shopsite.model.CategoryProducts;
import cn.mnu.shopsite.model.Product;
import cn.mnu.shopsite.model.User;
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
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductDao productDao;

    @Autowired
    private CartDao cartDao;

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

    private void addNavigateData(Model model) {
        List<BrandProducts> brandNewProducts = recommendingService.queryBrandNewProducts(5);
        List<CategoryProducts> categoryNewProductsLess = recommendingService.queryCategoryNewProducts(5);

        model.addAttribute("brandNewProducts", brandNewProducts);
        model.addAttribute("categoryNewProductsLess", categoryNewProductsLess);
    }
}
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
    public Map<String, Object> addToCart(HttpSession session, @RequestBody Map<String, String> info) {
        Map<String, Object> ret = new HashMap<>();

        User user = (User)session.getAttribute("user");
        if(user == null) {
            ret.put("result", "NotLogin");
            return ret;
        }

        int productId = Integer.parseInt(info.get("productId"));
        int amount = Integer.parseInt(info.get("amount"));

        cartDao.addProductToCart(user, productId, amount);

        ret.put("result", "Success");
        return ret;
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
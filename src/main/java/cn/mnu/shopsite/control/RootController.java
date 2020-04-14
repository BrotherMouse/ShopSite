package cn.mnu.shopsite.control;

import cn.mnu.shopsite.model.BrandProducts;
import cn.mnu.shopsite.model.CategoryProducts;
import cn.mnu.shopsite.model.Product;
import cn.mnu.shopsite.service.RecommendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 首页控制器
 *
 * @author Yanghai
 */
@Controller
@RequestMapping("/")
public class RootController {
    /**
     * 商品推荐服务
     */
    @Autowired
    private RecommendingService recommendingService;

    @RequestMapping("")
    public String root() {
        return "redirect:index";
    }

    @RequestMapping("index")
    public String index(Model model) {
        addNavigateData(model);

        return "index";
    }

    /**
     * 添加导航数据，即页面顶部各分类和品牌推荐商品、中部轮播和人气商品、底部各分类商品的信息
     *
     * @param model model
     */
    private void addNavigateData(Model model) {
        List<BrandProducts> brandNewProducts = recommendingService.queryBrandNewProducts(5);
        List<CategoryProducts> categoryNewProductsLess = recommendingService.queryCategoryNewProducts(5);
        List<Product> slideProducts = recommendingService.querySlideProducts(6);
        List<Product> popProducts = recommendingService.queryPopProducts(9);
        List<CategoryProducts> categoryNewProductsMore = recommendingService.queryCategoryNewProducts(8);

        model.addAttribute("brandNewProducts", brandNewProducts);
        model.addAttribute("categoryNewProductsLess", categoryNewProductsLess);
        model.addAttribute("slideProducts", slideProducts);
        model.addAttribute("popProducts", popProducts);
        model.addAttribute("categoryNewProductsMore", categoryNewProductsMore);
    }
}
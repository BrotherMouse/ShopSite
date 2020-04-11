package cn.mnu.shopsite.service;

import cn.mnu.shopsite.dao.ProductBrandDao;
import cn.mnu.shopsite.dao.ProductCategoryDao;
import cn.mnu.shopsite.dao.ProductDao;
import cn.mnu.shopsite.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品推荐服务<br>
 *
 * 根据商品的销售情况，在首页展示热卖商品、最新商品等
 */
@Service
public class RecommendingService {
    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Autowired
    private ProductBrandDao productBrandDao;

    @Autowired
    private ProductDao productDao;

    /**
     * 查询各品牌最新上市的n个商品信息
     *
     * @param n 取最新上市的n个
     * @return 各品牌最新上市的m个商品信息，m <= n
     */
    public List<BrandProducts> queryBrandNewProducts(int n) {
        List<BrandProducts> newProducts = new ArrayList<>();

        List<ProductBrand> brands = productBrandDao.getAllBrandsRanking();
        for(ProductBrand brand : brands) {
            List<Product> products = productDao.queryBrandNewProducts(brand.getId(), n);
            if(products.isEmpty()) {
                continue;
            }

            newProducts.add(new BrandProducts(brand, products));
        }

        return newProducts;
    }

    /**
     * 查询各类商品中最热销的n个商品信息
     *
     * @param n 取最热销的n个
     * @return 各类商品中最热销的m个商品信息，m <= n
     */
    public List<CategoryProducts> queryCategoryHotProducts(int n) {
        List<CategoryProducts> hotProducts = new ArrayList<>();

        List<ProductCategory> categories = productCategoryDao.getAllCategoriesInOrder();
        for(ProductCategory category : categories) {
            List<Product> products = productDao.queryCategoryHotProducts(category.getId(), n);
            if(products.isEmpty()) {
                continue;
            }

            hotProducts.add(new CategoryProducts(category, products));
        }

        return hotProducts;
    }

    /**
     * 查询各类商品中最新上市的n个商品信息
     *
     * @param n 取最新上市的n个
     * @return 各类商品中最新上市的m个商品信息，m <= n
     */
    public List<CategoryProducts> queryCategoryNewProducts(int n) {
        List<CategoryProducts> hotProducts = new ArrayList<>();

        List<ProductCategory> categories = productCategoryDao.getAllCategoriesInOrder();
        for(ProductCategory category : categories) {
            List<Product> products = productDao.queryCategoryNewProducts(category.getId(), n);
            if(products.isEmpty()) {
                continue;
            }

            hotProducts.add(new CategoryProducts(category, products));
        }

        return hotProducts;
    }

    /**
     * 查询最具人气的n个商品信息
     *
     * @param n 取最具人气的n个
     * @return 最具人气的m个商品信息，m <= n
     */
    public List<Product> queryPopProducts(int n) {
        return productDao.queryPopProducts(n);
    }
}
package cn.mnu.shopsite.service;

import cn.mnu.shopsite.dao.AdvertisementDao;
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
 * 根据商品的销售情况，在首页展示最新商品、人气商品等
 *
 * @author Yanghai
 */
@Service
public class RecommendingService {
    /**
     * 广告信息dao
     */
    @Autowired
    private AdvertisementDao advertisementDao;

    /**
     * 商品分类信息dao
     */
    @Autowired
    private ProductCategoryDao productCategoryDao;

    /**
     * 商品品牌信息dao
     */
    @Autowired
    private ProductBrandDao productBrandDao;

    /**
     * 商品信息dao
     */
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

            List<Product> adProducts = advertisementDao.queryCategoryAdvertisement(category.getId());
            hotProducts.add(new CategoryProducts(category, products, adProducts.isEmpty() ? null : adProducts.get(0)));
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

    /**
     * 获得轮播广告商品信息
     *
     * @param n 获取n个
     * @return m个要轮广告的商品信息，m <= n
     */
    public List<Product> querySlideProducts(int n) {
        return advertisementDao.querySlideAdvertisement(n);
    }
}
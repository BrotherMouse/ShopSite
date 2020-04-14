package cn.mnu.shopsite;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 虚拟路径配置类<br>
 * 为方便管理，商品的图片文件需要单独存放，路径为${product.images}，配置在application.yml文件中。
 * 这里设置一个虚拟路径/pimages/**，所有访问该路径资源的请求都会被映射到${product.images}。
 * 比如，${product.images}配置为D:/ProductImages/，那么网面中的<img src="/pimages/xiaomi8.jpg">标签其实访问的是
 * D:/ProductImages/xiaomi8.jpg。
 *
 * @author Yanghai
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * 商品图片存储位置，配置在application.yml文件中
     */
    @Value("${product.images}")
    private String productImagesPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/pimages/**").addResourceLocations("file:/" + productImagesPath);
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
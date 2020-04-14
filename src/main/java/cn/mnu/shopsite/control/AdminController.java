package cn.mnu.shopsite.control;

import cn.mnu.shopsite.dao.*;
import cn.mnu.shopsite.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 管理员操作页面控制器
 *
 * @author Yanghai
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    /**
     * 商品图片存储位置，配置在application.yml文件中
     */
    @Value("${product.images}")
    private String productImagesPath;

    /**
     * 用户信息dao
     */
    @Autowired
    private UserDao userDao;

    /**
     * 商品信息dao
     */
    @Autowired
    private ProductDao productDao;

    /**
     * 商品品牌信息dao
     */
    @Autowired
    private ProductBrandDao productBrandDao;

    /**
     * 商品分类信息dao
     */
    @Autowired
    private ProductCategoryDao productCategoryDao;

    /**
     * 广告信息dao
     */
    @Autowired
    private AdvertisementDao advertisementDao;

    @RequestMapping("/login")
    public String login() {
        return "admin/login";
    }

    @RequestMapping("/checkLogin")
    @ResponseBody
    public Map<String, Object> checkLogin(HttpSession session, User adminUser) {
        Map<String, Object> ret = new HashMap<>();

        User user = userDao.getAdminUser(adminUser.getAccount());
        if(user == null) {
            ret.put("result", "NotExist");
        }
        else {
            //检查密码是否正确
            String inputPassword = adminUser.getPassword();
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
        return "admin/register";
    }

    @RequestMapping("/registerUser")
    @ResponseBody
    public Map<String, Object> registerUser(User adminUser) {
        Map<String, Object> ret = new HashMap<>();

        if(userDao.adminAccountExists(adminUser.getAccount())) {
            ret.put("result", "AccountExists");
        }
        else {
            boolean success = userDao.addAdminUser(adminUser);
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

    @RequestMapping("/management")
    public String addProduct(HttpSession session, Model model) {
        User user = (User)session.getAttribute("user");
        if(user == null) {
            return "redirect:login";
        }

        List<ProductCategory> categories = productCategoryDao.getAllCategoriesInOrder();
        List<ProductBrand> brands = productBrandDao.getAllBrandsRanking();
        List<Product> products = productDao.getAllProduct();
        List<Advertisement> advertisements = advertisementDao.getAllAdvertisements();

        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        model.addAttribute("products", products);
        model.addAttribute("advertisements", advertisements);
        return "admin/management";
    }

    @RequestMapping("/addCategory")
    @ResponseBody
    public Map<String, Object> addCategory(HttpSession session, ProductCategory category) {
        Map<String, Object> ret = new HashMap<>();

        User user = (User)session.getAttribute("user");
        if(user == null) {
            ret.put("result", "NotLogin");
            return ret;
        }

        boolean success = productCategoryDao.addCategory(category);
        if(success) {
            ret.put("result", "Success");
        }
        else {
            ret.put("result", "DuplicatedId");
        }

        return ret;
    }

    @RequestMapping("/deleteCategory")
    @ResponseBody
    public Map<String, Object> deleteCategory(HttpSession session, ProductCategory category) {
        Map<String, Object> ret = new HashMap<>();

        User user = (User)session.getAttribute("user");
        if(user == null) {
            ret.put("result", "NotLogin");
            return ret;
        }

        productCategoryDao.deleteCategory(category.getId());
        ret.put("result", "Success");

        return ret;
    }

    @RequestMapping("/addBrand")
    @ResponseBody
    public Map<String, Object> addBrand(HttpSession session, ProductBrand brand) {
        Map<String, Object> ret = new HashMap<>();

        User user = (User)session.getAttribute("user");
        if(user == null) {
            ret.put("result", "NotLogin");
            return ret;
        }

        boolean success = productBrandDao.addBrand(brand);
        if(success) {
            ret.put("result", "Success");
        }
        else {
            ret.put("result", "DuplicatedId");
        }

        return ret;
    }

    @RequestMapping("/deleteBrand")
    @ResponseBody
    public Map<String, Object> deleteBrand(HttpSession session, ProductBrand brand) {
        Map<String, Object> ret = new HashMap<>();

        User user = (User)session.getAttribute("user");
        if(user == null) {
            ret.put("result", "NotLogin");
            return ret;
        }

        productBrandDao.deleteBrand(brand.getId());
        ret.put("result", "Success");

        return ret;
    }

    @RequestMapping("/addProductInfo")
    @ResponseBody
    public Map<String, Object> addProductInfo(HttpSession session, Product product) {
        Map<String, Object> ret = new HashMap<>();

        User user = (User)session.getAttribute("user");
        if(user == null) {
            ret.put("result", "NotLogin");
            return ret;
        }

        productDao.addProduct(product);

        ret.put("result", "Success");
        ret.put("productId", product.getId());

        return ret;
    }

    @RequestMapping("/uploadImages")
    @ResponseBody
    public Map<String, Object> uploadImages(HttpSession session, HttpServletRequest request)
            throws IllegalStateException, IOException {
        Map<String, Object> ret = new HashMap<>();

        User user = (User)session.getAttribute("user");
        if(user == null) {
            ret.put("result", "NotLogin");
            return ret;
        }

        try {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            saveFile(multiRequest);
            ret.put("result", "Success");
        }
        catch(ClassCastException classException) {
            ret.put("result", "NotAFileUploadRequest");
        }
        catch(Exception ex) {
            ret.put("result", "SaveFileFail");
        }

        return ret;
    }

    /**
     * 保存图片文件
     *
     * @param request 文件上传请求
     * @throws IOException 写文件时发生错误
     */
    private void saveFile(MultipartHttpServletRequest request) throws IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        String type = request.getParameter("type");

        //多个文件，逐个保存
        Iterator<String> iterator = request.getFileNames();
        while(iterator.hasNext()) {
            MultipartFile file = request.getFile(iterator.next());
            if(file == null) {
                continue;
            }

            String filePureName = getFilePureName(file.getOriginalFilename());
            String uuidFileName = generateUuidImageFileName(file.getOriginalFilename());

            File dir = new File(productImagesPath);
            if(!dir.exists()) {
                dir.mkdirs();
            }

            file.transferTo(new File(productImagesPath + uuidFileName));

            productDao.addProductImage(productId, type, uuidFileName, filePureName);
        }
    }

    /**
     * 获得纯粹的文件名<br>
     * 上传到服务器端的文件名可能带路径信息，这里去掉其路径
     * @param filePath 文件名，可能形如D:/images/xiaomi8.jpg
     * @return 纯粹的文件名，如xiaomi8.jpg
     */
    private String getFilePureName(String filePath) {
        int unixSep = filePath.lastIndexOf('/');
        int winSep = filePath.lastIndexOf('\\');

        int pos = Math.max(winSep, unixSep);
        if(pos < 0) {
            return filePath;
        }
        else {
            return filePath.substring(pos + 1);
        }
    }

    /**
     * 生成uuid文件名<br>
     * 为保证图片文件名不重复，使用uuid对上传的图片文件进行重命名（不带-）
     *
     * @param fileName 上传的图片文件的名称
     * @return 以uuid为名称的文件名，形如bc8ec553e65e491688c7ca8364408378.jpg
     */
    private String generateUuidImageFileName(String fileName) {
        //先获得图片文件的类型，无法获得置设为.jpg
        String fileType;
        int dotPosition = fileName.lastIndexOf('.');
        if(dotPosition < 0) {
            fileType = ".jpg";
        }
        else {
            fileType = fileName.substring(dotPosition);
        }

        //生成uuid并去掉其中的-符号，与文件类型拼成新的文件名
        return UUID.randomUUID().toString().replaceAll("-", "") + fileType;
    }

    @RequestMapping("/deleteProduct")
    @ResponseBody
    public Map<String, Object> deleteProduct(HttpSession session, Product product) {
        Map<String, Object> ret = new HashMap<>();

        User user = (User)session.getAttribute("user");
        if(user == null) {
            ret.put("result", "NotLogin");
            return ret;
        }

        productDao.deleteProduct(product.getId());
        ret.put("result", "Success");

        return ret;
    }

    @RequestMapping("/addAdvertisement")
    @ResponseBody
    public Map<String, Object> addAdvertisement(HttpSession session, Advertisement advertisement) {
        Map<String, Object> ret = new HashMap<>();

        User user = (User)session.getAttribute("user");
        if(user == null) {
            ret.put("result", "NotLogin");
            return ret;
        }

        boolean success = advertisementDao.addAdvertisement(advertisement);
        if(success) {
            ret.put("result", "Success");
        }
        else {
            ret.put("result", "DuplicatedProduct");
        }

        return ret;
    }

    @RequestMapping("/deleteAdvertisement")
    @ResponseBody
    public Map<String, Object> deleteAdvertisement(HttpSession session, Advertisement advertisement) {
        Map<String, Object> ret = new HashMap<>();

        User user = (User)session.getAttribute("user");
        if(user == null) {
            ret.put("result", "NotLogin");
            return ret;
        }

        advertisementDao.deleteAdvertisement(advertisement);
        ret.put("result", "Success");

        return ret;
    }
}
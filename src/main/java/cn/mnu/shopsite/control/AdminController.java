package cn.mnu.shopsite.control;

import cn.mnu.shopsite.dao.*;
import cn.mnu.shopsite.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserDao userDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductBrandDao productBrandDao;

    @Autowired
    private ProductCategoryDao productCategoryDao;

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
    public String addProduct(Model model) {
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
    public Map<String, Object> addCategory(ProductCategory category) {
        Map<String, Object> ret = new HashMap<>();

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
    public Map<String, Object> deleteCategory(ProductCategory category) {
        Map<String, Object> ret = new HashMap<>();

        productCategoryDao.deleteCategory(category);
        ret.put("result", "Success");

        return ret;
    }

    @RequestMapping("/addBrand")
    @ResponseBody
    public Map<String, Object> addBrand(ProductBrand brand) {
        Map<String, Object> ret = new HashMap<>();

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
    public Map<String, Object> deleteBrand(ProductBrand brand) {
        Map<String, Object> ret = new HashMap<>();

        productBrandDao.deleteBrand(brand);
        ret.put("result", "Success");

        return ret;
    }

    @RequestMapping("/addProductInfo")
    @ResponseBody
    public Map<String, Object> addProductInfo(Product product) {
        Map<String, Object> ret = new HashMap<>();

        productDao.addProduct(product);

        ret.put("result", "Success");
        ret.put("productId", product.getId());

        return ret;
    }

    @RequestMapping("/uploadImages")
    @ResponseBody
    public Map<String, Object> uploadImages(HttpServletRequest request) throws IllegalStateException, IOException {
        Map<String, Object> ret = new HashMap<>();

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

    private String saveFile(MultipartHttpServletRequest request) throws IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        String type = request.getParameter("type");

        Iterator<String> iterator = request.getFileNames();
        while(iterator.hasNext()) {
            MultipartFile file = request.getFile(iterator.next());
            if(file == null) {
                continue;
            }

            String filePureName = getFilePureName(file.getOriginalFilename());
            String uuidFileName = generateUuidImageFileName(file.getOriginalFilename());

            file.transferTo(new File("D:/ProductImages/" + uuidFileName));

            productDao.addProductImage(productId, type, uuidFileName, filePureName);
        }

        return "Success";
    }

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

    private String generateUuidImageFileName(String fileName) {
        String fileType;
        int dotPosition = fileName.lastIndexOf('.');
        if(dotPosition < 0) {
            fileType = ".jpg";
        }
        else {
            fileType = fileName.substring(dotPosition);
        }

        return UUID.randomUUID().toString().replaceAll("-", "") + fileType;
    }

    @RequestMapping("/deleteProduct")
    @ResponseBody
    public Map<String, Object> deleteProduct(Product product) {
        Map<String, Object> ret = new HashMap<>();

        productDao.deleteProduct(product);
        ret.put("result", "Success");

        return ret;
    }

    @RequestMapping("/addAdvertisement")
    @ResponseBody
    public Map<String, Object> addAdvertisement(Advertisement advertisement) {
        Map<String, Object> ret = new HashMap<>();

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
    public Map<String, Object> deleteAdvertisement(Advertisement advertisement) {
        Map<String, Object> ret = new HashMap<>();

        advertisementDao.deleteAdvertisement(advertisement);
        ret.put("result", "Success");

        return ret;
    }
}
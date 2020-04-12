package cn.mnu.shopsite.control;

import cn.mnu.shopsite.dao.ProductBrandDao;
import cn.mnu.shopsite.dao.ProductCategoryDao;
import cn.mnu.shopsite.dao.ProductDao;
import cn.mnu.shopsite.model.Product;
import cn.mnu.shopsite.model.ProductBrand;
import cn.mnu.shopsite.model.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductBrandDao productBrandDao;

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/addProduct")
    public String addProduct(Model model) {
        List<ProductCategory> categories = productCategoryDao.getAllCategoriesInOrder();
        List<ProductBrand> brands = productBrandDao.getAllBrandsRanking();

        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        return "addProduct";
    }

    @RequestMapping("/addproductinfo")
    @ResponseBody
    public Map<String, Object> addProductInfo(Product product) {
        Map<String, Object> ret = new HashMap<>();

        productDao.addProduct(product);

        ret.put("result", "Success");
        ret.put("productId", product.getId());

        return ret;
    }

    @RequestMapping("/uploadimages")
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
}
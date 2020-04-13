function addCategory() {
    var categoryId = $("#cmCategoryId").val();
    var categoryName = $("#cbCategoryName").val();
    var categorySlogan = $("#cbCategorySlogan").val();
    var categoryDisplayOrder = $("#cbCategoryDisplayOrder").val();

    if(categoryId == "" || categoryName == "" || categorySlogan == "" || categoryDisplayOrder == "") {
        alert("商品分类的属性不可为空");
        return false;
    }

    var formData = new FormData();
    formData.append("id", categoryId);
    formData.append("name", categoryName);
    formData.append("slogan", categorySlogan);
    formData.append("displayOrder", categoryDisplayOrder);
    $.ajax({
        type : "post",
        url : "addCategory",
        data : formData,
        processData : false,
        contentType : false,
        success : function(data) {
            if(data.result == "Success") {
                location.reload(true);
            }
            else {
                alert("添加失败，" + data.result);
            }
        }
    });
}

function deleteCategory(categoryId) {
    if(!confirm("确认删除？")) {
        return false;
    }

    var formData = new FormData();
    formData.append("id", categoryId);
    $.ajax({
        type : "post",
        url : "deleteCategory",
        data : formData,
        processData : false,
        contentType : false,
        success : function(data) {
            if(data.result == "Success") {
                location.reload(true);
            }
            else {
                alert("删除失败，" + data.result);
            }
        }
    });
}

function addBrand() {
    var brandId = $("#cmBrandId").val();
    var brandName = $("#cbBrandName").val();
    var brandRankOrder = $("#cbBrandRankOrder").val();

    if(brandId == "" || brandName == "" || brandRankOrder == "") {
        alert("商品品牌的属性不可为空");
        return false;
    }

    var formData = new FormData();
    formData.append("id", brandId);
    formData.append("name", brandName);
    formData.append("rankOrder", brandRankOrder);
    $.ajax({
        type : "post",
        url : "addBrand",
        data : formData,
        processData : false,
        contentType : false,
        success : function(data) {
            if(data.result == "Success") {
                location.reload(true);
            }
            else {
                alert("添加失败，" + data.result);
            }
        }
    });
}

function deleteBrand(brandId) {
    if(!confirm("确认删除？")) {
        return false;
    }

    var formData = new FormData();
    formData.append("id", brandId);
    $.ajax({
        type : "post",
        url : "deleteBrand",
        data : formData,
        processData : false,
        contentType : false,
        success : function(data) {
            if(data.result == "Success") {
                location.reload(true);
            }
            else {
                alert("删除失败，" + data.result);
            }
        }
    });
}

//保存商品信息
function saveProduct() {
    var result;

    result = checkProductInfo();
    if(result != "success") {
        alert(result);
        return false;
    }

    result = saveProductInfo();
    if(result != "success") {
        alert(result);
        return false;
    }

    var productId = $("#addProductId").val();

    result = uploadImageFiles($("#addCoverImage"), productId, "cover");
    if(result != "success") {
        alert(result);
        return false;
    }

    var exhibitImageFiles = $("#addExhibitImages").val();
    if(exhibitImageFiles != "") {
        result = uploadImageFiles($("#addExhibitImages"), productId, "exhibit");
        if(result != "success") {
            alert(result);
            return false;
        }
    }

    alert("保存商品成功");
    location.reload(true);
    return true;
}

//检查商品信息要素
function checkProductInfo() {
    var categoryId = $("#addCategoryId").val();
    var brandId = $("#addBrandId").val();
    var name = $("#addName").val();
    var price = $("#addPrice").val();
    var salePrice = $("#addSalePrice").val();
    var purchasedAmount = $("#addPurchasedAmount").val();
    var stockBalance = $("#addStockBalance").val();
    var listingDate = $("#addListingDate").val();
    var description = $("#addDescription").val();
    var coverImageFile = $("#addCoverImage").val()

    if(categoryId == "" || brandId == "" || name == "" || price == "" || salePrice == "" || purchasedAmount == ""
        || stockBalance == "" || listingDate == "" || coverImageFile == "") {
        return "商品信息不可为空，商品编号、商品描述、展示图片除外";
    }
    else {
        return "success";
    }
}

//保存商品基本信息
function saveProductInfo() {
    var result = "fail";

    var categoryId = $("#addCategoryId").val();
    var brandId = $("#addBrandId").val();
    var name = $("#addName").val();
    var price = $("#addPrice").val();
    var salePrice = $("#addSalePrice").val();
    var purchasedAmount = $("#addPurchasedAmount").val();
    var stockBalance = $("#addStockBalance").val();
    var listingDate = $("#addListingDate").val().replace(/-/g, "/");
    var description = $("#addDescription").val();

    var formData = new FormData();
    formData.append("categoryId", categoryId);
    formData.append("brandId", brandId);
    formData.append("name", name);
    formData.append("price", price);
    formData.append("salePrice", salePrice);
    formData.append("purchasedAmount", purchasedAmount);
    formData.append("stockBalance", stockBalance);
    formData.append("listingDate", listingDate);
    formData.append("description", description);
    $.ajax({
        type : "post",
        url : "addProductInfo",
        async: false,
        data : formData,
        processData : false,
        contentType : false,
        success : function(data) {
            if(data.result == "Success") {
                $("#addProductId").val(data.productId);
                result = "success";
            }
            else {
                result = "保存商品基本信息失败，" + data.result;
            }
        }
    });

    return result;
}

//上传商品图片
function uploadImageFiles(fileObj, productId, type) {
    var result = "fail";

    var fileName = fileObj.val();
    if(fileName == "") {
        result = "请选择上传的目标文件";
        return result;
    }

    var reg = new RegExp("(.jpg|.png|.jpeg|.gif|.bmp)$");
    var fileName, fileSize;
    for(var index = 0; index < fileObj[0].files.length; index++) {
        //检查文件格式
        fileName = fileObj[0].files[index].name;
        if(!reg.test(fileName.toLowerCase())) {
            result = "请选择图片文件！";
            return result;
        }

        //检查文件大小
        fileSize = fileObj[0].files[index].size;
        if (fileSize > 10485760) {
            result = "上传文件不能大于10M!";
            return result;
        }
    }

    var formData = new FormData(); //这里需要实例化一个FormData来进行文件上传
    for(var index = 0; index < fileObj[0].files.length; index++) {
        formData.append("file" + index, fileObj[0].files[index]);
    }
    formData.append("productId", productId); //添加商品id
    formData.append("type", type); //图片类型
    $.ajax({
        type : "post",
        url : "uploadImages",
        async: false,
        data : formData,
        processData : false,
        contentType : false,
        success : function(data) {
            if(data.result == "Success") {
                result = "success";
            }
            else {
                result = "上传商品图片失败，" + data.result;
            }
        }
    });

    return result;
}

function deleteProduct(productId) {
    if(!confirm("确认下架？")) {
        return false;
    }

    var formData = new FormData();
    formData.append("id", productId);
    $.ajax({
        type : "post",
        url : "deleteProduct",
        data : formData,
        processData : false,
        contentType : false,
        success : function(data) {
            if(data.result == "Success") {
                location.reload(true);
            }
            else {
                alert("下架失败，" + data.result);
            }
        }
    });
}

function addAdvertisement() {
    var type = $("#addAdType").val();
    var subtype = $("#addAdSubtype").val();
    var productId = $("#addAdProductId").val();

    if(type == "" || subtype == "" || productId == "") {
        alert("广告信息不可为空");
        return;
    }

    var formData = new FormData();
    formData.append("type", type);
    formData.append("subtype", subtype);
    formData.append("productId", productId);
    $.ajax({
        type : "post",
        url : "addAdvertisement",
        data : formData,
        processData : false,
        contentType : false,
        success : function(data) {
            if(data.result == "Success") {
                location.reload(true);
            }
            else {
                alert("添加广告失败，" + data.result);
            }
        }
    });
}

function deleteAdvertisement(type, subtype, id) {
    if(!confirm("确认删除？")) {
        return false;
    }

    var formData = new FormData();
    formData.append("type", type);
    formData.append("subtype", subtype);
    formData.append("id", id);
    $.ajax({
        type : "post",
        url : "deleteAdvertisement",
        data : formData,
        processData : false,
        contentType : false,
        success : function(data) {
            if(data.result == "Success") {
                location.reload(true);
            }
            else {
                alert("删除广告失败，" + data.result);
            }
        }
    });
}
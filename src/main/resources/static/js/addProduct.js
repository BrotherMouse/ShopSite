//保存商品信息
    function saveProductInfo() {
      var productId = $("#productId").val();
      var categoryId = $("#categoryId").val();
      var brandId = $("#brandId").val();
      var name = $("#name").val();
      var price = $("#price").val();
      var salePrice = $("#salePrice").val();
      var purchasedAmount = $("#purchasedAmount").val();
      var stockBalance = $("#stockBalance").val();
      var listingDate = $("#listingDate").val();
      var description = $("#description").val();

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
        url : "/admin/addproductinfo",
        data : formData,
        processData : false,
        contentType : false,
        success : function(data) {
          if (data.result == "Success") {
            alert("商品添加成功!");
            $("#productId").val(data.productId);
          }
          else {
            alert("商品添加成功，" + data.result);
          }
        }
      });
    }

    //上传单个文件及商品id
    function uploadSingleFile() {
      var productId = $("#productId").val();
      var type = "thumbnail";
      uploadImageFiles($("#singleFileUpload"), productId, type);
    }

    //上传多个文件及商品id
    function uploadMultipleFiles() {
      var productId = $("#productId").val();
      var type = "exhibit";
      uploadImageFiles($("#multipleFilesUpload"), productId, type);
    }

    //上传n个文件及商品id，n >= 1
    function uploadImageFiles(fileObj, productId, type) {
      var fileName = fileObj.val();
      if(fileName == "") {
        alert("请选择上传的目标文件");
        return false;
      }

      var reg = new RegExp("(.jpg|.png|.jpeg|.gif|.bmp)$");
      var fileName, fileSize;
      for(var index = 0; index < fileObj[0].files.length; index++) {
        //检查文件格式
        fileName = fileObj[0].files[index].name;
        if(!reg.test(fileName.toLowerCase())) {
          alert("请选择图片文件！");
          return false;
        }

        //检查文件大小
        fileSize = fileObj[0].files[index].size;
        if (fileSize > 10485760) {
          alert("上传文件不能大于10M!");
          return false;
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
        url : "/admin/uploadimages",
        data : formData,
        processData : false,
        contentType : false,
        success : function(data) {
          if (data.result == "Success") {
            alert("文件上传成功!");
          }
          else {
            alert("文件上传失败，" + data.result);
          }
        }
      });
    }
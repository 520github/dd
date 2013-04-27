var productPara = {
    imageUrl:"imageUrl",
    price:"price",
    name:"name"
};

/***   jingDong product start  *******/
function JDProduct() {
    this.t1DivId = "tab-hot";//   tab-reco
    this.t2DivClass = "stabcon";
    this.t3DivClass = "master";
    this.t4_imgDivClass = "p-img";
    this.t4_nameDivClass = "p-name";
    this.t4_priceDivClass = "p-price";
};

JDProduct.prototype.getDivObjByClassName = function(nodes,className) {
    if(nodes == null)return null;
    var childrens = nodes.childNodes;
    for(var i=0;i<childrens.length;i++) {
        var child = childrens[i];
        if(child.nodeType != 1 && child.nodeType != 9)continue;
        var currentClassName = child.getAttribute("class");
        if(currentClassName == null)continue;
        if(currentClassName.indexOf(className) >-1) {
            return child;
        }
    }
    return null;
};

JDProduct.prototype.getInitDivObj = function() {
    return document.getElementById(this.t1DivId);
};

JDProduct.prototype.getT2DivObj = function() {
    var initDivObj = this.getInitDivObj();
    return this.getDivObjByClassName(initDivObj,this.t2DivClass);
};

JDProduct.prototype.getT3DivObj = function() {
    var t2DivObj = this.getT2DivObj();
    return this.getDivObjByClassName(t2DivObj,this.t3DivClass);
};

JDProduct.prototype.geT4_imgDivClass = function() {
    var t3DivObj = this.getT3DivObj();
    return this.getDivObjByClassName(t3DivObj,this.t4_imgDivClass);
};

JDProduct.prototype.geT4_nameDivClass = function() {
    var t3DivObj = this.getT3DivObj();
    return this.getDivObjByClassName(t3DivObj,this.t4_nameDivClass);
};

JDProduct.prototype.geT4_priceDivClass = function() {
    var t3DivObj = this.getT3DivObj();
    return this.getDivObjByClassName(t3DivObj,this.t4_priceDivClass);
};

JDProduct.prototype.getImgObj = function() {
    var t4_imgDivObj = this.geT4_imgDivClass();
    if(t4_imgDivObj == null) return null;
    //a
    var child = t4_imgDivObj.firstElementChild;
    if(child == null)return;
    //img
    return child.firstElementChild;
};

JDProduct.prototype.getNameObj = function() {
    var t4_nameDivObj = this.geT4_nameDivClass();
    if(t4_nameDivObj == null) return null;
    return t4_nameDivObj.firstElementChild;
};

JDProduct.prototype.getPriceObj = function() {
    var t4_priceDivObj = this.geT4_priceDivClass();
    if(t4_priceDivObj == null) return null;
    return t4_priceDivObj.firstElementChild;
};

JDProduct.prototype.getJingDongProductData = function() {
    var product = {};
    var imgObj = this.getImgObj();
    if(imgObj != null) {
        product[productPara.imageUrl] = imgObj.getAttribute("src");
    }
    
    var priceObj = this.getPriceObj();
    if(priceObj != null) {
        product[productPara.price] = priceObj.getAttribute("wmeprice");
    }
    
    var nameObj = this.getNameObj();
    if(nameObj != null) {
        product[productPara.name] = nameObj.innerText;
    }
    return product;
};

JDProduct.obj = new JDProduct();
/***   jingDong product end  *******/



function ProductData() {
    this.url = window.location.href;
    this.productUrl = [
          'http://item.taobao.com/item.htm?',
          'http://detail.tmall.com/item.htm',
          'http://www.360buy.com/product/'
          ];
    this.jingDongTag = "www.360buy.com";
};

ProductData.prototype.isProductUrl = function() {
    var isProduct = false;
    for(var i=0;i<this.productUrl.length;i++) {
        var tempUrl = this.productUrl[i];
        if(this.url.indexOf(tempUrl)>-1) {
            isProduct = true;
            break;
        }
     }
     return isProduct;
};

ProductData.prototype.getProductData = function() {
    var productData = {};
    if(!this.isProductUrl()) {
        return productData;
    }
    
    if(this.url.indexOf(this.jingDongTag) >-1) {
        productData = JDProduct.obj.getJingDongProductData();
    }
    
    //console.log(productData[productPara.imageUrl]+"|"+productData[productPara.price]+"|"+productData[productPara.name]);
    return productData;
};

ProductData.obj = new ProductData();




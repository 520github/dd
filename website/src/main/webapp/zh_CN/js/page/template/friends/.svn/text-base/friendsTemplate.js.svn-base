function FriendsTemplate() {
    Template.call(this);
    this.shareFriendsTemplateId = "#friendShareTemplate";
    this.emptyShareFriendsTemplateId = "#notFriendTemplate";
};

ObjectUtil.obj.extend(FriendsTemplate,Template);

FriendsTemplate.prototype.applyEmptyShareFriendsTemplate = function(data) {
    if(data == null)return "";
    return this.getHtmlFromApplyTpl(this.getEmptyShareFriendsTemplateHtml(),data);
};

FriendsTemplate.prototype.applyShareFriendsTemplate = function(datas) {
    if(datas == null)return "";
    var html = "";
    if(typeof datas === 'array') {
        html = this.getShareFriendsHtmlByArray(datas);
    }
    else if(typeof datas == 'object') {
        html = this.getShareFriendsHtmlByObject(datas);
    }
    return html;
};

FriendsTemplate.prototype.getShareFriendsHtmlByArray = function(datas) {
    if(datas == null) return '';
    var html = "";
    var templateHtml = this.getShareFriendsTemplateHtml();
    for(var i=0; i<datas.length;i++) {
        var data = datas[i];
        html += this.getHtmlFromApplyTpl(templateHtml,data);
    }
    return html;  
};

FriendsTemplate.prototype.getShareFriendsHtmlByObject = function(data) {
    if(data == null) return '';
    return this.getHtmlFromApplyTpl(this.getShareFriendsTemplateHtml(),data);
};

FriendsTemplate.prototype.getShareFriendsTemplateHtml = function() {
    return $(this.shareFriendsTemplateId).html();
};

FriendsTemplate.prototype.getEmptyShareFriendsTemplateHtml = function() {
    return $(this.emptyShareFriendsTemplateId).html();
};

FriendsTemplate.obj = new FriendsTemplate();

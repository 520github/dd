function Template() {
};

Template.prototype.getHtmlFromApplyTpl = function(htmlTpl,data) {
    htmlTpl = this.getTplHtml(htmlTpl);
    return Mustache.render(htmlTpl,data);
    //return dotpl.applyTpl(htmlTpl,data);
};

Template.prototype.getTplHtml = function(htmlTpl) {
    return htmlTpl;
};
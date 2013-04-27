function TextAreaCounter(textAreaObj,counterObj,defaultValue,defaultLength) {
    this.textAreaObj = textAreaObj;
    this.counterObj = counterObj;
    this.defaultValue = defaultValue;
    this.defaultLength = defaultLength;
};

TextAreaCounter.prototype.bindTextAreaCounter = function() {
    this.initData();
    this.onFocus();
    this.onKeyup();
    this.onKeydown();
};

TextAreaCounter.prototype.initData = function() {
    if(typeof this.textAreaObj === 'string')this.textAreaObj = $(this.textAreaObj);
    if(typeof this.counterObj === 'string')this.counterObj = $(this.counterObj);
    if(this.defaultValue == null)this.defaultValue = "";
    if(this.defaultLength <1) this.defaultLength = 1;
};

TextAreaCounter.prototype.onFocus = function() {
    this.textAreaObj.focus(function() {
		if ($(this).val() == this.defaultValue) {
			$(this).val("");
		}
    });
};

TextAreaCounter.prototype.onKeyup = function() {
    var point = this;
    this.textAreaObj.keyup(function() {
		var value = $(this).val();
        var length = $(this).val().length;
		if (length >= point.defaultLength) {
			$(this).val(value.substr(0, point.defaultLength));
		}
		point.counterObj.html(point.defaultLength - $(this).val().length);
	});
};

TextAreaCounter.prototype.onKeydown = function() {
    var point = this;
    this.textAreaObj.keydown(function() {
        var value = $(this).val();
        var length = $(this).val().length;
		if (length >= point.defaultLength) {
			$(this).val(value.substr(0, point.defaultLength));
		}
		point.counterObj.html(point.defaultLength - $(this).val().length);
	});
};


Namespace.register("com.hotent.form");
com.hotent.form.Form = function() {
	this.creatForm = function(a, c) {
		var b = document.getElementById(a);
		if (b == null || b == undefined) {
			b = document.createElement("FORM");
			document.body.appendChild(b);
		}
		b.action = c;
		this.form = b;
	};
	this.setMethod = function(a) {
		this.form.method = a;
	};
	this.setTarget = function(a) {
		this.form.target = a;
	};
	this.clearFormEl = function() {
		var c = this.form.childNodes;
		for ( var a = c.length; a >= 0; a--) {
			var b = c[a];
			this.form.removeNode(b);
		}
	};
	this.addFormEl = function(a, c) {
		var b = document.createElement("input");
		b.setAttribute("name", a);
		b.setAttribute("type", "hidden");
		b.setAttribute("value", c);
		this.form.appendChild(b);
	};
	this.submit = function() {
		this.form.submit();
	};
};
com.hotent.form.ResultMessage = function(data) {
	this.data = eval("(" + data + ")");
	this.isSuccess = function() {
		return this.data["result"] == 1;
	};
	this.getMessage = function() {
		return this.data["message"];
	};
};
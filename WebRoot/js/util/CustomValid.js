String.prototype.isEmpty = function() {
	var a = (this == null || this == undefined || this.trim() == "");
	return a;
};
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
};
String.prototype.lTrim = function() {
	return this.replace(/(^\s*)/g, "");
};
String.prototype.rTrim = function() {
	return this.replace(/(\s*$)/g, "");
};
(function($) {
	$
			.extend(
					$.fn,
					{
						form : function(conf) {
							if (conf) {
								if (conf.errorPlacement) {
									this.errorPlacement = conf.errorPlacement;
								}
								if (conf.rules) {
									for ( var i = 0, len = conf.rules.length; i < len; i++) {
										this.addRule(conf.rules[i]);
									}
								}
								if (conf.success) {
									this.success = conf.success;
								}
								if (conf.excludes) {
									this.excludes = conf.excludes;
								}
							}
							var form = this;
							form.delegate("[validate]", "blur", function() {
								form.handValidResult(this);
							});
							form.delegate("[validate]", "focus", function() {
								form.success(this);
							});
							return this;
						},
						addRule : function(rule) {
							var len = this.rules.length;
							for ( var i = 0; i < len; i++) {
								var r = this.rules[i];
								if (rule.name == r.name) {
									this.rules[i] = rule;
									return;
								}
							}
							this.rules.push(rule);
						},
						isInNotValid : function(obj) {
							if (!this.excludes) {
								return false;
							}
							var scope = $(this.excludes, this);
							var aryInput = $(
									"input:text,input:hidden,textarea,select,input:checkbox,input:radio",
									scope);
							for ( var i = 0, len = aryInput.length; i < len; i++) {
								var tmp = aryInput.get(i);
								if (obj == tmp) {
									return true;
								}
							}
							return false;
						},
						valid : function() {
							var _v = true, form = this;
							$("[validate]", form).each(function() {
								var rtn = form.handValidResult(this);
								if (!rtn) {
									_v = false;
								}
							});
							return _v;
						},
						handValidResult : function(obj) {
							var isInNotValid = this.isInNotValid(obj);
							if (isInNotValid) {
								return true;
							}
							var msg = this.validEach(obj);
							this.success(obj);
							if (msg != "") {
								this.errorPlacement(obj, msg);
								return false;
							} else {
								this.success(obj);
								return true;
							}
						},
						validEach : function(obj) {
							var element = $(obj);
							var rules = this.rules;
							var _valid = true;
							var validRule = element.attr("validate");
							var value = "";
							if (element.is(":checkbox,:radio")) {
								var name = element.attr("name");
								$(":checked[name=" + name + "]").each(
										function() {
											if (value == "") {
												value = $(this).val();
											} else {
												value += "," + $(this).val();
											}
										});
							} else {
								value = element.val();
							}
							value = value.trim();
							var json = eval("(" + validRule + ")");
							var isRequired = json.required;
							if ((isRequired == false || isRequired == undefined)
									&& value == "") {
								return "";
							}
							for ( var name in json) {
								var validvalue = json[name];
								for ( var m = 0; m < rules.length; m++) {
									var rule = rules[m];
									if (name != rule.name) {
										continue;
									}
									if (validvalue == true
											|| validvalue == false) {
										if (!rule.rule(value)
												&& validvalue == true) {
											_valid = false;
										}
									} else {
										_valid = rule.rule(value, validvalue);
									}
									if (!_valid) {
										return rule.msg;
									}
								}
							}
							return "";
						},
						errorPlacement : function(element, msg) {
							var errorId = $(element).attr("tipId");
							if (errorId) {
								$("#" + errorId).append(
										$('<label class="error">' + msg
												+ "</label>"));
								return;
							}
							$(element).parent().append(
									$('<label class="error">' + msg
											+ "</label>"));
						},
						success : function(element) {
							var errorId = $(element).attr("tipId");
							if (errorId) {
								$("#" + errorId).find("label.error").remove();
								return;
							}
							$(element).parent().find("label.error").remove();
						},
						rules : [
								{
									name : "required",
									rule : function(v) {
										if (v == "" || v.length == 0) {
											return false;
										}
										return true;
									},
									msg : "必填"
								},
								{
									name : "number",
									rule : function(v) {
										return /^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/
												.test(v.trim());
									},
									msg : "请输入一个合法的数字"
								},
								{
									name : "variable",
									rule : function(v) {
										return /^[a-z_0-9]*$/gi.test(v.trim());
									},
									msg : "只能是字母和下划线"
								},
								{
									name : "field",
									rule : function(v) {
										return /^[A-Za-z]{1}([A-Za-z0-9\_]+)?$/
												.test(v.trim());
									},
									msg : "只能以字母开头,允许字母、数字、下划线"
								},
								{
									name : "minlength",
									rule : function(v, b) {
										this.msg = String.format(this.msg, b);
										return (v.length >= b);
									},
									msg : "长度不少于{0}"
								},
								{
									name : "maxlength",
									rule : function(v, b) {
										this.msg = String.format(this.msg, b);
										return (v.trim().length <= b);
									},
									msg : "长度不超过{0}"
								},
								{
									name : "rangeLength",
									rule : function(v, args) {
										this.msg = String.format(this.msg,
												args[0], args[1]);
										return (v.trim().length >= args[0] && v
												.trim().length <= args[1]);
									},
									msg : "长度必须在{0}和{1}之间"
								},
								{
									name : "email",
									rule : function(v) {
										return /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$/i
												.test(v.trim());
									},
									msg : "请输入一合法的邮箱地址"
								},
								{
									name : "url",
									rule : function(v) {
										return /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i
												.test(v.trim());
									},
									msg : "请输入一合法的网址"
								},
								{
									name : "date",
									rule : function(v) {
										var d = new Date(v.trim().replace(/-/g,
												"/"));
										return !/Invalid|NaN/.test(d);
									},
									msg : "请输入一合法的日期"
								},
								{
									name : "digits",
									rule : function(v) {
										return /^\d+$/.test(v.trim());
									},
									msg : "请输入整数"
								},
								{
									name : "equalTo",
									rule : function(v, b) {
										var a = $("#" + b).val();
										return (v.trim() == a.trim());
									},
									msg : "两次输入不等"
								},
								{
									name : "range",
									rule : function(v, args) {
										this.msg = String.format(this.msg,
												args[0], args[1]);
										return v <= args[1] && v >= args[0];
									},
									msg : "请输入在{0},{1}范围之内数字"
								},
								{
									name : "maxIntLen",
									rule : function(v, b) {
										this.msg = String.format(this.msg, b);
										return (v + "").split(".")[0].length <= b;
									},
									msg : "整数位最大长度为{0}"
								},
								{
									name : "maxDecimalLen",
									rule : function(v, b) {
										this.msg = String.format(this.msg, b);
										return (v + "").replace(/^[^.]*[.]*/,
												"").length <= b;
									},
									msg : "小数位最大长度为{0}"
								} ]
					});
})(jQuery);
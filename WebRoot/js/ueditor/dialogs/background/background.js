var me = editor, doc = me.document, bodyStyle, cp = $G("colorPicker"), bkbodyStyle = "", bkcolor = "";
var popup = new UE.ui.Popup({
	content : new UE.ui.ColorPicker({
		noColorText : me.getLang("clearColor"),
		editor : me,
		onpickcolor : function(b, a) {
			domUtils.setStyle(cp, "background-color", a);
			bkcolor = a;
			UE.ui.Popup.postHide();
		},
		onpicknocolor : function(b, a) {
			domUtils.setStyle(cp, "background-color", "transparent");
			bkcolor = "";
			UE.ui.Popup.postHide();
		}
	}),
	editor : me,
	onhide : function() {
		setBody();
	}
});
domUtils.on(cp, "click", function() {
	popup.showAnchor(this);
});
domUtils.on(document, "mousedown", function(a) {
	var b = a.target || a.srcElement;
	UE.ui.Popup.postHide(b);
});
domUtils.on(window, "scroll", function() {
	UE.ui.Popup.postHide();
});
var getHead = function() {
	return domUtils.getElementsByTagName($G("tabhead"), "span");
};
var bindClick = function() {
	var c = getHead();
	for (var b = 0, a; a = c[b++];) {
		a.onclick = function() {
			var e = this.getAttribute("tabsrc");
			toggleHead(this);
			toggleBody(e);
			if (e == "imgManager") {
				ajax
						.request(
								editor.options.imageManagerUrl,
								{
									timeout : 100000,
									action : "get",
									onsuccess : function(p) {
										var n = utils.trim(p.responseText), l = !n ? []
												: n.split("ue_separate_ue"), i = l.length, j = $G("imageList");
										j.innerHTML = !i ? "&nbsp;&nbsp;"
												+ lang.noUploadImage : "";
										for (var m = 0, q; q = l[m++];) {
											var o = document
													.createElement("img");
											var h = document
													.createElement("div");
											h.appendChild(o);
											h.style.display = "none";
											j.appendChild(h);
											o.onclick = function() {
												var k = j.childNodes;
												for (var r = 0, s; s = k[r++];) {
													s.firstChild
															.removeAttribute("selected");
													s.firstChild.style.cssText = "filter:alpha(Opacity=100);-moz-opacity:1;opacity: 1;border: 2px solid #fff";
												}
												changeSelected(this);
											};
											o.onload = function() {
												this.parentNode.style.display = "";
												var k = this.width, r = this.height;
												scale(this, 95, 120, 80);
												this.title = lang.toggleSelect
														+ k + "X" + r;
											};
											o
													.setAttribute(
															m < 35 ? "src"
																	: "lazy_src",
															editor.options.imageManagerPath
																	+ q
																			.replace(
																					/\s+|\s+/ig,
																					""));
											o
													.setAttribute(
															"data_ue_src",
															editor.options.imageManagerPath
																	+ q
																			.replace(
																					/\s+|\s+/ig,
																					""));
										}
									},
									onerror : function() {
										$G("imageList").innerHTML = lang.imageLoadError;
									}
								});
			} else {
				var g = document.getElementsByName("t");
				for (var d = 0, f; f = g[d++];) {
					if (f.checked && f.value != "none") {
						$G("repeatType").style.display = "";
					}
				}
			}
		};
	}
};
function changeSelected(a) {
	if (a.getAttribute("selected")) {
		a.removeAttribute("selected");
		a.style.cssText = "filter:alpha(Opacity=100);-moz-opacity:1;opacity: 1;border: 2px solid #fff";
	} else {
		a.setAttribute("selected", "true");
		a.style.cssText = "filter:alpha(Opacity=50);-moz-opacity:0.5;opacity: 0.5;border:2px solid blue;";
	}
	$G("url").value = a.getAttribute("src");
}
function scale(d, f, i, b) {
	var a = 0, h = 0, e, c = d.width || i, g = d.height || b;
	if (c > f || g > f) {
		if (c >= g) {
			if (a = c - f) {
				e = (a / c).toFixed(2);
				d.height = g - g * e;
				d.width = f;
			}
		} else {
			if (h = g - f) {
				e = (h / g).toFixed(2);
				d.width = c - c * e;
				d.height = f;
			}
		}
	}
}
var toggleBody = function(d) {
	var b = [ "normal", "imgManager" ];
	for (var c = 0, a; a = b[c++];) {
		$G(a).style.zIndex = a == d ? 200 : 1;
	}
};
var toggleHead = function(d) {
	var c = getHead();
	for (var b = 0, a; a = c[b++];) {
		domUtils.removeClasses(a, [ "focus" ]);
		$G("repeatType").style.display = "none";
	}
	domUtils.addClass(d, "focus");
};
var getCheckedTab = function() {
	var c = getHead();
	for (var b = 0, a; a = c[b++];) {
		if (domUtils.hasClass(a, "focus")) {
			return a;
		}
	}
};
var init = function() {
	bindClick();
	getHead()[0].click();
	$G("alignment").style.display = "none";
	$G("custom").style.display = "none";
	domUtils.setStyle(cp, "background-color", domUtils.getComputedStyle(
			doc.body, "background-color"));
	var a = domUtils.getComputedStyle(doc.body, "background-color");
	if ((a && a != "#ffffff" && a != "transparent")
			|| domUtils.getComputedStyle(doc.body, "background-image") != "none") {
		setTimeout(function() {
			document.getElementsByName("t")[1].click();
		}, 200);
	}
	initImgUrl();
	initSelfPos();
	initAlign();
};
function initSelfPos() {
	var b, c;
	if (browser.ie) {
		b = domUtils.getComputedStyle(doc.body, "background-position-x")
				.replace(/50%|%|px|center/ig, "");
		c = domUtils.getComputedStyle(doc.body, "background-position-y")
				.replace(/50%|%|px|center/ig, "");
	} else {
		var a = domUtils.getComputedStyle(doc.body, "background-position")
				.match(/\s?(\d*)px/ig);
		if (a && a.length) {
			b = a[0].replace("px", "");
			c = a[1].replace("px", "");
		}
	}
	$G("x").value = b || 0;
	$G("y").value = c || 0;
}
function initImgUrl() {
	var a = domUtils.getComputedStyle(doc.body, "background-image"), b = "";
	if (a.indexOf(me.options.imagePath) > 0) {
		b = a.match(/url\("?(.*[^\)"])"?/i);
		if (b && b.length) {
			b = b[1].substring(b[1].indexOf(me.options.imagePath), b[1].length);
		}
	} else {
		b = a != "none" ? a.replace(/url\("?|"?\)/ig, "") : "";
	}
	$G("url").value = b;
}
function initAlign() {
	var c = domUtils.getComputedStyle(doc.body, "background-repeat"), a = $G("repeatType");
	if (c == "no-repeat") {
		var b = domUtils.getComputedStyle(doc.body,
				browser.ie ? "background-position-x" : "background-position");
		a.value = b && b.match(/\s?(\d*)px/ig) ? "self" : "center";
		if (b == "center") {
			a.value = "center";
		}
		$G("custom").style.display = a.value == "self" ? "" : "none";
	} else {
		a.value = c;
	}
}
init();
function Stylesheet(a) {
	if (typeof a == "number") {
		a = doc.styleSheets[a];
	}
	this.ss = a;
}
Stylesheet.prototype.getRules = function() {
	return this.ss.cssRules ? this.ss.cssRules : this.ss.rules;
};
Stylesheet.prototype.getRule = function(b) {
	var c = this.getRules();
	if (!c) {
		return null;
	}
	if (typeof b == "number") {
		return c[b];
	}
	b = b.toLowerCase();
	for (var a = c.length - 1; a >= 0; a--) {
		if (c[a].selectorText.toLowerCase() == b) {
			return c[a];
		}
	}
	return null;
};
Stylesheet.prototype.getStyleText = function(a) {
	var b = this.getRule(a);
	if (b && b.style && b.style.cssText) {
		return b.style.cssText;
	} else {
		return "";
	}
};
Stylesheet.prototype.insertRule = function(a, b, d) {
	if (!d) {
		var c = this.getRules();
		c.length && this.deleteRule(c.length - 1);
	}
	if (this.ss.insertRule) {
		this.ss.insertRule(a + "{" + b + "}", 0);
	} else {
		if (this.ss.addRule) {
			this.ss.addRule(a, b, 0);
		}
	}
};
Stylesheet.prototype.deleteRule = function(b) {
	if (!b) {
		var c = this.getRules();
		b = c.length - 1;
	}
	if (typeof b != "number") {
		b = b.toLowerCase();
		var c = this.getRules();
		for (var a = c.length - 1; a >= 0; a--) {
			if (c[a].selectorText.toLowerCase() == b) {
				b = a;
				break;
			}
		}
		if (a == -1) {
			return;
		}
	}
	if (this.ss.deleteRule) {
		this.ss.deleteRule(b);
	} else {
		if (this.ss.removeRule) {
			this.ss.removeRule(b);
		}
	}
};
function getCheckIpt() {
	var a = document.getElementsByName("t");
	for (var b = 0, c; c = a[b++];) {
		if (c.checked) {
			return c.value;
		}
	}
}
var net = function(c) {
	var d = $G("alignment"), a = $G("url"), b = $G("custom");
	if (c.value == "none") {
		d.style.display = "none";
		b.style.display = "none";
		if (browser.ie) {
			a.onpropertychange = null;
		} else {
			a.removeEventListener("input", setBody);
		}
	} else {
		bindSelfPos();
		$G("repeatType").style.display = "";
		d.style.display = "";
		if (browser.ie) {
			a.onpropertychange = setBody;
		} else {
			a.addEventListener("input", setBody, false);
		}
	}
	setBody();
};
var bindSelfPos = function() {
	var a = $G("x"), c = $G("y");
	domUtils.on(a, [ "propertychange", "input", "keydown" ], function(d) {
		b(d, this);
	});
	domUtils.on(c, [ "propertychange", "input", "keydown" ], function(d) {
		b(d, this);
	});
	function b(d, e) {
		d = d || event;
		if (d.keyCode == 38 || d.keyCode == 40) {
			e.value = d.keyCode == 38 ? parseInt(e.value) + 1
					: parseInt(e.value) - 1;
			if (e.value < 0) {
				e.value = 0;
			}
		} else {
			if (d.keyCode < 48 && d.keyCode > 57) {
				domUtils.preventDefault(d);
			}
		}
		setBody();
	}
};
var showAlign = function() {
	$G("alignment").style.display = "";
};
var selectAlign = function(a) {
	$G("custom").style.display = a.value == "self" ? "" : "none";
	setBody();
};
var setBody = function() {
	var b = domUtils.getStyle(cp, "background-color"), e = $G("url").value, f = $G("repeatType").value, d = {
		"background-repeat" : "no-repeat",
		"background-position" : "center center"
	}, a = [];
	if (b) {
		d["background-color"] = b;
	}
	if (e) {
		d["background-image"] = 'url("' + e + '")';
	}
	switch (f) {
	case "repeat-x":
		d["background-repeat"] = "repeat-x;";
		break;
	case "repeat-y":
		d["background-repeat"] = "repeat-y;";
		break;
	case "repeat":
		d["background-repeat"] = "repeat;";
		break;
	case "self":
		d["background-position"] = $G("x").value + "px " + $G("y").value + "px";
		break;
	}
	for ( var c in d) {
		if (d.hasOwnProperty(c)) {
			a.push(c + ":" + d[c]);
		}
	}
	if (getCheckIpt() != "none") {
		setStyleSheet(a.join(";"));
	} else {
		setStyleSheet(" ");
	}
};
function getStyleSheet() {
	bodyStyle = doc.getElementsByTagName("head")[0].lastChild;
	if (/style/ig.test(bodyStyle.tagName)) {
		var a = doc.styleSheets.length - 1;
		var b = new Stylesheet(a);
		bkbodyStyle = b.getStyleText(0);
	} else {
		bodyStyle = null;
	}
}
var setStyleSheet = function(b) {
	if (bodyStyle) {
		var a = doc.styleSheets.length - 1;
		var c = new Stylesheet(a);
		c.insertRule("body", b);
	} else {
		if (browser.ie) {
			bodyStyle = doc.createStyleSheet();
			bodyStyle.cssText = b;
		} else {
			bodyStyle = domUtils.creElm(doc, "style", {
				"type" : "text/css"
			});
			bodyStyle.innerHTML = b;
			doc.head.appendChild(bodyStyle);
		}
	}
	bkbodyStyle = bkbodyStyle || b;
};
dialog.onok = function() {
	setBody();
};
dialog.oncancel = function() {
	var a = doc.styleSheets.length - 1;
	var b = new Stylesheet(a);
	b.insertRule("body", bkbodyStyle || " ");
};
getStyleSheet();
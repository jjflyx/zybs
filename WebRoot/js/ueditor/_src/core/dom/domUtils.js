function getDomNode(g, h, b, a, e, f) {
	var d = a && g[h], c;
	!d && (d = g[b]);
	while (!d && (c = (c || g).parentNode)) {
		if (c.tagName == "BODY" || f && !f(c)) {
			return null;
		}
		d = c[b];
	}
	if (d && e && !e(d)) {
		return getDomNode(d, h, b, false, e);
	}
	return d;
}
var attrFix = ie && browser.version < 9 ? {
	tabindex : "tabIndex",
	readonly : "readOnly",
	"for" : "htmlFor",
	"class" : "className",
	maxlength : "maxLength",
	cellspacing : "cellSpacing",
	cellpadding : "cellPadding",
	rowspan : "rowSpan",
	colspan : "colSpan",
	usemap : "useMap",
	frameborder : "frameBorder"
} : {
	tabindex : "tabIndex",
	readonly : "readOnly"
}, styleBlock = utils.listToMap([ "-webkit-box", "-moz-box", "block",
		"list-item", "table", "table-row-group", "table-header-group",
		"table-footer-group", "table-row", "table-column-group",
		"table-column", "table-cell", "table-caption" ]);
var domUtils = dom.domUtils = {
	NODE_ELEMENT : 1,
	NODE_DOCUMENT : 9,
	NODE_TEXT : 3,
	NODE_COMMENT : 8,
	NODE_DOCUMENT_FRAGMENT : 11,
	POSITION_IDENTICAL : 0,
	POSITION_DISCONNECTED : 1,
	POSITION_FOLLOWING : 2,
	POSITION_PRECEDING : 4,
	POSITION_IS_CONTAINED : 8,
	POSITION_CONTAINS : 16,
	fillChar : ie && browser.version == "6" ? "\ufeff" : "\u200B",
	keys : {
		8 : 1,
		46 : 1,
		16 : 1,
		17 : 1,
		18 : 1,
		37 : 1,
		38 : 1,
		39 : 1,
		40 : 1,
		13 : 1
	},
	getPosition : function(f, d) {
		if (f === d) {
			return 0;
		}
		var b, e = [ f ], c = [ d ];
		b = f;
		while (b = b.parentNode) {
			if (b === d) {
				return 10;
			}
			e.push(b);
		}
		b = d;
		while (b = b.parentNode) {
			if (b === f) {
				return 20;
			}
			c.push(b);
		}
		e.reverse();
		c.reverse();
		if (e[0] !== c[0]) {
			return 1;
		}
		var a = -1;
		while (a++, e[a] === c[a]) {
		}
		f = e[a];
		d = c[a];
		while (f = f.nextSibling) {
			if (f === d) {
				return 4;
			}
		}
		return 2;
	},
	getNodeIndex : function(b, c) {
		var d = b, a = 0;
		while (d = d.previousSibling) {
			if (c && d.nodeType == 3) {
				continue;
			}
			a++;
		}
		return a;
	},
	inDoc : function(a, b) {
		while (a = a.parentNode) {
			if (a === b) {
				return true;
			}
		}
		return false;
	},
	findParent : function(c, a, b) {
		if (!domUtils.isBody(c)) {
			c = b ? c : c.parentNode;
			while (c) {
				if (!a || a(c) || this.isBody(c)) {
					return a && !a(c) && this.isBody(c) ? null : c;
				}
				c = c.parentNode;
			}
		}
		return null;
	},
	findParentByTagName : function(c, b, a, d) {
		if (c && c.nodeType && !this.isBody(c)
				&& (c.nodeType == 1 || c.nodeType)) {
			b = utils.listToMap(utils.isArray(b) ? b : [ b ]);
			c = c.nodeType == 3 || !a ? c.parentNode : c;
			while (c && c.tagName && c.nodeType != 9) {
				if (d && d(c)) {
					break;
				}
				if (b[c.tagName]) {
					return c;
				}
				c = c.parentNode;
			}
		}
		return null;
	},
	findParents : function(e, d, a, c) {
		var b = d && (a && a(e) || !a) ? [ e ] : [];
		while (e = domUtils.findParent(e, a)) {
			b.push(e);
		}
		return c ? b : b.reverse();
	},
	insertAfter : function(b, a) {
		return b.parentNode.insertBefore(a, b.nextSibling);
	},
	remove : function(b, c) {
		var a = b.parentNode, d;
		if (a) {
			if (c && b.hasChildNodes()) {
				while (d = b.firstChild) {
					a.insertBefore(d, b);
				}
			}
			a.removeChild(b);
		}
		return b;
	},
	getNextDomNode : function(d, a, b, c) {
		return getDomNode(d, "firstChild", "nextSibling", a, b, c);
	},
	isBookmarkNode : function(a) {
		return a.nodeType == 1 && a.id && /^_baidu_bookmark_/i.test(a.id);
	},
	getWindow : function(a) {
		var b = a.ownerDocument || a;
		return b.defaultView || b.parentWindow;
	},
	getCommonAncestor : function(f, d) {
		if (f === d) {
			return f;
		}
		var e = [ f ], c = [ d ], b = f, a = -1;
		while (b = b.parentNode) {
			if (b === d) {
				return b;
			}
			e.push(b);
		}
		b = d;
		while (b = b.parentNode) {
			if (b === f) {
				return b;
			}
			c.push(b);
		}
		e.reverse();
		c.reverse();
		while (a++, e[a] === c[a]) {
		}
		return a == 0 ? null : e[a - 1];
	},
	clearEmptySibling : function(d, b, c) {
		function a(g, e) {
			var f;
			while (g
					&& !domUtils.isBookmarkNode(g)
					&& (domUtils.isEmptyInlineElement(g) || !new RegExp(
							"[^\t\n\r" + domUtils.fillChar + "]")
							.test(g.nodeValue))) {
				f = g[e];
				domUtils.remove(g);
				g = f;
			}
		}
		!b && a(d.nextSibling, "nextSibling");
		!c && a(d.previousSibling, "previousSibling");
	},
	split : function(d, f) {
		var e = d.ownerDocument;
		if (browser.ie && f == d.nodeValue.length) {
			var c = e.createTextNode("");
			return domUtils.insertAfter(d, c);
		}
		var a = d.splitText(f);
		if (browser.ie8) {
			var b = e.createTextNode("");
			domUtils.insertAfter(a, b);
			domUtils.remove(b);
		}
		return a;
	},
	isWhitespace : function(a) {
		return !new RegExp("[^ \t\n\r" + domUtils.fillChar + "]")
				.test(a.nodeValue);
	},
	getXY : function(b) {
		var a = 0, c = 0;
		while (b.offsetParent) {
			c += b.offsetTop;
			a += b.offsetLeft;
			b = b.offsetParent;
		}
		return {
			"x" : a,
			"y" : c
		};
	},
	on : function(f, e, d) {
		var c = utils.isArray(e) ? e : [ e ], a = c.length;
		if (a) {
			while (a--) {
				e = c[a];
				if (f.addEventListener) {
					f.addEventListener(e, d, false);
				} else {
					if (!d._d) {
						d._d = {};
					}
					var b = e + d.toString();
					if (!d._d[b]) {
						d._d[b] = function(g) {
							return d.call(g.srcElement, g || window.event);
						};
						f.attachEvent("on" + e, d._d[b]);
					}
				}
			}
		}
		f = null;
	},
	un : function(f, e, d) {
		var c = utils.isArray(e) ? e : [ e ], a = c.length;
		if (a) {
			while (a--) {
				e = c[a];
				if (f.removeEventListener) {
					f.removeEventListener(e, d, false);
				} else {
					var b = e + d.toString();
					f.detachEvent("on" + e, d._d ? d._d[b] : d);
					if (d._d && d._d[b]) {
						delete d._d[b];
					}
				}
			}
		}
	},
	isSameElement : function(d, c) {
		if (d.tagName != c.tagName) {
			return 0;
		}
		var h = d.attributes, g = c.attributes;
		if (!ie && h.length != g.length) {
			return 0;
		}
		var a, j, f = 0, b = 0;
		for ( var e = 0; a = h[e++];) {
			if (a.nodeName == "style") {
				if (a.specified) {
					f++;
				}
				if (domUtils.isSameStyle(d, c)) {
					continue;
				} else {
					return 0;
				}
			}
			if (ie) {
				if (a.specified) {
					f++;
					j = g.getNamedItem(a.nodeName);
				} else {
					continue;
				}
			} else {
				j = c.attributes[a.nodeName];
			}
			if (!j.specified || a.nodeValue != j.nodeValue) {
				return 0;
			}
		}
		if (ie) {
			for (e = 0; j = g[e++];) {
				if (j.specified) {
					b++;
				}
			}
			if (f != b) {
				return 0;
			}
		}
		return 1;
	},
	isSameStyle : function(d, c) {
		var b = d.style.cssText.replace(/( ?; ?)/g, ";").replace(/( ?: ?)/g,
				":"), a = c.style.cssText.replace(/( ?; ?)/g, ";").replace(
				/( ?: ?)/g, ":");
		if (browser.opera) {
			b = d.style;
			a = c.style;
			if (b.length != a.length) {
				return 0;
			}
			for ( var g in b) {
				if (/^(\d+|csstext)$/i.test(g)) {
					continue;
				}
				if (b[g] != a[g]) {
					return 0;
				}
			}
			return 1;
		}
		if (!b || !a) {
			return b == a ? 1 : 0;
		}
		b = b.split(";");
		a = a.split(";");
		if (b.length != a.length) {
			return 0;
		}
		for ( var f = 0, e; e = b[f++];) {
			if (utils.indexOf(a, e) == -1) {
				return 0;
			}
		}
		return 1;
	},
	isBlockElm : function(a) {
		return a.nodeType == 1
				&& (dtd.$block[a.tagName] || styleBlock[domUtils
						.getComputedStyle(a, "display")])
				&& !dtd.$nonChild[a.tagName];
	},
	isBody : function(a) {
		return a && a.nodeType == 1 && a.tagName.toLowerCase() == "body";
	},
	breakParent : function(e, c) {
		var b, g = e, f = e, a, d;
		do {
			g = g.parentNode;
			if (a) {
				b = g.cloneNode(false);
				b.appendChild(a);
				a = b;
				b = g.cloneNode(false);
				b.appendChild(d);
				d = b;
			} else {
				a = g.cloneNode(false);
				d = a.cloneNode(false);
			}
			while (b = f.previousSibling) {
				a.insertBefore(b, a.firstChild);
			}
			while (b = f.nextSibling) {
				d.appendChild(b);
			}
			f = g;
		} while (c !== g);
		b = c.parentNode;
		b.insertBefore(a, c);
		b.insertBefore(d, c);
		b.insertBefore(e, d);
		domUtils.remove(c);
		return e;
	},
	isEmptyInlineElement : function(a) {
		if (a.nodeType != 1 || !dtd.$removeEmpty[a.tagName]) {
			return 0;
		}
		a = a.firstChild;
		while (a) {
			if (domUtils.isBookmarkNode(a)) {
				return 0;
			}
			if (a.nodeType == 1 && !domUtils.isEmptyInlineElement(a)
					|| a.nodeType == 3 && !domUtils.isWhitespace(a)) {
				return 0;
			}
			a = a.nextSibling;
		}
		return 1;
	},
	trimWhiteTextNode : function(b) {
		function a(c) {
			var d;
			while ((d = b[c]) && d.nodeType == 3 && domUtils.isWhitespace(d)) {
				b.removeChild(d);
			}
		}
		a("firstChild");
		a("lastChild");
	},
	mergChild : function(d, b, k) {
		var g = domUtils.getElementsByTagName(d, d.tagName.toLowerCase());
		for ( var f = 0, m; m = g[f++];) {
			if (!m.parentNode || domUtils.isBookmarkNode(m)) {
				continue;
			}
			if (m.tagName.toLowerCase() == "span") {
				if (d === m.parentNode) {
					domUtils.trimWhiteTextNode(d);
					if (d.childNodes.length == 1) {
						d.style.cssText = m.style.cssText + ";"
								+ d.style.cssText;
						domUtils.remove(m, true);
						continue;
					}
				}
				m.style.cssText = d.style.cssText + ";" + m.style.cssText;
				if (k) {
					var a = k.style;
					if (a) {
						a = a.split(";");
						for ( var e = 0, l; l = a[e++];) {
							m.style[utils.cssStyleToDomStyle(l.split(":")[0])] = l
									.split(":")[1];
						}
					}
				}
				if (domUtils.isSameStyle(m, d)) {
					domUtils.remove(m, true);
				}
				continue;
			}
			if (domUtils.isSameElement(d, m)) {
				domUtils.remove(m, true);
			}
		}
		if (b == "span") {
			var c = domUtils.getElementsByTagName(d, "a");
			for ( var f = 0, h; h = c[f++];) {
				h.style.cssText = ";" + d.style.cssText;
				h.style.textDecoration = "underline";
			}
		}
	},
	getElementsByTagName : function(e, b) {
		var f = e.getElementsByTagName(b), a = [];
		for ( var d = 0, c; c = f[d++];) {
			a.push(c);
		}
		return a;
	},
	mergToParent : function(b) {
		var a = b.parentNode;
		while (a && dtd.$removeEmpty[a.tagName]) {
			if (a.tagName == b.tagName || a.tagName == "A") {
				domUtils.trimWhiteTextNode(a);
				if (a.tagName == "SPAN" && !domUtils.isSameStyle(a, b)
						|| (a.tagName == "A" && b.tagName == "SPAN")) {
					if (a.childNodes.length > 1 || a !== b.parentNode) {
						b.style.cssText = a.style.cssText + ";"
								+ b.style.cssText;
						a = a.parentNode;
						continue;
					} else {
						a.style.cssText += ";" + b.style.cssText;
						if (a.tagName == "A") {
							a.style.textDecoration = "underline";
						}
					}
				}
				if (a.tagName != "A") {
					a === b.parentNode && domUtils.remove(b, true);
					break;
				}
			}
			a = a.parentNode;
		}
	},
	mergSibling : function(c, b, a) {
		function d(g, h, f) {
			var e;
			if ((e = f[g]) && !domUtils.isBookmarkNode(e) && e.nodeType == 1
					&& domUtils.isSameElement(f, e)) {
				while (e.firstChild) {
					if (h == "firstChild") {
						f.insertBefore(e.lastChild, f.firstChild);
					} else {
						f.appendChild(e.firstChild);
					}
				}
				domUtils.remove(e);
			}
		}
		!b && d("previousSibling", "firstChild", c);
		!a && d("nextSibling", "lastChild", c);
	},
	unselectable : ie || browser.opera ? function(c) {
		c.onselectstart = function() {
			return false;
		};
		c.onclick = c.onkeyup = c.onkeydown = function() {
			return false;
		};
		c.unselectable = "on";
		c.setAttribute("unselectable", "on");
		for ( var b = 0, a; a = c.all[b++];) {
			switch (a.tagName.toLowerCase()) {
			case "iframe":
			case "textarea":
			case "input":
			case "select":
				break;
			default:
				a.unselectable = "on";
				c.setAttribute("unselectable", "on");
			}
		}
	}
			: function(a) {
				a.style.MozUserSelect = a.style.webkitUserSelect = a.style.KhtmlUserSelect = "none";
			},
	removeAttributes : function(d, c) {
		for ( var b = 0, a; a = c[b++];) {
			a = attrFix[a] || a;
			switch (a) {
			case "className":
				d[a] = "";
				break;
			case "style":
				d.style.cssText = "";
				!browser.ie
						&& d.removeAttributeNode(d.getAttributeNode("style"));
			}
			d.removeAttribute(a);
		}
	},
	creElm : function(c, a, b) {
		return this.setAttributes(c.createElement(a), b);
	},
	setAttributes : function(c, b) {
		for ( var a in b) {
			var d = b[a];
			switch (a) {
			case "class":
				c.className = d;
				break;
			case "style":
				c.style.cssText = c.style.cssText + ";" + d;
				break;
			case "innerHTML":
				c[a] = d;
				break;
			case "value":
				c.value = d;
				break;
			default:
				c.setAttribute(attrFix[a] || a, d);
			}
		}
		return c;
	},
	getComputedStyle : function(c, b) {
		function f(e, i) {
			if (e == "font-size" && /pt$/.test(i)) {
				i = Math.round(parseFloat(i) / 0.75) + "px";
			}
			return i;
		}
		if (c.nodeType == 3) {
			c = c.parentNode;
		}
		if (browser.ie && browser.version < 9 && b == "font-size"
				&& !c.style.fontSize && !dtd.$empty[c.tagName]
				&& !dtd.$nonChild[c.tagName]) {
			var d = c.ownerDocument.createElement("span");
			d.style.cssText = "padding:0;border:0;font-family:simsun;";
			d.innerHTML = ".";
			c.appendChild(d);
			var a = d.offsetHeight;
			c.removeChild(d);
			d = null;
			return a + "px";
		}
		try {
			var g = domUtils.getStyle(c, b)
					|| (window.getComputedStyle ? domUtils.getWindow(c)
							.getComputedStyle(c, "").getPropertyValue(b)
							: (c.currentStyle || c.style)[utils
									.cssStyleToDomStyle(b)]);
		} catch (h) {
			return null;
		}
		return f(b, utils.fixColor(b, g));
	},
	removeClasses : function(a, b) {
		b = utils.isArray(b) ? b : [ b ];
		a.className = (" " + a.className + " ").replace(new RegExp("(?:\\s+(?:"
				+ b.join("|") + "))+\\s+", "g"), " ");
	},
	addClass : function(a, b) {
		if (!this.hasClass(a, b)) {
			a.className += " " + b;
		}
	},
	removeStyle : function(b, a) {
		b.style[utils.cssStyleToDomStyle(a)] = "";
		if (!b.style.cssText) {
			domUtils.removeAttributes(b, [ "style" ]);
		}
	},
	hasClass : function(a, b) {
		return (" " + a.className + " ").indexOf(" " + b + " ") > -1;
	},
	preventDefault : function(a) {
		a.preventDefault ? a.preventDefault() : (a.returnValue = false);
	},
	getStyle : function(b, a) {
		var c = b.style[utils.cssStyleToDomStyle(a)];
		return utils.fixColor(a, c);
	},
	setStyle : function(b, a, c) {
		b.style[utils.cssStyleToDomStyle(a)] = c;
	},
	setStyles : function(b, c) {
		for ( var a in c) {
			if (c.hasOwnProperty(a)) {
				domUtils.setStyle(b, a, c[a]);
			}
		}
	},
	removeDirtyAttr : function(d) {
		for ( var c = 0, b, a = d.getElementsByTagName("*"); b = a[c++];) {
			b.removeAttribute("_moz_dirty");
		}
		d.removeAttribute("_moz_dirty");
	},
	getChildCount : function(c, a) {
		var b = 0, d = c.firstChild;
		a = a || function() {
			return 1;
		};
		while (d) {
			if (a(d)) {
				b++;
			}
			d = d.nextSibling;
		}
		return b;
	},
	isEmptyNode : function(a) {
		return !a.firstChild
				|| domUtils.getChildCount(a, function(b) {
					return !domUtils.isBr(b) && !domUtils.isBookmarkNode(b)
							&& !domUtils.isWhitespace(b);
				}) == 0;
	},
	clearSelectedArr : function(a) {
		var b;
		while (b = a.pop()) {
			domUtils.removeAttributes(b, [ "class" ]);
		}
	},
	scrollToView : function(c, i, a) {
		var b = function() {
			var j = i.document, k = j.compatMode == "CSS1Compat";
			return {
				width : (k ? j.documentElement.clientWidth : j.body.clientWidth) || 0,
				height : (k ? j.documentElement.clientHeight
						: j.body.clientHeight) || 0
			};
		}, h = function(k) {
			if ("pageXOffset" in k) {
				return {
					x : k.pageXOffset || 0,
					y : k.pageYOffset || 0
				};
			} else {
				var j = k.document;
				return {
					x : j.documentElement.scrollLeft || j.body.scrollLeft || 0,
					y : j.documentElement.scrollTop || j.body.scrollTop || 0
				};
			}
		};
		var f = b().height, g = f * -1 + a;
		g += (c.offsetHeight || 0);
		var d = domUtils.getXY(c);
		g += d.y;
		var e = h(i).y;
		if (g > e || g < e - f) {
			i.scrollTo(0, g + (g < 0 ? -20 : 20));
		}
	},
	isBr : function(a) {
		return a.nodeType == 1 && a.tagName == "BR";
	},
	isFillChar : function(a) {
		return a.nodeType == 3
				&& !a.nodeValue.replace(new RegExp(domUtils.fillChar), "").length;
	},
	isStartInblock : function(b) {
		var f = b.cloneRange(), a = 0, g = f.startContainer, c;
		while (g && domUtils.isFillChar(g)) {
			c = g;
			g = g.previousSibling;
		}
		if (c) {
			f.setStartBefore(c);
			g = f.startContainer;
		}
		if (g.nodeType == 1 && domUtils.isEmptyNode(g) && f.startOffset == 1) {
			f.setStart(g, 0).collapse(true);
		}
		while (!f.startOffset) {
			g = f.startContainer;
			if (domUtils.isBlockElm(g) || domUtils.isBody(g)) {
				a = 1;
				break;
			}
			var e = f.startContainer.previousSibling, d;
			if (!e) {
				f.setStartBefore(f.startContainer);
			} else {
				while (e && domUtils.isFillChar(e)) {
					d = e;
					e = e.previousSibling;
				}
				if (d) {
					f.setStartBefore(d);
				} else {
					f.setStartBefore(f.startContainer);
				}
			}
		}
		return a && !domUtils.isBody(f.startContainer) ? 1 : 0;
	},
	isEmptyBlock : function(b) {
		var a = new RegExp("[ \t\r\n" + domUtils.fillChar + "]", "g");
		if (b[browser.ie ? "innerText" : "textContent"].replace(a, "").length > 0) {
			return 0;
		}
		for ( var c in dtd.$isNotEmpty) {
			if (b.getElementsByTagName(c).length) {
				return 0;
			}
		}
		return 1;
	},
	setViewportOffset : function(a, g) {
		var f = parseInt(a.style.left) | 0;
		var e = parseInt(a.style.top) | 0;
		var c = a.getBoundingClientRect();
		var d = g.left - c.left;
		var b = g.top - c.top;
		if (d) {
			a.style.left = f + d + "px";
		}
		if (b) {
			a.style.top = e + b + "px";
		}
	},
	fillNode : function(c, b) {
		var a = browser.ie ? c.createTextNode(domUtils.fillChar) : c
				.createElement("br");
		b.innerHTML = "";
		b.appendChild(a);
	},
	moveChild : function(c, a, b) {
		while (c.firstChild) {
			if (b && a.firstChild) {
				a.insertBefore(c.lastChild, a.firstChild);
			} else {
				a.appendChild(c.firstChild);
			}
		}
	},
	hasNoAttributes : function(a) {
		return browser.ie ? /^<\w+\s*?>/.test(a.outerHTML)
				: a.attributes.length == 0;
	},
	isCustomeNode : function(a) {
		return a.nodeType == 1 && a.getAttribute("_ue_custom_node_");
	},
	isTagNode : function(b, a) {
		return b.nodeType == 1 && b.tagName.toLowerCase() == a;
	}
};
var fillCharReg = new RegExp(domUtils.fillChar, "g");
var utils = UE.utils = {
	makeInstance : function(b) {
		var a = new Function();
		a.prototype = b;
		b = new a;
		a.prototype = null;
		return b;
	},
	extend : function(d, e, a) {
		if (e) {
			for ( var c in e) {
				if (!a || !d.hasOwnProperty(c)) {
					d[c] = e[c];
				}
			}
		}
		return d;
	},
	isArray : function(a) {
		return Object.prototype.toString.apply(a) === "[object Array]";
	},
	isString : function(a) {
		return typeof a == "string" || a.constructor == String;
	},
	inherits : function(d, b) {
		var a = d.prototype, c = utils.makeInstance(b.prototype);
		utils.extend(c, a, true);
		d.prototype = c;
		return (c.constructor = d);
	},
	bind : function(a, b) {
		return function() {
			return a.apply(b, arguments);
		};
	},
	defer : function(b, a, c) {
		var d;
		return function() {
			if (c) {
				clearTimeout(d);
			}
			d = setTimeout(b, a);
		};
	},
	indexOf : function(e, d, a) {
		for ( var c = a || 0, b = e.length; c < b; c++) {
			if (e[c] === d) {
				return c;
			}
		}
		return -1;
	},
	findNode : function(b, a, e) {
		for ( var d = 0, c; c = b[d++];) {
			if (e ? e(c) : this.indexOf(a, c.tagName.toLowerCase()) != -1) {
				return c;
			}
		}
	},
	removeItem : function(d, c) {
		for ( var b = 0, a = d.length; b < a; b++) {
			if (d[b] === c) {
				d.splice(b, 1);
				b--;
			}
		}
	},
	trim : function(a) {
		return a.replace(/(^[ \t\n\r]+)|([ \t\n\r]+$)/g, "");
	},
	listToMap : function(c) {
		if (!c) {
			return {};
		}
		c = utils.isArray(c) ? c : c.split(",");
		for ( var b = 0, a, d = {}; a = c[b++];) {
			d[a.toUpperCase()] = d[a] = 1;
		}
		return d;
	},
	unhtml : function(b, a) {
		return b ? b.replace(a || /[&<">]/g, function(c) {
			return {
				"<" : "&lt;",
				"&" : "&amp;",
				'"' : "&quot;",
				">" : "&gt;"
			}[c];
		}) : "";
	},
	html : function(a) {
		return a ? a.replace(/&((g|l|quo)t|amp);/g, function(b) {
			return {
				"&lt;" : "<",
				"&amp;" : "&",
				"&quot;" : '"',
				"&gt;" : ">"
			}[b];
		}) : "";
	},
	cssStyleToDomStyle : function() {
		var b = document.createElement("div").style, a = {
			"float" : b.cssFloat != undefined ? "cssFloat"
					: b.styleFloat != undefined ? "styleFloat" : "float"
		};
		return function(c) {
			return a[c] || (a[c] = c.toLowerCase().replace(/-./g, function(d) {
				return d.charAt(1).toUpperCase();
			}));
		};
	}(),
	loadFile : function() {
		var a = {};
		return function(g, f, b) {
			var d = a[f.src || f.href];
			if (d) {
				if (utils.isArray(d.funs)) {
					d.ready ? b() : a[f.src || f.href].funs.push(b);
				}
				return;
			}
			a[f.src || f.href] = b ? {
				"funs" : [ b ]
			} : 1;
			if (!g.body) {
				g.write('<script src="' + f.src + '"><\/script>');
				return;
			}
			if (f.id && g.getElementById(f.id)) {
				return;
			}
			var c = g.createElement(f.tag);
			delete f.tag;
			for ( var e in f) {
				c.setAttribute(e, f[e]);
			}
			c.onload = c.onreadystatechange = function() {
				if (!this.readyState || /loaded|complete/.test(this.readyState)) {
					d = a[f.src || f.href];
					if (d.funs) {
						d.ready = 1;
						for ( var h; h = d.funs.pop();) {
							h();
						}
					}
					c.onload = c.onreadystatechange = null;
				}
			};
			g.getElementsByTagName("head")[0].appendChild(c);
		};
	}(),
	isEmptyObject : function(b) {
		for ( var a in b) {
			return false;
		}
		return true;
	},
	isFunction : function(a) {
		return "[object Function]" == Object.prototype.toString.call(a);
	},
	fixColor : function(b, d) {
		if (/color/i.test(b) && /rgba?/.test(d)) {
			var e = d.split(",");
			if (e.length > 3) {
				return "";
			}
			d = "#";
			for ( var c = 0, a; a = e[c++];) {
				a = parseInt(a.replace(/[^\d]/gi, ""), 10).toString(16);
				d += a.length == 1 ? "0" + a : a;
			}
			d = d.toUpperCase();
		}
		return d;
	},
	optCss : function(e) {
		var d, c, a;
		e = e.replace(/(padding|margin|border)\-([^:]+):([^;]+);?/gi, function(
				i, g, f, h) {
			if (h.split(" ").length == 1) {
				switch (g) {
				case "padding":
					!d && (d = {});
					d[f] = h;
					return "";
				case "margin":
					!c && (c = {});
					c[f] = h;
					return "";
				case "border":
					return h == "initial" ? "" : i;
				}
			}
			return i;
		});
		function b(m, h) {
			if (!m) {
				return "";
			}
			var i = m.top, f = m.bottom, g = m.left, j = m.right, n = "";
			if (!i || !g || !f || !j) {
				for ( var k in m) {
					n += ";" + h + "-" + k + ":" + m[k] + ";";
				}
			} else {
				n += ";"
						+ h
						+ ":"
						+ (i == f && f == g && g == j ? i
								: i == f && g == j ? (i + " " + g)
										: g == j ? (i + " " + g + " " + f) : (i
												+ " " + j + " " + f + " " + g))
						+ ";";
			}
			return n;
		}
		e += b(d, "padding") + b(c, "margin");
		return e.replace(/^[ \n\r\t;]*|[ \n\r\t]*$/, "").replace(
				/;([ \n\r\t]+)|\1;/g, ";").replace(
				/(&((l|g)t|quot|#39))?;{2,}/g, function(g, f) {
					return f ? f + ";;" : ";";
				});
	},
	domReady : function() {
		var b = false, a = [];
		function c() {
			b = true;
			for ( var d; d = a.pop();) {
				d();
			}
		}
		return function(d) {
			if (document.readyState === "complete") {
				return d && setTimeout(d, 1);
			}
			d && a.push(d);
			b && c();
			if (browser.ie) {
				(function() {
					if (b) {
						return;
					}
					try {
						document.documentElement.doScroll("left");
					} catch (e) {
						setTimeout(arguments.callee, 0);
						return;
					}
					c();
				})();
				window.attachEvent("onload", c);
			} else {
				document.addEventListener("DOMContentLoaded", function() {
					document.removeEventListener("DOMContentLoaded",
							arguments.callee, false);
					c();
				}, false);
				window.addEventListener("load", c, false);
			}
		};
	}(),
	json2str : function(d) {
		var a = [];
		var b = function(e) {
			if (typeof e == "object" && e != null) {
				return utils.json2str(e);
			}
			return /^(string)$/.test(typeof e) ? "'" + e + "'" : e;
		};
		for ( var c in d) {
			a.push(c + ":" + b(d[c]));
		}
		return "{" + a.join(",") + "}";
	},
	htmlEncode : function(a) {
		return a.replace(/\"/g, "&quot;").replace(/\'/g, "&#39;").replace(
				/\n/g, "#newline#");
	},
	htmlDecode : function(a) {
		return a.replace(/\&quot\;/g, '"').replace(/\&\#39\;/g, "'");
	},
	isEmpty : function(b, a) {
		return b === null || b === undefined || (!a ? b === "" : false);
	}
};
utils.domReady();
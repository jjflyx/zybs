UE.plugins["serialize"] = function() {
	var n = browser.ie, d = browser.version;
	function s(u) {
		return /pt/.test(u) ? u.replace(/([\d.]+)pt/g, function(v) {
			return Math.round(parseFloat(v) * 96 / 72) + "px";
		}) : u;
	}
	var t = this, m = t.options.autoClearEmptyNode, g = dtd.$empty, o = function() {
		var w = /<(?:(?:\/([^>]+)>)|(?:!--([\S|\s]*?)-->)|(?:([^\s\/>]+)\s*((?:(?:"[^"]*")|(?:'[^']*')|[^"'<>])*)\/?>))/g, u = /([\w\-:.]+)(?:(?:\s*=\s*(?:(?:"([^"]*)")|(?:'([^']*)')|([^\s>]+)))|(?=\s|$))/g, x = {
			checked : 1,
			compact : 1,
			declare : 1,
			defer : 1,
			disabled : 1,
			ismap : 1,
			multiple : 1,
			nohref : 1,
			noresize : 1,
			noshade : 1,
			nowrap : 1,
			readonly : 1,
			selected : 1
		}, A = {
			script : 1,
			style : 1
		}, v = {
			"li" : {
				"$" : "ul",
				"ul" : 1,
				"ol" : 1
			},
			"dd" : {
				"$" : "dl",
				"dl" : 1
			},
			"dt" : {
				"$" : "dl",
				"dl" : 1
			},
			"option" : {
				"$" : "select",
				"select" : 1
			},
			"td" : {
				"$" : "tr",
				"tr" : 1
			},
			"th" : {
				"$" : "tr",
				"tr" : 1
			},
			"tr" : {
				"$" : "tbody",
				"tbody" : 1,
				"thead" : 1,
				"tfoot" : 1,
				"table" : 1
			},
			"tbody" : {
				"$" : "table",
				"table" : 1,
				"colgroup" : 1
			},
			"thead" : {
				"$" : "table",
				"table" : 1
			},
			"tfoot" : {
				"$" : "table",
				"table" : 1
			},
			"col" : {
				"$" : "colgroup",
				"colgroup" : 1
			}
		};
		var y = {
			"table" : "td",
			"tbody" : "td",
			"thead" : "td",
			"tfoot" : "td",
			"tr" : "td",
			"colgroup" : "col",
			"ul" : "li",
			"ol" : "li",
			"dl" : "dd",
			"select" : "option"
		};
		function z(E, I) {
			var F, G = 1, C, N;
			w.exec("");
			while ((F = w.exec(E))) {
				var L = F.index;
				if (L > G) {
					var O = E.slice(G, L);
					if (N) {
						N.push(O);
					} else {
						var P = /^\s*$/.test(O);
						if (!P) {
							I.onText(O);
						}
					}
				}
				G = w.lastIndex;
				if ((C = F[1])) {
					C = C.toLowerCase();
					if (N && C == N._tag_name) {
						I.onCDATA(N.join(""));
						N = null;
					}
					if (!N) {
						I.onTagClose(C);
						continue;
					}
				}
				if (N) {
					N.push(F[0]);
					continue;
				}
				if ((C = F[3])) {
					if (/="/.test(C)) {
						continue;
					}
					C = C.toLowerCase();
					var M = F[4], B, D = {}, K = M && M.slice(-1) == "/";
					if (M) {
						u.exec("");
						while ((B = u.exec(M))) {
							var J = B[1].toLowerCase(), H = B[2] || B[3]
									|| B[4] || "";
							if (!H && x[J]) {
								H = J;
							}
							if (J == "style") {
								if (n && d <= 6) {
									H = H.replace(/(?!;)\s*([\w-]+):/g,
											function(Q, R) {
												return R.toLowerCase() + ":";
											});
								}
							}
							if (H) {
								D[J] = H.replace(/:\s*/g, ":");
							}
						}
					}
					I.onTagOpen(C, D, K);
					if (!N && A[C]) {
						N = [];
						N._tag_name = C;
					}
					continue;
				}
				if ((C = F[2])) {
					I.onComment(C);
				}
			}
			if (E.length > G) {
				I.onText(E.slice(G, E.length));
			}
		}
		return function(E, F) {
			var C = {
				type : "fragment",
				parent : null,
				children : []
			};
			var D = C;
			function H(I) {
				I.parent = D;
				D.children.push(I);
			}
			function G(J, I) {
				var K = J;
				if (v[K.tag]) {
					while (v[D.tag] && v[D.tag][K.tag]) {
						D = D.parent;
					}
					if (D.tag == K.tag) {
						D = D.parent;
					}
					while (v[K.tag]) {
						if (v[K.tag][D.tag]) {
							break;
						}
						K = K.parent = {
							type : "element",
							tag : v[K.tag]["$"],
							attributes : {},
							children : [ K ]
						};
					}
				}
				if (F) {
					while (dtd[K.tag]
							&& !(D.tag == "span" ? utils.extend(dtd["strong"],
									{
										"a" : 1,
										"A" : 1
									}) : (dtd[D.tag] || dtd["div"]))[K.tag]) {
						if (B(D)) {
							continue;
						}
						if (!D.parent) {
							break;
						}
						D = D.parent;
					}
				}
				K.parent = D;
				D.children.push(K);
				if (I) {
					D = J;
				}
				if (J.attributes.style) {
					J.attributes.style = J.attributes.style.toLowerCase();
				}
				return J;
			}
			function B(I) {
				var J;
				if (!I.children.length && (J = y[I.tag])) {
					G({
						type : "element",
						tag : J,
						attributes : {},
						children : []
					}, true);
					return true;
				}
				return false;
			}
			z(
					E,
					{
						onText : function(I) {
							while (!(dtd[D.tag] || dtd["div"])["#"]) {
								if (B(D)) {
									continue;
								}
								D = D.parent;
							}
							H({
								type : "text",
								data : I
							});
						},
						onComment : function(I) {
							H({
								type : "comment",
								data : I
							});
						},
						onCDATA : function(I) {
							while (!(dtd[D.tag] || dtd["div"])["#"]) {
								if (B(D)) {
									continue;
								}
								D = D.parent;
							}
							H({
								type : "cdata",
								data : I
							});
						},
						onTagOpen : function(I, K, J) {
							J = J || g[I];
							G({
								type : "element",
								tag : I,
								attributes : K,
								closed : J,
								children : []
							}, !J);
						},
						onTagClose : function(I) {
							var J = D;
							while (J && I != J.tag) {
								J = J.parent;
							}
							if (J) {
								for ( var K = D; K !== J.parent; K = K.parent) {
									B(K);
								}
								D = J.parent;
							} else {
								if (!(dtd.$removeEmpty[I]
										|| dtd.$removeEmptyBlock[I] || I == "embed")) {
									J = {
										type : "element",
										tag : I,
										attributes : {},
										children : []
									};
									G(J, true);
									B(J);
									D = J.parent;
								}
							}
						}
					});
			while (D !== C) {
				B(D);
				D = D.parent;
			}
			return C;
		};
	}();
	var j = function() {
		var v = {
			"<" : "&lt;",
			">" : "&gt;",
			'"' : "&quot;",
			"'" : "&#39;"
		};
		function u(w) {
			return v[w];
		}
		return function(w) {
			w = w + "";
			return w ? w.replace(/[<>"']/g, u) : "";
		};
	}();
	var l = function() {
		function v(D, C) {
			var B = D.children;
			var E = [];
			for ( var A = 0, z; z = B[A]; A++) {
				E.push(l(z, C));
			}
			return E.join("");
		}
		function w(A) {
			var C = [];
			for ( var z in A) {
				var B = A[z];
				if (z == "style") {
					B = s(B);
					if (/rgba?\s*\([^)]*\)/.test(B)) {
						B = B.replace(/rgba?\s*\(([^)]*)\)/g, function(D) {
							return utils.fixColor("color", D);
						});
					}
					A[z] = utils.optCss(B.replace(/windowtext/g, "#000"))
							.replace(/white-space[^;]+;/g, "");
				}
				C.push(z + '="' + j(A[z]) + '"');
			}
			return C.join(" ");
		}
		function u(A, z) {
			return z ? A.data.replace(/&nbsp;/g, " ") : j(A.data).replace(/ /g,
					"&nbsp;");
		}
		var y = {
			"div" : "p",
			"li" : "p",
			"tr" : "p",
			"br" : "br",
			"p" : "p"
		};
		function x(D, C) {
			if (D.type == "element" && !D.children.length
					&& (dtd.$removeEmpty[D.tag]) && D.tag != "a"
					&& utils.isEmptyObject(D.attributes) && m) {
				return B;
			}
			var z = D.tag;
			if (C && z == "td") {
				if (!B) {
					B = "";
				}
				B += v(D, C) + "&nbsp;&nbsp;&nbsp;";
			} else {
				var A = w(D.attributes);
				var B = "<" + (C && y[z] ? y[z] : z) + (A ? " " + A : "")
						+ (g[z] ? " />" : ">");
				if (!g[z]) {
					if (z == "p" && !D.children.length) {
						B += browser.ie ? "&nbsp;" : "<br/>";
					}
					B += v(D, C);
					B += "</" + (C && y[z] ? y[z] : z) + ">";
				}
			}
			return B;
		}
		return function(A, z) {
			if (A.type == "fragment") {
				return v(A, z);
			} else {
				if (A.type == "element") {
					return x(A, z);
				} else {
					if (A.type == "text" || A.type == "cdata") {
						return u(A, dtd.$notTransContent[A.parent.tag]);
					} else {
						if (A.type == "comment") {
							return "<!--" + A.data + "-->";
						}
					}
				}
			}
			return "";
		};
	}();
	var h = function() {
		function v(y) {
			var x = new RegExp(
					/(class="?Mso|style="[^"]*\bmso\-|w:WordDocument|<v:)/ig);
			return x.test(y);
		}
		function w(x) {
			x = x.replace(/([\d.]+)([\w]+)?/g, function(y, A, z) {
				return (Math.round(parseFloat(A)) || 1) + (z || "px");
			});
			return x;
		}
		function u(x) {
			x = x
					.replace(/<!--\s*EndFragment\s*-->[\s\S]*$/, "")
					.replace(/^(\r\n|\n|\r)|(\r\n|\n|\r)$/ig, "")
					.replace(/^\s*(&nbsp;)+/ig, "")
					.replace(/(&nbsp;|<br[^>]*>)+\s*$/ig, "")
					.replace(/<!--[\s\S]*?-->/ig, "")
					.replace(
							/<v:shape [^>]*>[\s\S]*?.<\/v:shape>/gi,
							function(C) {
								if (browser.opera) {
									return "";
								}
								try {
									var z = C.match(/width:([ \d.]*p[tx])/i)[1], y = C
											.match(/height:([ \d.]*p[tx])/i)[1], B = C
											.match(/src=\s*"([^"]*)"/i)[1];
									return '<img width="' + s(z) + '" height="'
											+ s(y) + '" src="' + B + '" />';
								} catch (A) {
									return "";
								}
							})
					.replace(/v:\w+=["']?[^'"]+["']?/g, "")
					.replace(
							/<(!|script[^>]*>.*?<\/script(?=[>\s])|\/?(\?xml(:\w+)?|xml|meta|link|style|\w+:\w+)(?=[\s\/>]))[^>]*>/gi,
							"")
					.replace(/<p [^>]*class="?MsoHeading"?[^>]*>(.*?)<\/p>/gi,
							"<p><strong>$1</strong></p>")
					.replace(/(lang)\s*=\s*([\'\"]?)[\w-]+\2/ig, "")
					.replace(/<font[^>]*>\s*<\/font>/gi, "")
					.replace(
							/class\s*=\s*["']?(?:(?:MsoTableGrid)|(?:MsoListParagraph)|(?:MsoNormal(Table)?))\s*["']?/gi,
							"");
			x = x
					.replace(
							/(<[a-z][^>]*)\sstyle=(["'])([^\2]*?)\2/gi,
							function(E, I, C, z) {
								var A = [], D = 0, H = z.replace(/^\s+|\s+$/,
										"").replace(/&quot;/gi, "'").split(
										/;\s*/g);
								for ( var D = 0; D < H.length; D++) {
									var G = H[D];
									var y, F, B = G.split(":");
									if (B.length == 2) {
										y = B[0].toLowerCase();
										F = B[1].toLowerCase();
										switch (y) {
										case "mso-padding-alt":
										case "mso-padding-top-alt":
										case "mso-padding-right-alt":
										case "mso-padding-bottom-alt":
										case "mso-padding-left-alt":
										case "mso-margin-alt":
										case "mso-margin-top-alt":
										case "mso-margin-right-alt":
										case "mso-margin-bottom-alt":
										case "mso-margin-left-alt":
										case "mso-height":
										case "mso-width":
										case "mso-vertical-align-alt":
											if (!/<table/.test(I)) {
												A[D] = y.replace(
														/^mso-|-alt$/g, "")
														+ ":" + w(F);
											}
											continue;
										case "horiz-align":
											A[D] = "text-align:" + F;
											continue;
										case "vert-align":
											A[D] = "vertical-align:" + F;
											continue;
										case "font-color":
										case "mso-foreground":
											A[D] = "color:" + F;
											continue;
										case "mso-background":
										case "mso-highlight":
											A[D] = "background:" + F;
											continue;
										case "mso-default-height":
											A[D] = "min-height:" + w(F);
											continue;
										case "mso-default-width":
											A[D] = "min-width:" + w(F);
											continue;
										case "mso-padding-between-alt":
											A[D] = "border-collapse:separate;border-spacing:"
													+ w(F);
											continue;
										case "text-line-through":
											if ((F == "single")
													|| (F == "double")) {
												A[D] = "text-decoration:line-through";
											}
											continue;
										case "mso-zero-height":
											if (F == "yes") {
												A[D] = "display:none";
											}
											continue;
										case "margin":
											if (!/[1-9]/.test(B[1])) {
												continue;
											}
										}
										if (/^(mso|column|font-emph|lang|layout|line-break|list-image|nav|panose|punct|row|ruby|sep|size|src|tab-|table-border|text-(?:decor|trans)|top-bar|version|vnd|word-break)/
												.test(y)) {
											continue;
										}
										if (/text\-indent|padding|margin/
												.test(y)
												&& /\-[\d.]+/.test(F)) {
											continue;
										}
										A[D] = y + ":" + B[1];
									}
								}
								if (D > 0) {
									return I + ' style="' + A.join(";") + '"';
								} else {
									return I;
								}
							});
			x = x.replace(/([ ]+)<\/span>/ig, function(y, z) {
				return new Array(z.length + 1).join("&nbsp;") + "</span>";
			});
			return x;
		}
		return function(x) {
			f = null;
			r = "", i = "", p = "";
			if (v(x)) {
				x = u(x);
			}
			return x.replace(/>[ \t\r\n]*</g, "><");
		};
	}();
	var c = {
		"text" : "#text",
		"comment" : "#comment",
		"cdata" : "#cdata-section",
		"fragment" : "#document-fragment"
	};
	var f, r = "", i = "", p;
	function e(x, y) {
		var A = [ 0, 10, 12, 16, 18, 24, 32, 48 ], z, C = utils.indexOf;
		switch (x.tag) {
		case "script":
			x.tag = "div";
			x.attributes._ue_div_script = 1;
			x.attributes._ue_script_data = x.children[0] ? encodeURIComponent(x.children[0].data)
					: "";
			x.attributes._ue_custom_node_ = 1;
			x.children = [];
			break;
		case "style":
			x.tag = "div";
			x.attributes._ue_div_style = 1;
			x.attributes._ue_style_data = x.children[0] ? encodeURIComponent(x.children[0].data)
					: "";
			x.attributes._ue_custom_node_ = 1;
			x.children = [];
			break;
		case "img":
			if (x.attributes.src && /^data:/.test(x.attributes.src)) {
				return {
					type : "fragment",
					children : []
				};
			}
			if (x.attributes.src && /^(?:file)/.test(x.attributes.src)) {
				if (!/(gif|bmp|png|jpg|jpeg)$/.test(x.attributes.src)) {
					return {
						type : "fragment",
						children : []
					};
				}
				x.attributes.word_img = x.attributes.src;
				x.attributes.src = t.options.UEDITOR_HOME_URL
						+ "themes/default/images/spacer.gif";
				var B = parseInt(x.attributes.width) < 128
						|| parseInt(x.attributes.height) < 43;
				x.attributes.style = "background:url("
						+ (B ? t.options.UEDITOR_HOME_URL
								+ "themes/default/images/word.gif"
								: t.options.langPath + t.options.lang
										+ "/images/localimage.png")
						+ ") no-repeat center center;border:1px solid #ddd";
				y && (y.flag = 1);
			}
			if (browser.ie && browser.version < 7) {
				x.attributes.orgSrc = x.attributes.src;
			}
			x.attributes.data_ue_src = x.attributes.data_ue_src
					|| x.attributes.src;
			break;
		case "li":
			var v = x.children[0];
			if (!v || v.type != "element" || v.tag != "p" && dtd.p[v.tag]) {
				var w = {
					type : "element",
					tag : "p",
					attributes : {},
					parent : x
				};
				w.children = v ? x.children : [ browser.ie ? {
					type : "text",
					data : domUtils.fillChar,
					parent : w
				} : {
					type : "element",
					tag : "br",
					attributes : {},
					closed : true,
					children : [],
					parent : w
				} ];
				x.children = [ w ];
			}
			break;
		case "table":
		case "td":
			a(x);
			break;
		case "a":
			if (x.attributes["anchorname"]) {
				x.tag = "img";
				x.attributes = {
					"class" : "anchorclass",
					"anchorname" : x.attributes["name"]
				};
				x.closed = 1;
			}
			x.attributes.href && (x.attributes.data_ue_src = x.attributes.href);
			break;
		case "b":
			x.tag = x.name = "strong";
			break;
		case "i":
			x.tag = x.name = "em";
			break;
		case "u":
			x.tag = x.name = "span";
			x.attributes.style = (x.attributes.style || "")
					+ ";text-decoration:underline;";
			break;
		case "s":
		case "del":
			x.tag = x.name = "span";
			x.attributes.style = (x.attributes.style || "")
					+ ";text-decoration:line-through;";
			if (x.children.length == 1) {
				v = x.children[0];
				if (v.tag == x.tag) {
					x.attributes.style += ";" + v.attributes.style;
					x.children = v.children;
				}
			}
			break;
		case "span":
			var u = x.attributes.style;
			if (u) {
				if (!x.attributes.style || browser.webkit
						&& u == "white-space:nowrap;") {
					delete x.attributes.style;
				}
			}
			if (browser.gecko && browser.version <= 10902 && x.parent) {
				var D = x.parent;
				if (D.tag == "span" && D.attributes && D.attributes.style) {
					x.attributes.style = D.attributes.style + ";"
							+ x.attributes.style;
				}
			}
			if (utils.isEmptyObject(x.attributes) && m) {
				x.type = "fragment";
			}
			break;
		case "font":
			x.tag = x.name = "span";
			z = x.attributes;
			x.attributes = {
				"style" : (z.size ? "font-size:" + (A[z.size] || 12) + "px"
						: "")
						+ ";"
						+ (z.color ? "color:" + z.color : "")
						+ ";"
						+ (z.face ? "font-family:" + z.face : "")
						+ ";"
						+ (z.style || "")
			};
			while (x.parent.tag == x.tag && x.parent.children.length == 1) {
				x.attributes.style
						&& (x.parent.attributes.style ? (x.parent.attributes.style += ";"
								+ x.attributes.style)
								: (x.parent.attributes.style = x.attributes.style));
				x.parent.children = x.children;
				x = x.parent;
			}
			break;
		case "p":
			if (x.attributes.align) {
				x.attributes.style = (x.attributes.style || "")
						+ ";text-align:" + x.attributes.align + ";";
				delete x.attributes.align;
			}
		}
		return x;
	}
	function a(v) {
		if (n && v.attributes.style) {
			var u = v.attributes.style;
			v.attributes.style = u.replace(/;\s*/g, ";");
			v.attributes.style = v.attributes.style.replace(/^\s*|\s*$/, "");
		}
	}
	function k(u) {
		switch (u.tag) {
		case "div":
			if (u.attributes._ue_div_script) {
				u.tag = "script";
				u.children = [ {
					type : "cdata",
					data : u.attributes._ue_script_data ? decodeURIComponent(u.attributes._ue_script_data)
							: "",
					parent : u
				} ];
				delete u.attributes._ue_div_script;
				delete u.attributes._ue_script_data;
				delete u.attributes._ue_custom_node_;
			}
			if (u.attributes._ue_div_style) {
				u.tag = "style";
				u.children = [ {
					type : "cdata",
					data : u.attributes._ue_style_data ? decodeURIComponent(u.attributes._ue_style_data)
							: "",
					parent : u
				} ];
				delete u.attributes._ue_div_style;
				delete u.attributes._ue_style_data;
				delete u.attributes._ue_custom_node_;
			}
			break;
		case "table":
			!u.attributes.style && delete u.attributes.style;
			if (n && u.attributes.style) {
				a(u);
			}
			if (u.attributes["class"] == "noBorderTable") {
				delete u.attributes["class"];
			}
			break;
		case "td":
		case "th":
			if (/display\s*:\s*none/i.test(u.attributes.style)) {
				return {
					type : "fragment",
					children : []
				};
			}
			if (n && !u.children.length) {
				var v = {
					type : "text",
					data : domUtils.fillChar,
					parent : u
				};
				u.children[0] = v;
			}
			if (n && u.attributes.style) {
				a(u);
			}
			if (u.attributes["class"] == "selectTdClass") {
				delete u.attributes["class"];
			}
			break;
		case "img":
			if (u.attributes.anchorname) {
				u.tag = "a";
				u.attributes = {
					name : u.attributes.anchorname,
					anchorname : 1
				};
				u.closed = null;
			} else {
				if (u.attributes.data_ue_src) {
					u.attributes.src = u.attributes.data_ue_src;
					delete u.attributes.data_ue_src;
				}
			}
			break;
		case "a":
			if (u.attributes.data_ue_src) {
				u.attributes.href = u.attributes.data_ue_src;
				delete u.attributes.data_ue_src;
			}
		}
		return u;
	}
	function b(A, z, u) {
		if (!A.children || !A.children.length) {
			return A;
		}
		var y = A.children;
		for ( var x = 0; x < y.length; x++) {
			var w = z(y[x], u);
			if (w.type == "fragment") {
				var v = [ x, 1 ];
				v.push.apply(v, w.children);
				y.splice.apply(y, v);
				if (!y.length) {
					A = {
						type : "fragment",
						children : []
					};
				}
				x--;
			} else {
				y[x] = w;
			}
		}
		return A;
	}
	function q(u) {
		this.rules = u;
	}
	q.prototype = {
		rules : null,
		filter : function(w, z, y) {
			z = z || this.rules;
			var v = z && z.whiteList;
			var u = z && z.blackList;
			function x(E, D) {
				E.name = E.type == "element" ? E.tag : c[E.type];
				if (D == null) {
					return b(E, x, E);
				}
				if (u && u[E.name]) {
					y && (y.flag = 1);
					return {
						type : "fragment",
						children : []
					};
				}
				if (v) {
					if (E.type == "element") {
						if (D.type == "fragment" ? v[E.name] : v[E.name]
								&& v[D.name][E.name]) {
							var C;
							if ((C = v[E.name].$)) {
								var F = E.attributes;
								var B = {};
								for ( var A in C) {
									if (F[A]) {
										B[A] = F[A];
									}
								}
								E.attributes = B;
							}
						} else {
							y && (y.flag = 1);
							E.type = "fragment";
							E.name = D.name;
						}
					} else {
					}
				}
				if (u || v) {
					b(E, x, E);
				}
				return E;
			}
			return x(w, null);
		},
		transformInput : function(v, u) {
			function w(x) {
				x = e(x, u);
				x = b(x, w, x);
				if (t.options.pageBreakTag && x.type == "text"
						&& x.data.replace(/\s/g, "") == t.options.pageBreakTag) {
					x.type = "element";
					x.name = x.tag = "hr";
					delete x.data;
					x.attributes = {
						"class" : "pagebreak",
						noshade : "noshade",
						size : "5",
						"unselectable" : "on",
						"style" : "moz-user-select:none;-khtml-user-select: none;"
					};
					x.children = [];
				}
				if (x.type == "text" && !dtd.$notTransContent[x.parent.tag]) {
					x.data = x.data.replace(/[\r\t\n]*/g, "");
				}
				return x;
			}
			return w(v);
		},
		transformOutput : function(u) {
			function v(w) {
				if (w.tag == "hr" && w.attributes["class"] == "pagebreak") {
					delete w.tag;
					w.type = "text";
					w.data = t.options.pageBreakTag;
					delete w.children;
				}
				w = k(w);
				if (w.tag == "ol" || w.tag == "ul") {
					f = 1;
				}
				w = b(w, v, w);
				if (w.tag == "ol" || w.tag == "ul") {
					f = 0;
				}
				return w;
			}
			return v(u);
		},
		toHTML : l,
		parseHTML : o,
		word : h
	};
	t.serialize = new q(t.options.serialize || {});
	UE.serialize = new q({});
};
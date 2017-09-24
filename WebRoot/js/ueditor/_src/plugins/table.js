UE.plugins["table"] = function() {
	var n = this, q = domUtils.keys, f = domUtils.clearSelectedArr;
	var o, g, m = domUtils.isEmptyNode;
	function c(r) {
		var s = r.parentNode.cells;
		for ( var u = 0, t; t = s[u]; u++) {
			if (t === r) {
				return u;
			}
		}
	}
	function l(t, s) {
		var u = t.ownerDocument.createElement("p");
		domUtils.fillNode(n.document, u);
		var r = t.parentNode;
		if (r && r.getAttribute("dropdrag")) {
			t = r;
		}
		t.parentNode.insertBefore(u, t);
		domUtils.remove(t);
		s.setStart(u, 0).setCursor();
	}
	function k(r) {
		return r.style.display == "none";
	}
	function b(r) {
		var u = 0;
		for ( var s = 0, t; t = r[s++];) {
			if (!k(t)) {
				u++;
			}
		}
		return u;
	}
	n.currentSelectedArr = [];
	n.addListener("mousedown", d);
	n.addListener("keydown", function(s, r) {
		var t = r.keyCode || r.which;
		if (!q[t] && !r.ctrlKey && !r.metaKey && !r.shiftKey && !r.altKey) {
			f(n.currentSelectedArr);
		}
	});
	n.addListener("mouseup", function() {
		o = null;
		n.removeListener("mouseover", d);
		var u = n.currentSelectedArr[0];
		if (u) {
			n.document.body.style.webkitUserSelect = "";
			var s = new dom.Range(n.document);
			if (m(u)) {
				s.setStart(n.currentSelectedArr[0], 0).setCursor();
			} else {
				s.selectNodeContents(n.currentSelectedArr[0]).select();
			}
		} else {
			var s = n.selection.getRange().shrinkBoundary();
			if (!s.collapsed) {
				var t = domUtils.findParentByTagName(s.startContainer, "td",
						true), r = domUtils.findParentByTagName(s.endContainer,
						"td", true);
				if (t && !r || !t && r || t && r && t !== r) {
					s.collapse(true).select(true);
				}
			}
		}
	});
	function j() {
		n.currentSelectedArr = [];
		o = null;
	}
	n.commands["inserttable"] = {
		queryCommandState : function() {
			if (this.highlight) {
				return -1;
			}
			var r = this.selection.getRange();
			return domUtils
					.findParentByTagName(r.startContainer, "table", true)
					|| domUtils.findParentByTagName(r.endContainer, "table",
							true) || n.currentSelectedArr.length > 0 ? -1 : 0;
		},
		execCommand : function(u, x) {
			x = x || {
				numRows : 5,
				numCols : 5,
				border : 1
			};
			var w = [ "<table "
					+ (x.border == "0" ? ' class="noBorderTable"' : "")
					+ ' _innerCreateTable = "true" ' ];
			if (x.cellSpacing && x.cellSpacing != "0" || x.cellPadding
					&& x.cellPadding != "0") {
				w.push(' style="border-collapse:separate;" ');
			}
			x.cellSpacing && x.cellSpacing != "0"
					&& w.push(' cellSpacing="' + x.cellSpacing + '" ');
			x.cellPadding && x.cellPadding != "0"
					&& w.push(' cellPadding="' + x.cellPadding + '" ');
			w.push(' width="' + (x.width || 100)
					+ (typeof x.widthUnit == "undefined" ? "%" : x.widthUnit)
					+ '" ');
			x.height
					&& w.push(' height="'
							+ x.height
							+ (typeof x.heightUnit == "undefined" ? "%"
									: x.heightUnit) + '" ');
			x.align && (w.push(' align="' + x.align + '" '));
			w.push(' border="' + (x.border || 0) + '" borderColor="'
					+ (x.borderColor || "#000000") + '"');
			x.borderType == "1" && w.push(' borderType="1" ');
			x.bgColor && w.push(' bgColor="' + x.bgColor + '"');
			w.push(" ><tbody>");
			x.width = Math.floor((x.width || "100") / x.numCols);
			for ( var v = 0; v < x.numRows; v++) {
				w.push("<tr>");
				for ( var t = 0; t < x.numCols; t++) {
					w.push('<td style="width:'
							+ x.width
							+ (typeof x.widthUnit == "undefined" ? "%"
									: x.widthUnit)
							+ ";"
							+ (x.borderType == "1" ? "border:" + x.border
									+ "px solid "
									+ (x.borderColor || "#000000") : "") + '">'
							+ (browser.ie ? domUtils.fillChar : "<br/>")
							+ "</td>");
				}
				w.push("</tr>");
			}
			n.execCommand("insertHtml", w.join("") + "</tbody></table>");
			j();
			if (x.align) {
				var s = n.selection.getRange(), r = s.createBookmark(), y = s.startContainer;
				while (y && !domUtils.isBody(y)) {
					if (domUtils.isBlockElm(y)) {
						y.style.clear = "both";
						s.moveToBookmark(r).select();
						break;
					}
					y = y.parentNode;
				}
			}
		}
	};
	n.commands["edittable"] = {
		queryCommandState : function() {
			var r = this.selection.getRange();
			if (this.highlight) {
				return -1;
			}
			return domUtils
					.findParentByTagName(r.startContainer, "table", true)
					|| n.currentSelectedArr.length > 0 ? 0 : -1;
		},
		execCommand : function(r, t) {
			var x = n.selection.getStart(), w = domUtils.findParentByTagName(x,
					"table", true);
			if (w) {
				w.style.cssText = w.style.cssText.replace(/border[^;]+/gi, "");
				w.style.borderCollapse = t.cellSpacing && t.cellSpacing != "0"
						|| t.cellPadding && t.cellPadding != "0" ? "separate"
						: "collapse";
				t.cellSpacing && t.cellSpacing != "0" ? w.setAttribute(
						"cellSpacing", t.cellSpacing) : w
						.removeAttribute("cellSpacing");
				t.cellPadding && t.cellPadding != "0" ? w.setAttribute(
						"cellPadding", t.cellPadding) : w
						.removeAttribute("cellPadding");
				t.height && w.setAttribute("height", t.height + t.heightUnit);
				t.align && w.setAttribute("align", t.align);
				t.width && w.setAttribute("width", t.width + t.widthUnit);
				if (t.bgColor) {
					w.setAttribute("bgColor", t.bgColor);
				} else {
					domUtils.removeAttributes(w, [ "bgColor" ]);
				}
				t.borderColor && w.setAttribute("borderColor", t.borderColor);
				w.setAttribute("border", t.border);
				if (domUtils.hasClass(w, "noBorderTable")) {
					domUtils.removeClasses(w, [ "noBorderTable" ]);
				}
				domUtils.addClass(w, t.border == "0" ? " noBorderTable" : "");
				if (t.borderType == "1") {
					for ( var s = 0, v, u = w.getElementsByTagName("td"); v = u[s++];) {
						v.style.border = t.border + "px solid "
								+ (t.borderColor || "#000000");
					}
					w.setAttribute("borderType", "1");
				} else {
					for ( var s = 0, v, u = w.getElementsByTagName("td"); v = u[s++];) {
						if (browser.ie) {
							v.style.cssText = v.style.cssText.replace(
									/border[^;]+/gi, "");
						} else {
							domUtils.removeStyle(v, "border");
							domUtils.removeStyle(v, "border-image");
						}
					}
					w.removeAttribute("borderType");
				}
			}
		}
	};
	n.commands["edittd"] = {
		queryCommandState : function() {
			if (this.highlight) {
				return -1;
			}
			var r = this.selection.getRange();
			return (domUtils.findParentByTagName(r.startContainer, "table",
					true) && domUtils.findParentByTagName(r.endContainer,
					"table", true))
					|| n.currentSelectedArr.length > 0 ? 0 : -1;
		},
		execCommand : function(s, v) {
			var r = this.selection.getRange(), u = !n.currentSelectedArr.length ? [ domUtils
					.findParentByTagName(r.startContainer, [ "td", "th" ], true) ]
					: n.currentSelectedArr;
			for ( var t = 0, w; w = u[t++];) {
				domUtils.setAttributes(w, {
					"dateClass" : v.dateClass,
					"bgColor" : v.bgColor,
					"align" : v.align,
					"vAlign" : v.vAlign
				});
			}
		}
	};
	n.commands["deletetable"] = {
		queryCommandState : function() {
			if (this.highlight) {
				return -1;
			}
			var r = this.selection.getRange();
			return (domUtils.findParentByTagName(r.startContainer, "table",
					true) && domUtils.findParentByTagName(r.endContainer,
					"table", true))
					|| n.currentSelectedArr.length > 0 ? 0 : -1;
		},
		execCommand : function() {
			var r = this.selection.getRange(), s = domUtils
					.findParentByTagName(
							n.currentSelectedArr.length > 0 ? n.currentSelectedArr[0]
									: r.startContainer, "table", true);
			l(s, r);
			j();
		}
	};
	n.commands["mergeright"] = {
		queryCommandState : function() {
			if (this.highlight) {
				return -1;
			}
			var r = this.selection.getRange(), w = r.startContainer, v = domUtils
					.findParentByTagName(w, [ "td", "th" ], true);
			if (!v || this.currentSelectedArr.length > 1) {
				return -1;
			}
			var u = v.parentNode;
			var s = c(v) + v.colSpan;
			if (s >= u.cells.length) {
				return -1;
			}
			var t = u.cells[s];
			if (k(t)) {
				return -1;
			}
			return v.rowSpan == t.rowSpan ? 0 : -1;
		},
		execCommand : function() {
			var x = this.selection.getRange(), r = x.startContainer, s = domUtils
					.findParentByTagName(r, [ "td", "th" ], true)
					|| n.currentSelectedArr[0], y = s.parentNode, B = y.parentNode.parentNode.rows;
			var u = y.rowIndex, A = c(s) + s.colSpan, w = B[u].cells[A];
			for ( var v = u; v < u + w.rowSpan; v++) {
				for ( var t = A; t < A + w.colSpan; t++) {
					var z = B[v].cells[t];
					z.setAttribute("rootRowIndex", y.rowIndex);
					z.setAttribute("rootCellIndex", c(s));
				}
			}
			s.colSpan += w.colSpan || 1;
			i(s, w);
			w.style.display = "none";
			if (domUtils.isEmptyBlock(s)) {
				x.setStart(s, 0).setCursor();
			} else {
				x.selectNodeContents(s).setCursor(true, true);
			}
			browser.ie && domUtils.removeAttributes(s, [ "width", "height" ]);
		}
	};
	n.commands["mergedown"] = {
		queryCommandState : function() {
			if (this.highlight) {
				return -1;
			}
			var s = this.selection.getRange(), y = s.startContainer, x = domUtils
					.findParentByTagName(y, "td", true);
			if (!x || b(n.currentSelectedArr) > 1) {
				return -1;
			}
			var w = x.parentNode, u = w.parentNode.parentNode, v = u.rows;
			var t = w.rowIndex + x.rowSpan;
			if (t >= v.length) {
				return -1;
			}
			var r = v[t].cells[c(x)];
			if (k(r)) {
				return -1;
			}
			return x.colSpan == r.colSpan ? 0 : -1;
		},
		execCommand : function() {
			var x = this.selection.getRange(), r = x.startContainer, t = domUtils
					.findParentByTagName(r, [ "td", "th" ], true)
					|| n.currentSelectedArr[0];
			var z = t.parentNode, B = z.parentNode.parentNode.rows;
			var s = z.rowIndex + t.rowSpan, y = c(t), v = B[s].cells[y];
			for ( var w = s; w < s + v.rowSpan; w++) {
				for ( var u = y; u < y + v.colSpan; u++) {
					var A = B[w].cells[u];
					A.setAttribute("rootRowIndex", z.rowIndex);
					A.setAttribute("rootCellIndex", c(t));
				}
			}
			t.rowSpan += v.rowSpan || 1;
			i(t, v);
			v.style.display = "none";
			if (domUtils.isEmptyBlock(t)) {
				x.setStart(t, 0).setCursor();
			} else {
				x.selectNodeContents(t).setCursor(true, true);
			}
			browser.ie && domUtils.removeAttributes(t, [ "width", "height" ]);
		}
	};
	n.commands["deleterow"] = {
		queryCommandState : function() {
			if (this.highlight) {
				return -1;
			}
			var r = this.selection.getRange(), t = r.startContainer, s = domUtils
					.findParentByTagName(t, [ "td", "th" ], true);
			if (!s && n.currentSelectedArr.length == 0) {
				return -1;
			}
			return 0;
		},
		execCommand : function() {
			var C = this.selection.getRange(), v = C.startContainer, x = domUtils
					.findParentByTagName(v, [ "td", "th" ], true), r, H, u, z, A, y;
			if (x && n.currentSelectedArr.length == 0) {
				var w = (x.rowSpan || 1) - 1;
				n.currentSelectedArr.push(x);
				r = x.parentNode;
				H = r.parentNode.parentNode;
				z = H.rows, A = r.rowIndex + 1, y = c(x);
				while (w) {
					n.currentSelectedArr.push(z[A].cells[y]);
					w--;
					A++;
				}
			}
			while (x = n.currentSelectedArr.pop()) {
				if (!domUtils.findParentByTagName(x, "table")) {
					continue;
				}
				r = x.parentNode, H = r.parentNode.parentNode;
				u = r.cells, z = H.rows, A = r.rowIndex, y = c(x);
				for ( var B = 0; B < u.length;) {
					var t = u[B];
					if (k(t)) {
						var s = z[t.getAttribute("rootRowIndex")].cells[t
								.getAttribute("rootCellIndex")];
						s.rowSpan--;
						B += s.colSpan;
					} else {
						if (t.rowSpan == 1) {
							B += t.colSpan;
						} else {
							var F = z[A + 1].cells[B];
							F.style.display = "";
							F.rowSpan = t.rowSpan - 1;
							F.colSpan = t.colSpan;
							B += t.colSpan;
						}
					}
				}
				domUtils.remove(r);
				var I, D, G;
				if (A == z.length) {
					if (A == 0) {
						l(H, C);
						return;
					}
					var E = A - 1;
					I = z[E].cells[y];
					D = k(I) ? z[I.getAttribute("rootRowIndex")].cells[I
							.getAttribute("rootCellIndex")] : I;
				} else {
					G = z[A].cells[y];
					D = k(G) ? z[G.getAttribute("rootRowIndex")].cells[G
							.getAttribute("rootCellIndex")] : G;
				}
			}
			C.setStart(D, 0).setCursor();
			h(H);
		}
	};
	n.commands["deletecol"] = {
		queryCommandState : function() {
			if (this.highlight) {
				return -1;
			}
			var r = this.selection.getRange(), t = r.startContainer, s = domUtils
					.findParentByTagName(t, [ "td", "th" ], true);
			if (!s && n.currentSelectedArr.length == 0) {
				return -1;
			}
			return 0;
		},
		execCommand : function() {
			var D = this.selection.getRange(), v = D.startContainer, y = domUtils
					.findParentByTagName(v, [ "td", "th" ], true);
			if (y && n.currentSelectedArr.length == 0) {
				var x = (y.colSpan || 1) - 1;
				n.currentSelectedArr.push(y);
				while (x) {
					do {
						y = y.nextSibling;
					} while (y.nodeType == 3);
					n.currentSelectedArr.push(y);
					x--;
				}
			}
			while (y = n.currentSelectedArr.pop()) {
				if (!domUtils.findParentByTagName(y, "table")) {
					continue;
				}
				var r = y.parentNode, H = r.parentNode.parentNode, A = c(y), B = H.rows, t = r.cells, C = r.rowIndex;
				var u;
				for ( var M = 0; M < B.length;) {
					var s = B[M].cells[A];
					if (k(s)) {
						var I = B[s.getAttribute("rootRowIndex")].cells[s
								.getAttribute("rootCellIndex")];
						u = I.rowSpan;
						for ( var G = 0; G < I.rowSpan; G++) {
							var K = B[M + G].cells[A];
							domUtils.remove(K);
						}
						I.colSpan--;
						M += u;
					} else {
						if (s.colSpan == 1) {
							u = s.rowSpan;
							for ( var G = M, F = M + s.rowSpan; G < F; G++) {
								domUtils.remove(B[G].cells[A]);
							}
							M += u;
						} else {
							var w = B[M].cells[A + 1];
							w.style.display = "";
							w.rowSpan = s.rowSpan;
							w.colSpan = s.colSpan - 1;
							M += s.rowSpan;
							domUtils.remove(s);
						}
					}
				}
				var L, E, J;
				if (A == t.length) {
					if (A == 0) {
						l(H, D);
						return;
					}
					var z = A - 1;
					L = B[C].cells[z];
					E = k(L) ? B[L.getAttribute("rootRowIndex")].cells[L
							.getAttribute("rootCellIndex")] : L;
				} else {
					J = B[C].cells[A];
					E = k(J) ? B[J.getAttribute("rootRowIndex")].cells[J
							.getAttribute("rootCellIndex")] : J;
				}
			}
			D.setStart(E, 0).setCursor();
			h(H);
		}
	};
	n.commands["splittocells"] = {
		queryCommandState : function() {
			if (this.highlight) {
				return -1;
			}
			var r = this.selection.getRange(), t = r.startContainer, s = domUtils
					.findParentByTagName(t, [ "td", "th" ], true);
			return s
					&& (s.rowSpan > 1 || s.colSpan > 1)
					&& (!n.currentSelectedArr.length || b(n.currentSelectedArr) == 1) ? 0
					: -1;
		},
		execCommand : function() {
			var v = this.selection.getRange(), r = v.startContainer, s = domUtils
					.findParentByTagName(r, [ "td", "th" ], true), x = s.parentNode, C = x.parentNode.parentNode;
			var A = x.rowIndex, z = c(s), y = s.rowSpan, w = s.colSpan;
			for ( var u = 0; u < y; u++) {
				for ( var t = 0; t < w; t++) {
					var B = C.rows[A + u].cells[z + t];
					B.rowSpan = 1;
					B.colSpan = 1;
					if (k(B)) {
						B.style.display = "";
						B.innerHTML = browser.ie ? "" : "<br/>";
					}
				}
			}
		}
	};
	n.commands["splittorows"] = {
		queryCommandState : function() {
			if (this.highlight) {
				return -1;
			}
			var r = this.selection.getRange(), t = r.startContainer, s = domUtils
					.findParentByTagName(t, "td", true)
					|| n.currentSelectedArr[0];
			return s
					&& (s.rowSpan > 1)
					&& (!n.currentSelectedArr.length || b(n.currentSelectedArr) == 1) ? 0
					: -1;
		},
		execCommand : function() {
			var v = this.selection.getRange(), r = v.startContainer, s = domUtils
					.findParentByTagName(r, "td", true)
					|| n.currentSelectedArr[0], x = s.parentNode, D = x.parentNode.parentNode.rows;
			var A = x.rowIndex, z = c(s), y = s.rowSpan, w = s.colSpan;
			for ( var u = 0; u < y; u++) {
				var C = D[A + u], B = C.cells[z];
				B.rowSpan = 1;
				B.colSpan = w;
				if (k(B)) {
					B.style.display = "";
					B.innerHTML = browser.ie ? "" : "<br/>";
				}
				for ( var t = z + 1; t < z + w; t++) {
					B = C.cells[t];
					B.setAttribute("rootRowIndex", A + u);
				}
			}
			f(n.currentSelectedArr);
			this.selection.getRange().setStart(s, 0).setCursor();
		}
	};
	n.commands["insertparagraphbeforetable"] = {
		queryCommandState : function() {
			if (this.highlight) {
				return -1;
			}
			var r = this.selection.getRange(), t = r.startContainer, s = domUtils
					.findParentByTagName(t, "td", true)
					|| n.currentSelectedArr[0];
			return s && domUtils.findParentByTagName(s, "table") ? 0 : -1;
		},
		execCommand : function() {
			var r = this.selection.getRange(), t = r.startContainer, s = domUtils
					.findParentByTagName(t, "table", true);
			t = n.document.createElement(n.options.enterTag);
			s.parentNode.insertBefore(t, s);
			f(n.currentSelectedArr);
			if (t.tagName == "P") {
				t.innerHTML = browser.ie ? "" : "<br/>";
				r.setStart(t, 0);
			} else {
				r.setStartBefore(t);
			}
			r.setCursor();
		}
	};
	n.commands["splittocols"] = {
		queryCommandState : function() {
			if (this.highlight) {
				return -1;
			}
			var r = this.selection.getRange(), t = r.startContainer, s = domUtils
					.findParentByTagName(t, [ "td", "th" ], true)
					|| n.currentSelectedArr[0];
			return s
					&& (s.colSpan > 1)
					&& (!n.currentSelectedArr.length || b(n.currentSelectedArr) == 1) ? 0
					: -1;
		},
		execCommand : function() {
			var v = this.selection.getRange(), r = v.startContainer, s = domUtils
					.findParentByTagName(r, [ "td", "th" ], true)
					|| n.currentSelectedArr[0], x = s.parentNode, D = x.parentNode.parentNode.rows;
			var A = x.rowIndex, z = c(s), y = s.rowSpan, w = s.colSpan;
			for ( var u = 0; u < w; u++) {
				var C = D[A].cells[z + u];
				C.rowSpan = y;
				C.colSpan = 1;
				if (k(C)) {
					C.style.display = "";
					C.innerHTML = browser.ie ? "" : "<br/>";
				}
				for ( var t = A + 1; t < A + y; t++) {
					var B = D[t].cells[z + u];
					B.setAttribute("rootCellIndex", z + u);
				}
			}
			f(n.currentSelectedArr);
			this.selection.getRange().setStart(s, 0).setCursor();
		}
	};
	n.commands["insertrow"] = {
		queryCommandState : function() {
			if (this.highlight) {
				return -1;
			}
			var r = this.selection.getRange();
			return domUtils
					.findParentByTagName(r.startContainer, "table", true)
					|| domUtils.findParentByTagName(r.endContainer, "table",
							true) || n.currentSelectedArr.length != 0 ? 0 : -1;
		},
		execCommand : function() {
			var w = this.selection.getRange(), r = w.startContainer, y = domUtils
					.findParentByTagName(r, "tr", true)
					|| n.currentSelectedArr[0].parentNode, B = y.parentNode.parentNode, E = B.rows;
			var z = y.rowIndex, C = E[z].cells;
			var D = B.insertRow(z);
			var u;
			for ( var x = 0; x < C.length;) {
				var A = C[x];
				if (k(A)) {
					var v = E[A.getAttribute("rootRowIndex")].cells[A
							.getAttribute("rootCellIndex")];
					v.rowSpan++;
					for ( var t = 0; t < v.colSpan; t++) {
						u = A.cloneNode(false);
						domUtils.removeAttributes(u, [ "dateClass", "bgColor", "valign",
								"align" ]);
						u.rowSpan = u.colSpan = 1;
						u.innerHTML = browser.ie ? "" : "<br/>";
						u.className = "";
						if (D.children[x + t]) {
							D.insertBefore(u, D.children[x + t]);
						} else {
							D.appendChild(u);
						}
						u.style.display = "none";
					}
					x += v.colSpan;
				} else {
					for ( var s = 0; s < A.colSpan; s++) {
						u = A.cloneNode(false);
						domUtils.removeAttributes(u, [ "dateClass", "bgColor", "valign",
								"align" ]);
						u.rowSpan = u.colSpan = 1;
						u.innerHTML = browser.ie ? "" : "<br/>";
						u.className = "";
						if (D.children[x + s]) {
							D.insertBefore(u, D.children[x + s]);
						} else {
							D.appendChild(u);
						}
					}
					x += A.colSpan;
				}
			}
			h(B);
			w.setStart(D.cells[0], 0).setCursor();
			f(n.currentSelectedArr);
		}
	};
	n.commands["insertcol"] = {
		queryCommandState : function() {
			if (this.highlight) {
				return -1;
			}
			var r = this.selection.getRange();
			return domUtils
					.findParentByTagName(r.startContainer, "table", true)
					|| domUtils.findParentByTagName(r.endContainer, "table",
							true) || n.currentSelectedArr.length != 0 ? 0 : -1;
		},
		execCommand : function() {
			var w = this.selection.getRange(), r = w.startContainer, s = domUtils
					.findParentByTagName(r, [ "td", "th" ], true)
					|| n.currentSelectedArr[0], C = domUtils
					.findParentByTagName(s, "table"), D = C.rows;
			var y = c(s), v;
			for ( var A = 0; A < D.length;) {
				var B = D[A].cells[y], x;
				if (k(B)) {
					var z = D[B.getAttribute("rootRowIndex")].cells[B
							.getAttribute("rootCellIndex")];
					z.colSpan++;
					for ( var u = 0; u < z.rowSpan; u++) {
						v = s.cloneNode(false);
						domUtils.removeAttributes(v, [ "dateClass", "bgColor", "valign",
								"align" ]);
						v.rowSpan = v.colSpan = 1;
						v.innerHTML = browser.ie ? "" : "<br/>";
						v.className = "";
						x = D[A + u];
						if (x.children[y]) {
							x.insertBefore(v, x.children[y]);
						} else {
							x.appendChild(v);
						}
						v.style.display = "none";
					}
					A += z.rowSpan;
				} else {
					for ( var t = 0; t < B.rowSpan; t++) {
						v = s.cloneNode(false);
						domUtils.removeAttributes(v, [ "dateClass", "bgColor", "valign",
								"align" ]);
						v.rowSpan = v.colSpan = 1;
						v.innerHTML = browser.ie ? "" : "<br/>";
						v.className = "";
						x = D[A + t];
						if (x.children[y]) {
							x.insertBefore(v, x.children[y]);
						} else {
							x.appendChild(v);
						}
						v.innerHTML = browser.ie ? "" : "<br/>";
					}
					A += B.rowSpan;
				}
			}
			h(C);
			w.setStart(D[0].cells[y], 0).setCursor();
			f(n.currentSelectedArr);
		}
	};
	n.commands["mergecells"] = {
		queryCommandState : function() {
			if (this.highlight) {
				return -1;
			}
			var t = 0;
			for ( var r = 0, s; s = this.currentSelectedArr[r++];) {
				if (!k(s)) {
					t++;
				}
			}
			return t > 1 ? 0 : -1;
		},
		execCommand : function() {
			var s = n.currentSelectedArr[0], v = n.currentSelectedArr[n.currentSelectedArr.length - 1], C = domUtils
					.findParentByTagName(s, "table"), E = C.rows, r = {
				beginRowIndex : s.parentNode.rowIndex,
				beginCellIndex : c(s),
				endRowIndex : v.parentNode.rowIndex,
				endCellIndex : c(v)
			}, B = r.beginRowIndex, u = r.beginCellIndex, t = r.endRowIndex
					- r.beginRowIndex + 1, A = r.endCellIndex
					- r.beginCellIndex + 1, y = E[B].cells[u];
			for ( var x = 0, z; (z = E[B + x++]) && x <= t;) {
				for ( var w = 0, D; (D = z.cells[u + w++]) && w <= A;) {
					if (x == 1 && w == 1) {
						D.style.display = "";
						D.rowSpan = t;
						D.colSpan = A;
					} else {
						D.style.display = "none";
						D.rowSpan = 1;
						D.colSpan = 1;
						D.setAttribute("rootRowIndex", B);
						D.setAttribute("rootCellIndex", u);
						i(y, D);
					}
				}
			}
			this.selection.getRange().setStart(y, 0).setCursor();
			browser.ie && domUtils.removeAttributes(y, [ "width", "height" ]);
			f(n.currentSelectedArr);
		}
	};
	function i(s, r) {
		if (m(r)) {
			return;
		}
		if (m(s)) {
			s.innerHTML = r.innerHTML;
			return;
		}
		var t = s.lastChild;
		if (t.nodeType != 1 || t.tagName != "BR") {
			s.appendChild(s.ownerDocument.createElement("br"));
		}
		while (t = r.firstChild) {
			s.appendChild(t);
		}
	}
	function e(v, u) {
		var E = v.parentNode, D = u.parentNode, F = E.rowIndex, t = D.rowIndex, B = E.parentNode.parentNode.rows, O = B.length, s = B[0].cells.length, I = c(v), P = c(u);
		if (v == u) {
			return {
				beginRowIndex : F,
				beginCellIndex : I,
				endRowIndex : F + v.rowSpan - 1,
				endCellIndex : P + v.colSpan - 1
			};
		}
		var K = Math.min(F, t), C = Math.min(I, P), r = Math.max(F + v.rowSpan
				- 1, t + u.rowSpan - 1), J = Math.max(I + v.colSpan - 1, P
				+ u.colSpan - 1);
		while (1) {
			var H = K, M = C, N = r, y = J;
			if (K > 0) {
				for (z = C; z <= J;) {
					var w = B[K].cells[z];
					if (k(w)) {
						K = w.getAttribute("rootRowIndex");
						w = B[w.getAttribute("rootRowIndex")].cells[w
								.getAttribute("rootCellIndex")];
					}
					z = c(w) + (w.colSpan || 1);
				}
			}
			if (C > 0) {
				for ( var A = K; A <= r;) {
					var L = B[A].cells[C];
					if (k(L)) {
						C = L.getAttribute("rootCellIndex");
						L = B[L.getAttribute("rootRowIndex")].cells[L
								.getAttribute("rootCellIndex")];
					}
					A = L.parentNode.rowIndex + (L.rowSpan || 1);
				}
			}
			if (r < O) {
				for ( var z = C; z <= J;) {
					var x = B[r].cells[z];
					if (k(x)) {
						x = B[x.getAttribute("rootRowIndex")].cells[x
								.getAttribute("rootCellIndex")];
					}
					r = x.parentNode.rowIndex + x.rowSpan - 1;
					z = c(x) + (x.colSpan || 1);
				}
			}
			if (J < s) {
				for (A = K; A <= r;) {
					var G = B[A].cells[J];
					if (k(G)) {
						G = B[G.getAttribute("rootRowIndex")].cells[G
								.getAttribute("rootCellIndex")];
					}
					J = c(G) + G.colSpan - 1;
					A = G.parentNode.rowIndex + (G.rowSpan || 1);
				}
			}
			if (M == C && y == J && N == r && H == K) {
				break;
			}
		}
		return {
			beginRowIndex : K,
			beginCellIndex : C,
			endRowIndex : r,
			endCellIndex : J
		};
	}
	function d(s, r) {
		o = r.target || r.srcElement;
		if (n.queryCommandState("highlightcode")
				|| domUtils.findParent(o, function(t) {
					return t.tagName == "DIV" && /highlighter/.test(t.id);
				})) {
			return;
		}
		if (r.button == 2) {
			return;
		}
		n.document.body.style.webkitUserSelect = "";
		f(n.currentSelectedArr);
		domUtils.clearSelectedArr(n.currentSelectedArr);
		if (o.tagName !== "TD") {
			o = domUtils.findParentByTagName(o, "td") || o;
		}
		if (o.tagName == "TD") {
			n.addListener("mouseover",
					function(u, t) {
						var v = t.target || t.srcElement;
						a.call(n, v);
						t.preventDefault ? t.preventDefault()
								: (t.returnValue = false);
					});
		} else {
			j();
		}
	}
	function a(t) {
		if (o
				&& t.tagName == "TD"
				&& domUtils.findParentByTagName(o, "table") == domUtils
						.findParentByTagName(t, "table")) {
			n.document.body.style.webkitUserSelect = "none";
			var s = t.parentNode.parentNode.parentNode;
			n.selection.getNative()[browser.ie ? "empty" : "removeAllRanges"]();
			var r = e(o, t);
			p(s, r);
		}
	}
	function p(u, t) {
		var v = u.rows;
		f(n.currentSelectedArr);
		for ( var s = t.beginRowIndex; s <= t.endRowIndex; s++) {
			for ( var r = t.beginCellIndex; r <= t.endCellIndex; r++) {
				var w = v[s].cells[r];
				w.className = "selectTdClass";
				n.currentSelectedArr.push(w);
			}
		}
	}
	function h(B) {
		var x = B.getElementsByTagName("td"), A, z, C = B.rows;
		for ( var v = 0, t; t = x[v++];) {
			if (!k(t)) {
				A = t.parentNode.rowIndex;
				z = c(t);
				for ( var s = 0; s < t.rowSpan; s++) {
					var y = s == 0 ? 1 : 0;
					for (; y < t.colSpan; y++) {
						var w = C[A + s].children[z + y];
						w.setAttribute("rootRowIndex", A);
						w.setAttribute("rootCellIndex", z);
					}
				}
			}
			if (!k(t)) {
				domUtils.removeAttributes(t,
						[ "rootRowIndex", "rootCellIndex" ]);
			}
			if (t.colSpan && t.colSpan == 1) {
				t.removeAttribute("colSpan");
			}
			if (t.rowSpan && t.rowSpan == 1) {
				t.removeAttribute("rowSpan");
			}
			var u;
			if (!k(t) && (u = t.style.width) && /%/.test(u)) {
				t.style.width = Math.floor(100 / t.parentNode.cells.length)
						+ "%";
			}
		}
	}
	n.adjustTable = function(H) {
		var F = H.getElementsByTagName("table");
		for ( var z = 0, u; u = F[z++];) {
			if (u.getAttribute("align")) {
				var A = u.nextSibling;
				while (A) {
					if (domUtils.isBlockElm(A)) {
						break;
					}
					A = A.nextSibling;
				}
				if (A) {
					A.style.clear = "both";
				}
			}
			u.removeAttribute("_innerCreateTable");
			var y = domUtils.getElementsByTagName(u, "td"), w, v;
			for ( var x = 0, t; t = y[x++];) {
				if (domUtils.isEmptyNode(t)) {
					t.innerHTML = browser.ie ? domUtils.fillChar : "<br/>";
				}
				var B = c(t), E = t.parentNode.rowIndex, G = domUtils
						.findParentByTagName(t, "table").rows;
				for ( var s = 0; s < t.rowSpan; s++) {
					var C = s == 0 ? 1 : 0;
					for (; C < t.colSpan; C++) {
						if (!w) {
							w = t.cloneNode(false);
							w.rowSpan = w.colSpan = 1;
							w.style.display = "none";
							w.innerHTML = browser.ie ? "" : "<br/>";
						} else {
							w = w.cloneNode(true);
						}
						w.setAttribute("rootRowIndex", t.parentNode.rowIndex);
						w.setAttribute("rootCellIndex", B);
						if (s == 0) {
							if (t.nextSibling) {
								t.parentNode.insertBefore(w, t.nextSibling);
							} else {
								t.parentNode.appendChild(w);
							}
						} else {
							v = G[E + s].children[B];
							if (v) {
								v.parentNode.insertBefore(w, v);
							} else {
								G[E + s].appendChild(w);
							}
						}
					}
				}
			}
			var D = domUtils.getComputedStyle(u, "border-width");
			if (D == "0px"
					&& u.style.border != "none"
					|| ((D == "" || D == "medium") && u.getAttribute("border") === "0")) {
				domUtils.addClass(u, "noBorderTable");
			}
		}
		n.fireEvent("afteradjusttable", H);
	};
};
UE.plugins["highlightcode"] = function() {
	var b = this;
	if (!/highlightcode/i.test(b.options.toolbars.join(""))) {
		return;
	}
	b.commands["highlightcode"] = {
		execCommand : function(m, g, i) {
			if (g && i) {
				var h = document.createElement("pre");
				h.className = "brush: " + i + ";toolbar:false;";
				h.style.display = "";
				h.appendChild(document.createTextNode(g));
				document.body.appendChild(h);
				if (b.queryCommandState("highlightcode")) {
					b.execCommand("highlightcode");
				}
				b.execCommand("inserthtml", SyntaxHighlighter.highlight(h,
						null, true), true);
				var d = b.document.getElementById(SyntaxHighlighter
						.getHighlighterDivId());
				d.setAttribute("highlighter", h.className);
				domUtils.remove(h);
				c();
			} else {
				var k = this.selection.getRange(), f = domUtils
						.findParentByTagName(k.startContainer, "table", true), j = domUtils
						.findParentByTagName(k.endContainer, "table", true), l;
				if (f
						&& j
						&& f === j
						&& f.parentNode.className.indexOf("syntaxhighlighter") > -1) {
					l = f.parentNode;
					if (domUtils.isBody(l.parentNode)) {
						var e = b.document.createElement("p");
						e.innerHTML = browser.ie ? "" : "<br/>";
						b.body.insertBefore(e, l);
						k.setStart(e, 0);
					} else {
						k.setStartBefore(l);
					}
					k.setCursor();
					domUtils.remove(l);
				}
			}
		},
		queryCommandState : function() {
			var e = this.selection.getRange(), f, d;
			e.adjustmentBoundary();
			f = domUtils.findParent(e.startContainer, function(g) {
				return g.nodeType == 1 && g.tagName == "DIV"
						&& domUtils.hasClass(g, "syntaxhighlighter");
			}, true);
			d = domUtils.findParent(e.endContainer, function(g) {
				return g.nodeType == 1 && g.tagName == "DIV"
						&& domUtils.hasClass(g, "syntaxhighlighter");
			}, true);
			return f && d && f == d ? 1 : 0;
		}
	};
	b.addListener("beforeselectionchange afterselectionchange", function(d) {
		b.highlight = /^b/.test(d) ? b.queryCommandState("highlightcode") : 0;
	});
	b.addListener("ready", function() {
		if (typeof XRegExp == "undefined") {
			utils.loadFile(document, {
				id : "syntaxhighlighter_js",
				src : b.options.highlightJsUrl || b.options.UEDITOR_HOME_URL
						+ "third-party/SyntaxHighlighter/shCore.js",
				tag : "script",
				type : "text/javascript",
				defer : "defer"
			}, function() {
				a();
			});
		}
		if (!b.document.getElementById("syntaxhighlighter_css")) {
			utils.loadFile(b.document, {
				id : "syntaxhighlighter_css",
				tag : "link",
				rel : "stylesheet",
				type : "text/css",
				href : b.options.highlightCssUrl || b.options.UEDITOR_HOME_URL
						+ "third-party/SyntaxHighlighter/shCoreDefault.css"
			});
		}
	});
	b
			.addListener(
					"beforegetcontent",
					function() {
						for ( var f = 0, h, g = domUtils.getElementsByTagName(
								b.body, "div"); h = g[f++];) {
							if (h.className == "container") {
								var d = h.parentNode;
								while (d) {
									if (d.tagName == "DIV"
											&& /highlighter/.test(d.id)) {
										break;
									}
									d = d.parentNode;
								}
								if (!d) {
									return;
								}
								var j = b.document.createElement("pre");
								for ( var k = [], l = 0, e; e = h.childNodes[l++];) {
									k.push(e[browser.ie ? "innerText"
											: "textContent"]);
								}
								j.appendChild(b.document.createTextNode(k
										.join("\n")));
								j.className = d.getAttribute("highlighter");
								d.parentNode.insertBefore(j, d);
								domUtils.remove(d);
							}
						}
					});
	b.addListener("aftergetcontent aftersetcontent", a);
	function c() {
		setTimeout(function() {
			var h = b.document.getElementById(SyntaxHighlighter
					.getHighlighterDivId());
			if (h) {
				var g = h.getElementsByTagName("td");
				for ( var f = 0, d, e; d = g[0].childNodes[f]; f++) {
					e = g[1].firstChild.childNodes[f];
					if (e) {
						e.style.height = d.style.height = e.offsetHeight + "px";
					}
				}
			}
		});
	}
	function a() {
		for ( var f = 0, l, j = domUtils
				.getElementsByTagName(b.document, "pre"); l = j[f++];) {
			if (l.className.indexOf("brush") > -1) {
				var h = document.createElement("pre"), d, k;
				h.className = l.className;
				h.style.display = "none";
				h.appendChild(document
						.createTextNode(l[browser.ie ? "innerText"
								: "textContent"]));
				document.body.appendChild(h);
				try {
					d = SyntaxHighlighter.highlight(h, null, true);
				} catch (g) {
					domUtils.remove(h);
					return;
				}
				k = b.document.createElement("div");
				k.innerHTML = d;
				k.firstChild.setAttribute("highlighter", h.className);
				l.parentNode.insertBefore(k.firstChild, l);
				domUtils.remove(h);
				domUtils.remove(l);
				c();
			}
		}
	}
	b
			.addListener(
					"getAllHtml",
					function(k, g) {
						var d = "";
						for ( var f = 0, e, j = domUtils.getElementsByTagName(
								b.document, "div"); e = j[f++];) {
							if (domUtils.hasClass(e, "syntaxhighlighter")) {
								if (!b.document
										.getElementById("syntaxhighlighter_css")) {
									d = '<link id="syntaxhighlighter_css" rel="stylesheet" type="text/css" href="'
											+ (b.options.highlightCssUrl || b.options.UEDITOR_HOME_URL
													+ 'third-party/SyntaxHighlighter/shCoreDefault.css"')
											+ " ></link>";
								}
								if (!b.window.XRegExp) {
									d += '<script id="syntaxhighlighter_js"  type="text/javascript" src="'
											+ (b.options.highlightJsUrl || b.options.UEDITOR_HOME_URL
													+ 'third-party/SyntaxHighlighter/shCore.js"')
											+ " ><\/script>"
											+ '<script type="text/javascript">window.onload = function(){SyntaxHighlighter.highlight();'
											+ "setTimeout(function(){"
											+ "for(var i=0,di;di=SyntaxHighlighter.highlightContainers[i++];){"
											+ 'var tds = di.getElementsByTagName("td");'
											+ "for(var j=0,li,ri;li=tds[0].childNodes[j];j++){"
											+ "ri = tds[1].firstChild.childNodes[j];"
											+ 'ri.style.height = li.style.height = ri.offsetHeight + "px";'
											+ "}" + "}},100)}<\/script>";
								}
								break;
							}
						}
						if (!d) {
							var h;
							if (h = b.document
									.getElementById("syntaxhighlighter_css")) {
								domUtils.remove(h);
							}
							if (h = b.document
									.getElementById("syntaxhighlighter_js")) {
								domUtils.remove(h);
							}
						}
						g.html = d;
					});
	b.addListener("fullscreenchanged", function() {
		var l = domUtils.getElementsByTagName(b.document, "div");
		for ( var f = 0, k; k = l[f++];) {
			if (/^highlighter/.test(k.id)) {
				var h = k.getElementsByTagName("td");
				for ( var g = 0, d, e; d = h[0].childNodes[g]; g++) {
					e = h[1].firstChild.childNodes[g];
					e.style.height = d.style.height = e.offsetHeight + "px";
				}
			}
		}
	});
};
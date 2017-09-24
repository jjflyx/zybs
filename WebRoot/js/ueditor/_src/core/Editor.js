(function() {
	var d = 0, b;
	function c(m) {
		var l = m.getElementsByTagName("img"), k;
		for (var j = 0, h; h = l[j++];) {
			if (k = h.getAttribute("orgSrc")) {
				h.src = k;
				h.removeAttribute("orgSrc");
			}
		}
		var g = m.getElementsByTagName("a");
		for (var j = 0, f; f = g[j++]; j++) {
			if (f.getAttribute("data_ue_src")) {
				f.setAttribute("href", f.getAttribute("data_ue_src"));
			}
		}
	}
	function e(l, k) {
		var f;
		if (k.textarea) {
			if (utils.isString(k.textarea)) {
				for (var h = 0, j, g = domUtils.getElementsByTagName(l,
						"textarea"); j = g[h++];) {
					if (j.id == "ueditor_textarea_" + k.options.textarea) {
						f = j;
						break;
					}
				}
			} else {
				f = k.textarea;
			}
		}
		if (!f) {
			l.appendChild(f = domUtils.creElm(document, "textarea", {
				"name" : k.options.textarea,
				"id" : "ueditor_textarea_" + k.options.textarea,
				"style" : "display:none"
			}));
		}
		f.value = k.options.allHtmlEnabled ? k.getAllHtml() : k.getContent(
				null, null, true);
	}
	var a = UE.Editor = function(f) {
		var g = this;
		g.uid = d++;
		EventBase.call(g);
		g.commands = {};
		g.options = utils.extend(f || {}, UEDITOR_CONFIG, true);
		g.setOpt({
			isShow : true,
			initialContent : "",
			autoClearinitialContent : false,
			iframeCssUrl : g.options.UEDITOR_HOME_URL
					+ "/themes/default/iframe.css",
			textarea : "editorValue",
			focus : false,
			minFrameHeight : 320,
			autoClearEmptyNode : true,
			fullscreen : false,
			readonly : false,
			zIndex : 999,
			imagePopup : true,
			enterTag : "p",
			pageBreakTag : "_baidu_page_break_tag_",
			customDomain : false,
			lang : "zh-cn",
			langPath : g.options.UEDITOR_HOME_URL + "lang/",
			allHtmlEnabled : false
		});
		utils.loadFile(document, {
			src : g.options.langPath + g.options.lang + "/" + g.options.lang
					+ ".js",
			tag : "script",
			type : "text/javascript",
			defer : "defer"
		}, function() {
			for ( var h in UE.plugins) {
				UE.plugins[h].call(g);
			}
			g.langIsReady = true;
			g.fireEvent("langReady");
		});
		UE.instants["ueditorInstant" + g.uid] = g;
	};
	a.prototype = {
		ready : function(f) {
			var g = this;
			if (f) {
				g.isReady ? f.apply(g) : g.addListener("ready", f);
			}
		},
		setOpt : function(f, h) {
			var g = {};
			if (utils.isString(f)) {
				g[f] = h;
			} else {
				g = f;
			}
			utils.extend(this.options, g, true);
		},
		destroy : function() {
			var f = this;
			f.fireEvent("destroy");
			f.container.innerHTML = "";
			domUtils.remove(f.container);
			for ( var g in f) {
				if (f.hasOwnProperty(g)) {
					delete this[g];
				}
			}
		},
		render : function(g) {
			var j = this, h = j.options;
			if (g.constructor === String) {
				g = document.getElementById(g);
			}
			if (g) {
				var f = ie && browser.version < 9, i = (ie
						&& browser.version < 9 ? "" : "<!DOCTYPE html>")
						+ "<html xmlns='http://www.w3.org/1999/xhtml'"
						+ (!f ? " class='view'" : "")
						+ "><head>"
						+ (h.iframeCssUrl ? "<link rel='stylesheet' type='text/css' href='"
								+ utils.unhtml(h.iframeCssUrl) + "'/>"
								: "")
						+ "<style type='text/css'>"
						+ ".selectTdClass{background-color:#3399FF !important;}"
						+ "table.noBorderTable td{border:1px dashed #ddd !important}"
						+ "table{clear:both;margin-bottom:10px;border-collapse:collapse;word-break:break-all;}"
						+ ".pagebreak{display:block;clear:both !important;cursor:default !important;width: 100% !important;margin:0;}"
						+ ".anchorclass{background: url('"
						+ j.options.UEDITOR_HOME_URL
						+ "themes/default/images/anchor.gif') no-repeat scroll left center transparent;border: 1px dotted #0000FF;cursor: auto;display: inline-block;height: 16px;width: 15px;}"
						+ ".view{padding:0;word-wrap:break-word;cursor:text;height:100%;}\n"
						+ "body{margin:8px;font-family:'宋体';font-size:16px;}"
						+ "li{clear:both}"
						+ "p{margin:5px 0;}"
						+ (h.initialStyle || "")
						+ "</style></head><body"
						+ (f ? " class='view'" : "") + "></body>";
				if (h.customDomain && document.domain != location.hostname) {
					i += "<script>window.parent.UE.instants['ueditorInstant"
							+ j.uid + "']._setup(document);<\/script></html>";
					g
							.appendChild(domUtils
									.creElm(
											document,
											"iframe",
											{
												id : "baidu_editor_" + j.uid,
												width : "100%",
												height : "100%",
												frameborder : "0",
												src : 'javascript:void(function(){document.open();document.domain="'
														+ document.domain
														+ '";'
														+ 'document.write("'
														+ i
														+ '");document.close();}())'
											}));
				} else {
					g.innerHTML = '<iframe id="'
							+ "baidu_editor_"
							+ this.uid
							+ '"'
							+ 'width="100%" height="100%" scroll="no" frameborder="0" ></iframe>';
					var k = g.firstChild.contentWindow.document;
					!browser.webkit && k.open();
					k.write(i + "</html>");
					!browser.webkit && k.close();
					j._setup(k);
				}
				g.style.overflow = "hidden";
			}
		},
		_setup : function(j) {
			var i = this, f = i.options;
			if (ie) {
				j.body.disabled = true;
				j.body.contentEditable = true;
				j.body.disabled = false;
			} else {
				j.body.contentEditable = true;
				j.body.spellcheck = false;
			}
			i.document = j;
			i.window = j.defaultView || j.parentWindow;
			i.iframe = i.window.frameElement;
			i.body = j.body;
			i.setHeight(f.minFrameHeight);
			i.selection = new dom.Selection(j);
			var k;
			if (browser.gecko && (k = this.selection.getNative())) {
				k.removeAllRanges();
			}
			this._initEvents();
			if (f.initialContent) {
				if (f.autoClearinitialContent) {
					var g = i.execCommand;
					i.execCommand = function() {
						i.fireEvent("firstBeforeExecCommand");
						g.apply(i, arguments);
					};
					this.setDefaultContent(f.initialContent);
				} else {
					this.setContent(f.initialContent, true);
				}
			}
			for (var h = this.iframe.parentNode; !domUtils.isBody(h); h = h.parentNode) {
				if (h.tagName == "FORM") {
					domUtils.on(h, "submit", function() {
						e(this, i);
					});
					break;
				}
			}
			if (domUtils.isEmptyNode(i.body)) {
				i.body.innerHTML = "<p>" + (browser.ie ? "" : "<br/>") + "</p>";
			}
			if (f.focus) {
				setTimeout(function() {
					i.focus();
					!i.options.autoClearinitialContent && i._selectionChange();
				});
			}
			if (!i.container) {
				i.container = this.iframe.parentNode;
			}
			if (f.fullscreen && i.ui) {
				i.ui.setFullScreen(true);
			}
			i.isReady = 1;
			i.fireEvent("ready");
			if (!browser.ie) {
				domUtils.on(i.window, [ "blur", "focus" ], function(l) {
					if (l.type == "blur") {
						i._bakRange = i.selection.getRange();
						try {
							i.selection.getNative().removeAllRanges();
						} catch (l) {
						}
					} else {
						try {
							i._bakRange && i._bakRange.select();
						} catch (l) {
						}
					}
				});
			}
			if (browser.gecko && browser.version <= 10902) {
				i.body.contentEditable = false;
				setTimeout(function() {
					i.body.contentEditable = true;
				}, 100);
				setInterval(function() {
					i.body.style.height = i.iframe.offsetHeight - 20 + "px";
				}, 100);
			}
			!f.isShow && i.setHide();
			f.readonly && i.setDisabled();
		},
		sync : function(h) {
			var g = this, f = h ? document.getElementById(h) : domUtils
					.findParent(g.iframe.parentNode, function(i) {
						return i.tagName == "FORM";
					}, true);
			f && e(f, g);
		},
		setHeight : function(f) {
			if (f !== parseInt(this.iframe.parentNode.style.height)) {
				this.iframe.parentNode.style.height = f + "px";
			}
			this.document.body.style.height = f - 20 + "px";
		},
		getContent : function(l, i, f) {
			var k = this;
			if (l && utils.isFunction(l)) {
				i = l;
				l = "";
			}
			if (i ? !i() : !this.hasContents()) {
				return "";
			}
			k.fireEvent("beforegetcontent", l);
			var h = new RegExp(domUtils.fillChar, "g"), g = k.body.innerHTML
					.replace(h, "").replace(/>[\t\r\n]*?</g, "><");
			k.fireEvent("aftergetcontent", l);
			if (k.serialize) {
				var j = k.serialize.parseHTML(g);
				j = k.serialize.transformOutput(j);
				g = k.serialize.toHTML(j);
			}
			if (ie && f) {
				g = g.replace(/<p>\s*?<\/p>/g, "<p>&nbsp;</p>");
			} else {
				g = g
						.replace(
								/(&nbsp;)+/g,
								function(o) {
									for (var n = 0, p = [], m = o.split(";").length - 1; n < m; n++) {
										p.push(n % 2 == 0 ? " " : "&nbsp;");
									}
									return p.join("");
								});
			}
			return g;
		},
		getAllHtml : function() {
			var h = this, g = {
				html : ""
			}, f = "";
			h.fireEvent("getAllHtml", g);
			return "<html><head>"
					+ (h.options.charset ? '<meta http-equiv="Content-Type" content="text/html; charset='
							+ h.options.charset + '"/>'
							: "")
					+ h.document.getElementsByTagName("head")[0].innerHTML
					+ g.html + "</head>" + "<body "
					+ (ie && browser.version < 9 ? 'class="view"' : "") + ">"
					+ h.getContent(null, null, true) + "</body></html>";
		},
		getPlainTxt : function() {
			var g = new RegExp(domUtils.fillChar, "g"), f = this.body.innerHTML
					.replace(/[\n\r]/g, "");
			f = f.replace(/<(p|div)[^>]*>(<br\/?>|&nbsp;)<\/\1>/gi, "\n")
					.replace(/<br\/?>/gi, "\n").replace(/<[^>/]+>/g, "")
					.replace(/(\n)?<\/([^>]+)>/g, function(i, h, j) {
						return dtd.$block[j] ? "\n" : h ? h : "";
					});
			return f.replace(g, "").replace(/\u00a0/g, " ").replace(/&nbsp;/g,
					" ");
		},
		getContentTxt : function() {
			var f = new RegExp(domUtils.fillChar, "g");
			return this.body[browser.ie ? "innerText" : "textContent"].replace(
					f, "").replace(/\u00a0/g, " ");
		},
		setContent : function(l, j) {
			var o = this, n = utils.extend({
				a : 1,
				A : 1
			}, dtd.$inline, true), k;
			l = l.replace(/^[ \t\r\n]*?</, "<").replace(/>[ \t\r\n]*?$/, ">")
					.replace(/>[\t\r\n]*?</g, "><").replace(
							/[\s\/]?(\w+)?>[ \t\r\n]*?<\/?(\w+)/gi,
							function(r, p, s) {
								if (p) {
									k = s;
								} else {
									p = k;
								}
								return !n[p] && !n[s] ? r.replace(
										/>[ \t\r\n]*?</, "><") : r;
							});
			o.fireEvent("beforesetcontent");
			var q = this.serialize;
			if (q) {
				var i = q.parseHTML(l);
				i = q.transformInput(i);
				i = q.filter(i);
				l = q.toHTML(i);
			}
			this.body.innerHTML = l.replace(new RegExp("[\r"
					+ domUtils.fillChar + "]*", "g"), "");
			if (browser.ie && browser.version < 7) {
				c(this.document.body);
			}
			if (o.options.enterTag == "p") {
				var g = this.body.firstChild, h;
				if (!g || g.nodeType == 1
						&& (dtd.$cdata[g.tagName] || domUtils.isCustomeNode(g))
						&& g === this.body.lastChild) {
					this.body.innerHTML = "<p>"
							+ (browser.ie ? "&nbsp;" : "<br/>") + "</p>"
							+ this.body.innerHTML;
				} else {
					var f = o.document.createElement("p");
					while (g) {
						while (g
								&& (g.nodeType == 3 || g.nodeType == 1
										&& dtd.p[g.tagName]
										&& !dtd.$cdata[g.tagName])) {
							h = g.nextSibling;
							f.appendChild(g);
							g = h;
						}
						if (f.firstChild) {
							if (!g) {
								o.body.appendChild(f);
								break;
							} else {
								o.body.insertBefore(f, g);
								f = o.document.createElement("p");
							}
						}
						g = g.nextSibling;
					}
				}
			}
			o.adjustTable && o.adjustTable(o.body);
			o.fireEvent("aftersetcontent");
			o.fireEvent("contentchange");
			!j && o._selectionChange();
			o._bakRange = o._bakIERange = null;
			var m;
			if (browser.gecko && (m = this.selection.getNative())) {
				m.removeAllRanges();
			}
		},
		focus : function(i) {
			try {
				var g = this, f = g.selection.getRange();
				if (i) {
					f.setStartAtLast(g.body.lastChild).setCursor(false, true);
				} else {
					f.select(true);
				}
			} catch (h) {
			}
		},
		_initEvents : function() {
			var g = this, k = g.document, j = g.window;
			g._proxyDomEvent = utils.bind(g._proxyDomEvent, g);
			domUtils.on(k, [ "click", "contextmenu", "mousedown", "keydown",
					"keyup", "keypress", "mouseup", "mouseover", "mouseout",
					"selectstart" ], g._proxyDomEvent);
			domUtils.on(j, [ "focus", "blur" ], g._proxyDomEvent);
			domUtils.on(k, [ "mouseup", "keydown" ], function(l) {
				if (l.type == "keydown"
						&& (l.ctrlKey || l.metaKey || l.shiftKey || l.altKey)) {
					return;
				}
				if (l.button == 2) {
					return;
				}
				g._selectionChange(250, l);
			});
			var f = 0, h = browser.ie ? g.body : g.document, i;
			domUtils.on(h, "dragstart", function() {
				f = 1;
			});
			domUtils.on(h, browser.webkit ? "dragover" : "drop", function() {
				return browser.webkit ? function() {
					clearTimeout(i);
					i = setTimeout(function() {
						if (!f) {
							var p = g.selection, l = p.getRange();
							if (l) {
								var m = l.getCommonAncestor();
								if (m && g.serialize) {
									var o = g.serialize, n = o.filter(o
											.transformInput(o.parseHTML(o
													.word(m.innerHTML))));
									m.innerHTML = o.toHTML(n);
								}
							}
						}
						f = 0;
					}, 200);
				} : function(l) {
					if (!f) {
						l.preventDefault ? l.preventDefault()
								: (l.returnValue = false);
					}
					f = 0;
				};
			}());
		},
		_proxyDomEvent : function(f) {
			return this.fireEvent(f.type.replace(/^on/, ""), f);
		},
		_selectionChange : function(i, f) {
			var l = this;
			var k = false;
			var j, h;
			if (browser.ie && browser.version < 9 && f && f.type == "mouseup") {
				var g = this.selection.getRange();
				if (!g.collapsed) {
					k = true;
					j = f.clientX;
					h = f.clientY;
				}
			}
			clearTimeout(b);
			b = setTimeout(
					function() {
						if (!l.selection.getNative()) {
							return;
						}
						var n;
						if (k && l.selection.getNative().type == "None") {
							n = l.document.body.createTextRange();
							try {
								n.moveToPoint(j, h);
							} catch (m) {
								n = null;
							}
						}
						var o;
						if (n) {
							o = l.selection.getIERange;
							l.selection.getIERange = function() {
								return n;
							};
						}
						l.selection.cache();
						if (o) {
							l.selection.getIERange = o;
						}
						if (l.selection._cachedRange
								&& l.selection._cachedStartElement) {
							l.fireEvent("beforeselectionchange");
							l.fireEvent("selectionchange", !!f);
							l.fireEvent("afterselectionchange");
							l.selection.clear();
						}
					}, i || 50);
		},
		_callCmdFn : function(j, g) {
			var f = g[0].toLowerCase(), i, h;
			i = this.commands[f] || UE.commands[f];
			h = i && i[j];
			if ((!i || !h) && j == "queryCommandState") {
				return 0;
			} else {
				if (h) {
					return h.apply(this, g);
				}
			}
		},
		execCommand : function(g) {
			g = g.toLowerCase();
			var h = this, f, i = h.commands[g] || UE.commands[g];
			if (!i || !i.execCommand) {
				return;
			}
			if (!i.notNeedUndo && !h.__hasEnterExecCommand) {
				h.__hasEnterExecCommand = true;
				if (h.queryCommandState(g) != -1) {
					h.fireEvent("beforeexeccommand", g);
					f = this._callCmdFn("execCommand", arguments);
					h.fireEvent("afterexeccommand", g);
				}
				h.__hasEnterExecCommand = false;
			} else {
				f = this._callCmdFn("execCommand", arguments);
			}
			h._selectionChange();
			return f;
		},
		queryCommandState : function(f) {
			return this._callCmdFn("queryCommandState", arguments);
		},
		queryCommandValue : function(f) {
			return this._callCmdFn("queryCommandValue", arguments);
		},
		hasContents : function(g) {
			if (g) {
				for (var j = 0, h; h = g[j++];) {
					if (this.document.getElementsByTagName(h).length > 0) {
						return true;
					}
				}
			}
			if (!domUtils.isEmptyBlock(this.body)) {
				return true;
			}
			g = [ "div" ];
			for (j = 0; h = g[j++];) {
				var f = domUtils.getElementsByTagName(this.document, h);
				for (var l = 0, k; k = f[l++];) {
					if (domUtils.isCustomeNode(k)) {
						return true;
					}
				}
			}
			return false;
		},
		reset : function() {
			this.fireEvent("reset");
		},
		setEnabled : function() {
			var g = this, f;
			if (g.body.contentEditable == "false") {
				g.body.contentEditable = true;
				f = g.selection.getRange();
				try {
					f.moveToBookmark(g.lastBk);
					delete g.lastBk;
				} catch (h) {
					f.setStartAtFirst(g.body).collapse(true);
				}
				f.select(true);
				if (g.bkqueryCommandState) {
					g.queryCommandState = g.bkqueryCommandState;
					delete g.bkqueryCommandState;
				}
				g.fireEvent("selectionchange");
			}
		},
		setDisabled : function(f) {
			var g = this;
			f = f ? utils.isArray(f) ? f : [ f ] : [];
			if (g.body.contentEditable == "true") {
				if (!g.lastBk) {
					g.lastBk = g.selection.getRange().createBookmark(true);
				}
				g.body.contentEditable = false;
				g.bkqueryCommandState = g.queryCommandState;
				g.queryCommandState = function(h) {
					if (utils.indexOf(f, h) != -1) {
						return g.bkqueryCommandState.apply(g, arguments);
					}
					return -1;
				};
				g.fireEvent("selectionchange");
			}
		},
		setDefaultContent : function() {
			function f() {
				var h = this;
				if (h.document.getElementById("initContent")) {
					h.document.body.innerHTML = "<p>" + (ie ? "" : "<br/>")
							+ "</p>";
					var g = h.selection.getRange();
					h.removeListener("firstBeforeExecCommand", f);
					h.removeListener("focus", f);
					setTimeout(function() {
						g.setStart(h.document.body.firstChild, 0)
								.collapse(true).select(true);
						h._selectionChange();
					});
				}
			}
			return function(g) {
				var h = this;
				h.document.body.innerHTML = '<p id="initContent">' + g + "</p>";
				if (browser.ie && browser.version < 7) {
					c(h.document.body);
				}
				h.addListener("firstBeforeExecCommand", f);
				h.addListener("focus", f);
			};
		}(),
		setShow : function() {
			var g = this, f = g.selection.getRange();
			if (g.container.style.display == "none") {
				try {
					f.moveToBookmark(g.lastBk);
					delete g.lastBk;
				} catch (h) {
					f.setStartAtFirst(g.body).collapse(true);
				}
				f.select(true);
				g.container.style.display = "";
			}
		},
		setHide : function() {
			var f = this;
			if (!f.lastBk) {
				f.lastBk = f.selection.getRange().createBookmark(true);
			}
			f.container.style.display = "none";
		},
		getLang : function(h) {
			var j = UE.I18N[this.options.lang];
			h = (h || "").split(".");
			for (var g = 0, f; f = h[g++];) {
				j = j[f];
				if (!j) {
					break;
				}
			}
			return j;
		},
		ifSourceMode : function() {
			var f = this.commands["source"];
			return f.queryCommandState();
		}
	};
	utils.inherits(a, EventBase);
})();
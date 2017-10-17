(function() {
	var b = baidu.editor.utils, d = baidu.editor.ui.uiUtils, e = baidu.editor.ui.UIBase, a = baidu.editor.dom.domUtils;
	function c(f) {
		this.initOptions(f);
		this.initEditorUI();
	}
	c.prototype = {
		uiName : "editor",
		initEditorUI : function() {
			this.editor.ui = this;
			this._dialogs = {};
			this.initUIBase();
			this._initToolbars();
			var g = this.editor, h = this;
			g
					.addListener(
							"ready",
							function() {
								g.getDialog = function(i) {
									return g.ui._dialogs[i + "Dialog"];
								};
								a.on(g.window, "scroll", function() {
									baidu.editor.ui.Popup.postHide();
								});
								if (g.options.elementPathEnabled) {
									g.ui.getDom("elementpath").innerHTML = '<div class="edui-editor-breadcrumb">'
											+ g.getLang("elementPathTip")
											+ ":</div>";
								}
								if (g.options.wordCount) {
									g.ui.getDom("wordcount").innerHTML = g
											.getLang("wordCountTip");
									g.addListener("keyup", function(j, i) {
										var k = i.keyCode || i.which;
										if (k == 32) {
											h._wordCount();
										}
									});
								}
								if (!g.options.elementPathEnabled
										&& !g.options.wordCount) {
									g.ui.getDom("elementpath").style.display = "none";
									g.ui.getDom("wordcount").style.display = "none";
								}
								if (!g.selection.isFocus()) {
									return;
								}
								g.fireEvent("selectionchange", false, true);
							});
			g.addListener("mousedown", function(j, i) {
				var k = i.target || i.srcElement;
				baidu.editor.ui.Popup.postHide(k);
			});
			g.addListener("contextmenu", function(j, i) {
				baidu.editor.ui.Popup.postHide();
			});
			g.addListener("selectionchange", function() {
				if (g.options.elementPathEnabled) {
					h[(g.queryCommandState("elementpath") == -1 ? "dis" : "en")
							+ "ableElementPath"]();
				}
				if (g.options.wordCount) {
					h[(g.queryCommandState("wordcount") == -1 ? "dis" : "en")
							+ "ableWordCount"]();
				}
			});
			var f = new baidu.editor.ui.Popup({
				editor : g,
				content : "",
				className : "edui-bubble",
				_onEditButtonClick : function() {
					this.hide();
					g.ui._dialogs.linkDialog.open();
				},
				_onImgEditButtonClick : function(i) {
					this.hide();
					g.ui._dialogs[i] && g.ui._dialogs[i].open();
				},
				_onImgSetFloat : function(i) {
					this.hide();
					g.execCommand("imagefloat", i);
				},
				_setIframeAlign : function(i) {
					var j = f.anchorEl;
					var k = j.cloneNode(true);
					switch (i) {
					case -2:
						k.setAttribute("align", "");
						break;
					case -1:
						k.setAttribute("align", "left");
						break;
					case 1:
						k.setAttribute("align", "right");
						break;
					case 2:
						k.setAttribute("align", "middle");
						break;
					}
					j.parentNode.insertBefore(k, j);
					a.remove(j);
					f.anchorEl = k;
					f.showAnchor(f.anchorEl);
				},
				_updateIframe : function() {
					g._iframe = f.anchorEl;
					g.ui._dialogs.insertframeDialog.open();
					f.hide();
				},
				_onRemoveButtonClick : function(i) {
					g.execCommand(i);
					this.hide();
				},
				queryAutoHide : function(i) {
					if (i && i.ownerDocument == g.document) {
						if (i.tagName.toLowerCase() == "img"
								|| a.findParentByTagName(i, "a", true)) {
							return i !== f.anchorEl;
						}
					}
					return baidu.editor.ui.Popup.prototype.queryAutoHide.call(
							this, i);
				},
				_onInputDelButtonClick : function() {
					if (confirm(g.getLang("confirmDelete"))) {
						this.hide();
						if (f.anchorEl) {
							f.anchorEl.parentNode.removeChild(f.anchorEl);
						}
					}
				},
				_onInputEditButtonClick : function() {
					this.hide();
					var j = f.anchorEl.childNodes[0];
					if (j) {
						var i = f.anchorEl.getAttribute("external");
						if (i) {
							g.curInputElement = f.anchorEl;
							g.execCommand(f.anchorEl.className, false);
							return;
						}
						switch (j.tagName.toLowerCase()) {
						case "a":
							g.curInput = f.anchorEl;
							g.ui._dialogs.addinputbutton.open();
							break;
						case "textarea":
							g.curOpinion = f.anchorEl;
							g.ui._dialogs.addopinion.open();
							break;
						}
					}
				},
				_onInputDialogButtonClick : function() {
					this.hide();
					var i = f.anchorEl.childNodes[0];
					if (i) {
						g.curInput = i;
						g.execCommand("customdialog");
					}
				}
			});
			f.render();
			if (g.options.imagePopup) {
				g
						.addListener(
								"mouseover",
								function(m, i) {
									i = i || window.event;
									var n = i.target || i.srcElement;
									if (!(n.tagName && n.parentElement)) {
										return;
									}
									var p = n.getAttribute("name") == "editable-input";
									var j = n.parentElement
											.getAttribute("name") == "editable-input";
									if (p || j) {
										var o = n.className.indexOf("extend") > -1
												|| n.parentElement.className
														.indexOf("extend") > -1;
										var k = ' <span onclick="$$._onInputDelButtonClick()" class="edui-clickable">'
												+ g.getLang("delete")
												+ "</span>&nbsp;&nbsp;";
										if (g.canEditColumnNameAndType) {
											k = "";
										}
										var l = f.formatHtml("<nobr>"
												+ g.getLang("property") + ":"
												+ k + "</nobr>");
										if (g.options.editorModel) {
											if (g.options.editorModel == "designModel") {
												l = f
														.formatHtml("<nobr>"
																+ g
																		.getLang("property")
																+ ":"
																+ k
																+ '<span onclick="$$._onInputEditButtonClick(event, this);" class="edui-clickable">'
																+ g
																		.getLang("modify")
																+ "</span>&nbsp;&nbsp;"
																+ "</nobr>");
											}
										}
										if (o) {
											l = f
													.formatHtml("<nobr>"
															+ g
																	.getLang("property")
															+ ":"
															+ k
															+ '<span onclick="$$._onInputEditButtonClick(event, this);" class="edui-clickable">'
															+ g
																	.getLang("modify")
															+ "</span>&nbsp;&nbsp;"
															+ '<span onclick="$$._onInputDialogButtonClick(event, this);" class="edui-clickable">'
															+ g
																	.getLang("customDialog")
															+ "</span></nobr>");
										}
										if (l) {
											f.getDom("content").innerHTML = l;
											f.anchorEl = p ? n
													: n.parentElement;
											f.showAnchor(f.anchorEl);
										} else {
											f.hide();
										}
									} else {
										if (g.ui._dialogs.insertframeDialog
												&& /iframe/ig.test(n.tagName)) {
											var l = f
													.formatHtml("<nobr>"
															+ g
																	.getLang("property")
															+ ': <span onclick=$$._setIframeAlign(-2) class="edui-clickable">'
															+ g
																	.getLang("default")
															+ '</span>&nbsp;&nbsp;<span onclick=$$._setIframeAlign(-1) class="edui-clickable">'
															+ g
																	.getLang("justifyleft")
															+ '</span>&nbsp;&nbsp;<span onclick=$$._setIframeAlign(1) class="edui-clickable">'
															+ g
																	.getLang("justifyright")
															+ "</span>&nbsp;&nbsp;"
															+ '<span onclick=$$._setIframeAlign(2) class="edui-clickable">'
															+ g
																	.getLang("justifycenter")
															+ "</span>"
															+ ' <span onclick="$$._updateIframe( this);" class="edui-clickable">'
															+ g
																	.getLang("modify")
															+ "</span></nobr>");
											if (l) {
												f.getDom("content").innerHTML = l;
												f.anchorEl = n;
												f.showAnchor(f.anchorEl);
											} else {
												f.hide();
											}
										}
									}
								});
				g
						.addListener(
								"selectionchange",
								function(l, j) {
									if (!j) {
										return;
									}
									var k = "", i = g.selection.getRange()
											.getClosedNode(), m = g.ui._dialogs;
									if (i && i.tagName == "IMG") {
										var n = "insertimageDialog";
										if (i.className
												.indexOf("edui-faked-video") != -1) {
											n = "insertvideoDialog";
										}
										if (i.className
												.indexOf("edui-faked-webapp") != -1) {
											n = "webappDialog";
										}
										if (i.src
												.indexOf("http://api.map.baidu.com") != -1) {
											n = "mapDialog";
										}
										if (i.src
												.indexOf("http://maps.google.com/maps/api/staticmap") != -1) {
											n = "gmapDialog";
										}
										if (i.getAttribute("anchorname")) {
											n = "anchorDialog";
											k = f
													.formatHtml("<nobr>"
															+ g
																	.getLang("property")
															+ ': <span onclick=$$._onImgEditButtonClick("anchorDialog") class="edui-clickable">'
															+ g
																	.getLang("modify")
															+ "</span>&nbsp;&nbsp;"
															+ "<span onclick=$$._onRemoveButtonClick('anchor') class=\"edui-clickable\">"
															+ g
																	.getLang("delete")
															+ "</span></nobr>");
										}
										if (i.getAttribute("word_img")) {
											g.word_img = [ i
													.getAttribute("word_img") ];
											n = "wordimageDialog";
										}
										if (!m[n]) {
											return;
										}
										!k
												&& (k = f
														.formatHtml("<nobr>"
																+ g
																		.getLang("property")
																+ ': <span onclick=$$._onImgSetFloat("none") class="edui-clickable">'
																+ g
																		.getLang("default")
																+ "</span>&nbsp;&nbsp;"
																+ '<span onclick=$$._onImgSetFloat("left") class="edui-clickable">'
																+ g
																		.getLang("justifyleft")
																+ "</span>&nbsp;&nbsp;"
																+ '<span onclick=$$._onImgSetFloat("right") class="edui-clickable">'
																+ g
																		.getLang("justifyright")
																+ "</span>&nbsp;&nbsp;"
																+ '<span onclick=$$._onImgSetFloat("center") class="edui-clickable">'
																+ g
																		.getLang("justifycenter")
																+ "</span>&nbsp;&nbsp;"
																+ "<span onclick=\"$$._onImgEditButtonClick('"
																+ n
																+ '\');" class="edui-clickable">'
																+ g
																		.getLang("modify")
																+ "</span></nobr>"));
									}
									if (k) {
										f.getDom("content").innerHTML = k;
										f.anchorEl = i || link;
										f.showAnchor(f.anchorEl);
									} else {
										f.hide();
									}
								});
			}
		},
		_initToolbars : function() {
			var n = this.editor;
			var p = this.toolbars || [];
			var h = [];
			for ( var m = 0; m < p.length; m++) {
				var o = p[m];
				var k = new baidu.editor.ui.Toolbar();
				for ( var l = 0; l < o.length; l++) {
					var f = o[l];
					var g = null;
					if (typeof f == "string") {
						f = f.toLowerCase();
						if (f == "|") {
							f = "Separator";
						}
						if (baidu.editor.ui[f]) {
							g = new baidu.editor.ui[f](n);
						}
						if (f == "fullscreen") {
							if (h && h[0]) {
								h[0].items.splice(0, 0, g);
							} else {
								g && k.items.splice(0, 0, g);
							}
							continue;
						}
					} else {
						g = f;
					}
					if (g) {
						k.add(g);
					}
				}
				h[m] = k;
			}
			this.toolbars = h;
		},
		getHtmlTpl : function() {
			return '<div id="##" class="%%">'
					+ '<div id="##_toolbarbox" class="%%-toolbarbox">'
					+ (this.toolbars.length ? '<div id="##_toolbarboxouter" class="%%-toolbarboxouter"><div class="%%-toolbarboxinner">'
							+ this.renderToolbarBoxHtml() + "</div></div>"
							: "")
					+ '<div id="##_toolbarmsg" class="%%-toolbarmsg" style="display:none;">'
					+ '<div id = "##_upload_dialog" class="%%-toolbarmsg-upload" onclick="$$.showWordImageDialog();">'
					+ this.editor.getLang("clickToUpload")
					+ "</div>"
					+ '<div class="%%-toolbarmsg-close" onclick="$$.hideToolbarMsg();">x</div>'
					+ '<div id="##_toolbarmsg_label" class="%%-toolbarmsg-label"></div>'
					+ '<div style="height:0;overflow:hidden;clear:both;"></div>'
					+ "</div>"
					+ "</div>"
					+ '<div id="##_iframeholder" class="%%-iframeholder"></div>'
					+ '<div id="##_bottombar" class="%%-bottomContainer"><table><tr>'
					+ '<td id="##_elementpath" class="%%-bottombar"></td>'
					+ '<td id="##_wordcount" class="%%-wordcount"></td>'
					+ "</tr></table></div>" + "</div>";
		},
		showWordImageDialog : function() {
			this.editor.execCommand("wordimage", "word_img");
			this._dialogs["wordimageDialog"].open();
		},
		renderToolbarBoxHtml : function() {
			var g = [];
			for ( var f = 0; f < this.toolbars.length; f++) {
				g.push(this.toolbars[f].renderHtml());
			}
			return g.join("");
		},
		setFullScreen : function(i) {
			if (this._fullscreen != i) {
				this._fullscreen = i;
				this.editor.fireEvent("beforefullscreenchange", i);
				var h = this.editor;
				if (baidu.editor.browser.gecko) {
					var f = h.selection.getRange().createBookmark();
				}
				if (i) {
					this._bakHtmlOverflow = document.documentElement.style.overflow;
					this._bakBodyOverflow = document.body.style.overflow;
					this._bakAutoHeight = this.editor.autoHeightEnabled;
					this._bakScrollTop = Math.max(
							document.documentElement.scrollTop,
							document.body.scrollTop);
					if (this._bakAutoHeight) {
						h.autoHeightEnabled = false;
						this.editor.disableAutoHeight();
					}
					document.documentElement.style.overflow = "hidden";
					document.body.style.overflow = "hidden";
					this._bakCssText = this.getDom().style.cssText;
					this._bakCssText1 = this.getDom("iframeholder").style.cssText;
					this._updateFullScreen();
				} else {
					this.getDom().style.cssText = this._bakCssText;
					this.getDom("iframeholder").style.cssText = this._bakCssText1;
					if (this._bakAutoHeight) {
						h.autoHeightEnabled = true;
						this.editor.enableAutoHeight();
					}
					document.documentElement.style.overflow = this._bakHtmlOverflow;
					document.body.style.overflow = this._bakBodyOverflow;
					window.scrollTo(0, this._bakScrollTop);
				}
				if (baidu.editor.browser.gecko) {
					var g = document.createElement("input");
					document.body.appendChild(g);
					h.body.contentEditable = false;
					setTimeout(function() {
						g.focus();
						setTimeout(function() {
							h.body.contentEditable = true;
							h.selection.getRange().moveToBookmark(f).select(
									true);
							baidu.editor.dom.domUtils.remove(g);
							i && window.scroll(0, 0);
						});
					});
				}
				this.editor.fireEvent("fullscreenchanged", i);
				this.triggerLayout();
			}
		},
		_wordCount : function() {
			var f = this.getDom("wordcount");
			if (!this.editor.options.wordCount) {
				f.style.display = "none";
				return;
			}
			f.innerHTML = this.editor.queryCommandValue("wordcount");
		},
		disableWordCount : function() {
			var f = this.getDom("wordcount");
			f.innerHTML = "";
			f.style.display = "none";
			this.wordcount = false;
		},
		enableWordCount : function() {
			var f = this.getDom("wordcount");
			f.style.display = "";
			this.wordcount = true;
			this._wordCount();
		},
		_updateFullScreen : function() {
			if (this._fullscreen) {
				var f = d.getViewportRect();
				this.getDom().style.cssText = "border:0;position:absolute;left:0;top:"
						+ (this.editor.options.topOffset || 0)
						+ "px;width:"
						+ f.width
						+ "px;height:"
						+ f.height
						+ "px;z-index:"
						+ (this.getDom().style.zIndex * 1 + 100);
				d.setViewportOffset(this.getDom(), {
					left : 0,
					top : this.editor.options.topOffset || 0
				});
				this.editor.setHeight(f.height
						- this.getDom("toolbarbox").offsetHeight
						- this.getDom("bottombar").offsetHeight
						- (this.editor.options.topOffset || 0));
			}
		},
		_updateElementPath : function() {
			var f = this.getDom("elementpath"), j;
			if (this.elementPathEnabled
					&& (j = this.editor.queryCommandValue("elementpath"))) {
				var k = [];
				for ( var h = 0, g; g = j[h]; h++) {
					k[h] = this
							.formatHtml('<span unselectable="on" onclick="$$.editor.execCommand(&quot;elementpath&quot;, &quot;'
									+ h + '&quot;);">' + g + "</span>");
				}
				f.innerHTML = '<div class="edui-editor-breadcrumb" onmousedown="return false;">'
						+ this.editor.getLang("elementPathTip")
						+ ": "
						+ k.join(" &gt; ") + "</div>";
			} else {
				f.style.display = "none";
			}
		},
		disableElementPath : function() {
			var f = this.getDom("elementpath");
			f.innerHTML = "";
			f.style.display = "none";
			this.elementPathEnabled = false;
		},
		enableElementPath : function() {
			var f = this.getDom("elementpath");
			f.style.display = "";
			this.elementPathEnabled = true;
			this._updateElementPath();
		},
		isFullScreen : function() {
			return this._fullscreen;
		},
		postRender : function() {
			e.prototype.postRender.call(this);
			for ( var h = 0; h < this.toolbars.length; h++) {
				this.toolbars[h].postRender();
			}
			var j = this;
			var g, f = baidu.editor.dom.domUtils, k = function() {
				clearTimeout(g);
				g = setTimeout(function() {
					j._updateFullScreen();
				});
			};
			f.on(window, "resize", k);
			j.addListener("destroy", function() {
				f.un(window, "resize", k);
				clearTimeout(g);
			});
		},
		showToolbarMsg : function(h, g) {
			this.getDom("toolbarmsg_label").innerHTML = h;
			this.getDom("toolbarmsg").style.display = "";
			if (!g) {
				var f = this.getDom("upload_dialog");
				f.style.display = "none";
			}
		},
		hideToolbarMsg : function() {
			this.getDom("toolbarmsg").style.display = "none";
		},
		mapUrl : function(f) {
			return f ? f.replace("~/", this.editor.options.UEDITOR_HOME_URL
					|| "") : "";
		},
		triggerLayout : function() {
			var f = this.getDom();
			if (f.style.zoom == "1") {
				f.style.zoom = "100%";
			} else {
				f.style.zoom = "1";
			}
		}
	};
	b.inherits(c, baidu.editor.ui.UIBase);
	baidu.editor.ui.Editor = function(f) {
		var h = new baidu.editor.Editor(f);
		h.options.editor = h;
		var g = h.render;
		h.render = function(i) {
			b
					.domReady(function() {
						h.langIsReady ? j() : h.addListener("langReady", j);
						function j() {
							h.setOpt({
								labelMap : h.options.labelMap
										|| UE.I18N[h.options.lang].labelMap
							});
							new c(h.options);
							if (i) {
								if (i.constructor === String) {
									i = document.getElementById(i);
								}
								i
										&& i.getAttribute("name")
										&& (h.options.textarea = i
												.getAttribute("name"));
								if (i && /script|textarea/ig.test(i.tagName)) {
									var m = document.createElement("div");
									i.parentNode.insertBefore(m, i);
									var k = i.value || i.innerHTML;
									h.options.initialContent = /^[\t\r\n ]*$/
											.test(k) ? h.options.initialContent
											: k
													.replace(
															/>[\n\r\t]+([ ]{4})+/g,
															">")
													.replace(
															/[\n\r\t]+([ ]{4})+</g,
															"<").replace(
															/>[\n\r\t]+</g,
															"><");
									i.id && (m.id = i.id);
									i.className && (m.className = i.className);
									i.style.cssText
											&& (m.style.cssText = i.style.cssText);
									if (/textarea/i.test(i.tagName)) {
										h.textarea = i;
										h.textarea.style.display = "none";
									} else {
										i.parentNode.removeChild(i);
									}
									i = m;
									i.innerHTML = "";
								}
							}
							h.ui.render(i);
							var l = h.ui.getDom("iframeholder");
							h.container = h.ui.getDom();
							h.container.style.zIndex = h.options.zIndex;
							g.call(h, l);
						}
					});
		};
		return h;
	};
})();
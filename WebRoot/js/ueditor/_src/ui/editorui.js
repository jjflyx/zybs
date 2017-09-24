(function() {
	var o = baidu.editor.utils;
	var k = baidu.editor.ui;
	var e = k.Dialog;
	k.Dialog = function(i) {
		var l = new e(i);
		l.addListener("hide", function() {
			if (l.editor) {
				var u = l.editor;
				try {
					if (browser.gecko) {
						var v = u.window.scrollY, p = u.window.scrollX;
						u.body.focus();
						u.window.scrollTo(p, v);
					} else {
						u.focus();
					}
				} catch (r) {
				}
			}
		});
		return l;
	};
	var f = {
		"anchor" : "~/dialogs/anchor/anchor.html",
		"insertimage" : "~/dialogs/image/image.html",
		"inserttable" : "~/dialogs/table/table.html",
		"link" : "~/dialogs/link/link.html",
		"spechars" : "~/dialogs/spechars/spechars.html",
		"searchreplace" : "~/dialogs/searchreplace/searchreplace.html",
		"map" : "~/dialogs/map/map.html",
		"gmap" : "~/dialogs/gmap/gmap.html",
		"insertvideo" : "~/dialogs/video/video.html",
		"help" : "~/dialogs/help/help.html",
		"highlightcode" : "~/dialogs/highlightcode/highlightcode.html",
		"emotion" : "~/dialogs/emotion/emotion.html",
		"wordimage" : "~/dialogs/wordimage/wordimage.html",
		"attachment" : "~/dialogs/attachment/attachment.html",
		"insertframe" : "~/dialogs/insertframe/insertframe.html",
		"edittd" : "~/dialogs/table/edittd.html",
		"webapp" : "~/dialogs/webapp/webapp.html",
		"snapscreen" : "~/dialogs/snapscreen/snapscreen.html",
		"scrawl" : "~/dialogs/scrawl/scrawl.html",
		"template" : "~/dialogs/template/template.html",
		"background" : "~/dialogs/background/background.html",
		"hidedomain" : "~/dialogs/input/hidedomain.html",
		"textfield" : "~/dialogs/input/textfield.html",
		"textarea" : "~/dialogs/input/textarea.html",
		"checkbox" : "~/dialogs/input/checkbox.html",
		"radioinput" : "~/dialogs/input/radioinput.html",
		"selectinput" : "~/dialogs/input/selectinput.html",
		"userunit" : "~/dialogs/input/userunit.html",
		"dictionary" : "~/dialogs/input/dictionary.html",
		"userselector" : "~/dialogs/input/userselector.html",
		"roleselector" : "~/dialogs/input/roleselector.html",
		"positionselector" : "~/dialogs/input/positionselector.html",
		"depselector" : "~/dialogs/input/depselector.html",
		"attachement" : "~/dialogs/input/attachement.html",
		"datepicker" : "~/dialogs/input/datepicker.html",
		"officecontrol" : "~/dialogs/input/officecontrol.html",
		"ckeditor" : "~/dialogs/input/ckeditor.html",
		"subtable" : "~/dialogs/input/subtable.html",
		"importform" : "~/dialogs/import/importform.html"
	};
	var j = [ "undo", "redo", "formatmatch", "bold", "italic", "underline",
			"touppercase", "tolowercase", "strikethrough", "subscript",
			"superscript", "source", "indent", "outdent", "blockquote",
			"pasteplain", "pagebreak", "selectall", "print", "preview",
			"horizontal", "removeformat", "time", "date", "unlink",
			"insertparagraphbeforetable", "insertrow", "insertcol",
			"mergeright", "mergedown", "deleterow", "deletecol", "splittorows",
			"splittocols", "splittocells", "mergecells", "deletetable",
			"hidedomain", "textfield", "textarea", "checkbox", "radioinput",
			"selectinput", "userunit","dictionary", "userselector", "roleselector",
			"positionselector", "depselector", "attachement", "datepicker",
			"officecontrol", "ckeditor", "subtable", "importform", "exportform" ];
	for ( var g = 0, t; t = j[g++];) {
		t = t.toLowerCase();
		k[t] = function(i) {
			return function(l) {
				var p = new k.Button({
					className : "edui-for-" + i,
					title : l.options.labelMap[i] || l.getLang("labelMap." + i)
							|| "",
					onclick : function() {
						l.execCommand(i);
					},
					showText : false
				});
				l.addListener("selectionchange", function(u, r, w) {
					var v = l.queryCommandState(i);
					if (v == -1) {
						p.setDisabled(true);
						p.setChecked(false);
					} else {
						if (!w) {
							p.setDisabled(false);
							p.setChecked(v);
						}
					}
				});
				return p;
			};
		}(t);
	}
	k.cleardoc = function(i) {
		var l = new k.Button({
			className : "edui-for-cleardoc",
			title : i.options.labelMap.cleardoc
					|| i.getLang("labelMap.cleardoc") || "",
			onclick : function() {
				if (confirm(i.getLang("confirmClear"))) {
					i.execCommand("cleardoc");
				}
			}
		});
		i.addListener("selectionchange", function() {
			l.setDisabled(i.queryCommandState("cleardoc") == -1);
		});
		return l;
	};
	var n = {
		"justify" : [ "left", "right", "center", "justify" ],
		"imagefloat" : [ "none", "left", "center", "right" ],
		"directionality" : [ "ltr", "rtl" ]
	};
	for ( var b in n) {
		(function(r, u) {
			for ( var p = 0, l; l = u[p++];) {
				(function(i) {
					k[r.replace("float", "") + i] = function(v) {
						var w = new k.Button(
								{
									className : "edui-for-"
											+ r.replace("float", "") + i,
									title : v.options.labelMap[r.replace(
											"float", "")
											+ i]
											|| v.getLang("labelMap."
													+ r.replace("float", "")
													+ i) || "",
									onclick : function() {
										v.execCommand(r, i);
									}
								});
						v.addListener("selectionchange", function(y, x, z) {
							w.setDisabled(v.queryCommandState(r) == -1);
							w.setChecked(v.queryCommandValue(r) == i && !z);
						});
						return w;
					};
				})(l);
			}
		})(b, n[b]);
	}
	for ( var g = 0, t; t = [ "backcolor", "forecolor" ][g++];) {
		k[t] = function(i) {
			return function(l) {
				var p = new k.ColorButton({
					className : "edui-for-" + i,
					color : "default",
					title : l.options.labelMap[i] || l.getLang("labelMap." + i)
							|| "",
					editor : l,
					onpickcolor : function(u, r) {
						l.execCommand(i, r);
					},
					onpicknocolor : function() {
						l.execCommand(i, "default");
						this.setColor("transparent");
						this.color = "default";
					},
					onbuttonclick : function() {
						l.execCommand(i, this.color);
					}
				});
				l.addListener("selectionchange", function() {
					p.setDisabled(l.queryCommandState(i) == -1);
				});
				return p;
			};
		}(t);
	}
	var h = {
		noOk : [ "searchreplace", "help", "spechars", "webapp" ],
		ok : [ "attachment", "anchor", "link", "insertimage", "map", "gmap",
				"insertframe", "wordimage", "insertvideo", "highlightcode",
				"insertframe", "edittd", "scrawl", "template", "background",
				"importform" ]
	};
	for ( var b in h) {
		(function(r, u) {
			for ( var p = 0, l; l = u[p++];) {
				if (browser.opera && l === "searchreplace") {
					continue;
				}
				(function(i) {
					k[i] = function(x, w, z) {
						w = w || (x.options.iframeUrlMap || {})[i] || f[i];
						z = x.options.labelMap[i] || x.getLang("labelMap." + i)
								|| "";
						var v;
						if (w) {
							v = new k.Dialog(o.extend({
								iframeUrl : x.ui.mapUrl(w),
								editor : x,
								className : "edui-for-" + i,
								title : z,
								closeDialog : x.getLang("closeDialog")
							}, r == "ok" ? {
								buttons : [ {
									className : "edui-okbutton",
									label : x.getLang("ok"),
									onclick : function() {
										v.close(true);
									}
								}, {
									className : "edui-cancelbutton",
									label : x.getLang("cancel"),
									onclick : function() {
										v.close(false);
									}
								} ]
							} : {}));
							x.ui._dialogs[i + "Dialog"] = v;
						}
						var y = new k.Button(
								{
									className : "edui-for-" + i,
									title : z,
									onclick : function() {
										if (v) {
											switch (i) {
											case "wordimage":
												x.execCommand("wordimage",
														"word_img");
												if (x.word_img) {
													v.render();
													v.open();
												}
												break;
											case "scrawl":
												if (x
														.queryCommandState("scrawl") != -1) {
													v.render();
													v.open();
												}
												break;
											default:
												v.render();
												v.open();
											}
										}
									},
									disabled : i == "scrawl"
											&& x.queryCommandState("scrawl") == -1
								});
						x.addListener("selectionchange", function() {
							var A = {
								"edittd" : 1,
								"edittable" : 1
							};
							if (i in A) {
								return;
							}
							var B = x.queryCommandState(i);
							y.setDisabled(B == -1);
							y.setChecked(B);
						});
						return y;
					};
				})(l.toLowerCase());
			}
		})(b, h[b]);
	}
	k.snapscreen = function(p, l, u) {
		u = p.options.labelMap["snapscreen"]
				|| p.getLang("labelMap.snapscreen") || "";
		var r = new k.Button({
			className : "edui-for-snapscreen",
			title : u,
			onclick : function() {
				p.execCommand("snapscreen");
			},
			disabled : !browser.ie
		});
		if (browser.ie) {
			l = l || (p.options.iframeUrlMap || {})["snapscreen"]
					|| f["snapscreen"];
			if (l) {
				var i = new k.Dialog({
					iframeUrl : p.ui.mapUrl(l),
					editor : p,
					className : "edui-for-snapscreen",
					title : u,
					buttons : [ {
						className : "edui-okbutton",
						label : p.getLang("ok"),
						onclick : function() {
							i.close(true);
						}
					}, {
						className : "edui-cancelbutton",
						label : p.getLang("cancel"),
						onclick : function() {
							i.close(false);
						}
					} ]
				});
				i.render();
				p.ui._dialogs["snapscreenDialog"] = i;
			}
		}
		p.addListener("selectionchange", function() {
			r.setDisabled(p.queryCommandState("snapscreen") == -1);
		});
		return r;
	};
	k.fontfamily = function(u, w, y) {
		w = u.options["fontfamily"] || [];
		y = u.options.labelMap["fontfamily"]
				|| u.getLang("labelMap.fontfamily") || "";
		for ( var r = 0, p, l = []; p = w[r]; r++) {
			var x = u.getLang("fontfamily")[p.name] || "";
			(function(i, z) {
				l
						.push({
							label : i,
							value : z,
							renderLabelHtml : function() {
								return '<div class="edui-label %%-label" style="font-family:'
										+ o.unhtml(this.value)
										+ '">'
										+ (this.label || "") + "</div>";
							}
						});
			})(p.label || x, p.val);
		}
		var v = new k.Combox({
			editor : u,
			items : l,
			onselect : function(z, i) {
				u.execCommand("FontFamily", this.items[i].value);
			},
			onbuttonclick : function() {
				this.showPopup();
			},
			title : y,
			initValue : y,
			className : "edui-for-fontfamily",
			indexByValue : function(B) {
				if (B) {
					for ( var A = 0, z; z = this.items[A]; A++) {
						if (z.value.indexOf(B) != -1) {
							return A;
						}
					}
				}
				return -1;
			}
		});
		u.addListener("selectionchange", function(z, i, C) {
			if (!C) {
				var B = u.queryCommandState("FontFamily");
				if (B == -1) {
					v.setDisabled(true);
				} else {
					v.setDisabled(false);
					var A = u.queryCommandValue("FontFamily");
					A && (A = A.replace(/['"]/g, "").split(",")[0]);
					v.setValue(A);
				}
			}
		});
		return v;
	};
	k.fontsize = function(u, w, x) {
		x = u.options.labelMap["fontsize"] || u.getLang("labelMap.fontsize")
				|| "";
		w = w || u.options["fontsize"] || [];
		var l = [];
		for ( var r = 0; r < w.length; r++) {
			var p = w[r] + "px";
			l
					.push({
						label : p,
						value : p,
						renderLabelHtml : function() {
							return '<div class="edui-label %%-label" style="line-height:1;font-size:'
									+ this.value
									+ '">'
									+ (this.label || "")
									+ "</div>";
						}
					});
		}
		var v = new k.Combox({
			editor : u,
			items : l,
			title : x,
			initValue : x,
			onselect : function(y, i) {
				u.execCommand("FontSize", this.items[i].value);
			},
			onbuttonclick : function() {
				this.showPopup();
			},
			className : "edui-for-fontsize"
		});
		u.addListener("selectionchange", function(y, i, A) {
			if (!A) {
				var z = u.queryCommandState("FontSize");
				if (z == -1) {
					v.setDisabled(true);
				} else {
					v.setDisabled(false);
					v.setValue(u.queryCommandValue("FontSize"));
				}
			}
		});
		return v;
	};
	k.paragraph = function(r, v, w) {
		w = r.options.labelMap["paragraph"] || r.getLang("labelMap.paragraph")
				|| "";
		v = r.options["paragraph"] || [];
		var l = [];
		for ( var p in v) {
			l
					.push({
						value : p,
						label : v[p] || r.getLang("paragraph")[p],
						renderLabelHtml : function() {
							return '<div class="edui-label %%-label"><span class="edui-for-'
									+ this.value
									+ '">'
									+ (this.label || "")
									+ "</span></div>";
						}
					});
		}
		var u = new k.Combox({
			editor : r,
			items : l,
			title : w,
			initValue : w,
			className : "edui-for-paragraph",
			onselect : function(x, i) {
				r.execCommand("Paragraph", this.items[i].value);
			},
			onbuttonclick : function() {
				this.showPopup();
			}
		});
		r.addListener("selectionchange", function(y, x, B) {
			if (!B) {
				var A = r.queryCommandState("Paragraph");
				if (A == -1) {
					u.setDisabled(true);
				} else {
					u.setDisabled(false);
					var z = r.queryCommandValue("Paragraph");
					var i = u.indexByValue(z);
					if (i != -1) {
						u.setValue(z);
					} else {
						u.setValue(u.initValue);
					}
				}
			}
		});
		return u;
	};
	k.customstyle = function(v) {
		var x = v.options["customstyle"] || [], y = v.options.labelMap["customstyle"]
				|| v.getLang("labelMap.customstyle") || "";
		if (!x) {
			return;
		}
		var u = v.getLang("customstyle");
		for ( var r = 0, l = [], p; p = x[r++];) {
			(function(z) {
				var i = {};
				i.label = z.label ? z.label : u[z.name];
				i.style = z.style;
				i.className = z.className;
				i.tag = z.tag;
				l
						.push({
							label : i.label,
							value : i,
							renderLabelHtml : function() {
								return '<div class="edui-label %%-label">'
										+ "<"
										+ i.tag
										+ " "
										+ (i.className ? ' class="'
												+ i.className + '"' : "")
										+ (i.style ? ' style="' + i.style + '"'
												: "") + ">" + i.label + "</"
										+ i.tag + ">" + "</div>";
							}
						});
			})(p);
		}
		var w = new k.Combox({
			editor : v,
			items : l,
			title : y,
			initValue : y,
			className : "edui-for-customstyle",
			onselect : function(z, i) {
				v.execCommand("customstyle", this.items[i].value);
			},
			onbuttonclick : function() {
				this.showPopup();
			},
			indexByValue : function(B) {
				for ( var z = 0, A; A = this.items[z++];) {
					if (A.label == B) {
						return z - 1;
					}
				}
				return -1;
			}
		});
		v.addListener("selectionchange", function(A, z, D) {
			if (!D) {
				var C = v.queryCommandState("customstyle");
				if (C == -1) {
					w.setDisabled(true);
				} else {
					w.setDisabled(false);
					var B = v.queryCommandValue("customstyle");
					var i = w.indexByValue(B);
					if (i != -1) {
						w.setValue(B);
					} else {
						w.setValue(w.initValue);
					}
				}
			}
		});
		return w;
	};
	k.inserttable = function(r, p, v) {
		p = p || (r.options.iframeUrlMap || {})["inserttable"]
				|| f["inserttable"];
		v = r.options.labelMap["inserttable"]
				|| r.getLang("labelMap.inserttable") || "";
		if (p) {
			var l = new k.Dialog({
				iframeUrl : r.ui.mapUrl(p),
				editor : r,
				className : "edui-for-inserttable",
				title : v,
				buttons : [ {
					className : "edui-okbutton",
					label : r.getLang("ok"),
					onclick : function() {
						l.close(true);
					}
				}, {
					className : "edui-cancelbutton",
					label : r.getLang("cancel"),
					onclick : function() {
						l.close(false);
					}
				} ]
			});
			l.render();
			r.ui._dialogs["inserttableDialog"] = l;
		}
		var i = function() {
			if (l) {
				if (browser.webkit) {
					l.open();
					l.close();
				}
				l.open();
			}
		};
		var u = new k.TableButton({
			editor : r,
			title : v,
			className : "edui-for-inserttable",
			onpicktable : function(w, y, x) {
				r.execCommand("InsertTable", {
					numRows : x,
					numCols : y,
					border : 1
				});
			},
			onmore : i,
			onbuttonclick : i
		});
		r.addListener("selectionchange", function() {
			u.setDisabled(r.queryCommandState("inserttable") == -1);
		});
		return u;
	};
	k.lineheight = function(u) {
		var w = u.options.lineheight;
		for ( var r = 0, p, l = []; p = w[r++];) {
			l.push({
				label : p,
				value : p,
				onclick : function() {
					u.execCommand("lineheight", this.value);
				}
			});
		}
		var v = new k.MenuButton({
			editor : u,
			className : "edui-for-lineheight",
			title : u.options.labelMap["lineheight"]
					|| u.getLang("labelMap.lineheight") || "",
			items : l,
			onbuttonclick : function() {
				var i = u.queryCommandValue("LineHeight") || this.value;
				u.execCommand("LineHeight", i);
			}
		});
		u.addListener("selectionchange", function() {
			var x = u.queryCommandState("LineHeight");
			if (x == -1) {
				v.setDisabled(true);
			} else {
				v.setDisabled(false);
				var i = u.queryCommandValue("LineHeight");
				i && v.setValue((i + "").replace(/cm/, ""));
				v.setChecked(x);
			}
		});
		return v;
	};
	var c = [ "top", "bottom" ];
	for ( var a = 0, m; m = c[a++];) {
		(function(i) {
			k["rowspacing" + i] = function(u) {
				var w = u.options["rowspacing" + i];
				for ( var r = 0, p, l = []; p = w[r++];) {
					l.push({
						label : p,
						value : p,
						onclick : function() {
							u.execCommand("rowspacing", this.value, i);
						}
					});
				}
				var v = new k.MenuButton({
					editor : u,
					className : "edui-for-rowspacing" + i,
					title : u.options.labelMap["rowspacing" + i]
							|| u.getLang("labelMap.rowspacing" + i) || "",
					items : l,
					onbuttonclick : function() {
						var x = u.queryCommandValue("rowspacing", i)
								|| this.value;
						u.execCommand("rowspacing", x, i);
					}
				});
				u.addListener("selectionchange", function() {
					var y = u.queryCommandState("rowspacing", i);
					if (y == -1) {
						v.setDisabled(true);
					} else {
						v.setDisabled(false);
						var x = u.queryCommandValue("rowspacing", i);
						x && v.setValue((x + "").replace(/%/, ""));
						v.setChecked(y);
					}
				});
				return v;
			};
		})(m);
	}
	var s = [ "insertorderedlist", "insertunorderedlist" ];
	for ( var d = 0, q; q = s[d++];) {
		(function(i) {
			k[i] = function(r) {
				var v = r.options[i], w = function() {
					r.execCommand(i, this.value);
				}, l = [];
				for ( var p in v) {
					l.push({
						label : v[p] || r.getLang()[i][p] || "",
						value : p,
						onclick : w
					});
				}
				var u = new k.MenuButton({
					editor : r,
					className : "edui-for-" + i,
					title : r.getLang("labelMap." + i) || "",
					"items" : l,
					onbuttonclick : function() {
						var x = r.queryCommandValue(i) || this.value;
						r.execCommand(i, x);
					}
				});
				r.addListener("selectionchange", function() {
					var y = r.queryCommandState(i);
					if (y == -1) {
						u.setDisabled(true);
					} else {
						u.setDisabled(false);
						var x = r.queryCommandValue(i);
						u.setValue(x);
						u.setChecked(y);
					}
				});
				return u;
			};
		})(q);
	}
	k.fullscreen = function(i, p) {
		p = i.options.labelMap["fullscreen"]
				|| i.getLang("labelMap.fullscreen") || "";
		var l = new k.Button({
			className : "edui-for-fullscreen",
			title : p,
			onclick : function() {
				if (i.ui) {
					i.ui.setFullScreen(!i.ui.isFullScreen());
				}
				this.setChecked(i.ui.isFullScreen());
			}
		});
		i.addListener("selectionchange", function() {
			var r = i.queryCommandState("fullscreen");
			l.setDisabled(r == -1);
			l.setChecked(i.ui.isFullScreen());
		});
		return l;
	};
	k.emotion = function(l, i) {
		var p = new k.MultiMenuPop({
			title : l.options.labelMap["emotion"]
					|| l.getLang("labelMap.emotion") || "",
			editor : l,
			className : "edui-for-emotion",
			iframeUrl : l.ui.mapUrl(i
					|| (l.options.iframeUrlMap || {})["emotion"]
					|| f["emotion"])
		});
		l.addListener("selectionchange", function() {
			p.setDisabled(l.queryCommandState("emotion") == -1);
		});
		return p;
	};
	k.autotypeset = function(i) {
		var l = new k.AutoTypeSetButton({
			editor : i,
			title : i.options.labelMap["autotypeset"]
					|| i.getLang("labelMap.autotypeset") || "",
			className : "edui-for-autotypeset",
			onbuttonclick : function() {
				i.execCommand("autotypeset");
			}
		});
		i.addListener("selectionchange", function() {
			l.setDisabled(i.queryCommandState("autotypeset") == -1);
		});
		return l;
	};
	k.input = function(p, l, u) {
		l = l || (p.options.iframeUrlMap || {})["input"] || f["input"];
		u = u || p.options.labelMap["input"] || "";
		if (!l) {
			return;
		}
		var i = new k.Dialog({
			iframeUrl : p.ui.mapUrl(l),
			editor : p,
			className : "edui-for-input",
			title : u,
			buttons : [ {
				className : "edui-okbutton",
				label : p.getLang("ok"),
				onclick : function() {
					i.close(true);
				}
			}, {
				className : "edui-cancelbutton",
				label : p.getLang("cancel"),
				onclick : function() {
					i.close(false);
				}
			} ]
		});
		i.render();
		p.ui._dialogs["addinputbutton"] = i;
		var r = new k.Button({
			editor : p,
			className : "edui-for-input",
			title : u || p.options.labelMap["input"] || "",
			onclick : function() {
				p.execCommand("addinput");
			}
		});
		p.addListener("selectionchange", function(x, w) {
			r.setDisabled(p.queryCommandState("addinput") == -1);
		});
		return r;
	};
})();
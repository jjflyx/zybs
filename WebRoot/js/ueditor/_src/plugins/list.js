UE.plugins["list"] = function() {
	var a = this, c = {
		"TD" : 1,
		"PRE" : 1,
		"BLOCKQUOTE" : 1
	};
	a.setOpt({
		"insertorderedlist" : {
			"decimal" : "",
			"lower-alpha" : "",
			"lower-roman" : "",
			"upper-alpha" : "",
			"upper-roman" : ""
		},
		"insertunorderedlist" : {
			"circle" : "",
			"disc" : "",
			"square" : ""
		}
	});
	function b(h, e, g) {
		var f = h.nextSibling;
		if (f
				&& f.nodeType == 1
				&& f.tagName.toLowerCase() == e
				&& (domUtils.getStyle(f, "list-style-type") || (e == "ol" ? "decimal"
						: "disc")) == g) {
			domUtils.moveChild(f, h);
			if (f.childNodes.length == 0) {
				domUtils.remove(f);
			}
		}
		var d = h.previousSibling;
		if (d
				&& d.nodeType == 1
				&& d.tagName.toLowerCase() == e
				&& (domUtils.getStyle(d, "list-style-type") || (e == "ol" ? "decimal"
						: "disc")) == g) {
			domUtils.moveChild(h, d);
		}
		if (h.childNodes.length == 0) {
			domUtils.remove(h);
		}
	}
	a.addListener(
					"keydown",
					function(m, t) {
						function i() {
							t.preventDefault ? t.preventDefault()
									: (t.returnValue = false);
							a.undoManger && a.undoManger.save();
						}
						var u = t.keyCode || t.which;
						if (u == 13) {
							var l = a.selection.getRange(), e = domUtils
									.findParentByTagName(l.startContainer, [
											"ol", "ul" ], true, function(p) {
										return p.tagName == "TABLE";
									}), h = domUtils.findParentByTagName(
									l.endContainer, [ "ol", "ul" ], true,
									function(p) {
										return p.tagName == "TABLE";
									});
							if (e && h && e === h) {
								if (!l.collapsed) {
									e = domUtils.findParentByTagName(
											l.startContainer, "li", true);
									h = domUtils.findParentByTagName(
											l.endContainer, "li", true);
									if (e && h && e === h) {
										l.deleteContents();
										r = domUtils.findParentByTagName(
												l.startContainer, "li", true);
										if (r && domUtils.isEmptyBlock(r)) {
											g = r.previousSibling;
											k = r.nextSibling;
											d = a.document.createElement("p");
											domUtils.fillNode(a.document, d);
											o = r.parentNode;
											if (g && k) {
												l.setStart(k, 0).collapse(true)
														.select(true);
												domUtils.remove(r);
											} else {
												if (!g && !k || !g) {
													o.parentNode.insertBefore(
															d, o);
												} else {
													r.parentNode.parentNode
															.insertBefore(
																	d,
																	o.nextSibling);
												}
												domUtils.remove(r);
												if (!o.firstChild) {
													domUtils.remove(o);
												}
												l.setStart(d, 0).setCursor();
											}
											i();
											return;
										}
									} else {
										var s = l.cloneRange(), f = s.collapse(
												false).createBookmark();
										l.deleteContents();
										s.moveToBookmark(f);
										var r = domUtils.findParentByTagName(
												s.startContainer, "li", true), g = r.previousSibling, k = r.nextSibling;
										if (g) {
											r = g;
											if (g.firstChild
													&& domUtils
															.isBlockElm(g.firstChild)) {
												g = g.firstChild;
											}
											if (domUtils.isEmptyNode(g)) {
												domUtils.remove(r);
											}
										}
										if (k) {
											r = k;
											if (k.firstChild
													&& domUtils
															.isBlockElm(k.firstChild)) {
												k = k.firstChild;
											}
											if (domUtils.isEmptyNode(k)) {
												domUtils.remove(r);
											}
										}
										s.select();
										i();
										return;
									}
								}
								r = domUtils.findParentByTagName(
										l.startContainer, "li", true);
								if (r) {
									if (domUtils.isEmptyBlock(r)) {
										f = l.createBookmark();
										var o = r.parentNode;
										if (r !== o.lastChild) {
											domUtils.breakParent(r, o);
										} else {
											o.parentNode.insertBefore(r,
													o.nextSibling);
											if (domUtils.isEmptyNode(o)) {
												domUtils.remove(o);
											}
										}
										if (!dtd.$list[r.parentNode.tagName]) {
											if (!domUtils
													.isBlockElm(r.firstChild)) {
												d = a.document
														.createElement("p");
												r.parentNode.insertBefore(d, r);
												while (r.firstChild) {
													d.appendChild(r.firstChild);
												}
												domUtils.remove(r);
											} else {
												domUtils.remove(r, true);
											}
										}
										l.moveToBookmark(f).select();
									} else {
										var j = r.firstChild;
										if (!j || !domUtils.isBlockElm(j)) {
											var d = a.document
													.createElement("p");
											!r.firstChild
													&& domUtils.fillNode(
															a.document, d);
											while (r.firstChild) {
												d.appendChild(r.firstChild);
											}
											r.appendChild(d);
											j = d;
										}
										var n = a.document
												.createElement("span");
										l.insertNode(n);
										domUtils.breakParent(n, r);
										var q = n.nextSibling;
										j = q.firstChild;
										if (!j) {
											d = a.document.createElement("p");
											domUtils.fillNode(a.document, d);
											q.appendChild(d);
											j = d;
										}
										if (domUtils.isEmptyNode(j)) {
											j.innerHTML = "";
											domUtils.fillNode(a.document, j);
										}
										l.setStart(j, 0).collapse(true)
												.shrinkBoundary().select();
										domUtils.remove(n);
										g = q.previousSibling;
										if (g && domUtils.isEmptyBlock(g)) {
											g.innerHTML = "<p></p>";
											domUtils.fillNode(a.document,
													g.firstChild);
										}
									}
									i();
								}
							}
						}
						if (u == 8) {
							l = a.selection.getRange();
							if (l.collapsed && domUtils.isStartInblock(l)) {
								s = l.cloneRange().trimBoundary();
								r = domUtils.findParentByTagName(
										l.startContainer, "li", true);
								if (r && domUtils.isStartInblock(s)) {
									if (r && (g = r.previousSibling)) {
										if (u == 46 && r.childNodes.length) {
											return;
										}
										if (dtd.$list[g.tagName]) {
											g = g.lastChild;
										}
										a.undoManger && a.undoManger.save();
										j = r.firstChild;
										if (domUtils.isBlockElm(j)) {
											if (domUtils.isEmptyNode(j)) {
												g.appendChild(j);
												l.setStart(j, 0).setCursor(
														false, true);
												while (r.firstChild) {
													g.appendChild(r.firstChild);
												}
											} else {
												e = domUtils
														.findParentByTagName(
																l.startContainer,
																"p", true);
												if (e && e !== j) {
													return;
												}
												n = a.document
														.createElement("span");
												l.insertNode(n);
												if (domUtils.isEmptyBlock(g)) {
													g.innerHTML = "";
												}
												domUtils.moveChild(r, g);
												l.setStartBefore(n).collapse(
														true).select(true);
												domUtils.remove(n);
											}
										} else {
											if (domUtils.isEmptyNode(r)) {
												var d = a.document
														.createElement("p");
												g.appendChild(d);
												l.setStart(d, 0).setCursor();
											} else {
												l
														.setEnd(
																g,
																g.childNodes.length)
														.collapse()
														.select(true);
												while (r.firstChild) {
													g.appendChild(r.firstChild);
												}
											}
										}
										domUtils.remove(r);
										a.undoManger && a.undoManger.save();
										domUtils.preventDefault(t);
										return;
									}
									if (r && !r.previousSibling) {
										j = r.firstChild;
										if (!j
												|| r.lastChild === j
												&& domUtils
														.isEmptyNode(domUtils
																.isBlockElm(j) ? j
																: r)) {
											var d = a.document
													.createElement("p");
											r.parentNode.parentNode
													.insertBefore(d,
															r.parentNode);
											domUtils.fillNode(a.document, d);
											l.setStart(d, 0).setCursor();
											domUtils
													.remove(!r.nextSibling ? r.parentNode
															: r);
											a.undoManger && a.undoManger.save();
											domUtils.preventDefault(t);
											return;
										}
									}
								}
							}
						}
					});
	a.commands["insertorderedlist"] = a.commands["insertunorderedlist"] = {
		execCommand : function(g, z) {
			if (!z) {
				z = g.toLowerCase() == "insertorderedlist" ? "decimal" : "disc";
			}
			var G = this, s = this.selection.getRange(), t = function(i) {
				return i.nodeType == 1 ? i.tagName.toLowerCase() != "br"
						: !domUtils.isWhitespace(i);
			}, I = g.toLowerCase() == "insertorderedlist" ? "ol" : "ul", f = G.document
					.createDocumentFragment();
			s.adjustmentBoundary().shrinkBoundary();
			var l = s.createBookmark(true), j = domUtils.findParentByTagName(
					G.document.getElementById(l.start), "li"), w = 0, h = domUtils
					.findParentByTagName(G.document.getElementById(l.end), "li"), x = 0, E, o, A, C;
			if (j || h) {
				j && (E = j.parentNode);
				if (!l.end) {
					h = j;
				}
				h && (o = h.parentNode);
				if (E === o) {
					while (j !== h) {
						C = j;
						j = j.nextSibling;
						if (!domUtils.isBlockElm(C.firstChild)) {
							var u = G.document.createElement("p");
							while (C.firstChild) {
								u.appendChild(C.firstChild);
							}
							C.appendChild(u);
						}
						f.appendChild(C);
					}
					C = G.document.createElement("span");
					E.insertBefore(C, h);
					if (!domUtils.isBlockElm(h.firstChild)) {
						u = G.document.createElement("p");
						while (h.firstChild) {
							u.appendChild(h.firstChild);
						}
						h.appendChild(u);
					}
					f.appendChild(h);
					domUtils.breakParent(C, E);
					if (domUtils.isEmptyNode(C.previousSibling)) {
						domUtils.remove(C.previousSibling);
					}
					if (domUtils.isEmptyNode(C.nextSibling)) {
						domUtils.remove(C.nextSibling);
					}
					var k = domUtils.getComputedStyle(E, "list-style-type")
							|| (g.toLowerCase() == "insertorderedlist" ? "decimal"
									: "disc");
					if (E.tagName.toLowerCase() == I && k == z) {
						for ( var y = 0, q, m = G.document
								.createDocumentFragment(); q = f.childNodes[y++];) {
							while (q.firstChild) {
								m.appendChild(q.firstChild);
							}
						}
						C.parentNode.insertBefore(m, C);
					} else {
						A = G.document.createElement(I);
						domUtils.setStyle(A, "list-style-type", z);
						A.appendChild(f);
						C.parentNode.insertBefore(A, C);
					}
					domUtils.remove(C);
					A && b(A, I, z);
					s.moveToBookmark(l).select();
					return;
				}
				if (j) {
					while (j) {
						C = j.nextSibling;
						var d = G.document.createDocumentFragment(), D = 0;
						while (j.firstChild) {
							if (domUtils.isBlockElm(j.firstChild)) {
								D = 1;
							}
							d.appendChild(j.firstChild);
						}
						if (!D) {
							var H = G.document.createElement("p");
							H.appendChild(d);
							f.appendChild(H);
						} else {
							f.appendChild(d);
						}
						domUtils.remove(j);
						j = C;
					}
					E.parentNode.insertBefore(f, E.nextSibling);
					if (domUtils.isEmptyNode(E)) {
						s.setStartBefore(E);
						domUtils.remove(E);
					} else {
						s.setStartAfter(E);
					}
					w = 1;
				}
				if (h) {
					j = o.firstChild;
					while (j !== h) {
						C = j.nextSibling;
						d = G.document.createDocumentFragment();
						D = 0;
						while (j.firstChild) {
							if (domUtils.isBlockElm(j.firstChild)) {
								D = 1;
							}
							d.appendChild(j.firstChild);
						}
						if (!D) {
							H = G.document.createElement("p");
							H.appendChild(d);
							f.appendChild(H);
						} else {
							f.appendChild(d);
						}
						domUtils.remove(j);
						j = C;
					}
					f.appendChild(h.firstChild);
					domUtils.remove(h);
					o.parentNode.insertBefore(f, o);
					s.setEndBefore(o);
					if (domUtils.isEmptyNode(o)) {
						domUtils.remove(o);
					}
					x = 1;
				}
			}
			if (!w) {
				s.setStartBefore(G.document.getElementById(l.start));
			}
			if (l.end && !x) {
				s.setEndAfter(G.document.getElementById(l.end));
			}
			s.enlarge(true, function(i) {
				return c[i.tagName];
			});
			f = G.document.createDocumentFragment();
			var e = s.createBookmark(), v = domUtils.getNextDomNode(e.start,
					false, t), F = s.cloneRange(), B, n = domUtils.isBlockElm;
			while (v
					&& v !== e.end
					&& (domUtils.getPosition(v, e.end) & domUtils.POSITION_PRECEDING)) {
				if (v.nodeType == 3 || dtd.li[v.tagName]) {
					if (v.nodeType == 1 && dtd.$list[v.tagName]) {
						while (v.firstChild) {
							f.appendChild(v.firstChild);
						}
						B = domUtils.getNextDomNode(v, false, t);
						domUtils.remove(v);
						v = B;
						continue;
					}
					B = v;
					F.setStartBefore(v);
					while (v && v !== e.end
							&& (!n(v) || domUtils.isBookmarkNode(v))) {
						B = v;
						v = domUtils.getNextDomNode(v, false, null,
								function(i) {
									return !c[i.tagName];
								});
					}
					if (v && n(v)) {
						C = domUtils.getNextDomNode(B, false, t);
						if (C && domUtils.isBookmarkNode(C)) {
							v = domUtils.getNextDomNode(C, false, t);
							B = C;
						}
					}
					F.setEndAfter(B);
					v = domUtils.getNextDomNode(B, false, t);
					var r = s.document.createElement("li");
					r.appendChild(F.extractContents());
					f.appendChild(r);
				} else {
					v = domUtils.getNextDomNode(v, true, t);
				}
			}
			s.moveToBookmark(e).collapse(true);
			A = G.document.createElement(I);
			domUtils.setStyle(A, "list-style-type", z);
			A.appendChild(f);
			s.insertNode(A);
			b(A, I, z);
			s.moveToBookmark(l).select();
		},
		queryCommandState : function(d) {
			return this.highlight ? -1
					: utils.findNode(this.selection.getStartElementPath(),
							[ d.toLowerCase() == "insertorderedlist" ? "ol"
									: "ul" ]) ? 1 : 0;
		},
		queryCommandValue : function(e) {
			var d = utils.findNode(this.selection.getStartElementPath(), [ e
					.toLowerCase() == "insertorderedlist" ? "ol" : "ul" ]);
			return d ? domUtils.getComputedStyle(d, "list-style-type") : null;
		}
	};
};
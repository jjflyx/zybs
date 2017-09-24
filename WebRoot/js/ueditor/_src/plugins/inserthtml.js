UE.commands["inserthtml"] = {
	execCommand : function(a, g, s) {
		var t = this, h, j, o = t.currentSelectedArr;
		h = t.selection.getRange();
		j = h.document.createElement("div");
		j.style.display = "inline";
		var m = t.serialize;
		if (!s && m) {
			var n = m.parseHTML(g);
			n = m.transformInput(n);
			n = m.filter(n);
			g = m.toHTML(n);
		}
		j.innerHTML = utils.trim(g);
		try {
			t.adjustTable && t.adjustTable(j);
		} catch (q) {
		}
		if (o && o.length) {
			for ( var p = 0, c; c = o[p++];) {
				c.className = "";
			}
			o[0].innerHTML = "";
			h.setStart(o[0], 0).collapse(true);
			t.currentSelectedArr = [];
		}
		if (!h.collapsed) {
			h.deleteContents();
			if (h.startContainer.nodeType == 1) {
				var d = h.startContainer.childNodes[h.startOffset], k;
				if (d && domUtils.isBlockElm(d) && (k = d.previousSibling)
						&& domUtils.isBlockElm(k)) {
					h.setEnd(k, k.childNodes.length).collapse();
					while (d.firstChild) {
						k.appendChild(d.firstChild);
					}
					domUtils.remove(d);
				}
			}
		}
		var d, f, k, r, b = 0;
		while (d = j.firstChild) {
			h.insertNode(d);
			if (!b && d.nodeType == domUtils.NODE_ELEMENT
					&& domUtils.isBlockElm(d)) {
				f = domUtils.findParent(d, function(e) {
					return domUtils.isBlockElm(e);
				});
				if (f && f.tagName.toLowerCase() != "body"
						&& !(dtd[f.tagName][d.nodeName] && d.parentNode === f)) {
					if (!dtd[f.tagName][d.nodeName]) {
						k = f;
					} else {
						r = d.parentNode;
						while (r !== f) {
							k = r;
							r = r.parentNode;
						}
					}
					domUtils.breakParent(d, k || r);
					var k = d.previousSibling;
					domUtils.trimWhiteTextNode(k);
					if (!k.childNodes.length) {
						domUtils.remove(k);
					}
					if (!browser.ie && (l = d.nextSibling)
							&& domUtils.isBlockElm(l) && l.lastChild
							&& !domUtils.isBr(l.lastChild)) {
						l.appendChild(t.document.createElement("br"));
					}
					b = 1;
				}
			}
			var l = d.nextSibling;
			if (!j.firstChild && l && domUtils.isBlockElm(l)) {
				h.setStart(l, 0).collapse(true);
				break;
			}
			h.setEndAfter(d).collapse();
		}
		d = h.startContainer;
		if (domUtils.isBlockElm(d) && domUtils.isEmptyNode(d)) {
			d.innerHTML = browser.ie ? "" : "<br/>";
		}
		h.select(true);
		setTimeout(function() {
			h = t.selection.getRange();
			h.scrollToView(t.autoHeightEnabled, t.autoHeightEnabled ? domUtils
					.getXY(t.iframe).y : 0);
		}, 200);
	}
};
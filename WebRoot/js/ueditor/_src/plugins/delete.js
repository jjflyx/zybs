UE.commands["delete"] = {
	execCommand : function() {
		var f = this.selection.getRange(), d = 0, j = 0, h = this;
		if (this.selectAll) {
			h.body.innerHTML = "<p>" + (browser.ie ? "&nbsp;" : "<br/>")
					+ "</p>";
			f.setStart(h.body.firstChild, 0).setCursor(false, true);
			h.selectAll = false;
			return;
		}
		if (h.currentSelectedArr && h.currentSelectedArr.length > 0) {
			for ( var e = 0, l; l = h.currentSelectedArr[e++];) {
				if (l.style.display != "none") {
					l.innerHTML = browser.ie ? domUtils.fillChar : "<br/>";
				}
			}
			f.setStart(h.currentSelectedArr[0], 0).setCursor();
			return;
		}
		if (f.collapsed) {
			return;
		}
		f.txtToElmBoundary();
		while (!f.startOffset && !domUtils.isBody(f.startContainer)
				&& !dtd.$tableContent[f.startContainer.tagName]) {
			d = 1;
			f.setStartBefore(f.startContainer);
		}
		while (f.endContainer.nodeType != 3 && !domUtils.isBody(f.endContainer)
				&& !dtd.$tableContent[f.endContainer.tagName]) {
			var b, k = f.endContainer, g = f.endOffset;
			b = k.childNodes[g];
			if (!b || domUtils.isBr(b) && k.lastChild === b) {
				f.setEndAfter(k);
				continue;
			}
			break;
		}
		if (d) {
			var a = h.document.createElement("span");
			a.innerHTML = "start";
			a.id = "_baidu_cut_start";
			f.insertNode(a).setStartBefore(a);
		}
		if (j) {
			var c = h.document.createElement("span");
			c.innerHTML = "end";
			c.id = "_baidu_cut_end";
			f.cloneRange().collapse(false).insertNode(c);
			f.setEndAfter(c);
		}
		f.deleteContents();
		if (domUtils.isBody(f.startContainer) && domUtils.isEmptyBlock(h.body)) {
			h.body.innerHTML = "<p>" + (browser.ie ? "" : "<br/>") + "</p>";
			f.setStart(h.body.firstChild, 0).collapse(true);
		} else {
			if (!browser.ie && domUtils.isEmptyBlock(f.startContainer)) {
				f.startContainer.innerHTML = "<br/>";
			}
		}
		f.select(true);
	},
	queryCommandState : function() {
		if (this.currentSelectedArr && this.currentSelectedArr.length > 0) {
			return 0;
		}
		return this.highlight || this.selection.getRange().collapsed ? -1 : 0;
	}
};
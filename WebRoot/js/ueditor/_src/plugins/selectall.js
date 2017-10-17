UE.plugins["selectall"] = function() {
	var a = this;
	a.commands["selectall"] = {
		execCommand : function() {
			var d = this, b = d.body, c = d.selection.getRange();
			c.selectNodeContents(b);
			if (domUtils.isEmptyBlock(b)) {
				if (browser.opera && b.firstChild && b.firstChild.nodeType == 1) {
					c.setStartAtFirst(b.firstChild);
				}
				c.collapse(true);
			}
			c.select(true);
			this.selectAll = true;
		},
		notNeedUndo : 1
	};
	a.addListener("ready", function() {
		domUtils.on(a.document, "click", function(b) {
			a.selectAll = false;
		});
	});
};
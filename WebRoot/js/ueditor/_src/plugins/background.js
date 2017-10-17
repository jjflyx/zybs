(function() {
	UE.plugins["background"] = function() {
		var a = this;
		UE.commands["background"] = {
			queryCommandState : function() {
				return this.highlight ? -1 : 0;
			}
		};
		a.addListener("getAllHtml", function(g, f) {
			var b = this.body, c = domUtils.getComputedStyle(b,
					"background-image"), e = "";
			if (c.indexOf(a.options.imagePath) > 0) {
				e = c.substring(c.indexOf(a.options.imagePath), c.length - 1)
						.replace(/"|\(|\)/ig, "");
			} else {
				e = c != "none" ? c.replace(/url\("?|"?\)/ig, "") : "";
			}
			f.html = '<style type="text/css">body{';
			var h = {
				"background-color" : domUtils.getComputedStyle(b,
						"background-color")
						|| "",
				"background-image" : e ? "url(" + e + ")" : "",
				"background-repeat" : domUtils.getComputedStyle(b,
						"background-repeat")
						|| "",
				"background-position" : browser.ie ? (domUtils
						.getComputedStyle(b, "background-position-x")
						+ " " + domUtils.getComputedStyle(b,
						"background-position-y")) : domUtils.getComputedStyle(
						b, "background-position"),
				"height" : domUtils.getComputedStyle(b, "height")
			};
			for ( var d in h) {
				if (h.hasOwnProperty(d)) {
					f.html += d + ":" + h[d] + ";";
				}
			}
			f.html += "}</style> ";
		});
	};
})();
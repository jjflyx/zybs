(function() {
	var a = window.parent;
	dialog = a.$EDITORUI[window.frameElement.id.replace(/_iframe$/, "")];
	editor = dialog.editor;
	UE = a.UE;
	domUtils = UE.dom.domUtils;
	utils = UE.utils;
	browser = UE.browser;
	ajax = UE.ajax;
	$G = function(b) {
		return document.getElementById(b);
	};
	$focus = function(b) {
		setTimeout(function() {
			if (browser.ie) {
				var c = b.createTextRange();
				c.collapse(false);
				c.select();
			} else {
				b.focus();
			}
		}, 0);
	};
	lang = UE.I18N[editor.options.lang][dialog.className.split("-")[2]];
	
	utils.domReady(function() {
		var c = editor.options.langPath + editor.options.lang + "/images/";
		for ( var h in lang._static) {
			var f = $G(h);
			if (!f) {
				continue;
			}
			var e = f.tagName, k = lang._static[h];
			if (k.src) {
				k = utils.extend({}, k, false);
				k.src = c + k.src;
			}
			if (k.style) {
				k = utils.extend({}, k, false);
				k.style = k.style.replace(/url\s*\(/g, "url(" + c);
			}
			switch (e.toLowerCase()) {
			case "var":
				f.parentNode.replaceChild(document.createTextNode(k), f);
				break;
			case "select":
				var d = f.options;
				for (var g = 0, l; l = d[g];) {
					l.innerHTML = k.options[g++];
				}
				for ( var b in k) {
					b != "options" && f.setAttribute(b, k[b]);
				}
				break;
			default:
				domUtils.setAttributes(f, k);
			}
		}
	});
})();
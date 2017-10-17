var init = function() {
	addColorPickListener();
	addPxChangeListener();
	addFloatListener();
	addBorderTypeChangeListener();
};
function addBorderTypeChangeListener() {
	domUtils.on($G("borderType"), "change", createTable);
}
function addFloatListener() {
	domUtils.on($G("align"), "change", function() {
		setTablePosition(this.value);
	});
}
function setTablePosition(c) {
	var a = $G("preview").children[0], b = (a.parentNode.offsetWidth - a.offsetWidth) / 2;
	if (c == "center") {
		a.style.marginLeft = b + "px";
	} else {
		if (c == "right") {
			a.style.marginLeft = 2 * b + "px";
		} else {
			a.style.marginLeft = "5px";
		}
	}
}
function addPxChangeListener() {
	var c = [ "border", "cellPadding", "cellSpacing" ];
	for ( var b = 0, a; a = $G(c[b++]);) {
		domUtils.on(a, "keyup", function() {
			$G("message").style.display = "none";
			switch (this.id) {
			case "border":
				$G("border").value = filter(this.value, "border");
				break;
			case "cellPadding":
				$G("cellPadding").value = filter(this.value, "cellPadding");
				break;
			case "cellSpacing":
				$G("cellSpacing").value = filter(this.value, "cellSpacing");
				break;
			default:
			}
			createTable();
		});
	}
}
function isNum(a) {
	return /^(0|[1-9][0-9]*)$/.test(a);
}
function createTable() {
	var e = $G("border").value || 1, c = $G("borderColor").value || "#000000", h = $G("cellPadding").value || 0, b = $G("cellSpacing").value || 0, m = $G("bgColor").value
			|| "#FFFFFF", l = $G("align").value || "", a = $G("borderType").value || 0;
	e = setMax(e, 5);
	h = setMax(h, 5);
	b = setMax(b, 5);
	var j = [ "<table " ];
	if (b > 0) {
		j.push(' style="border-collapse:separate;" ');
	} else {
		j.push(' style="border-collapse:collapse;" ');
	}
	b > 0 && j.push(' cellSpacing="' + b + '" ');
	j.push(' border="' + (e || 1) + '" borderColor="' + (c || "#000000") + '"');
	m && j.push(' bgColor="' + m + '"');
	j
			.push(' ><tr><td colspan="3"><var id="lang_forPreview">'
					+ lang._static.lang_forPreview
					+ "</var></td></tr><tr><td></td><td></td><td></td></tr><tr><td></td><td></td><td></td></tr></table>");
	var k = $G("preview");
	k.innerHTML = j.join("");
	var n = k.firstChild;
	if (a == "1") {
		for ( var g = 0, d, f = domUtils.getElementsByTagName(n, "td"); d = f[g++];) {
			d.style.border = e + "px solid " + c;
		}
	}
	for ( var g = 0, d, f = domUtils.getElementsByTagName(n, "td"); d = f[g++];) {
		d.style.padding = h + "px";
	}
	setTablePosition(l.toLowerCase());
}
function setMax(b, a) {
	return b > a ? a : b;
}
function filter(c, b) {
	var a = 5, d = 10;
	if (!isNum(c) && c != "") {
		$G(b).value = "";
		$G("message").style.display = "";
		$G("messageContent").innerHTML = lang.errorNum;
		return b == "border" ? 1 : 0;
	}
	if (c > a) {
		$G("message").style.display = "";
		$G("messageContent").innerHTML = lang.overflowPreviewMsg.replace(
				"{#value}", a);
		if (c > d) {
			$G("messageContent").innerHTML = lang.overflowMsg.replace(
					"{#value}", d);
			$G(b).value = d;
			return d;
		}
	}
	return c;
}
function addColorPickListener() {
	var a = getColorPicker(), d = [ "bgColor", "borderColor" ];
	for ( var c = 0, b; b = $G(d[c++]);) {
		domUtils.on(b, "click", function() {
			var e = this;
			showColorPicker(a, e);
			a.content.onpickcolor = function(g, f) {
				e.value = f.toUpperCase();
				a.hide();
				createTable();
			};
			a.content.onpicknocolor = function() {
				e.value = "";
				a.hide();
				createTable();
			};
		});
		domUtils.on(b, "keyup", function() {
			a.hide();
			createTable();
		});
	}
	domUtils.on(document, "mousedown", function() {
		UE.ui.Popup.postHide(this);
	});
}
function getColorPicker() {
	return new UE.ui.Popup({
		content : new UE.ui.ColorPicker({
			noColorText : lang.noColor,
			editor : editor
		})
	});
}
function showColorPicker(b, a) {
	b.showAnchor(a);
}
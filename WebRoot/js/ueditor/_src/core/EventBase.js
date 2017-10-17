var EventBase = UE.EventBase = function() {
};
EventBase.prototype = {
	addListener : function(b, d) {
		b = utils.trim(b).split(" ");
		for ( var a = 0, c; c = b[a++];) {
			getListener(this, c, true).push(d);
		}
	},
	removeListener : function(b, d) {
		b = utils.trim(b).split(" ");
		for ( var a = 0, c; c = b[a++];) {
			utils.removeItem(getListener(this, c) || [], d);
		}
	},
	fireEvent : function(d) {
		d = utils.trim(d).split(" ");
		for ( var c = 0, f; f = d[c++];) {
			var e = getListener(this, f), g, b, a;
			if (e) {
				a = e.length;
				while (a--) {
					b = e[a].apply(this, arguments);
					if (b !== undefined) {
						g = b;
					}
				}
			}
			if (b = this["on" + f.toLowerCase()]) {
				g = b.apply(this, arguments);
			}
		}
		return g;
	}
};
function getListener(d, b, c) {
	var a;
	b = b.toLowerCase();
	return ((a = (d.__allListeners || c && (d.__allListeners = {}))) && (a[b] || c
			&& (a[b] = [])));
}
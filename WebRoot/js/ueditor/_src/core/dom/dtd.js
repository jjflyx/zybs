var dtd = dom.dtd = (function() {
	function z(B) {
		for ( var A in B) {
			B[A.toUpperCase()] = B[A];
		}
		return B;
	}
	function a(E) {
		var B = arguments;
		for ( var D = 1; D < B.length; D++) {
			var A = B[D];
			for ( var C in A) {
				if (!E.hasOwnProperty(C)) {
					E[C] = A[C];
				}
			}
		}
		return E;
	}
	var y = z({
		isindex : 1,
		fieldset : 1
	}), x = z({
		input : 1,
		button : 1,
		select : 1,
		textarea : 1,
		label : 1
	}), w = a(z({
		a : 1
	}), x), v = a({
		iframe : 1
	}, w), u = z({
		hr : 1,
		ul : 1,
		menu : 1,
		div : 1,
		blockquote : 1,
		noscript : 1,
		table : 1,
		center : 1,
		address : 1,
		dir : 1,
		pre : 1,
		h5 : 1,
		dl : 1,
		h4 : 1,
		noframes : 1,
		h6 : 1,
		ol : 1,
		h1 : 1,
		h3 : 1,
		h2 : 1
	}), t = z({
		ins : 1,
		del : 1,
		script : 1,
		style : 1
	}), s = a(z({
		b : 1,
		acronym : 1,
		bdo : 1,
		"var" : 1,
		"#" : 1,
		abbr : 1,
		code : 1,
		br : 1,
		i : 1,
		cite : 1,
		kbd : 1,
		u : 1,
		strike : 1,
		s : 1,
		tt : 1,
		strong : 1,
		q : 1,
		samp : 1,
		em : 1,
		dfn : 1,
		span : 1
	}), t), r = a(z({
		sub : 1,
		img : 1,
		embed : 1,
		object : 1,
		sup : 1,
		basefont : 1,
		map : 1,
		applet : 1,
		font : 1,
		big : 1,
		small : 1
	}), s), q = a(z({
		p : 1
	}), r), p = a(z({
		iframe : 1
	}), r, x), n = z({
		img : 1,
		embed : 1,
		noscript : 1,
		br : 1,
		kbd : 1,
		center : 1,
		button : 1,
		basefont : 1,
		h5 : 1,
		h4 : 1,
		samp : 1,
		h6 : 1,
		ol : 1,
		h1 : 1,
		h3 : 1,
		h2 : 1,
		form : 1,
		font : 1,
		"#" : 1,
		select : 1,
		menu : 1,
		ins : 1,
		abbr : 1,
		label : 1,
		code : 1,
		table : 1,
		script : 1,
		cite : 1,
		input : 1,
		iframe : 1,
		strong : 1,
		textarea : 1,
		noframes : 1,
		big : 1,
		small : 1,
		span : 1,
		hr : 1,
		sub : 1,
		bdo : 1,
		"var" : 1,
		div : 1,
		object : 1,
		sup : 1,
		strike : 1,
		dir : 1,
		map : 1,
		dl : 1,
		applet : 1,
		del : 1,
		isindex : 1,
		fieldset : 1,
		ul : 1,
		b : 1,
		acronym : 1,
		a : 1,
		blockquote : 1,
		i : 1,
		u : 1,
		s : 1,
		tt : 1,
		address : 1,
		q : 1,
		pre : 1,
		p : 1,
		em : 1,
		dfn : 1
	}), m = a(z({
		a : 0
	}), p), k = z({
		tr : 1
	}), j = z({
		"#" : 1
	}), i = a(z({
		param : 1
	}), n), h = a(z({
		form : 1
	}), y, v, u, q), g = z({
		li : 1
	}), f = z({
		style : 1,
		script : 1
	}), e = z({
		base : 1,
		link : 1,
		meta : 1,
		title : 1
	}), d = a(e, f), c = z({
		head : 1,
		body : 1
	}), b = z({
		html : 1
	});
	var l = z({
		address : 1,
		blockquote : 1,
		center : 1,
		dir : 1,
		div : 1,
		dl : 1,
		fieldset : 1,
		form : 1,
		h1 : 1,
		h2 : 1,
		h3 : 1,
		h4 : 1,
		h5 : 1,
		h6 : 1,
		hr : 1,
		isindex : 1,
		menu : 1,
		noframes : 1,
		ol : 1,
		p : 1,
		pre : 1,
		table : 1,
		ul : 1
	}), o = z({
		area : 1,
		base : 1,
		br : 1,
		col : 1,
		hr : 1,
		img : 1,
		input : 1,
		link : 1,
		meta : 1,
		param : 1,
		embed : 1
	});
	return z({
		$nonBodyContent : a(b, c, e),
		$block : l,
		$inline : m,
		$body : a(z({
			script : 1,
			style : 1
		}), l),
		$cdata : z({
			script : 1,
			style : 1
		}),
		$empty : o,
		$nonChild : z({
			iframe : 1,
			textarea : 1
		}),
		$listItem : z({
			dd : 1,
			dt : 1,
			li : 1
		}),
		$list : z({
			ul : 1,
			ol : 1,
			dl : 1
		}),
		$isNotEmpty : z({
			table : 1,
			ul : 1,
			ol : 1,
			dl : 1,
			iframe : 1,
			area : 1,
			base : 1,
			col : 1,
			hr : 1,
			img : 1,
			embed : 1,
			input : 1,
			link : 1,
			meta : 1,
			param : 1
		}),
		$removeEmpty : z({
			a : 1,
			abbr : 1,
			acronym : 1,
			address : 1,
			b : 1,
			bdo : 1,
			big : 1,
			cite : 1,
			code : 1,
			del : 1,
			dfn : 1,
			em : 1,
			font : 1,
			i : 1,
			ins : 1,
			label : 1,
			kbd : 1,
			q : 1,
			s : 1,
			samp : 1,
			small : 1,
			span : 1,
			strike : 1,
			strong : 1,
			sub : 1,
			sup : 1,
			tt : 1,
			u : 1,
			"var" : 1
		}),
		$removeEmptyBlock : z({
			"p" : 1,
			"div" : 1
		}),
		$tableContent : z({
			caption : 1,
			col : 1,
			colgroup : 1,
			tbody : 1,
			td : 1,
			tfoot : 1,
			th : 1,
			thead : 1,
			tr : 1,
			table : 1
		}),
		$notTransContent : z({
			pre : 1,
			script : 1,
			style : 1,
			textarea : 1
		}),
		html : c,
		head : d,
		style : j,
		script : j,
		body : h,
		base : {},
		link : {},
		meta : {},
		title : j,
		col : {},
		tr : z({
			td : 1,
			th : 1
		}),
		img : {},
		embed : {},
		colgroup : z({
			thead : 1,
			col : 1,
			tbody : 1,
			tr : 1,
			tfoot : 1
		}),
		noscript : h,
		td : h,
		br : {},
		th : h,
		center : h,
		kbd : m,
		button : a(q, u),
		basefont : {},
		h5 : m,
		h4 : m,
		samp : m,
		h6 : m,
		ol : g,
		h1 : m,
		h3 : m,
		option : j,
		h2 : m,
		form : a(y, v, u, q),
		select : z({
			optgroup : 1,
			option : 1
		}),
		font : m,
		ins : m,
		menu : g,
		abbr : m,
		label : m,
		table : z({
			thead : 1,
			col : 1,
			tbody : 1,
			tr : 1,
			colgroup : 1,
			caption : 1,
			tfoot : 1
		}),
		code : m,
		tfoot : k,
		cite : m,
		li : h,
		input : {},
		iframe : h,
		strong : m,
		textarea : j,
		noframes : h,
		big : m,
		small : m,
		span : z({
			"#" : 1,
			br : 1
		}),
		hr : m,
		dt : m,
		sub : m,
		optgroup : z({
			option : 1
		}),
		param : {},
		bdo : m,
		"var" : m,
		div : h,
		object : i,
		sup : m,
		dd : h,
		strike : m,
		area : {},
		dir : g,
		map : a(z({
			area : 1,
			form : 1,
			p : 1
		}), y, t, u),
		applet : i,
		dl : z({
			dt : 1,
			dd : 1
		}),
		del : m,
		isindex : {},
		fieldset : a(z({
			legend : 1
		}), n),
		thead : k,
		ul : g,
		acronym : m,
		b : m,
		a : a(z({
			a : 1
		}), p),
		blockquote : a(z({
			td : 1,
			tr : 1,
			tbody : 1,
			li : 1
		}), h),
		caption : m,
		i : m,
		u : m,
		tbody : k,
		s : m,
		address : a(v, q),
		tt : m,
		legend : m,
		q : m,
		pre : a(s, w),
		p : a(z({
			"a" : 1
		}), m),
		em : m,
		dfn : m
	});
})();
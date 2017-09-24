(function() {
	var a;
	var b = window.location.pathname;
	a = __ctxPath + "/js/ueditor/";
	var c = __ctxPath + "/attachFiles/ueditor/";
	window.UEDITOR_CONFIG = {
		UEDITOR_HOME_URL : a,
		lang : "zh-cn",
		langPath : a + "lang/",
		imageUrl : a + "jsp/imageUp.jsp",
		imagePath : c,
		scrawlUrl : a + "jsp/scrawlUp.jsp",
		scrawlPath : c,
		fileUrl : a + "jsp/fileUp.jsp",
		filePath : c,
		catcherUrl : a + "jsp/getRemoteImage.jsp",
		catcherPath : c,
		imageManagerUrl : a + "jsp/imageManager.jsp",
		imageManagerPath : c,
		snapscreenHost : "127.0.0.1",
		snapscreenServerUrl : a + "jsp/imageUp.jsp",
		snapscreenPath : c,
		wordImageUrl : a + "jsp/imageUp.jsp",
		wordImagePath : c,
		getMovieUrl : a + "jsp/getMovie.jsp",
		toolbars : [ [ "fullscreen", "source", "|", "undo", "redo", "|",
				"bold", "italic", "underline", "strikethrough", "superscript",
				"subscript", "removeformat", "formatmatch", "autotypeset", "|",
				"blockquote", "|", "pasteplain", "|", "forecolor", "backcolor",
				"insertorderedlist", "insertunorderedlist", "selectall",
				"cleardoc", "|", "customstyle", "paragraph", "|",
				"rowspacingtop", "rowspacingbottom", "lineheight", "|",
				"fontfamily", "fontsize", "|", "directionalityltr",
				"directionalityrtl", "|", "", "indent", "|", "justifyleft",
				"justifycenter", "justifyright", "justifyjustify", "|", "link",
				"unlink", "anchor", "|", "imagenone", "imageleft",
				"imageright", "imagecenter", "|", "insertimage", "emotion",
				"insertframe","highlightcode", "horizontal", "pagebreak", "template",
				"background", "spechars", "|", "inserttable", "deletetable",
				"insertparagraphbeforetable", "insertrow", "deleterow",
				"insertcol", "deletecol", "mergecells", "mergeright",
				"mergedown", "splittocells", "splittorows", "splittocols", "|",
				"searchreplace", "help", "|", "hidedomain", "textfield",
				"textarea", "checkbox", "radioinput", "selectinput", "|",
				"userunit",/*"dictionary",*/ "userselector",/* "roleselector",*/
				//"positionselector", "depselector","officecontrol", "ckeditor", "|",
				"attachement", "datepicker","|", "subtable", "importform",
				"exportform", "opinion" ] ],
		labelMap : {
			"anchor" : "",
			"undo" : ""
		},
		webAppKey : "",
		editorModel : "designModel",
		iframeCssUrl : __ctxPath + "/css/form.css",
		emotionLocalization : true,
		autoHeightEnabled : false,
		zIndex : 1,     //编辑器层级的基数,默认是900
		serialize : {
			blackList : {}
		}
	};
})();
(function() {
	var d = [ "editor.js", "core/browser.js", "core/utils.js",
			"core/EventBase.js", "core/dom/dtd.js", "core/dom/domUtils.js",
			"core/dom/Range.js", "core/dom/Selection.js", "core/Editor.js",
			"core/ajax.js", "plugins/inserthtml.js", "plugins/autotypeset.js",
			"plugins/autosubmit.js", "plugins/background.js",
			"plugins/image.js", "plugins/justify.js", "plugins/font.js",
			"plugins/link.js", "plugins/map.js", "plugins/iframe.js",
			"plugins/scrawl.js", "plugins/removeformat.js",
			"plugins/blockquote.js", "plugins/convertcase.js",
			"plugins/indent.js", "plugins/print.js", "plugins/preview.js",
			"plugins/spechars.js", "plugins/emotion.js",
			"plugins/selectall.js", "plugins/paragraph.js",
			"plugins/directionality.js", "plugins/horizontal.js",
			"plugins/time.js", "plugins/rowspacing.js",
			"plugins/lineheight.js", "plugins/cleardoc.js",
			"plugins/anchor.js", "plugins/delete.js", "plugins/wordcount.js",
			"plugins/pagebreak.js", "plugins/wordimage.js", "plugins/undo.js",
			"plugins/paste.js", "plugins/list.js", "plugins/source.js",
			"plugins/shortcutkeys.js", "plugins/enterkey.js",
			"plugins/keystrokes.js", "plugins/fiximgclick.js",
			"plugins/autolink.js", "plugins/autoheight.js",
			"plugins/autofloat.js", "plugins/highlightcode.js",
			"plugins/serialize.js", "plugins/video.js", "plugins/table.js",
			"plugins/contextmenu.js", "plugins/basestyle.js",
			"plugins/elementpath.js", "plugins/formatmatch.js",
			"plugins/searchreplace.js", "plugins/customstyle.js",
			"plugins/catchremoteimage.js", "plugins/snapscreen.js",
			"plugins/attachment.js", "plugins/webapp.js",
			"plugins/template.js", "plugins/input.js", "plugins/importform.js",
			"plugins/exportform.js", "ui/ui.js", "ui/uiutils.js",
			"ui/uibase.js", "ui/separator.js", "ui/mask.js", "ui/popup.js",
			"ui/colorpicker.js", "ui/tablepicker.js", "ui/stateful.js",
			"ui/button.js", "ui/splitbutton.js", "ui/colorbutton.js",
			"ui/tablebutton.js", "ui/autotypesetpicker.js",
			"ui/autotypesetbutton.js", "ui/toolbar.js", "ui/menu.js",
			"ui/combox.js", "ui/dialog.js", "ui/menubutton.js",
			"ui/editorui.js", "ui/editor.js", "ui/multiMenu.js" ], b = __ctxPath
			+ "/js/ueditor/_src/";
	for ( var a = 0, c; c = d[a++];) {
		document.write('<script type="text/javascript" src="' + b + c
				+ '"><\/script>');
	}
})();
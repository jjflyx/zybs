(function(){var b=baidu.editor.utils,a=baidu.editor.dom.domUtils,d=baidu.editor.ui.UIBase,c=baidu.editor.ui.uiUtils;var e=baidu.editor.ui.Mask=function(f){this.initOptions(f);this.initUIBase();};e.prototype={getHtmlTpl:function(){return'<div id="##" class="edui-mask %%" onmousedown="return $$._onMouseDown(event, this);"></div>';},postRender:function(){var f=this;a.on(window,"resize",function(){setTimeout(function(){if(!f.isHidden()){f._fill();}});});},show:function(f){this._fill();this.getDom().style.display="";this.getDom().style.zIndex=f;},hide:function(){this.getDom().style.display="none";this.getDom().style.zIndex="";},isHidden:function(){return this.getDom().style.display=="none";},_onMouseDown:function(){return false;},_fill:function(){var f=this.getDom();var g=c.getViewportRect();f.style.width=g.width+"px";f.style.height=g.height+"px";}};b.inherits(e,d);})();
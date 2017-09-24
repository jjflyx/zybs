(function(){var h=baidu.editor.utils,d=baidu.editor.dom.domUtils,a=baidu.editor.ui.uiUtils,e=baidu.editor.ui.Mask,b=baidu.editor.ui.UIBase,f=baidu.editor.ui.Button,g=baidu.editor.ui.Dialog=function(j){this.initOptions(h.extend({autoReset:true,draggable:true,onok:function(){},oncancel:function(){},onclose:function(l,k){return k?this.onok():this.oncancel();}},j));this.initDialog();};var c;var i;g.prototype={draggable:false,uiName:"dialog",initDialog:function(){var k=this;this.initUIBase();this.modalMask=(c||(c=new e({className:"edui-dialog-modalmask"})));this.dragMask=(i||(i=new e({className:"edui-dialog-dragmask"})));this.closeButton=new f({className:"edui-dialog-closebutton",title:k.closeDialog,onclick:function(){k.close(false);}});if(this.buttons){for(var j=0;j<this.buttons.length;j++){if(!(this.buttons[j] instanceof f)){this.buttons[j]=new f(this.buttons[j]);}}}},fitSize:function(){var j=this.getDom("body");var k=this.mesureSize();j.style.width=k.width+"px";j.style.height=k.height+"px";return k;},safeSetOffset:function(p){var l=this;var j=l.getDom();var o=a.getViewportRect();var k=a.getClientRect(j);var n=p.left;if(n+k.width>o.right){n=o.right-k.width;}var m=p.top;if(m+k.height>o.bottom){m=o.bottom-k.height;}j.style.left=Math.max(n,0)+"px";j.style.top=Math.max(m,0)+"px";},showAtCenter:function(){this.getDom().style.display="";var o=a.getViewportRect();var j=this.fitSize();var n=this.getDom("titlebar").offsetHeight|0;var m=o.width/2-j.width/2;var l=o.height/2-(j.height-n)/2-n;var k=this.getDom();this.safeSetOffset({left:Math.max(m|0,0),top:Math.max(l|0,0)});if(!d.hasClass(k,"edui-state-centered")){k.className+=" edui-state-centered";}this._show();},getContentHtml:function(){var j="";if(typeof this.content=="string"){j=this.content;}else{if(this.iframeUrl){j='<span id="'+this.id+'_contmask" class="dialogcontmask"></span><iframe id="'+this.id+'_iframe" class="%%-iframe" height="100%" width="100%" frameborder="0" src="'+this.iframeUrl+'"></iframe>';}}return j;},getHtmlTpl:function(){var k="";if(this.buttons){var l=[];for(var j=0;j<this.buttons.length;j++){l[j]=this.buttons[j].renderHtml();}k='<div class="%%-foot">'+'<div id="##_buttons" class="%%-buttons">'+l.join("")+"</div>"+"</div>";}return'<div id="##" class="%%"><div class="%%-wrap"><div id="##_body" class="%%-body">'+'<div class="%%-shadow"></div>'+'<div id="##_titlebar" class="%%-titlebar">'+'<div class="%%-draghandle" onmousedown="$$._onTitlebarMouseDown(event, this);">'+'<span class="%%-caption">'+(this.title||"")+"</span>"+"</div>"+this.closeButton.renderHtml()+"</div>"+'<div id="##_content" class="%%-content">'+(this.autoReset?"":this.getContentHtml())+"</div>"+k+"</div></div></div>";},postRender:function(){if(!this.modalMask.getDom()){this.modalMask.render();this.modalMask.hide();}if(!this.dragMask.getDom()){this.dragMask.render();this.dragMask.hide();}var k=this;this.addListener("show",function(){k.modalMask.show(this.getDom().style.zIndex-2);});this.addListener("hide",function(){k.modalMask.hide();});if(this.buttons){for(var j=0;j<this.buttons.length;j++){this.buttons[j].postRender();}}d.on(window,"resize",function(){setTimeout(function(){if(!k.isHidden()){k.safeSetOffset(a.getClientRect(k.getDom()));}});});this._hide();},mesureSize:function(){var j=this.getDom("body");var k=a.getClientRect(this.getDom("content")).width;var l=j.style;l.width=k;return a.getClientRect(j);},_onTitlebarMouseDown:function(j,k){if(this.draggable){var m;var n=a.getViewportRect();var l=this;a.startDrag(j,{ondragstart:function(){m=a.getClientRect(l.getDom());l.getDom("contmask").style.visibility="visible";l.dragMask.show(l.getDom().style.zIndex-1);},ondragmove:function(o,r){var q=m.left+o;var p=m.top+r;l.safeSetOffset({left:q,top:p});},ondragstop:function(){l.getDom("contmask").style.visibility="hidden";d.removeClasses(l.getDom(),["edui-state-centered"]);l.dragMask.hide();}});}},reset:function(){this.getDom("content").innerHTML=this.getContentHtml();},clearContent:function(){var j=this.getDom("");j.className="";j.innerHTML="";},_show:function(){if(this._hidden){this.getDom().style.display="";this.editor.container.style.zIndex&&(this.getDom().style.zIndex=this.editor.container.style.zIndex*1+10);this._hidden=false;this.fireEvent("show");baidu.editor.ui.uiUtils.getFixedLayer().style.zIndex=this.getDom().style.zIndex-4;}},isHidden:function(){return this._hidden;},_hide:function(){if(!this._hidden){this.getDom().style.display="none";this.getDom().style.zIndex="";this._hidden=true;this.fireEvent("hide");}},open:function(){if(this.autoReset){try{this.reset();}catch(k){this.render();this.open();}}this.showAtCenter();if(this.iframeUrl){try{this.getDom("iframe").focus();}catch(j){}}},_onCloseButtonClick:function(j,k){this.close(false);},close:function(j){if(this.fireEvent("close",j)!==false){this._hide();}}};h.inherits(g,b);})();
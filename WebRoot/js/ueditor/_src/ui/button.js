(function(){var b=baidu.editor.utils,d=baidu.editor.ui.UIBase,c=baidu.editor.ui.Stateful,a=baidu.editor.ui.Button=function(e){this.initOptions(e);this.initButton();};a.prototype={uiName:"button",label:"",title:"",showIcon:true,showText:true,initButton:function(){this.initUIBase();this.Stateful_init();},getHtmlTpl:function(){return'<div id="##" class="edui-box %%">'+'<div id="##_state" stateful>'+'<div class="%%-wrap"><div id="##_body" unselectable="on" '+(this.title?'title="'+this.title+'"':"")+' class="%%-body" onmousedown="return false;" onclick="return $$._onClick();">'+(this.showIcon?'<div class="edui-box edui-icon"></div>':"")+(this.showText?'<div class="edui-box edui-label">'+this.label+"</div>":"")+"</div>"+"</div>"+"</div></div>";},postRender:function(){this.Stateful_postRender();this.setDisabled(this.disabled);},_onClick:function(){if(!this.isDisabled()){this.fireEvent("click");}}};b.inherits(a,d);b.extend(a.prototype,c);})();
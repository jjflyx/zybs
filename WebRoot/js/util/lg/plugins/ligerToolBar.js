(function(a){a.fn.ligerToolBar=function(b){return a.ligerui.run.call(this,"ligerToolBar",arguments);};a.fn.ligerGetToolBarManager=function(){return a.ligerui.run.call(this,"ligerGetToolBarManager",arguments);};a.ligerDefaults.ToolBar={};a.ligerMethos.ToolBar={};a.ligerui.controls.ToolBar=function(c,b){a.ligerui.controls.ToolBar.base.constructor.call(this,c,b);};a.ligerui.controls.ToolBar.ligerExtend(a.ligerui.core.UIComponent,{__getType:function(){return"ToolBar";},__idPrev:function(){return"ToolBar";},_extendMethods:function(){return a.ligerMethos.ToolBar;},_render:function(){var b=this,c=this.options;b.toolBar=a(this.element);b.toolBar.addClass("l-toolbar");b.set(c);},_setItems:function(b){var c=this;a(b).each(function(d,e){c.addItem(e);});},addItem:function(d){var c=this,e=this.options;if(d.line){c.toolBar.append('<div class="l-bar-separator"></div>');return;}var b=a('<div class="l-toolbar-item l-panel-btn"><span></span><div class="l-panel-btn-l"></div><div class="l-panel-btn-r"></div></div>');c.toolBar.append(b);d.id&&b.attr("toolbarid",d.id);if(d.img){b.append("<img src='"+d.img+"' />");b.addClass("l-toolbar-item-hasicon");}else{if(d.icon){b.append("<div class='l-icon l-icon-"+d.icon+"'></div>");b.addClass("l-toolbar-item-hasicon");}}d.text&&a("span:first",b).html(d.text);d.disable&&b.addClass("l-toolbar-item-disable");d.click&&b.click(function(){d.click(d);});b.hover(function(){a(this).addClass("l-panel-btn-over");},function(){a(this).removeClass("l-panel-btn-over");});}});})(jQuery);
(function(a){a.fn.ligerMenuBar=function(b){return a.ligerui.run.call(this,"ligerMenuBar",arguments);};a.fn.ligerGetMenuBarManager=function(){return a.ligerui.run.call(this,"ligerGetMenuBarManager",arguments);};a.ligerDefaults.MenuBar={};a.ligerMethos.MenuBar={};a.ligerui.controls.MenuBar=function(c,b){a.ligerui.controls.MenuBar.base.constructor.call(this,c,b);};a.ligerui.controls.MenuBar.ligerExtend(a.ligerui.core.UIComponent,{__getType:function(){return"MenuBar";},__idPrev:function(){return"MenuBar";},_extendMethods:function(){return a.ligerMethos.MenuBar;},_render:function(){var b=this,c=this.options;b.menubar=a(this.element);if(!b.menubar.hasClass("l-menubar")){b.menubar.addClass("l-menubar");}if(c&&c.items){a(c.items).each(function(d,e){b.addItem(e);});}a(document).click(function(){a(".l-panel-btn-selected",b.menubar).removeClass("l-panel-btn-selected");});b.set(c);},addItem:function(d){var c=this,e=this.options;var b=a('<div class="l-menubar-item l-panel-btn"><span></span><div class="l-panel-btn-l"></div><div class="l-panel-btn-r"></div><div class="l-menubar-item-down"></div></div>');c.menubar.append(b);d.id&&b.attr("menubarid",d.id);d.text&&a("span:first",b).html(d.text);d.disable&&b.addClass("l-menubar-item-disable");d.click&&b.click(function(){d.click(d);});if(d.menu){var f=a.ligerMenu(d.menu);b.hover(function(){c.actionMenu&&c.actionMenu.hide();var h=a(this).offset().left;var g=a(this).offset().top+a(this).height();f.show({top:g,left:h});c.actionMenu=f;a(this).addClass("l-panel-btn-over l-panel-btn-selected").siblings(".l-menubar-item").removeClass("l-panel-btn-selected");},function(){a(this).removeClass("l-panel-btn-over");});}else{b.hover(function(){a(this).addClass("l-panel-btn-over");},function(){a(this).removeClass("l-panel-btn-over");});a(".l-menubar-item-down",b).remove();}}});})(jQuery);
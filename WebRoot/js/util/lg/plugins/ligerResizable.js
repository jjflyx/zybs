(function(a){a.fn.ligerResizable=function(b){return a.ligerui.run.call(this,"ligerResizable",arguments,{idAttrName:"ligeruiresizableid",hasElement:false,propertyToElemnt:"target"});};a.fn.ligerGetResizableManager=function(){return a.ligerui.run.call(this,"ligerGetResizableManager",arguments,{idAttrName:"ligeruiresizableid",hasElement:false,propertyToElemnt:"target"});};a.ligerDefaults.Resizable={handles:"n, e, s, w, ne, se, sw, nw",maxWidth:2000,maxHeight:2000,minWidth:20,minHeight:20,scope:3,animate:false,onStartResize:function(b){},onResize:function(b){},onStopResize:function(b){},onEndResize:null};a.ligerui.controls.Resizable=function(b){a.ligerui.controls.Resizable.base.constructor.call(this,null,b);};a.ligerui.controls.Resizable.ligerExtend(a.ligerui.core.UIComponent,{__getType:function(){return"Resizable";},__idPrev:function(){return"Resizable";},_render:function(){var b=this,c=this.options;b.target=a(c.target);b.set(c);b.target.mousemove(function(f){if(c.disabled){return;}b.dir=b._getDir(f);if(b.dir){b.target.css("cursor",b.dir+"-resize");}else{if(b.target.css("cursor").indexOf("-resize")>0){b.target.css("cursor","default");}}if(c.target.ligeruidragid){var d=a.ligerui.get(c.target.ligeruidragid);if(d&&b.dir){d.set("disabled",true);}else{if(d){d.set("disabled",false);}}}}).mousedown(function(d){if(c.disabled){return;}if(b.dir){b._start(d);}});},_rendered:function(){this.options.target.ligeruiresizableid=this.id;},_getDir:function(j){var i=this,b=this.options;var d="";var m=i.target.offset();var c=i.target.width();var k=i.target.height();var l=b.scope;var h=j.pageX||j.screenX;var f=j.pageY||j.screenY;if(f>=m.top&&f<m.top+l){d+="n";}else{if(f<=m.top+k&&f>m.top+k-l){d+="s";}}if(h>=m.left&&h<m.left+l){d+="w";}else{if(h<=m.left+c&&h>m.left+c-l){d+="e";}}if(b.handles=="all"||d==""){return d;}if(a.inArray(d,i.handles)!=-1){return d;}return"";},_setHandles:function(b){if(!b){return;}this.handles=b.replace(/(\s*)/g,"").split(",");},_createProxy:function(){var b=this;b.proxy=a('<div class="l-resizable"></div>');b.proxy.width(b.target.width()).height(b.target.height());b.proxy.attr("resizableid",b.id).appendTo("body");},_removeProxy:function(){var b=this;if(b.proxy){b.proxy.remove();b.proxy=null;}},_start:function(d){var b=this,c=this.options;b._createProxy();b.proxy.css({left:b.target.offset().left,top:b.target.offset().top,position:"absolute"});b.current={dir:b.dir,left:b.target.offset().left,top:b.target.offset().top,startX:d.pageX||d.screenX,startY:d.pageY||d.clientY,width:b.target.width(),height:b.target.height()};a(document).bind("selectstart.resizable",function(){return false;});a(document).bind("mouseup.resizable",function(){b._stop.apply(b,arguments);});a(document).bind("mousemove.resizable",function(){b._drag.apply(b,arguments);});b.proxy.show();b.trigger("startResize",[b.current,d]);},changeBy:{t:["n","ne","nw"],l:["w","sw","nw"],w:["w","sw","nw","e","ne","se"],h:["n","ne","nw","s","se","sw"]},_drag:function(h){var d=this,f=this.options;if(!d.current){return;}if(!d.proxy){return;}d.proxy.css("cursor",d.current.dir==""?"default":d.current.dir+"-resize");var c=h.pageX||h.screenX;var b=h.pageY||h.screenY;d.current.diffX=c-d.current.startX;d.current.diffY=b-d.current.startY;d._applyResize(d.proxy);d.trigger("resize",[d.current,h]);},_stop:function(d){var b=this,c=this.options;if(b.hasBind("stopResize")){if(b.trigger("stopResize",[b.current,d])!=false){b._applyResize();}}else{b._applyResize();}b._removeProxy();b.trigger("endResize",[b.current,d]);a(document).unbind("selectstart.resizable");a(document).unbind("mousemove.resizable");a(document).unbind("mouseup.resizable");},_applyResize:function(b){var c=this,d=this.options;var e={left:c.current.left,top:c.current.top,width:c.current.width,height:c.current.height};var f=false;if(!b){b=c.target;f=true;if(!isNaN(parseInt(c.target.css("top")))){e.top=parseInt(c.target.css("top"));}else{e.top=0;}if(!isNaN(parseInt(c.target.css("left")))){e.left=parseInt(c.target.css("left"));}else{e.left=0;}}if(a.inArray(c.current.dir,c.changeBy.l)>-1){e.left+=c.current.diffX;c.current.diffLeft=c.current.diffX;}else{if(f){delete e.left;}}if(a.inArray(c.current.dir,c.changeBy.t)>-1){e.top+=c.current.diffY;c.current.diffTop=c.current.diffY;}else{if(f){delete e.top;}}if(a.inArray(c.current.dir,c.changeBy.w)>-1){e.width+=(c.current.dir.indexOf("w")==-1?1:-1)*c.current.diffX;c.current.newWidth=e.width;}else{if(f){delete e.width;}}if(a.inArray(c.current.dir,c.changeBy.h)>-1){e.height+=(c.current.dir.indexOf("n")==-1?1:-1)*c.current.diffY;c.current.newHeight=e.height;}else{if(f){delete e.height;}}if(f&&d.animate){b.animate(e);}else{b.css(e);}}});})(jQuery);
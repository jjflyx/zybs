(function(a){a.fn.ligerTree=function(b){return a.ligerui.run.call(this,"ligerTree",arguments);};a.fn.ligerGetTreeManager=function(){return a.ligerui.run.call(this,"ligerGetTreeManager",arguments);};a.ligerDefaults.Tree={url:null,data:null,checkbox:true,autoCheckboxEven:true,parentIcon:"folder",childIcon:"leaf",textFieldName:"text",attribute:["id","url"],treeLine:true,nodeWidth:90,statusName:"__status",isLeaf:null,single:false,onBeforeExpand:function(){},onContextmenu:function(){},onExpand:function(){},onBeforeCollapse:function(){},onCollapse:function(){},onBeforeSelect:function(){},onSelect:function(){},onBeforeCancelSelect:function(){},onCancelselect:function(){},onCheck:function(){},onSuccess:function(){},onError:function(){},onClick:function(){},idFieldName:"id",parentIDFieldName:null,topParentIDValue:0,onBeforeAppend:function(){},onAppend:function(){},onAfterAppend:function(){},slide:true,iconFieldName:"icon",nodeDraggable:false,nodeDraggingRender:null,btnClickToToggleOnly:true};a.ligerui.controls.Tree=function(c,b){a.ligerui.controls.Tree.base.constructor.call(this,c,b);};a.ligerui.controls.Tree.ligerExtend(a.ligerui.core.UIComponent,{_init:function(){a.ligerui.controls.Tree.base._init.call(this);var b=this,c=this.options;if(c.single){c.autoCheckboxEven=false;}},_render:function(){var b=this,c=this.options;b.set(c,true);b.tree=a(b.element);b.tree.addClass("l-tree");b.sysAttribute=["isexpand","ischecked","href","style"];b.loading=a("<div class='l-tree-loading'></div>");b.tree.after(b.loading);b.data=[];b.maxOutlineLevel=1;b.treedataindex=0;b._applyTree();b._setTreeEven();b.set(c,false);},_setTreeLine:function(b){if(b){this.tree.removeClass("l-tree-noline");}else{this.tree.addClass("l-tree-noline");}},_setUrl:function(b){if(b){this.loadData(null,b);}},_setData:function(b){if(b){this.append(null,b);}},setData:function(b){this.set("data",b);},getData:function(){return this.data;},hasChildren:function(b){if(this.options.isLeaf){return this.options.isLeaf(b);}return b.children?true:false;},getParent:function(f,e){var d=this;f=d.getNodeDom(f);var c=d.getParentTreeItem(f,e);if(!c){return null;}var b=a(c).attr("treedataindex");return d._getDataNodeByTreeDataIndex(b);},getParentTreeItem:function(j,h){var d=this;j=d.getNodeDom(j);var f=a(j);if(f.parent().hasClass("l-tree")){return null;}if(h==undefined){if(f.parent().parent("li").length==0){return null;}return f.parent().parent("li")[0];}var e=parseInt(f.attr("outlinelevel"));var c=f;for(var b=e-1;b>=h;b--){c=c.parent().parent("li");}return c[0];},getChecked:function(){var c=this,d=this.options;if(!this.options.checkbox){return null;}var b=[];a(".l-checkbox-checked",c.tree).parent().parent("li").each(function(){var e=parseInt(a(this).attr("treedataindex"));b.push({target:this,data:c._getDataNodeByTreeDataIndex(c.data,e)});});return b;},getSelected:function(){var c=this,e=this.options;var b={};b.target=a(".l-selected",c.tree).parent("li")[0];if(b.target){var d=parseInt(a(b.target).attr("treedataindex"));b.data=c._getDataNodeByTreeDataIndex(c.data,d);return b;}return null;},upgrade:function(c){var b=this,d=this.options;a(".l-note",c).each(function(){a(this).removeClass("l-note").addClass("l-expandable-open");});a(".l-note-last",c).each(function(){a(this).removeClass("l-note-last").addClass("l-expandable-open");});a("."+b._getChildNodeClassName(),c).each(function(){a(this).removeClass(b._getChildNodeClassName()).addClass(b._getParentNodeClassName(true));});},demotion:function(c){var b=this,d=this.options;if(!c&&c[0].tagName.toLowerCase()!="li"){return;}var e=a(c).hasClass("l-last");a(".l-expandable-open",c).each(function(){a(this).removeClass("l-expandable-open").addClass(e?"l-note-last":"l-note");});a(".l-expandable-close",c).each(function(){a(this).removeClass("l-expandable-close").addClass(e?"l-note-last":"l-note");});a("."+b._getParentNodeClassName(true),c).each(function(){a(this).removeClass(b._getParentNodeClassName(true)).addClass(b._getChildNodeClassName());});},collapseAll:function(){var b=this,c=this.options;a(".l-expandable-open",b.tree).click();},expandAll:function(){var b=this,c=this.options;a(".l-expandable-close",b.tree).click();},loadData:function(e,c,h){var d=this,f=this.options;d.loading.show();var b=h?"post":"get";h=h||[];a.ajax({type:b,url:c,data:h,dataType:"json",success:function(g){if(!g){return;}d.loading.hide();d.append(e,g);d.trigger("success",[g]);},error:function(g,k,j){try{d.loading.hide();d.trigger("error",[g,k,j]);}catch(i){}}});},clear:function(){var b=this,c=this.options;a("> li",b.tree).each(function(){b.remove(this);});},getNodeDom:function(c){var b=this,d=this.options;if(c==null){return c;}if(typeof(c)=="string"||typeof(c)=="number"){return a("li[treedataindex="+c+"]",b.tree).get(0);}else{if(typeof(c)=="object"&&"treedataindex" in c){return b.getNodeDom(c["treedataindex"]);}}return c;},remove:function(f){var c=this,h=this.options;f=c.getNodeDom(f);var e=parseInt(a(f).attr("treedataindex"));var d=c._getDataNodeByTreeDataIndex(c.data,e);if(d){c._setTreeDataStatus([d],"delete");}var b=c.getParentTreeItem(f);if(h.checkbox){c._setParentCheckboxStatus(a(f));}a(f).remove();c._updateStyle(b?a("ul:first",b):c.tree);},_updateStyle:function(b){var c=this,e=this.options;var f=a(" > li",b);var d=f.length;if(!d){return;}f.each(function(g,h){if(g==0&&!a(this).hasClass("l-first")){a(this).addClass("l-first");}if(g==d-1&&!a(this).hasClass("l-last")){a(this).addClass("l-last");}if(g==0&&g==d-1){a(this).addClass("l-onlychild");}a("> div .l-note,> div .l-note-last",this).removeClass("l-note l-note-last").addClass(g==d-1?"l-note-last":"l-note");c._setTreeItem(this,{isLast:g==d-1});});},update:function(h,c){var d=this,f=this.options;h=d.getNodeDom(h);var e=parseInt(a(h).attr("treedataindex"));nodedata=d._getDataNodeByTreeDataIndex(d.data,e);for(var b in c){nodedata[b]=c[b];if(b==f.textFieldName){a("> .l-body > span",h).text(c[b]);}}},append:function(j,n,l,q){var k=this,c=this.options;j=k.getNodeDom(j);if(k.trigger("beforeAppend",[j,n])==false){return false;}if(!n||!n.length){return false;}if(c.idFieldName&&c.parentIDFieldName){n=k.arrayToTree(n,c.idFieldName,c.parentIDFieldName);}k._addTreeDataIndexToData(n);k._setTreeDataStatus(n,"add");if(l!=null){l=k.getNodeDom(l);}k.trigger("append",[j,n]);k._appendData(j,n);if(j==null){var d=k._getTreeHTMLByData(n,1,[],true);d[d.length-1]=d[0]="";if(l!=null){a(l)[q?"after":"before"](d.join(""));k._updateStyle(j?a("ul:first",j):k.tree);}else{if(a("> li:last",k.tree).length>0){k._setTreeItem(a("> li:last",k.tree)[0],{isLast:false});}k.tree.append(d.join(""));}a(".l-body",k.tree).hover(function(){a(this).addClass("l-over");},function(){a(this).removeClass("l-over");});k._upadteTreeWidth();k.trigger("afterAppend",[j,n]);return;}var b=a(j);var o=parseInt(b.attr("outlinelevel"));var h=a("> ul",b).length>0;if(!h){b.append("<ul class='l-children'></ul>");k.upgrade(j);}var f=[];for(var e=1;e<=o-1;e++){var m=a(k.getParentTreeItem(j,e));f.push(m.hasClass("l-last"));}f.push(b.hasClass("l-last"));var d=k._getTreeHTMLByData(n,o+1,f,true);d[d.length-1]=d[0]="";if(l!=null){a(l)[q?"after":"before"](d.join(""));k._updateStyle(j?a("ul:first",j):k.tree);}else{if(a("> .l-children > li:last",b).length>0){k._setTreeItem(a("> .l-children > li:last",b)[0],{isLast:false});}a(">.l-children",j).append(d.join(""));}k._upadteTreeWidth();a(">.l-children .l-body",j).hover(function(){a(this).addClass("l-over");},function(){a(this).removeClass("l-over");});k.trigger("afterAppend",[j,n]);},cancelSelect:function(d){var c=this,i=this.options;var b=c.getNodeDom(d);var j=a(b);var f=parseInt(j.attr("treedataindex"));var e=c._getDataNodeByTreeDataIndex(c.data,f);var h=a(">div:first",j);if(i.checkbox){a(".l-checkbox",h).removeClass("l-checkbox-checked").addClass("l-checkbox-unchecked");}else{h.removeClass("l-selected");}c.trigger("cancelSelect",[{data:e,target:j[0]}]);},selectNode:function(c){var b=this,i=this.options;var h=null;if(typeof(c)=="function"){h=c;}else{if(typeof(c)=="object"){var j=a(c);var e=parseInt(j.attr("treedataindex"));var d=b._getDataNodeByTreeDataIndex(b.data,e);var f=a(">div:first",j);if(i.checkbox){a(".l-checkbox",f).removeClass("l-checkbox-unchecked").addClass("l-checkbox-checked");}else{f.addClass("l-selected");}b.trigger("select",[{data:d,target:j[0]}]);return;}else{h=function(g){if(!g[i.idFieldName]){return false;}return g[i.idFieldName].toString()==c.toString();};}}a("li",b.tree).each(function(){var l=a(this);var k=parseInt(l.attr("treedataindex"));var g=b._getDataNodeByTreeDataIndex(b.data,k);if(h(g,k)){b.selectNode(this);}else{b.cancelSelect(this);}});},getTextByID:function(e){var b=this,d=this.options;var c=b.getDataByID(e);if(!c){return null;}return c[d.textFieldName];},getDataByID:function(e){var b=this,d=this.options;var c=null;a("li",b.tree).each(function(){if(c){return;}var h=a(this);var g=parseInt(h.attr("treedataindex"));var f=b._getDataNodeByTreeDataIndex(b.data,g);if(f[d.idFieldName].toString()==e.toString()){c=f;}});return c;},arrayToTree:function(f,b,h){if(!f||!f.length){return[];}var l=[];var d={};var j=f.length;for(var g=0;g<j;g++){var c=f[g];d[c[b]]=c;}for(var g=0;g<j;g++){var e=f[g];var k=d[e[h]];if(!k){l.push(e);continue;}k.children=k.children||[];k.children.push(e);}return l;},_getDataNodeByTreeDataIndex:function(e,d){var c=this,f=this.options;for(var b=0;b<e.length;b++){if(e[b].treedataindex==d){return e[b];}if(e[b].children){var h=c._getDataNodeByTreeDataIndex(e[b].children,d);if(h){return h;}}}return null;},_setTreeDataStatus:function(d,b){var c=this,e=this.options;a(d).each(function(){this[e.statusName]=b;if(this.children){c._setTreeDataStatus(this.children,b);}});},_addTreeDataIndexToData:function(c){var b=this,d=this.options;a(c).each(function(){if(this.treedataindex!=undefined){return;}this.treedataindex=b.treedataindex++;if(this.children){b._addTreeDataIndexToData(this.children);}});},_addToNodes:function(c){var b=this,d=this.options;b.nodes=b.nodes||[];if(a.inArray(c,b.nodes)==-1){b.nodes.push(c);}if(c.children){a(c.children).each(function(e,f){b._addToNodes(f);});}},_appendData:function(f,e){var b=this,h=this.options;var d=parseInt(a(f).attr("treedataindex"));var c=b._getDataNodeByTreeDataIndex(b.data,d);if(b.treedataindex==undefined){b.treedataindex=0;}if(c&&c.children==undefined){c.children=[];}a(e).each(function(g,j){if(c){c.children[c.children.length]=j;}else{b.data[b.data.length]=j;}b._addToNodes(j);});},_setTreeItem:function(f,c){var d=this,h=this.options;if(!c){return;}f=d.getNodeDom(f);var e=a(f);var b=parseInt(e.attr("outlinelevel"));if(c.isLast!=undefined){if(c.isLast==true){e.removeClass("l-last").addClass("l-last");a("> div .l-note",e).removeClass("l-note").addClass("l-note-last");a(".l-children li",e).find(".l-box:eq("+(b-1)+")").removeClass("l-line");}else{if(c.isLast==false){e.removeClass("l-last");a("> div .l-note-last",e).removeClass("l-note-last").addClass("l-note");a(".l-children li",e).find(".l-box:eq("+(b-1)+")").removeClass("l-line").addClass("l-line");}}}},_upadteTreeWidth:function(){var b=this,c=this.options;var d=b.maxOutlineLevel*22;if(c.checkbox){d+=22;}if(c.parentIcon||c.childIcon){d+=22;}d+=c.nodeWidth;b.tree.width(d);},_getChildNodeClassName:function(){var b=this,c=this.options;return"l-tree-icon-"+c.childIcon;},_getParentNodeClassName:function(c){var d=this,e=this.options;var b="l-tree-icon-"+e.parentIcon;if(c){b+="-open";}return b;},_getTreeHTMLByData:function(m,u,n,r){var s=this,c=this.options;if(s.maxOutlineLevel<u){s.maxOutlineLevel=u;}n=n||[];u=u||1;var v=[];if(!r){v.push('<ul class="l-children" style="display:none">');}else{v.push("<ul class='l-children'>");}for(var q=0;q<m.length;q++){var l=q==0;var f=q==m.length-1;var t=true;var d=m[q];if(d.isexpand==false||d.isexpand=="false"){t=false;}v.push("<li ");if(d.treedataindex!=undefined){v.push('treedataindex="'+d.treedataindex+'" ');}if(t){v.push("isexpand="+d.isexpand+" ");}v.push("outlinelevel="+u+" ");for(var h=0;h<s.sysAttribute.length;h++){if(a(this).attr(s.sysAttribute[h])){m[dataindex][s.sysAttribute[h]]=a(this).attr(s.sysAttribute[h]);}}for(var h=0;h<c.attribute.length;h++){if(d[c.attribute[h]]){v.push(c.attribute[h]+'="'+d[c.attribute[h]]+'" ');}}v.push('class="');l&&v.push("l-first ");f&&v.push("l-last ");l&&f&&v.push("l-onlychild ");v.push('"');v.push(">");v.push('<div class="l-body">');for(var e=0;e<=u-2;e++){if(n[e]){v.push('<div class="l-box"></div>');}else{v.push('<div class="l-box l-line"></div>');}}if(s.hasChildren(d)){if(t){v.push('<div class="l-box l-expandable-open"></div>');}else{v.push('<div class="l-box l-expandable-close"></div>');}if(c.checkbox){if(d.ischecked){v.push('<div class="l-box l-checkbox l-checkbox-checked"></div>');}else{v.push('<div class="l-box l-checkbox l-checkbox-unchecked"></div>');}}if(c.parentIcon){v.push('<div class="l-box l-tree-icon ');v.push(s._getParentNodeClassName(c.parentIcon?true:false)+" ");if(c.iconFieldName&&d[c.iconFieldName]){v.push("l-tree-icon-none");}v.push('">');if(c.iconFieldName&&d[c.iconFieldName]){v.push('<img src="'+d[c.iconFieldName]+'" />');}v.push("</div>");}}else{if(f){v.push('<div class="l-box l-note-last"></div>');}else{v.push('<div class="l-box l-note"></div>');}if(c.checkbox){if(d.ischecked){v.push('<div class="l-box l-checkbox l-checkbox-checked"></div>');}else{v.push('<div class="l-box l-checkbox l-checkbox-unchecked"></div>');}}if(c.childIcon){v.push('<div class="l-box l-tree-icon ');v.push(s._getChildNodeClassName()+" ");if(c.iconFieldName&&d[c.iconFieldName]){v.push("l-tree-icon-none");}v.push('">');if(c.iconFieldName&&d[c.iconFieldName]){v.push('<img src="'+d[c.iconFieldName]+'" />');}v.push("</div>");}}v.push("<span>"+d[c.textFieldName]+"</span></div>");if(s.hasChildren(d)){var b=[];for(var e=0;e<n.length;e++){b.push(n[e]);}b.push(f);v.push(s._getTreeHTMLByData(d.children,u+1,b,t).join(""));}v.push("</li>");}v.push("</ul>");return v;},_getDataByTreeHTML:function(d){var b=this,e=this.options;var c=[];a("> li",d).each(function(g,h){var k=c.length;c[k]={treedataindex:b.treedataindex++};c[k][e.textFieldName]=a("> span,> a",this).html();for(var f=0;f<b.sysAttribute.length;f++){if(a(this).attr(b.sysAttribute[f])){c[k][b.sysAttribute[f]]=a(this).attr(b.sysAttribute[f]);}}for(var f=0;f<e.attribute.length;f++){if(a(this).attr(e.attribute[f])){c[k][e.attribute[f]]=a(this).attr(e.attribute[f]);}}if(a("> ul",this).length>0){c[k].children=b._getDataByTreeHTML(a("> ul",this));}});return c;},_applyTree:function(){var c=this,d=this.options;c.data=c._getDataByTreeHTML(c.tree);var b=c._getTreeHTMLByData(c.data,1,[],true);b[b.length-1]=b[0]="";c.tree.html(b.join(""));c._upadteTreeWidth();a(".l-body",c.tree).hover(function(){a(this).addClass("l-over");},function(){a(this).removeClass("l-over");});},_applyTreeEven:function(c){var b=this,d=this.options;a("> .l-body",c).hover(function(){a(this).addClass("l-over");},function(){a(this).removeClass("l-over");});},_getSrcElementByEvent:function(k){var f=this;var j=(k.target||k.srcElement);var b=j.tagName.toLowerCase();var i=a(j).parents().add(j);var c=function(g){for(var e=i.length-1;e>=0;e--){if(a(i[e]).hasClass(g)){return i[e];}}return null;};if(i.index(this.element)==-1){return{out:true};}var d={tree:c("l-tree"),node:c("l-body"),checkbox:c("l-checkbox"),icon:c("l-tree-icon"),text:b=="span"};if(d.node){var h=parseInt(a(d.node).parent().attr("treedataindex"));d.data=f._getDataNodeByTreeDataIndex(f.data,h);}return d;},_setTreeEven:function(){var b=this,c=this.options;if(b.hasBind("contextmenu")){b.tree.bind("contextmenu",function(h){var g=(h.target||h.srcElement);var i=null;if(g.tagName.toLowerCase()=="a"||g.tagName.toLowerCase()=="span"||a(g).hasClass("l-box")){i=a(g).parent().parent();}else{if(a(g).hasClass("l-body")){i=a(g).parent();}else{if(g.tagName.toLowerCase()=="li"){i=a(g);}}}if(!i){return;}var f=parseInt(i.attr("treedataindex"));var d=b._getDataNodeByTreeDataIndex(b.data,f);return b.trigger("contextmenu",[{data:d,target:i[0]},h]);});}b.tree.click(function(j){var i=(j.target||j.srcElement);var l=null;if(i.tagName.toLowerCase()=="a"||i.tagName.toLowerCase()=="span"||a(i).hasClass("l-box")){l=a(i).parent().parent();}else{if(a(i).hasClass("l-body")){l=a(i).parent();}else{l=a(i);}}if(!l){return;}var h=parseInt(l.attr("treedataindex"));var g=b._getDataNodeByTreeDataIndex(b.data,h);var f=a("div.l-body:first",l).find("div.l-expandable-open:first,div.l-expandable-close:first");var d=a(i).hasClass("l-expandable-open")||a(i).hasClass("l-expandable-close");if(!a(i).hasClass("l-checkbox")&&!d){if(a(">div:first",l).hasClass("l-selected")){if(b.trigger("beforeCancelSelect",[{data:g,target:l[0]}])==false){return false;}a(">div:first",l).removeClass("l-selected");b.trigger("cancelSelect",[{data:g,target:l[0]}]);}else{if(b.trigger("beforeSelect",[{data:g,target:l[0]}])==false){return false;}a(".l-body",b.tree).removeClass("l-selected");a(">div:first",l).addClass("l-selected");b.trigger("select",[{data:g,target:l[0]}]);}}if(a(i).hasClass("l-checkbox")){if(c.autoCheckboxEven){if(a(i).hasClass("l-checkbox-unchecked")){a(i).removeClass("l-checkbox-unchecked").addClass("l-checkbox-checked");a(".l-children .l-checkbox",l).removeClass("l-checkbox-incomplete l-checkbox-unchecked").addClass("l-checkbox-checked");b.trigger("check",[{data:g,target:l[0]},true]);}else{if(a(i).hasClass("l-checkbox-checked")){a(i).removeClass("l-checkbox-checked").addClass("l-checkbox-unchecked");a(".l-children .l-checkbox",l).removeClass("l-checkbox-incomplete l-checkbox-checked").addClass("l-checkbox-unchecked");b.trigger("check",[{data:g,target:l[0]},false]);}else{if(a(i).hasClass("l-checkbox-incomplete")){a(i).removeClass("l-checkbox-incomplete").addClass("l-checkbox-checked");a(".l-children .l-checkbox",l).removeClass("l-checkbox-incomplete l-checkbox-unchecked").addClass("l-checkbox-checked");b.trigger("check",[{data:g,target:l[0]},true]);}}}b._setParentCheckboxStatus(l);}else{if(a(i).hasClass("l-checkbox-unchecked")){a(i).removeClass("l-checkbox-unchecked").addClass("l-checkbox-checked");if(c.single){a(".l-checkbox",b.tree).not(i).removeClass("l-checkbox-checked").addClass("l-checkbox-unchecked");}b.trigger("check",[{data:g,target:l[0]},true]);}else{if(a(i).hasClass("l-checkbox-checked")){a(i).removeClass("l-checkbox-checked").addClass("l-checkbox-unchecked");b.trigger("check",[{data:g,target:l[0]},false]);}}}}else{if(f.hasClass("l-expandable-open")&&(!c.btnClickToToggleOnly||d)){if(b.trigger("beforeCollapse",[{data:g,target:l[0]}])==false){return false;}f.removeClass("l-expandable-open").addClass("l-expandable-close");if(c.slide){a("> .l-children",l).slideToggle("fast");}else{a("> .l-children",l).toggle();}a("> div ."+b._getParentNodeClassName(true),l).removeClass(b._getParentNodeClassName(true)).addClass(b._getParentNodeClassName());b.trigger("collapse",[{data:g,target:l[0]}]);}else{if(f.hasClass("l-expandable-close")&&(!c.btnClickToToggleOnly||d)){if(b.trigger("beforeExpand",[{data:g,target:l[0]}])==false){return false;}f.removeClass("l-expandable-close").addClass("l-expandable-open");var k=function(){b.trigger("expand",[{data:g,target:l[0]}]);};if(c.slide){a("> .l-children",l).slideToggle("fast",k);}else{a("> .l-children",l).toggle();k();}a("> div ."+b._getParentNodeClassName(),l).removeClass(b._getParentNodeClassName()).addClass(b._getParentNodeClassName(true));}}}b.trigger("click",[{data:g,target:l[0]}]);});if(a.fn.ligerDrag&&c.nodeDraggable){b.nodeDroptip=a("<div class='l-drag-nodedroptip' style='display:none'></div>").appendTo("body");b.tree.ligerDrag({revert:true,animate:false,proxyX:20,proxyY:20,proxy:function(d,l){var m=b._getSrcElementByEvent(l);if(m.node){var k="dragging";if(c.nodeDraggingRender){k=c.nodeDraggingRender(d.draggingNodes,d,b);}else{k="";var h=false;for(var g in d.draggingNodes){var j=d.draggingNodes[g];if(h){k+=",";}k+=j.text;h=true;}}var f=a("<div class='l-drag-proxy' style='display:none'><div class='l-drop-icon l-drop-no'></div>"+k+"</div>").appendTo("body");return f;}},onRevert:function(){return false;},onRendered:function(){this.set("cursor","default");b.children[this.id]=this;},onStartDrag:function(h,g){if(g.button==2){return false;}this.set("cursor","default");var j=b._getSrcElementByEvent(g);if(j.checkbox){return false;}if(c.checkbox){var f=b.getChecked();this.draggingNodes=[];for(var d in f){this.draggingNodes.push(f[d].data);}if(!this.draggingNodes||!this.draggingNodes.length){return false;}}else{this.draggingNodes=[j.data];}this.draggingNode=j.data;this.set("cursor","move");b.nodedragging=true;this.validRange={top:b.tree.offset().top,bottom:b.tree.offset().top+b.tree.height(),left:b.tree.offset().left,right:b.tree.offset().left+b.tree.width()};},onDrag:function(q,r){var t=this.draggingNode;if(!t){return false;}var f=this.draggingNodes?this.draggingNodes:[t];if(b.nodeDropIn==null){b.nodeDropIn=-1;}var m=r.pageX;var h=r.pageY;var u=false;var v=this.validRange;if(m<v.left||m>v.right||h>v.bottom||h<v.top){b.nodeDropIn=-1;b.nodeDroptip.hide();this.proxy.find(".l-drop-icon:first").removeClass("l-drop-yes l-drop-add").addClass("l-drop-no");return;}for(var n=0,g=b.nodes.length;n<g;n++){var k=b.nodes[n];var p=k["treedataindex"];if(t["treedataindex"]==p){u=true;}if(a.inArray(k,f)!=-1){continue;}var w=u?true:false;if(b.nodeDropIn!=-1&&b.nodeDropIn!=p){continue;}var d=a("li[treedataindex="+p+"] div:first",b.tree);var j=d.offset();var o={top:j.top,bottom:j.top+d.height(),left:b.tree.offset().left,right:b.tree.offset().left+b.tree.width()};if(m>o.left&&m<o.right&&h>o.top&&h<o.bottom){var s=j.top;if(w){s+=d.height();}b.nodeDroptip.css({left:o.left,top:s,width:o.right-o.left}).show();b.nodeDropIn=p;b.nodeDropDir=w?"bottom":"top";if(h>o.top+7&&h<o.bottom-7){this.proxy.find(".l-drop-icon:first").removeClass("l-drop-no l-drop-yes").addClass("l-drop-add");b.nodeDroptip.hide();b.nodeDropInParent=true;}else{this.proxy.find(".l-drop-icon:first").removeClass("l-drop-no l-drop-add").addClass("l-drop-yes");b.nodeDroptip.show();b.nodeDropInParent=false;}break;}else{if(b.nodeDropIn!=-1){b.nodeDropIn=-1;b.nodeDropInParent=false;b.nodeDroptip.hide();this.proxy.find(".l-drop-icon:first").removeClass("l-drop-yes  l-drop-add").addClass("l-drop-no");}}}},onStopDrag:function(k,j){var d=this.draggingNodes;b.nodedragging=false;if(b.nodeDropIn!=-1){for(var g=0;g<d.length;g++){var f=d[g].children;if(f){d=a.grep(d,function(l,e){var m=a.inArray(l,f)==-1;return m;});}}for(var g in d){var h=d[g];if(b.nodeDropInParent){b.remove(h);b.append(b.nodeDropIn,[h]);}else{b.remove(h);b.append(b.getParent(b.nodeDropIn),[h],b.nodeDropIn,b.nodeDropDir=="bottom");}}b.nodeDropIn=-1;}b.nodeDroptip.hide();this.set("cursor","default");}});}},_setParentCheckboxStatus:function(f){var d=this,e=this.options;var b=a(".l-checkbox-unchecked",f.parent()).length==0;var c=a(".l-checkbox-checked",f.parent()).length==0;if(b){f.parent().prev().find(".l-checkbox").removeClass("l-checkbox-unchecked l-checkbox-incomplete").addClass("l-checkbox-checked");}else{if(c){f.parent().prev().find("> .l-checkbox").removeClass("l-checkbox-checked l-checkbox-incomplete").addClass("l-checkbox-unchecked");}else{f.parent().prev().find("> .l-checkbox").removeClass("l-checkbox-unchecked l-checkbox-checked").addClass("l-checkbox-incomplete");}}if(f.parent().parent("li").length>0){d._setParentCheckboxStatus(f.parent().parent("li"));}}});})(jQuery);
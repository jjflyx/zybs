(function(a){a.fn.ligerComboBox=function(b){return a.ligerui.run.call(this,"ligerComboBox",arguments);};a.fn.ligerGetComboBoxManager=function(){return a.ligerui.run.call(this,"ligerGetComboBoxManager",arguments);};a.ligerDefaults.ComboBox={resize:true,isMultiSelect:false,isShowCheckBox:false,columns:false,selectBoxWidth:false,selectBoxHeight:false,onBeforeSelect:false,onSelected:null,initValue:null,initText:null,valueField:"id",textField:"text",valueFieldID:null,slide:true,split:";",data:null,tree:null,treeLeafOnly:true,grid:null,onStartResize:null,onEndResize:null,hideOnLoseFocus:true,url:null,onSuccess:null,onError:null,onBeforeOpen:null,render:null,absolute:true};a.ligerMethos.ComboBox=a.ligerMethos.ComboBox||{};a.ligerui.controls.ComboBox=function(c,b){a.ligerui.controls.ComboBox.base.constructor.call(this,c,b);};a.ligerui.controls.ComboBox.ligerExtend(a.ligerui.controls.Input,{__getType:function(){return"ComboBox";},_extendMethods:function(){return a.ligerMethos.ComboBox;},_init:function(){a.ligerui.controls.ComboBox.base._init.call(this);var b=this.options;if(b.columns){b.isShowCheckBox=true;}if(b.isMultiSelect){b.isShowCheckBox=true;}},_render:function(){var b=this,c=this.options;b.data=c.data;b.inputText=null;b.select=null;b.textFieldID="";b.valueFieldID="";b.valueField=null;if(this.element.tagName.toLowerCase()=="input"){this.element.readOnly=true;b.inputText=a(this.element);b.textFieldID=this.element.id;}else{if(this.element.tagName.toLowerCase()=="select"){a(this.element).hide();b.select=a(this.element);c.isMultiSelect=false;c.isShowCheckBox=false;b.textFieldID=this.element.id+"_txt";b.inputText=a('<input type="text" readonly="true"/>');b.inputText.attr("id",b.textFieldID).insertAfter(a(this.element));}else{return;}}if(b.inputText[0].name==undefined){b.inputText[0].name=b.textFieldID;}b.valueField=null;if(c.valueFieldID){b.valueField=a("#"+c.valueFieldID+":input");if(b.valueField.length==0){b.valueField=a('<input type="hidden"/>');}b.valueField[0].id=b.valueField[0].name=c.valueFieldID;}else{b.valueField=a('<input type="hidden"/>');b.valueField[0].id=b.valueField[0].name=b.textFieldID+"_val";}if(b.valueField[0].name==undefined){b.valueField[0].name=b.valueField[0].id;}b.link=a('<div class="l-trigger"><div class="l-trigger-icon"></div></div>');b.selectBox=a('<div class="l-box-select"><div class="l-box-select-inner"><table cellpadding="0" cellspacing="0" border="0" class="l-box-select-table"></table></div></div>');b.selectBox.table=a("table:first",b.selectBox);b.wrapper=b.inputText.wrap('<div class="l-text l-text-combobox"></div>').parent();b.wrapper.append('<div class="l-text-l"></div><div class="l-text-r"></div>');b.wrapper.append(b.link);b.textwrapper=b.wrapper.wrap('<div class="l-text-wrapper"></div>').parent();if(c.absolute){b.selectBox.appendTo("body").addClass("l-box-select-absolute");}else{b.textwrapper.append(b.selectBox);}b.textwrapper.append(b.valueField);b.inputText.addClass("l-text-field");if(c.isShowCheckBox&&!b.select){a("table",b.selectBox).addClass("l-table-checkbox");}else{c.isShowCheckBox=false;a("table",b.selectBox).addClass("l-table-nocheckbox");}b.link.hover(function(){if(c.disabled){return;}this.className="l-trigger-hover";},function(){if(c.disabled){return;}this.className="l-trigger";}).mousedown(function(){if(c.disabled){return;}this.className="l-trigger-pressed";}).mouseup(function(){if(c.disabled){return;}this.className="l-trigger-hover";}).click(function(){if(c.disabled){return;}if(b.trigger("beforeOpen")==false){return false;}b._toggleSelectBox(b.selectBox.is(":visible"));});b.inputText.click(function(){if(c.disabled){return;}if(b.trigger("beforeOpen")==false){return false;}b._toggleSelectBox(b.selectBox.is(":visible"));}).blur(function(){if(c.disabled){return;}b.wrapper.removeClass("l-text-focus");}).focus(function(){if(c.disabled){return;}b.wrapper.addClass("l-text-focus");});b.wrapper.hover(function(){if(c.disabled){return;}b.wrapper.addClass("l-text-over");},function(){if(c.disabled){return;}b.wrapper.removeClass("l-text-over");});b.resizing=false;b.selectBox.hover(null,function(f){if(c.hideOnLoseFocus&&b.selectBox.is(":visible")&&!b.boxToggling&&!b.resizing){b._toggleSelectBox(true);}});var d=a("tr",b.selectBox.table).length;if(!c.selectBoxHeight&&d<8){c.selectBoxHeight=d*30;}if(c.selectBoxHeight){b.selectBox.height(c.selectBoxHeight);}b.bulidContent();b.set(c);if(c.selectBoxWidth){b.selectBox.width(c.selectBoxWidth);}else{b.selectBox.css("width",b.wrapper.css("width"));}},destroy:function(){if(this.wrapper){this.wrapper.remove();}if(this.selectBox){this.selectBox.remove();}this.options=null;a.ligerui.remove(this);},_setDisabled:function(b){if(b){this.wrapper.addClass("l-text-disabled");}else{this.wrapper.removeClass("l-text-disabled");}},_setLable:function(b){var c=this,d=this.options;if(b){if(c.labelwrapper){c.labelwrapper.find(".l-text-label:first").html(b+":&nbsp");}else{c.labelwrapper=c.textwrapper.wrap('<div class="l-labeltext"></div>').parent();c.labelwrapper.prepend('<div class="l-text-label" style="float:left;display:inline;">'+b+":&nbsp</div>");c.textwrapper.css("float","left");}if(!d.labelWidth){d.labelWidth=a(".l-text-label",c.labelwrapper).outerWidth();}else{a(".l-text-label",c.labelwrapper).outerWidth(d.labelWidth);}a(".l-text-label",c.labelwrapper).width(d.labelWidth);a(".l-text-label",c.labelwrapper).height(c.wrapper.height());c.labelwrapper.append('<br style="clear:both;" />');if(d.labelAlign){a(".l-text-label",c.labelwrapper).css("text-align",d.labelAlign);}c.textwrapper.css({display:"inline"});c.labelwrapper.width(c.wrapper.outerWidth()+d.labelWidth+2);}},_setWidth:function(c){var b=this;if(c>20){b.wrapper.css({width:c});b.inputText.css({width:c-20});b.textwrapper.css({width:c});}},_setHeight:function(c){var b=this;if(c>10){b.wrapper.height(c);b.inputText.height(c-2);b.link.height(c-4);b.textwrapper.css({width:c});}},_setResize:function(b){if(b&&a.fn.ligerResizable){var c=this;c.selectBox.ligerResizable({handles:"se,s,e",onStartResize:function(){c.resizing=true;c.trigger("startResize");},onEndResize:function(){c.resizing=false;if(c.trigger("endResize")==false){return false;}}});c.selectBox.append("<div class='l-btn-nw-drop'></div>");}},findTextByValue:function(e){var b=this,f=this.options;if(e==undefined){return"";}var d="";var c=function(j){var g=e.toString().split(f.split);for(var h=0;h<g.length;h++){if(g[h]==j){return true;}}return false;};a(b.data).each(function(h,j){var k=j[f.valueField];var g=j[f.textField];if(c(k)){d+=g+f.split;}});if(d.length>0){d=d.substr(0,d.length-1);}return d;},findValueByText:function(f){var c=this,e=this.options;if(!f&&f==""){return"";}var d=function(j){var g=f.toString().split(e.split);for(var h=0;h<g.length;h++){if(g[h]==j){return true;}}return false;};var b="";a(c.data).each(function(h,j){var k=j[e.valueField];var g=j[e.textField];if(d(g)){b+=k+e.split;}});if(b.length>0){b=b.substr(0,b.length-1);}return b;},removeItem:function(){},insertItem:function(){},addItem:function(){},_setValue:function(e){var d=this,f=this.options;var h=d.findTextByValue(e);if(f.tree){d.selectValueByTree(e);}else{if(!f.isMultiSelect){d._changeValue(e,h);a("tr[value="+e+"] td",d.selectBox).addClass("l-selected");a("tr[value!="+e+"] td",d.selectBox).removeClass("l-selected");}else{d._changeValue(e,h);var b=e.toString().split(f.split);a("table.l-table-checkbox :checkbox",d.selectBox).each(function(){this.checked=false;});for(var c=0;c<b.length;c++){a("table.l-table-checkbox tr[value="+b[c]+"] :checkbox",d.selectBox).each(function(){this.checked=true;});}}}},selectValue:function(b){this._setValue(b);},bulidContent:function(){var b=this,c=this.options;this.clearContent();if(b.select){b.setSelect();}else{if(b.data){b.setData(b.data);}else{if(c.tree){b.setTree(c.tree);}else{if(c.grid){b.setGrid(c.grid);}else{if(c.url){a.ajax({type:"post",url:c.url,cache:false,dataType:"json",success:function(d){b.data=d;b.setData(b.data);b.trigger("success",[b.data]);},error:function(d,e){b.trigger("error",[d,e]);}});}}}}}},clearContent:function(){var b=this,c=this.options;a("table",b.selectBox).html("");},setSelect:function(){var b=this,c=this.options;this.clearContent();a("option",b.select).each(function(e){var g=a(this).val();var d=a(this).html();var f=a("<tr><td index='"+e+"' value='"+g+"'>"+d+"</td>");a("table.l-table-nocheckbox",b.selectBox).append(f);a("td",f).hover(function(){a(this).addClass("l-over");},function(){a(this).removeClass("l-over");});});a("td:eq("+b.select[0].selectedIndex+")",b.selectBox).each(function(){if(a(this).hasClass("l-selected")){b.selectBox.hide();return;}a(".l-selected",b.selectBox).removeClass("l-selected");a(this).addClass("l-selected");if(b.select[0].selectedIndex!=a(this).attr("index")&&b.select[0].onchange){b.select[0].selectedIndex=a(this).attr("index");b.select[0].onchange();}var d=parseInt(a(this).attr("index"));b.select[0].selectedIndex=d;b.select.trigger("change");b.selectBox.hide();var e=a(this).attr("value");var f=a(this).html();if(c.render){b.inputText.val(c.render(e,f));}else{b.inputText.val(f);}});b._addClickEven();},setData:function(e){var k=this,b=this.options;this.clearContent();if(!e||!e.length){return;}if(k.data!=e){k.data=e;}if(b.columns){k.selectBox.table.headrow=a("<tr class='l-table-headerow'><td width='18px'></td></tr>");k.selectBox.table.append(k.selectBox.table.headrow);k.selectBox.table.addClass("l-box-select-grid");for(var d=0;d<b.columns.length;d++){var m=a("<td columnindex='"+d+"' columnname='"+b.columns[d].name+"'>"+b.columns[d].header+"</td>");if(b.columns[d].width){m.width(b.columns[d].width);}k.selectBox.table.headrow.append(m);}}for(var f=0;f<e.length;f++){var c=e[f][b.valueField];var h=e[f][b.textField];if(!b.columns){a("table.l-table-checkbox",k.selectBox).append("<tr value='"+c+"'><td style='width:18px;'  index='"+f+"' value='"+c+"' text='"+h+"' ><input type='checkbox' /></td><td index='"+f+"' value='"+c+"' align='left'>"+h+"</td>");a("table.l-table-nocheckbox",k.selectBox).append("<tr value='"+c+"'><td index='"+f+"' value='"+c+"' align='left'>"+h+"</td>");}else{var l=a("<tr value='"+c+"'><td style='width:18px;'  index='"+f+"' value='"+c+"' text='"+h+"' ><input type='checkbox' /></td></tr>");a("td",k.selectBox.table.headrow).each(function(){var g=a(this).attr("columnname");if(g){var i=a("<td>"+e[f][g]+"</td>");l.append(i);}});k.selectBox.table.append(l);}}if(b.isShowCheckBox&&a.fn.ligerCheckBox){a("table input:checkbox",k.selectBox).ligerCheckBox();}a(".l-table-checkbox input:checkbox",k.selectBox).change(function(){if(this.checked&&k.hasBind("beforeSelect")){var g=null;if(a(this).parent().get(0).tagName.toLowerCase()=="div"){g=a(this).parent().parent();}else{g=a(this).parent();}if(g!=null&&k.trigger("beforeSelect",[g.attr("value"),g.attr("text")])==false){k.selectBox.slideToggle("fast");return false;}}if(!b.isMultiSelect){if(this.checked){a("input:checked",k.selectBox).not(this).each(function(){this.checked=false;a(".l-checkbox-checked",a(this).parent()).removeClass("l-checkbox-checked");});k.selectBox.slideToggle("fast");}}k._checkboxUpdateValue();});a("table.l-table-nocheckbox td",k.selectBox).hover(function(){a(this).addClass("l-over");},function(){a(this).removeClass("l-over");});k._addClickEven();k._dataInit();},setTree:function(t){var l=this,e=this.options;this.clearContent();l.selectBox.table.remove();var s=t.selectValue;var k=t.nameKey;var d=t.data.key.name;if(!t.callback){t.callback={};}if(t.check&&t.check.enable){var r=function(i,v,u){var g=l.ztree.getCheckedNodes(true);var p=[],w=[];a(g).each(function(x,y){if(e.treeLeafOnly&&y.isParent){return;}p.push(y[e.valueField]);w.push(y[e.textField]);});l._changeValue(p.join(e.split),w.join(e.split));};var b=t.callback.onCheck;t.callback.onCheck=function(g,p,i){b&&b(g,p,i);r(g,p,i);};}else{var m=function(g,u,p){var w=l.ztree.getSelectedNodes();var i=[],v=[];a(w).each(function(x,y){if(e.treeLeafOnly&&y.isParent){return;}i.push(y[e.valueField]);v.push(y[e.textField]);});l._changeValue(i.join(e.split),v.join(e.split));};var q=t.callback.onClick;t.callback.onClick=function(g,p,i){q&&q(g,p,i);m(g,p,i);};}l.tree=a("<ul id='ztree"+new Date().getTime()+"' class='ztree'></ul>");a("div:first",l.selectBox).append(l.tree);if(t.data&&t.data.data){l.ztree=a.fn.zTree.init(l.tree,t,t.data.data);}else{l.ztree=a.fn.zTree.init(l.tree,t);}if(s!=null&&s!=undefined&&s!=""){var f=s.split(",");var n=new Array();for(var j=0;j<f.length;j++){var o=f[j];var c=l.ztree.getNodesByParam(k,o,null);if(c!=null&&c.length>0){l.ztree.selectNode(c[0]);var h=c[0][d];n.push(h);if(t.check){l.ztree.checkNode(c[0],true,false);}}}l._changeValue(s,n.join(","));}},selectValueByTree:function(d){var c=this,e=this.options;if(d!=null){var f="";var b=d.toString().split(e.split);a(b).each(function(g,h){c.ztree.selectNode(h.toString());f+=c.treeManager.getTextByID(h);if(g<b.length-1){f+=e.split;}});c._changeValue(d,f);}},setGrid:function(c){var d=this,e=this.options;this.clearContent();d.selectBox.table.remove();d.grid=a("div:first",d.selectBox);c.columnWidth=c.columnWidth||120;c.width="100%";c.height="100%";c.heightDiff=-2;c.InWindow=false;d.gridManager=d.grid.ligerGrid(c);e.hideOnLoseFocus=false;if(c.checkbox!=false){var b=function(){var f=d.gridManager.getCheckedRows();var g=[];var h=[];a(f).each(function(j,k){g.push(k[e.valueField]);h.push(k[e.textField]);});d._changeValue(g.join(e.split),h.join(e.split));};d.gridManager.bind("CheckAllRow",b);d.gridManager.bind("CheckRow",b);}else{d.gridManager.bind("SelectRow",function(j,h,f){var g=j[e.valueField];var i=j[e.textField];d._changeValue(g,i);});d.gridManager.bind("UnSelectRow",function(h,g,f){d._changeValue("","");});}d.bind("show",function(){if(d.gridManager){d.gridManager._updateFrozenWidth();}});d.bind("endResize",function(){if(d.gridManager){d.gridManager._updateFrozenWidth();d.gridManager.setHeight(d.selectBox.height()-2);}});},_getValue:function(){return a(this.valueField).val();},getValue:function(){return this._getValue();},updateStyle:function(){var b=this,c=this.options;b._dataInit();},_dataInit:function(){var b=this,d=this.options;var c=null;if(d.initValue!=null&&d.initText!=null){b._changeValue(d.initValue,d.initText);}if(d.initValue!=null){c=d.initValue;if(d.tree){if(c){b.selectValueByTree(c);}}else{var e=b.findTextByValue(c);b._changeValue(c,e);}}else{if(d.initText!=null){c=b.findValueByText(d.initText);b._changeValue(c,d.initText);}else{if(b.valueField.val()!=""){c=b.valueField.val();if(d.tree){if(c){b.selectValueByTree(c);}}else{var e=b.findTextByValue(c);b._changeValue(c,e);}}}}if(!d.isShowCheckBox&&c!=null){a("table tr",b.selectBox).find("td:first").each(function(){if(c==a(this).attr("value")){a(this).addClass("l-selected");}});}if(d.isShowCheckBox&&c!=null){a(":checkbox",b.selectBox).each(function(){var h=null;var g=a(this);if(g.parent().get(0).tagName.toLowerCase()=="div"){h=g.parent().parent();}else{h=g.parent();}if(h==null){return;}var f=c.toString().split(d.split);a(f).each(function(j,k){if(k==h.attr("value")){a(".l-checkbox",h).addClass("l-checkbox-checked");g[0].checked=true;}});});}},_changeValue:function(e,c){var b=this,d=this.options;if(b.valueField){b.valueField.val(e);}if(d.render){b.inputText.val(d.render(e,c));}else{b.inputText.val(c);}b.selectedValue=e;b.selectedText=c;b.inputText.trigger("change").focus();b.trigger("selected",[e,c]);},_checkboxUpdateValue:function(){var d=this,e=this.options;var b="";var c="";a("input:checked",d.selectBox).each(function(){var f=null;if(a(this).parent().get(0).tagName.toLowerCase()=="div"){f=a(this).parent().parent();}else{f=a(this).parent();}if(!f){return;}b+=f.attr("value")+e.split;c+=f.attr("text")+e.split;});if(b.length>0){b=b.substr(0,b.length-1);}if(c.length>0){c=c.substr(0,c.length-1);}d._changeValue(b,c);},_addClickEven:function(){var b=this,c=this.options;a(".l-table-nocheckbox td",b.selectBox).click(function(){var e=a(this).attr("value");var d=parseInt(a(this).attr("index"));var f=a(this).html();if(b.hasBind("beforeSelect")&&b.trigger("beforeSelect",[e,f])==false){if(c.slide){b.selectBox.slideToggle("fast");}else{b.selectBox.hide();}return false;}if(a(this).hasClass("l-selected")){if(c.slide){b.selectBox.slideToggle("fast");}else{b.selectBox.hide();}return;}a(".l-selected",b.selectBox).removeClass("l-selected");a(this).addClass("l-selected");if(b.select){if(b.select[0].selectedIndex!=d){b.select[0].selectedIndex=d;b.select.trigger("change");}}if(c.slide){b.boxToggling=true;b.selectBox.hide("fast",function(){b.boxToggling=false;});}else{b.selectBox.hide();}b._changeValue(e,f);});},updateSelectBoxPosition:function(){var b=this,d=this.options;if(d.absolute){b.selectBox.css({left:b.wrapper.offset().left,top:b.wrapper.offset().top+1+b.wrapper.outerHeight()});}else{var e=b.wrapper.offset().top-a(window).scrollTop();var c=b.selectBox.height()+textHeight+4;if(e+c>a(window).height()&&e>c){b.selectBox.css("marginTop",-1*(b.selectBox.height()+textHeight+5));}}},_toggleSelectBox:function(c){var d=this,e=this.options;var f=d.wrapper.height();d.boxToggling=true;if(c){if(e.slide){d.selectBox.slideToggle("fast",function(){d.boxToggling=false;});}else{d.selectBox.hide();d.boxToggling=false;}}else{d.updateSelectBoxPosition();if(e.slide){d.selectBox.slideToggle("fast",function(){d.boxToggling=false;if(!e.isShowCheckBox&&a("td.l-selected",d.selectBox).length>0){var g=(a("td.l-selected",d.selectBox).offset().top-d.selectBox.offset().top);a(".l-box-select-inner",d.selectBox).animate({scrollTop:g});}});}else{d.selectBox.show();d.boxToggling=false;if(!d.tree&&!d.grid&&!e.isShowCheckBox&&a("td.l-selected",d.selectBox).length>0){var b=(a("td.l-selected",d.selectBox).offset().top-d.selectBox.offset().top);a(".l-box-select-inner",d.selectBox).animate({scrollTop:b});}}}d.isShowed=d.selectBox.is(":visible");d.trigger("toggle",[c]);d.trigger(c?"hide":"show");}});a.ligerui.controls.ComboBox.prototype.setValue=a.ligerui.controls.ComboBox.prototype.selectValue;a.ligerui.controls.ComboBox.prototype.setInputValue=a.ligerui.controls.ComboBox.prototype._changeValue;})(jQuery);
UE.plugins["contextmenu"]=function(){var c=this,e=c.getLang("contextMenu"),d,a=c.options.contextMenu||[{label:e["delete"],cmdName:"delete"},{label:e["selectall"],cmdName:"selectall"},{label:e.deletecode,cmdName:"highlightcode",icon:"deletehighlightcode"},{label:e.cleardoc,cmdName:"cleardoc",exec:function(){if(confirm(e.confirmclear)){this.execCommand("cleardoc");}}},"-",{label:e.unlink,cmdName:"unlink"},"-",{group:e.paragraph,icon:"justifyjustify",subMenu:[{label:c.getLang("justifyleft"),cmdName:"justify",value:"left"},{label:c.getLang("justifyright"),cmdName:"justify",value:"right"},{label:c.getLang("justifyrenter"),cmdName:"justify",value:"center"},{label:c.getLang("justify"),cmdName:"justify",value:"justify"}]},"-",{label:e.edittable,cmdName:"edittable",exec:function(){this.ui._dialogs["inserttableDialog"].open();}},{label:e.edittd,cmdName:"edittd",exec:function(){if(UE.ui["edittd"]){new UE.ui["edittd"](this);}this.ui._dialogs["edittdDialog"].open();}},{group:e.table,icon:"table",subMenu:[{label:e.deletetable,cmdName:"deletetable"},{label:e.insertparagraphbeforetable,cmdName:"insertparagraphbeforetable"},"-",{label:e.deleterow,cmdName:"deleterow"},{label:e.deletecol,cmdName:"deletecol"},"-",{label:e.insertrow,cmdName:"insertrow"},{label:e.insertcol,cmdName:"insertcol"},"-",{label:e.mergeright,cmdName:"mergeright"},{label:e.mergedown,cmdName:"mergedown"},"-",{label:e.splittorows,cmdName:"splittorows"},{label:e.splittocols,cmdName:"splittocols"},{label:e.mergecells,cmdName:"mergecells"},{label:e.splittocells,cmdName:"splittocells"}]},{label:e["copy"],cmdName:"copy",exec:function(){alert(e.copymsg);},query:function(){return 0;}},{label:e["paste"],cmdName:"paste",exec:function(){alert(e.pastemsg);},query:function(){return 0;}}];if(!a.length){return;}var b=UE.ui.uiUtils;c.addListener("contextmenu",function(m,o){var h=b.getViewportOffsetByEvent(o);c.fireEvent("beforeselectionchange");if(d){d.destroy();}for(var j=0,f,p=[];f=a[j];j++){var n;(function(s){if(s=="-"){if((n=p[p.length-1])&&n!=="-"){p.push("-");}}else{if(s.hasOwnProperty("group")){for(var r=0,q,i=[];q=s.subMenu[r];r++){(function(t){if(t=="-"){if((n=i[i.length-1])&&n!=="-"){i.push("-");}}else{if((c.commands[t.cmdName]||UE.commands[t.cmdName]||t.query)&&(t.query?t.query():c.queryCommandState(t.cmdName))>-1){i.push({"label":t.label||c.getLang("contextMenu."+t.cmdName+(t.value||"")),"className":"edui-for-"+t.cmdName+(t.value||""),onclick:t.exec?function(){t.exec.call(c);}:function(){c.execCommand(t.cmdName,t.value);}});}}})(q);}if(i.length){p.push({"label":s.icon=="table"?c.getLang("contextMenu.table"):c.getLang("contextMenu.paragraph"),className:"edui-for-"+s.icon,"subMenu":{items:i,editor:c}});}}else{if((c.commands[s.cmdName]||UE.commands[s.cmdName]||s.query)&&(s.query?s.query():c.queryCommandState(s.cmdName))>-1){if(s.cmdName=="highlightcode"&&c.queryCommandState(s.cmdName)==0){return;}p.push({"label":s.label||c.getLang("contextMenu."+s.cmdName),className:"edui-for-"+(s.icon?s.icon:s.cmdName+(s.value||"")),onclick:s.exec?function(){s.exec.call(c);}:function(){c.execCommand(s.cmdName,s.value);}});}}}})(f);}if(p[p.length-1]=="-"){p.pop();}d=new UE.ui.Menu({items:p,editor:c});d.render();d.showAt(h);domUtils.preventDefault(o);if(browser.ie){var g;try{g=c.selection.getNative().createRange();}catch(l){return;}if(g.item){var k=new dom.Range(c.document);k.selectNode(g.item(0)).select(true,true);}}});};
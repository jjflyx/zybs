UE.plugins["elementpath"]=function(){var c,a,b=this;b.setOpt("elementPathEnabled",true);if(!b.options.elementPathEnabled){return;}b.commands["elementpath"]={execCommand:function(n,e){var g=a[e],l=b.selection.getRange();b.currentSelectedArr&&domUtils.clearSelectedArr(b.currentSelectedArr);c=e*1;if(dtd.$tableContent[g.tagName]){switch(g.tagName){case"TD":b.currentSelectedArr=[g];g.className=b.options.selectedTdClass;break;case"TR":var o=g.cells;for(var k=0,f;f=o[k++];){b.currentSelectedArr.push(f);f.className=b.options.selectedTdClass;}break;case"TABLE":case"TBODY":var p=g.rows;for(var k=0,m;m=p[k++];){o=m.cells;for(var h=0,d;d=o[h++];){b.currentSelectedArr.push(d);d.className=b.options.selectedTdClass;}}}g=b.currentSelectedArr[0];if(domUtils.isEmptyNode(g)){l.setStart(g,0).setCursor();}else{l.selectNodeContents(g).select();}}else{l.selectNode(g).select();}},queryCommandValue:function(){var e=[].concat(this.selection.getStartElementPath()).reverse(),h=[];a=e;for(var g=0,f;f=e[g];g++){if(f.nodeType==3){continue;}var d=f.tagName.toLowerCase();if(d=="img"&&f.getAttribute("anchorname")){d="anchor";}h[g]=d;if(c==g){c=-1;break;}}return h;}};};
UE.plugins["basestyle"]=function(){var d={"bold":["strong","b"],"italic":["em","i"],"subscript":["sub"],"superscript":["sup"]},c=function(f,e){var g=f.selection.getStartElementPath();return utils.findNode(g,e);},b=this;for(var a in d){(function(f,e){b.commands[f]={execCommand:function(h){var g=new dom.Range(b.document),n="";if(b.currentSelectedArr&&b.currentSelectedArr.length>0){for(var k=0,j;j=b.currentSelectedArr[k++];){if(j.style.display!="none"){g.selectNodeContents(j).select();!n&&(n=c(this,e));if(h=="superscript"||h=="subscript"){if(!n||n.tagName.toLowerCase()!=h){g.removeInlineStyle(["sub","sup"]);}}n?g.removeInlineStyle(e):g.applyInlineStyle(e[0]);}}g.selectNodeContents(b.currentSelectedArr[0]).select();}else{g=b.selection.getRange();n=c(this,e);if(g.collapsed){if(n){var m=b.document.createTextNode("");g.insertNode(m).removeInlineStyle(e);g.setStartBefore(m);domUtils.remove(m);}else{var l=g.document.createElement(e[0]);if(h=="superscript"||h=="subscript"){m=b.document.createTextNode("");g.insertNode(m).removeInlineStyle(["sub","sup"]).setStartBefore(m).collapse(true);}g.insertNode(l).setStart(l,0);}g.collapse(true);}else{if(h=="superscript"||h=="subscript"){if(!n||n.tagName.toLowerCase()!=h){g.removeInlineStyle(["sub","sup"]);}}n?g.removeInlineStyle(e):g.applyInlineStyle(e[0]);}g.select();}return true;},queryCommandState:function(){if(this.highlight){return -1;}return c(this,e)?1:0;}};})(a,d[a]);}};
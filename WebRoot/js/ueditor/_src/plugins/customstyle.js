UE.plugins["customstyle"]=function(){var a=this;a.setOpt({"customstyle":[{tag:"h1",name:"tc",style:"font-size:32px;font-weight:bold;border-bottom:#ccc 2px solid;padding:0 4px 0 0;text-align:center;margin:0 0 20px 0;"},{tag:"h1",name:"tl",style:"font-size:32px;font-weight:bold;border-bottom:#ccc 2px solid;padding:0 4px 0 0;text-align:left;margin:0 0 10px 0;"},{tag:"span",name:"im",style:"font-size:16px;font-style:italic;font-weight:bold;color:#000;line-height:18px;"},{tag:"span",name:"hi",style:"font-size:16px;font-style:italic;font-weight:bold;color:rgb(51, 153, 204);line-height:18px;"}]});a.commands["customstyle"]={execCommand:function(s,l){var q=this,f=l.tag,g=domUtils.findParent(q.selection.getStart(),function(i){return i.getAttribute("label");},true),m,h,j={};for(var d in l){j[d]=l[d];}delete j.tag;if(g&&g.getAttribute("label")==l.label){m=this.selection.getRange();h=m.createBookmark();if(m.collapsed){if(dtd.$block[g.tagName]){var t=q.document.createElement("p");domUtils.moveChild(g,t);g.parentNode.insertBefore(t,g);domUtils.remove(g);}else{domUtils.remove(g,true);}}else{var n=domUtils.getCommonAncestor(h.start,h.end),c=domUtils.getElementsByTagName(n,f);if(new RegExp(f,"i").test(n.tagName)){c.push(n);}for(var k=0,e;e=c[k++];){if(e.getAttribute("label")==l.label){var b=domUtils.getPosition(e,h.start),o=domUtils.getPosition(e,h.end);if((b&domUtils.POSITION_FOLLOWING||b&domUtils.POSITION_CONTAINS)&&(o&domUtils.POSITION_PRECEDING||o&domUtils.POSITION_CONTAINS)){if(dtd.$block[f]){var t=q.document.createElement("p");domUtils.moveChild(e,t);e.parentNode.insertBefore(t,e);}}domUtils.remove(e,true);}}g=domUtils.findParent(n,function(i){return i.getAttribute("label")==l.label;},true);if(g){domUtils.remove(g,true);}}m.moveToBookmark(h).select();}else{if(dtd.$block[f]){this.execCommand("paragraph",f,j,"customstyle");m=q.selection.getRange();if(!m.collapsed){m.collapse();g=domUtils.findParent(q.selection.getStart(),function(i){return i.getAttribute("label")==l.label;},true);var r=q.document.createElement("p");domUtils.insertAfter(g,r);domUtils.fillNode(q.document,r);m.setStart(r,0).setCursor();}}else{m=q.selection.getRange();if(m.collapsed){g=q.document.createElement(f);domUtils.setAttributes(g,j);m.insertNode(g).setStart(g,0).setCursor();return;}h=m.createBookmark();m.applyInlineStyle(f,j).moveToBookmark(h).select();}}},queryCommandValue:function(){var b=utils.findNode(this.selection.getStartElementPath(),null,function(c){return c.getAttribute("label");});return b?b.getAttribute("label"):"";},queryCommandState:function(){return this.highlight?-1:0;}};a.addListener("keyup",function(d,b){var g=b.keyCode||b.which;if(g==32||g==13){var c=a.selection.getRange();if(c.collapsed){var e=domUtils.findParent(a.selection.getStart(),function(h){return h.getAttribute("label");},true);if(e&&dtd.$block[e.tagName]&&domUtils.isEmptyNode(e)){var f=a.document.createElement("p");domUtils.insertAfter(e,f);domUtils.fillNode(a.document,f);domUtils.remove(e);c.setStart(f,0).setCursor();}}}});};
UE.plugins["paragraph"]=function(){var b=this,d=domUtils.isBlockElm,c=["TD","LI","PRE"],a=function(g,e,m,j){var i=g.createBookmark(),n=function(q){return q.nodeType==1?q.tagName.toLowerCase()!="br"&&!domUtils.isBookmarkNode(q):!domUtils.isWhitespace(q);},p;g.enlarge(true);var o=g.createBookmark(),h=domUtils.getNextDomNode(o.start,false,n),l=g.cloneRange(),f;while(h&&!(domUtils.getPosition(h,o.end)&domUtils.POSITION_FOLLOWING)){if(h.nodeType==3||!d(h)){l.setStartBefore(h);while(h&&h!==o.end&&!d(h)){f=h;h=domUtils.getNextDomNode(h,false,null,function(q){return !d(q);});}l.setEndAfter(f);p=g.document.createElement(e);if(m){domUtils.setAttributes(p,m);if(j&&j=="customstyle"&&m.style){p.style.cssText=m.style;}}p.appendChild(l.extractContents());if(domUtils.isEmptyNode(p)){domUtils.fillChar(g.document,p);}l.insertNode(p);var k=p.parentNode;if(d(k)&&!domUtils.isBody(p.parentNode)&&utils.indexOf(c,k.tagName)==-1){if(!(j&&j=="customstyle")){k.getAttribute("dir")&&p.setAttribute("dir",k.getAttribute("dir"));k.style.cssText&&(p.style.cssText=k.style.cssText+";"+p.style.cssText);k.style.textAlign&&!p.style.textAlign&&(p.style.textAlign=k.style.textAlign);k.style.textIndent&&!p.style.textIndent&&(p.style.textIndent=k.style.textIndent);k.style.padding&&!p.style.padding&&(p.style.padding=k.style.padding);}if(m&&/h\d/i.test(k.tagName)&&!/h\d/i.test(p.tagName)){domUtils.setAttributes(k,m);if(j&&j=="customstyle"&&m.style){k.style.cssText=m.style;}domUtils.remove(p,true);p=k;}else{domUtils.remove(p.parentNode,true);}}if(utils.indexOf(c,k.tagName)!=-1){h=k;}else{h=p;}h=domUtils.getNextDomNode(h,false,n);}else{h=domUtils.getNextDomNode(h,true,n);}}return g.moveToBookmark(o).moveToBookmark(i);};b.setOpt("paragraph",{"p":"","h1":"","h2":"","h3":"","h4":"","h5":"","h6":""});b.commands["paragraph"]={execCommand:function(r,e,q,o){var n=new dom.Range(this.document);if(this.currentSelectedArr&&this.currentSelectedArr.length>0){for(var m=0,f;f=this.currentSelectedArr[m++];){if(f.style.display=="none"){continue;}if(domUtils.isEmptyNode(f)){var k=this.document.createTextNode("paragraph");f.innerHTML="";f.appendChild(k);}a(n.selectNodeContents(f),e,q,o);if(k){var p=k.parentNode;domUtils.remove(k);if(domUtils.isEmptyNode(p)){domUtils.fillNode(this.document,p);}}}var j=this.currentSelectedArr[0];if(domUtils.isEmptyBlock(j)){n.setStart(j,0).setCursor(false,true);}else{n.selectNode(j).select();}}else{n=this.selection.getRange();if(n.collapsed){var l=this.document.createTextNode("p");n.insertNode(l);if(browser.ie){var h=l.previousSibling;if(h&&domUtils.isWhitespace(h)){domUtils.remove(h);}h=l.nextSibling;if(h&&domUtils.isWhitespace(h)){domUtils.remove(h);}}}n=a(n,e,q,o);if(l){n.setStartBefore(l).collapse(true);p=l.parentNode;domUtils.remove(l);if(domUtils.isBlockElm(p)&&domUtils.isEmptyNode(p)){domUtils.fillNode(this.document,p);}}if(browser.gecko&&n.collapsed&&n.startContainer.nodeType==1){var g=n.startContainer.childNodes[n.startOffset];if(g&&g.nodeType==1&&g.tagName.toLowerCase()==e){n.setStart(g,0).collapse(true);}}n.select();}return true;},queryCommandValue:function(){var e=utils.findNode(this.selection.getStartElementPath(),["p","h1","h2","h3","h4","h5","h6"]);return e?e.tagName.toLowerCase():"";},queryCommandState:function(){return this.highlight?-1:0;}};};
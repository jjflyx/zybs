UE.plugins["keystrokes"]=function(){var e=this,a=0,d=domUtils.keys,b={"B":"strong","I":"em","FONT":"span"},c=[0,10,12,16,18,24,32,48],f={"OL":["decimal","lower-alpha","lower-roman","upper-alpha","upper-roman"],"UL":["circle","disc","square"]};e.addListener("keydown",function(h,w){var n=w.keyCode||w.which;if(this.selectAll){this.selectAll=false;if((n==8||n==46)){e.undoManger&&e.undoManger.save();e.body.innerHTML="<p>"+(browser.ie?"":"<br/>")+"</p>";new dom.Range(e.document).setStart(e.body.firstChild,0).setCursor(false,true);e.undoManger&&e.undoManger.save();browser.ie&&e._selectionChange();domUtils.preventDefault(w);return;}}if(n==8){var t=e.selection.getRange(),F,k,j;if(t.collapsed){k=t.startContainer;if(domUtils.isWhitespace(k)){k=k.parentNode;}if(domUtils.isEmptyNode(k)&&k===e.body.firstChild){if(k.tagName!="P"){p=e.document.createElement("p");e.body.insertBefore(p,k);domUtils.fillNode(e.document,p);t.setStart(p,0).setCursor(false,true);domUtils.remove(k);}domUtils.preventDefault(w);return;}}if(t.collapsed&&t.startContainer.nodeType==3&&t.startContainer.nodeValue.replace(new RegExp(domUtils.fillChar,"g"),"").length==0){t.setStartBefore(t.startContainer).collapse(true);}if(k=t.getClosedNode()){e.undoManger&&e.undoManger.save();t.setStartBefore(k);domUtils.remove(k);t.setCursor();e.undoManger&&e.undoManger.save();domUtils.preventDefault(w);return;}if(!browser.ie){k=domUtils.findParentByTagName(t.startContainer,"table",true);j=domUtils.findParentByTagName(t.endContainer,"table",true);if(k&&!j||!k&&j||k!==j){w.preventDefault();return;}}if(e.undoManger){if(!t.collapsed){e.undoManger.save();a=1;}}}if(n==9){t=e.selection.getRange();e.undoManger&&e.undoManger.save();for(var D=0,u="",x=e.options.tabSize||4,B=e.options.tabNode||"&nbsp;";D<x;D++){u+=B;}var C=e.document.createElement("span");C.innerHTML=u;if(t.collapsed){var r=domUtils.findParentByTagName(t.startContainer,"li",true);if(r&&domUtils.isStartInblock(t)){g=t.createBookmark();var A=r.parentNode,E=e.document.createElement(A.tagName);var l=utils.indexOf(f[E.tagName],domUtils.getComputedStyle(A,"list-style-type"));l=l+1==f[E.tagName].length?0:l+1;domUtils.setStyle(E,"list-style-type",f[E.tagName][l]);A.insertBefore(E,r);E.appendChild(r);t.moveToBookmark(g).select();}else{t.insertNode(C.cloneNode(true).firstChild).setCursor(true);}}else{k=domUtils.findParentByTagName(t.startContainer,"table",true);j=domUtils.findParentByTagName(t.endContainer,"table",true);if(k||j){w.preventDefault?w.preventDefault():(w.returnValue=false);return;}k=domUtils.findParentByTagName(t.startContainer,["ol","ul"],true);j=domUtils.findParentByTagName(t.endContainer,["ol","ul"],true);if(k&&j&&k===j){var g=t.createBookmark();k=domUtils.findParentByTagName(t.startContainer,"li",true);j=domUtils.findParentByTagName(t.endContainer,"li",true);if(k===k.parentNode.firstChild){var s=e.document.createElement(k.parentNode.tagName);k.parentNode.parentNode.insertBefore(s,k.parentNode);s.appendChild(k.parentNode);}else{A=k.parentNode;E=e.document.createElement(A.tagName);l=utils.indexOf(f[E.tagName],domUtils.getComputedStyle(A,"list-style-type"));l=l+1==f[E.tagName].length?0:l+1;domUtils.setStyle(E,"list-style-type",f[E.tagName][l]);k.parentNode.insertBefore(E,k);var q;while(k!==j){q=k.nextSibling;E.appendChild(k);k=q;}E.appendChild(j);}t.moveToBookmark(g).select();}else{if(k||j){w.preventDefault?w.preventDefault():(w.returnValue=false);return;}k=domUtils.findParent(t.startContainer,v);j=domUtils.findParent(t.endContainer,v);if(k&&j&&k===j){t.deleteContents();t.insertNode(C.cloneNode(true).firstChild).setCursor(true);}else{var m=t.createBookmark(),v=function(i){return domUtils.isBlockElm(i);};t.enlarge(true);var z=t.createBookmark(),y=domUtils.getNextDomNode(z.start,false,v);while(y&&!(domUtils.getPosition(y,z.end)&domUtils.POSITION_FOLLOWING)){y.insertBefore(C.cloneNode(true).firstChild,y.firstChild);y=domUtils.getNextDomNode(y,false,v);}t.moveToBookmark(z).moveToBookmark(m).select();}}}e.undoManger&&e.undoManger.save();w.preventDefault?w.preventDefault():(w.returnValue=false);}if(browser.gecko&&n==46){t=e.selection.getRange();if(t.collapsed){k=t.startContainer;if(domUtils.isEmptyBlock(k)){var o=k.parentNode;while(domUtils.getChildCount(o)==1&&!domUtils.isBody(o)){k=o;o=o.parentNode;}if(k===o.lastChild){w.preventDefault();}return;}}}});e.addListener("keyup",function(r,u){var y=u.keyCode||u.which;if(!browser.gecko&&!d[y]&&!u.ctrlKey&&!u.metaKey&&!u.shiftKey&&!u.altKey){o=e.selection.getRange();if(o.collapsed){var h=o.startContainer,j=0;while(!domUtils.isBlockElm(h)){if(h.nodeType==1&&utils.indexOf(["FONT","B","I"],h.tagName)!=-1){var k=e.document.createElement(b[h.tagName]);if(h.tagName=="FONT"){k.style.cssText=(h.getAttribute("size")?"font-size:"+(c[h.getAttribute("size")]||12)+"px":"")+";"+(h.getAttribute("color")?"color:"+h.getAttribute("color"):"")+";"+(h.getAttribute("face")?"font-family:"+h.getAttribute("face"):"")+";"+h.style.cssText;}while(h.firstChild){k.appendChild(h.firstChild);}h.parentNode.insertBefore(k,h);domUtils.remove(h);if(!j){o.setEnd(k,k.childNodes.length).collapse(true);}h=k;j=1;}h=h.parentNode;}j&&o.select();}}if(y==8){if(browser.gecko){for(var m=0,v,x=domUtils.getElementsByTagName(this.body,"li");v=x[m++];){if(domUtils.isEmptyNode(v)&&!v.previousSibling){var w=v.parentNode;domUtils.remove(v);if(domUtils.isEmptyNode(w)){domUtils.remove(w);}}}}var o,h,t,l=this.currentSelectedArr;if(l&&l.length>0){for(var m=0,g;g=l[m++];){g.innerHTML=browser.ie?(browser.version<9?"&#65279":""):"<br/>";}o=new dom.Range(this.document);o.setStart(l[0],0).setCursor();if(a){e.undoManger.save();a=0;}if(browser.webkit){u.preventDefault();}return;}o=e.selection.getRange();h=o.startContainer;if(domUtils.isWhitespace(h)){h=h.parentNode;}var q=0;while(h.nodeType==1&&domUtils.isEmptyNode(h)&&dtd.$removeEmpty[h.tagName]){q=1;t=h.parentNode;domUtils.remove(h);h=t;}if(q&&h.nodeType==1&&domUtils.isEmptyNode(h)){if(browser.ie){var s=o.document.createElement("span");h.appendChild(s);o.setStart(h,0).setCursor();v=domUtils.findParentByTagName(h,"li",true);if(v){var n=v.nextSibling;while(n){if(domUtils.isEmptyBlock(n)){v=n;n=n.nextSibling;domUtils.remove(v);continue;}break;}}}else{h.innerHTML="<br/>";o.setStart(h,0).setCursor(false,true);}setTimeout(function(){if(browser.ie){domUtils.remove(s);}if(a){e.undoManger.save();a=0;}},0);}else{if(a){e.undoManger.save();a=0;}}}});};
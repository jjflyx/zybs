(function(){var a=function(b){return utils.findNode(b.selection.getStartElementPath(),["blockquote"]);};UE.commands["blockquote"]={execCommand:function(m,q){var p=this.selection.getRange(),o=a(this),c=dtd.blockquote,l=p.createBookmark(),t=this.currentSelectedArr;if(o){if(t&&t.length){domUtils.remove(o,true);}else{var j=p.startContainer,k=domUtils.isBlockElm(j)?j:domUtils.findParent(j,function(i){return domUtils.isBlockElm(i);}),e=p.endContainer,f=domUtils.isBlockElm(e)?e:domUtils.findParent(e,function(i){return domUtils.isBlockElm(i);});k=domUtils.findParentByTagName(k,"li",true)||k;f=domUtils.findParentByTagName(f,"li",true)||f;if(k.tagName=="LI"||k.tagName=="TD"||k===o){domUtils.remove(o,true);}else{domUtils.breakParent(k,o);}if(k!==f){o=domUtils.findParentByTagName(f,"blockquote");if(o){if(f.tagName=="LI"||f.tagName=="TD"){domUtils.remove(o,true);}else{domUtils.breakParent(f,o);}}}var h=domUtils.getElementsByTagName(this.document,"blockquote");for(var u=0,d;d=h[u++];){if(!d.childNodes.length){domUtils.remove(d);}else{if(domUtils.getPosition(d,k)&domUtils.POSITION_FOLLOWING&&domUtils.getPosition(d,f)&domUtils.POSITION_PRECEDING){domUtils.remove(d,true);}}}}}else{var v=p.cloneRange(),r=v.startContainer.nodeType==1?v.startContainer:v.startContainer.parentNode,b=r,s=1;while(1){if(domUtils.isBody(r)){if(b!==r){if(p.collapsed){v.selectNode(b);s=0;}else{v.setStartBefore(b);}}else{v.setStart(r,0);}break;}if(!c[r.tagName]){if(p.collapsed){v.selectNode(b);}else{v.setStartBefore(b);}break;}b=r;r=r.parentNode;}if(s){b=r=r=v.endContainer.nodeType==1?v.endContainer:v.endContainer.parentNode;while(1){if(domUtils.isBody(r)){if(b!==r){v.setEndAfter(b);}else{v.setEnd(r,r.childNodes.length);}break;}if(!c[r.tagName]){v.setEndAfter(b);break;}b=r;r=r.parentNode;}}r=p.document.createElement("blockquote");domUtils.setAttributes(r,q);r.appendChild(v.extractContents());v.insertNode(r);var g=domUtils.getElementsByTagName(r,"blockquote");for(var u=0,n;n=g[u++];){if(n.parentNode){domUtils.remove(n,true);}}}p.moveToBookmark(l).select();},queryCommandState:function(){if(this.highlight){return -1;}return a(this)?1:0;}};})();
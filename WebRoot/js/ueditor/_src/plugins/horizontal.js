UE.commands["horizontal"]={execCommand:function(b){var d=this;if(d.queryCommandState(b)!==-1){d.execCommand("insertHtml","<hr>");var a=d.selection.getRange(),e=a.startContainer;if(e.nodeType==1&&!e.childNodes[a.startOffset]){var c;if(c=e.childNodes[a.startOffset-1]){if(c.nodeType==1&&c.tagName=="HR"){if(d.options.enterTag=="p"){c=d.document.createElement("p");a.insertNode(c);a.setStart(c,0).setCursor();}else{c=d.document.createElement("br");a.insertNode(c);a.setStartBefore(c).setCursor();}}}}return true;}},queryCommandState:function(){return this.highlight||utils.findNode(this.selection.getStartElementPath(),["table"])?-1:0;}};
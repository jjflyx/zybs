UE.plugins["video"]=function(){var c=this,d;function b(f,h,e,j,g,i){return !g?(i?("<p "+(j!="none"?(j=="center"?' style="text-align:center;" ':' style="float:"'+j):"")+">"):"")+'<img align="'+j+'" width="'+h+'" height="'+e+'" _url="'+f+'" class="edui-faked-video"'+' src="'+c.options.UEDITOR_HOME_URL+'themes/default/images/spacer.gif" style="background:url('+c.options.UEDITOR_HOME_URL+'themes/default/images/videologo.gif) no-repeat center center; border:1px solid gray;" />'+(i?"</p>":""):'<embed type="application/x-shockwave-flash" class="edui-faked-video" pluginspage="http://www.macromedia.com/go/getflashplayer"'+' src="'+f+'" width="'+h+'" height="'+e+'" align="'+j+'"'+(j!="none"?' style= "'+(j=="center"?"display:block;":" float: "+j)+'"':"")+' wmode="transparent" play="true" loop="false" menu="false" allowscriptaccess="never" allowfullscreen="true" >';}function a(j){var g,e=domUtils.getElementsByTagName(c.document,!j?"embed":"img");for(var f=0,h;h=e[f++];){if(h.className!="edui-faked-video"){continue;}g=c.document.createElement("div");var k=h.style.cssFloat;g.innerHTML=b(j?h.getAttribute("_url"):h.getAttribute("src"),h.width,h.height,k||h.getAttribute("align"),j);h.parentNode.replaceChild(g.firstChild,h);}}c.addListener("beforegetcontent",function(){a(true);});c.addListener("aftersetcontent",function(){a(false);});c.addListener("aftergetcontent",function(e){if(e=="aftergetcontent"&&c.queryCommandState("source")){return;}a(false);});c.commands["insertvideo"]={execCommand:function(h,k){k=utils.isArray(k)?k:[k];var g=[];for(var f=0,j,e=k.length;f<e;f++){j=k[f];g.push(b(j.url,j.width||420,j.height||280,j.align||"none",false,true));}c.execCommand("inserthtml",g.join(""));},queryCommandState:function(){var f=c.selection.getRange().getClosedNode(),e=f&&(f.className=="edui-faked-video");return this.highlight?-1:(e?1:0);}};};
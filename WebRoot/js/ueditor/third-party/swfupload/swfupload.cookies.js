var SWFUpload;if(typeof(SWFUpload)==="function"){SWFUpload.prototype.initSettings=function(a){return function(b){if(typeof(a)==="function"){a.call(this,b);}this.refreshCookies(false);};}(SWFUpload.prototype.initSettings);SWFUpload.prototype.refreshCookies=function(k){if(k===undefined){k=true;}k=!!k;var f=this.settings.post_params;var d,j=document.cookie.split(";"),b=j.length,e,h,a,g;for(d=0;d<b;d++){e=j[d];while(e.charAt(0)===" "){e=e.substring(1,e.length);}h=e.indexOf("=");if(h>0){a=e.substring(0,h);g=e.substring(h+1);f[a]=g;}}if(k){this.setPostParams(f);}};}
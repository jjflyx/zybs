UE.ajax=function(){return{request:function(d,f){var j=b(),g=false,e={method:"POST",timeout:5000,async:true,data:{},onsuccess:function(){},onerror:function(){}};if(typeof d==="object"){f=d;d=f.url;}if(!j||!d){return;}var i=f?utils.extend(e,f):e;var l=a(i);if(!utils.isEmptyObject(i.data)){l+=(l?"&":"")+a(i.data);}var k=setTimeout(function(){if(j.readyState!=4){g=true;j.abort();clearTimeout(k);}},i.timeout);var c=i.method.toUpperCase();var h=d+(d.indexOf("?")==-1?"?":"&")+(c=="POST"?"":l+"&noCache="+ +new Date);j.open(c,h,i.async);j.onreadystatechange=function(){if(j.readyState==4){if(!g&&j.status==200){i.onsuccess(j);}else{i.onerror(j);}}};if(c=="POST"){j.setRequestHeader("Content-Type","application/x-www-form-urlencoded");j.send(l);}else{j.send(null);}}};function a(d){var e=[];for(var c in d){if(c=="method"||c=="timeout"||c=="async"){continue;}if(!((typeof d[c]).toLowerCase()=="function"||(typeof d[c]).toLowerCase()=="object")){e.push(encodeURIComponent(c)+"="+encodeURIComponent(d[c]));}}return e.join("&");}function b(){var c=null;if(window.XMLHttpRequest){c=new XMLHttpRequest();}else{try{c=new ActiveXObject("Msxml2.XMLHTTP");}catch(d){try{c=new ActiveXObject("Microsoft.XMLHTTP");}catch(d){}}}return c;}}();
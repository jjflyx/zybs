UE.plugins["autofloat"]=function(){var r=this,s=r.getLang();r.setOpt({topOffset:0});var m=r.options.autoFloatEnabled!==false,k=r.options.topOffset;if(!m){return;}var f=UE.ui.uiUtils,g=browser.ie&&browser.version<=6,l=browser.quirks;function a(t){if(!t.ui){alert(s.autofloatMsg);return 0;}return 1;}function h(){var t=document.body.style;t.backgroundImage='url("about:blank")';t.backgroundAttachment="fixed";}var i,n=document.createElement("div"),d,c,q,o=true;function j(){var u=domUtils.getXY(d),t=domUtils.getComputedStyle(d,"position"),v=domUtils.getComputedStyle(d,"left");d.style.width=d.offsetWidth+"px";d.style.zIndex=r.options.zIndex*1+1;d.parentNode.insertBefore(n,d);if(g||(l&&browser.ie)){if(d.style.position!="absolute"){d.style.position="absolute";}d.style.top=(document.body.scrollTop||document.documentElement.scrollTop)-c+k+"px";}else{if(browser.ie7Compat&&o){o=false;d.style.left=domUtils.getXY(d).x-document.documentElement.getBoundingClientRect().left+2+"px";}if(d.style.position!="fixed"){d.style.position="fixed";d.style.top=k+"px";((t=="absolute"||t=="relative")&&parseFloat(v))&&(d.style.left=u.x+"px");}}}function e(){o=true;if(n.parentNode){n.parentNode.removeChild(n);}d.style.cssText=i;}function b(){var t=q(r.container);if(t.top<0&&t.bottom-d.offsetHeight>0){j();}else{e();}}var p=utils.defer(function(){b();},browser.ie?200:100,true);r.addListener("destroy",function(){domUtils.un(window,["scroll","resize"],b);r.removeListener("keydown",p);});r.addListener("ready",function(){if(a(r)){q=f.getClientRect;d=r.ui.getDom("toolbarbox");c=q(d).top;i=d.style.cssText;n.style.height=d.offsetHeight+"px";if(g){h();}r.addListener("autoheightchanged",function(v,u){if(u){domUtils.on(window,["scroll","resize"],b);r.addListener("keydown",p);}else{domUtils.un(window,["scroll","resize"],b);r.removeListener("keydown",p);}});r.addListener("beforefullscreenchange",function(v,u){if(u){e();}});r.addListener("fullscreenchanged",function(v,u){if(!u){b();}});r.addListener("sourcemodechanged",function(v,u){setTimeout(function(){b();},0);});}});};
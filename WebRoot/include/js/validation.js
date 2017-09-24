//创建人 zj
//动态统计输入数
//id为notespan提醒标签maxChars最大字数which当前对象表单onkeyup="checkLength(this,200);"
function checkLength(which,maxChars) { 
        if (which.value.length > maxChars) 
        which.value = which.value.substring(0,maxChars); 
        var curr = maxChars - which.value.length; 
        document.getElementById("notespan").innerHTML = "("+which.value.length+"/"+curr+")" ;
        }
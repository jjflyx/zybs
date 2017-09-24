window.onload=function(){ 
	 var content_id = $("#trafficID").val();
        // alert("content_id1115555 : "+content_id);
        var url = "http://www.ahjt.gov.cn:9001/private/cms/index/cms_indexpv/contentTraffic?content_id="+content_id+"&callback=?";
    $.getJSON(url,function(backdata){
          $("#content_traffic").html("访问量 ："+backdata.traffic);
     }); 
}


























(function($){
$.fn.extend({
        Scroll:function(opt,callback){
                //������ʼ��
                if(!opt) var opt={};
                var _this=this.eq(0).find("ul:first");
                var        lineH=_this.find("li:first").height(), //��ȡ�и�
                        line=opt.line?parseInt(opt.line,10):parseInt(this.height()/lineH,10), //ÿ�ι�����������Ĭ��Ϊһ�������������߶�
                        speed=opt.speed?parseInt(opt.speed,10):1000, //���ٶȣ���ֵԽ���ٶ�Խ�������룩
                        timer=opt.timer?parseInt(opt.timer,10):3000; //������ʱ���������룩
                if(line==0) line=1;
                var upHeight=0-line*lineH;
                //��������
                scrollUp=function(){
                        _this.animate({
                                marginTop:upHeight
                        },speed,function(){
                                for(i=1;i<=line;i++){
                                        _this.find("li:first").appendTo(_this);
                                }
                                _this.css({marginTop:0});
                        });
                }
                //����¼���
                _this.hover(function(){
                        clearInterval(timerID);
                },function(){
                        timerID=setInterval("scrollUp()",timer);
                }).mouseout();
        }        
})
})(jQuery);

$(document).ready(function(){
        $("#scrollDiv").Scroll({line:2,speed:500,timer:2000});
});
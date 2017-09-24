
function getStateName(status){
	var name = "";
	if(status){
		if(status == -1){
			name = "草稿";
		}else if(status == -2){
			name = "评价不满意";
		}else if(status == -3){
			name = "单位申请回退给督办";
		}else if(status == 0){
			name = "新建";
		}else if(status == 1){
			name = "办理中";
		}else if(status == 2){
			name = "反馈";
		}else if(status == 3){
			name = "转办办结";
		}else if(status == 4){
			name = "回收站";
		}else if(status == 5){
			name = "呈批";
		}else if(status == 6){
			name = "退回";
		}else if(status == 7){
			name = "处理中";
		}else if(status == 8){
			name = "延期";
		}else if(status == 9){
			name = "直办办结";
		}
	}
	return name;
}
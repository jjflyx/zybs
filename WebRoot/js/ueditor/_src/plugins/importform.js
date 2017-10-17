UE.commands["importform"] = {
	execCommand : function(b, a) {
	},
	queryCommandState : function() {
		return this.highlight ? -1 : 0;
	}
};
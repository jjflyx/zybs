UE.commands["exportform"] = {
	execCommand : function(b, a) {
		exportform();
	},
	queryCommandState : function() {
		return this.highlight ? -1 : 0;
	}
};
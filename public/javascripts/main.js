$(function() {
	$(".vote-on").hover(function() {
		$(this).addClass("btn-danger").removeClass("btn-success");
	}, function() {
		$(this).addClass("btn-success").removeClass("btn-danger");
	})
	
	$(".vote-off").hover(function() {
		$(this).addClass("btn-info");
	}, function() {
		$(this).removeClass("btn-info");
	})
});
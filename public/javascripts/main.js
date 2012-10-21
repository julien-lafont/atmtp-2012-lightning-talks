$(function() {
	$(".vote-on").hover(function() {
		$(this).text("-1").addClass("btn-danger");
	}, function() {
		$(this).text("+1").removeClass("btn-danger");
	})
});
$(document).ready(function(){
    $("#addBtn").on("click",function(){
        var addName = $("#name").val();
        var addPrice = $("#price").val();
        var addPath = $("#addFile").prop('files');
    });
});
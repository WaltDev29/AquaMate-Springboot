$(document).ready(function () {
    $("main img").click(function () {
        $(this).css({
            top: "0px",
            cursor: "default",
            "z-index": "-1"
        });
        $("#div_sign").css({
            opacity: "1",
            visibility: "visible",
        });
    })
})
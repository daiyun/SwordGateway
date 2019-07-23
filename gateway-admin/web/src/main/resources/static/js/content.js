// var $parentNode = window.parent.document;

// function $childNode(name) {
//     return window.frames[name]
// }

// // tooltips
// $('.tooltip-demo').tooltip({
//     selector: "[data-toggle=tooltip]",
//     container: "body"
// });

// // 使用animation.css修改Bootstrap Modal
// $('.modal').appendTo("body");

// $("[data-toggle=popover]").popover();


//判断当前页面是否在iframe中
if (top == this) {
    var gohome = '<div class="gohome"><a class="animated bounceInUp" href="index.html?v=4.0" title="返回首页"><i class="fa fa-home"></i></a></div>';
    $('body').append(gohome);
}

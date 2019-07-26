var Api = (function ($) {
    $.route = {
        list: '/route/list',
        get: '/route/get',
        add: '/route/add',
        edit: '/route/edit',
        del: '/route/del',
        enable: '/route/enable',
        disable: '/route/disable',
        predicateList: '/route/predication/list?routeMark=${routeMark}',
        predicateEdit: '/route/predication/edit',
        predicateAdd: '/route/predication/add',
        predicateDel: '/route/predication/del',
        filterList: '/route/filter/list?routeMark=${routeMark}',
        filterEdit: '/route/filter/edit',
        filterAdd: '/route/filter/add',
        filterDel: '/route/filter/del',
    };
    $.payload = {
        list: '/payload/list',
        get: '/payload/get',
        add: '/payload/add',
        edit: '/payload/edit',
        del: '/payload/del',
        enable: '/payload/enable',
        disable: '/payload/disable',
    }
    return $;
})({});
String.prototype.format = function (opts) {//use 'my name is ${name}'.format({name:'lake'})
    var data = Array.prototype.slice.call(arguments, 0),
        toString = Object.prototype.toString;
    if (data.length) {
        data = data.length == 1 ?
            (opts !== null && (/\[object Array\]|\[object Object\]/.test(toString.call(opts))) ? opts : data) : data;
        return this.replace(/\$\{(.+?)\}/g, function (match, key) {
            var replacer = data[key];
            // chrome 下 typeof /a/ == 'function'

            if ('[object Function]' == toString.call(replacer)) {
                replacer = replacer(key);
            }
            return ('undefined' == typeof replacer ? '' : replacer);
        });
    }
    return this;
};
$.getUrlParam = function (search, q) {
    var pattern = new RegExp("[?&]" + q + "\=([^&]+)", "g");
    var matcher = pattern.exec(search);
    var items = null;
    if (null != matcher) {
        try {
            items = decodeURIComponent(decodeURIComponent(matcher[1]));
        } catch (e) {
            try {
                items = decodeURIComponent(matcher[1]);
            } catch (e) {
                items = matcher[1];
            }
        }
    }
    return items;
};
$.ajaxPostApi = function (ajaxOpts) {
    var defaultOpt = {
        type: 'post',
        async: false,
        contentType: "application/json",
        dataType: "json"
    };
    return $.ajax($.extend(defaultOpt, ajaxOpts));
};
$.ajaxGetApi = function (ajaxOpts) {
    var defaultOpt = {
        type: 'get',
        async: false,
        dataType: "json"
    };
    return $.ajax($.extend(defaultOpt, ajaxOpts));
};
var toastrOpt = (function ($) {
    $.success = {
        "closeButton": true,
        "debug": false,
        "progressBar": true,
        "positionClass": "toast-top-right",
        "onclick": null,
        "showDuration": "400",
        "hideDuration": "1000",
        "timeOut": "3000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };
    $.error = {
        "closeButton": true,
        "debug": false,
        "progressBar": true,
        "positionClass": "toast-top-right",
        "onclick": null,
        "showDuration": "400",
        "hideDuration": "1000",
        "timeOut": "7000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };
    return $;
})({});


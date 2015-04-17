var url=document.location.protocol+"//"+document.location.host + "/lifestyle/";
$(function(){
	$("#upload").uploadify({
	 	'swf'      : url+'js/uploadify/uploadify.swf',    //指定上传控件的主体文件
        'uploader' : url+'rest/pub/upanddown/uploadImage?var=' + new Date().getTime(),    //指定服务器端上传处理文件
        'cancelImg': 'js/uploadify/uploadify-cancel.png',
        'auto':true,
        'method':'post',
        'buttonText' : '选择文件',
        'fileSizeLimit':'50000000',
        'onUploadSuccess':function(file, _data, response){
        	$("#images").append('<img src="'+url+'images/'+encodeURI(file.name)+'" />');
        },
        'onComplete':function(){
        	alert('上传完成');
        }
	});
});
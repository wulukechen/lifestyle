var url=document.location.protocol+"//"+document.location.host + "/lifestyle/";
$(function(){
	$("#upload").uploadify({
	 	'swf'      : url+'js/uploadify/uploadify.swf',    //指定上传控件的主体文件
        'uploader' : url+'rest/pub/upanddown/uploadImage',    //指定服务器端上传处理文件
        'cancelImg': 'js/uploadify/uploadify-cancel.png',
        'auto':true,
        'fileObjName'  : 'upload',
        'fileDataName' : 'imageName',
        'fileTypeExts' : '*.*',
        'buttonText' : '选择文件',
        'fileSizeLimit':'50000000',
        'onUploadSuccess':function(file, _data, response){
        	data = eval("("+_data+")");
        	var src="<tr style='border:0px'>" 
        			+"<td style='border:0px' ><img src='"+url+"images/"+data.imagePath+"'  />"
        			+"<input type='hidden' name='id' value='"+data.id+"' />"
        			+"<input type='hidden' name='imageURL' value='"+data.imagePath+"' /></td>"
        			+"<td style='border:0px'><a href='javascript:void(0);' onclick='removeImage(this)'>删除</a></td></tr>";
        	$("#images").append(src);
         }
	});
});
var url=document.location.protocol+"//"+document.location.host + "/uploadfile/";
$(function(){
	alert(url);
	$("#upload").uploadify({
	 	'swf'      : url+'js/uploadify/uploadify.swf',    //指定上传控件的主体文件
        'uploader' : url+'upload/upload.do',    //指定服务器端上传处理文件
        'auto':true,
        'fileObjName'  : 'Upload',
        'buttonText' : '选择文件',
        'fileSizeLimit':'50000000',
        'onUploadSuccess':function(file, _data, response){
        	data = eval("("+_data+")");
        	var src="<tr style='border:0px'>" 
        			+"<td style='border:0px' ><img src='"+YMLib.url+"upload/"+data.attachmentUrl+"'  />"
        			+"<input type='hidden' name='imageName' value='"+data.attachmentName+"' />"
        			+"<input type='hidden' name='imageURL' value='"+data.attachmentUrl+"' /></td>"
        			+"<td style='border:0px'><a href='javascript:void(0);' onclick='removeImage(this)'>删除</a></td></tr>";
        	$("#images").append(src);
         }
	});
});
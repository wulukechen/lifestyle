var url=document.location.protocol+"//"+document.location.host + "/lifestyle/";
$(function(){
	var imageName=null;
	$("#upload").uploadify({
	 	'swf'      : url+'js/uploadify/uploadify.swf',    //指定上传控件的主体文件
        'uploader' : url+'rest/pub/upanddown/uploadImage?var=' + new Date().getTime(),    //指定服务器端上传处理文件
        'cancelImg': 'js/uploadify/uploadify-cancel.png',
        'auto':true,
        'method':'post',
        'buttonText' : '选择文件',
        'fileSizeLimit':'50000000',
        'onUploadSuccess':function(file, _data, response){
        	imageName=file.name;
        	$("#images").attr("src",url+'upload/'+encodeURI(file.name));
        	$(".jcrop-preview").attr("src",url+'upload/'+encodeURI(file.name));
        	$(".jcrop-preview").show();
        	$("#imageName").val(file.name);
        	cutImage();
        }
	});

	
	

	$("#crop_submit").click(function(){
		var params=$("#crop_form").serialize()
		if(parseInt($("#x").val())){
			$.post(url+'rest/pub/upanddown/cutImage',params,function(){},"json");
		}else{
			alert("要先在图片上划一个选区再单击确认剪裁的按钮哦！");
		}
	});
});
	function clearCoords(){
	  $('#coords input').val('');
	};
	function cutImage(){
		var jcrop_api,boundx,boundy,
		$preview = $('#preview-pane'),
		$pcnt = $('#preview-pane .preview-container'),
		$pimg = $('#preview-pane .preview-container img'),
		xsize = $pcnt.width(),
		ysize = $pcnt.height();
		$('#images').Jcrop({
			aspectRatio: xsize / ysize,
			onChange:updatePreview,
			onSelect: updatePreview
		},function(){
			// Use the API to get the real image size
		  var bounds = this.getBounds();
		  boundx = bounds[0];
		  boundy = bounds[1];
		  // Store the API in the jcrop_api variable
		  jcrop_api = this;
		  
		  // Move the preview into the jcrop container for css positioning
		  $preview.appendTo(jcrop_api.ui.holder);
		});
		
		function updatePreview(c){
			$('#x').val(c.x);
			$('#y').val(c.y);
			$('#w').val(c.w);
			$('#h').val(c.h);
				
			if (parseInt(c.w) > 0) {
			var rx = xsize / c.w;
			var ry = ysize / c.h;
			
			$pimg.css({
			  width: Math.round(rx * boundx) + 'px',
			  height: Math.round(ry * boundy) + 'px',
			  marginLeft: '-' + Math.round(rx * c.x) + 'px',
			  marginTop: '-' + Math.round(ry * c.y) + 'px'
			});
			}
		}
	}
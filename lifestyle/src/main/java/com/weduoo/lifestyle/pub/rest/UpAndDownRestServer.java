package com.weduoo.lifestyle.pub.rest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;

import com.hdsx.webutil.struts.BaseActionSupport;
import com.weduoo.lifestyle.pub.bean.Images;
import com.weduoo.lifestyle.pub.server.UpAndDownServer;
import com.weduoo.lifestyle.utils.JsonUtils;

@Path("/pub/upanddown")
@Service
public class UpAndDownRestServer extends BaseActionSupport{

	private static final long serialVersionUID = -1522944797660323689L;
	
	@Resource(name="upAndDownServerImpl")
	private UpAndDownServer upAndDownServer;
	
	@POST
	@Path("/uploadImage")
	@Consumes({MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public void UploadImage(@QueryParam("upload") File upload,
				@QueryParam("imageName") String imageName){
		try {
			FileInputStream in = new FileInputStream(upload);
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
			String date = sdf.format(new Date());
			String imagePath = date+imageName;
			String uri = ServletActionContext.getRequest().getRealPath("/images")+"\\"+imagePath;
			System.out.println(uri+"============================================");
			FileOutputStream out = new FileOutputStream(uri);
			byte[] buf = new byte[1024];
			for(int readNum;(readNum = in.read(buf))!=-1;)
				out.write(buf, 0, readNum);
			out.close();
			in.close();
			Images image = new Images();
			image.setImageName(imageName);;
			image.setImagePath(imagePath);
			
			JsonUtils.write(image, this.getresponse().getWriter());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}

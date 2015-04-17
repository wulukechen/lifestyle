package com.weduoo.lifestyle.pub.rest;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts2.ServletActionContext;
import org.apache.wink.common.annotations.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hdsx.webutil.struts.BaseActionSupport;
import com.weduoo.lifestyle.pub.bean.Images;
import com.weduoo.lifestyle.pub.server.UpAndDownServer;
import com.weduoo.lifestyle.utils.JsonUtils;

@Path("/pub/upanddown")
//@Consumes(MediaType.MULTIPART_FORM_DATA)
@Service
public class UpAndDownRestServer extends BaseActionSupport{

	 private static final long serialVersionUID = -5934888513038926173L;
	
	 @Resource(name="upAndDownServerImpl")
	 private UpAndDownServer upAndDownServer;
	 @POST
	 @Path("/uploadImage")
	 @Produces(MediaType.TEXT_PLAIN)
	 public void uploadStatePolicy(@Context HttpServletRequest request) throws IOException, Exception {
		 Images image = null;
		 try {
	         image = saveFile(request);
	         if (!image.getImageName().equals("")) {
	        	 String id=UUID.randomUUID().toString();
	        	 image.setId(id);
	        	 upAndDownServer.saveImages(image);
	        	 JsonUtils.write(image, this.getresponse().getWriter());
	         } else {
	         }
	     } catch (Exception ex) {
	    	 
	     }
		
	 }
	
	 private Images saveFile(HttpServletRequest request) {
		    String imageName = "";
		    String reName="";
		    try {
		        if (ServletFileUpload.isMultipartContent(request)) {
		            FileItemFactory factory = new DiskFileItemFactory();
		            ServletFileUpload upload = new ServletFileUpload(factory);
		            List<FileItem> items = null;
		            try {
		                items = upload.parseRequest(request);
		            } catch (FileUploadException e) {
		                e.printStackTrace();
		            }
		            if (items != null) {
		                Iterator<FileItem> iter = items.iterator();
		                while (iter.hasNext()) {
		                    FileItem item = iter.next();
		                    if (!item.isFormField() && item.getSize() > 0) {
		                        imageName = processFileName(item.getName());
		                        String subName=imageName.substring(
		                        		imageName.lastIndexOf("."), imageName.length());
		                    	SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
		            			reName = sdf.format(new Date());
		            			//String uri = ServletActionContext.getRequest().getRealPath("/images")+"\\"+reName;
		            			String uri=request.getSession().getServletContext().getRealPath("/images")+"\\"+ imageName;
		            			System.out.println(uri);
		            			try {
		                            item.write(new File(uri));
		                        } catch (Exception e) {
		                            e.printStackTrace();
		                        }
		                    }
		                }
		            }
		        }
		    } catch (Exception e) {
		    }
		    Images image=new Images();
		    image.setReName(reName);
		    image.setImageName(imageName);
		    return image;
		}

		private String processFileName(String fileNameInput) {
		    String fileNameOutput = null;
		    fileNameOutput = fileNameInput.substring(
		            fileNameInput.lastIndexOf("\\") + 1, fileNameInput.length());
		    return fileNameOutput;
		}

}

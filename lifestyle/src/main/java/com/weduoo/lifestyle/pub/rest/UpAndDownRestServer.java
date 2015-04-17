package com.weduoo.lifestyle.pub.rest;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
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
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
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
	        	 String id=UUID.randomUUID().toString().replaceAll("-", "");
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
		            			String uri=request.getSession().getServletContext().getRealPath("/upload")+"\\"+ imageName;
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
		    image.setReImageName(reName);
		    image.setImageName(imageName);
		    return image;
		}

		private String processFileName(String fileNameInput) {
		    String fileNameOutput = null;
		    fileNameOutput = fileNameInput.substring(
		            fileNameInput.lastIndexOf("\\") + 1, fileNameInput.length());
		    return fileNameOutput;
		}
		
		@POST 
		@Path("/cutImage")
		@Produces(MediaType.TEXT_PLAIN)
		public void cutImage(@FormParam("x") int x,@FormParam("y") int y,
				@FormParam("w") double w,@FormParam("h") double h,
				@FormParam("imageName") String imageName,@Context HttpServletRequest request){
			System.out.println(x+"-"+y+"-"+w+"-"+h+"-"+imageName);
			String  path = request.getSession().getServletContext().getRealPath("/upload")+"\\";
			String spath=path+imageName.substring(0,imageName.lastIndexOf("."));
			String  gpath=path+imageName;
			System.out.println(path);
			abscut(gpath, spath,x, y, w, h);
		}
		
		public static void abscut(String srcImageFile,String path, int x, int y, double destWidth,double destHeight) {
			try {
			Image img;
			ImageFilter cropFilter;
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			int srcWidth = bi.getWidth(); // 源图宽度
			int srcHeight = bi.getHeight(); // 源图高度

			if (srcWidth >= destWidth && srcHeight >= destHeight) {
			Image image = bi.getScaledInstance(srcWidth, srcHeight,Image.SCALE_DEFAULT);

			int x1 = x*srcWidth/420;
			int y1 = y*srcWidth/420;
			int w = (int) (destWidth*srcWidth/420);
			int h = (int) (destHeight*srcWidth/420);

			// 四个参数分别为图像起点坐标和宽高
			// 即: CropImageFilter(int x,int y,int width,int height)
			cropFilter = new CropImageFilter(x1, y1, w, h);
			img = Toolkit.getDefaultToolkit().createImage(
			new FilteredImageSource(image.getSource(), cropFilter));
			BufferedImage tag = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(img, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			// 输出为文件
			ImageIO.write(tag, "JPEG", new File(path+"_small.jpg"));
			}
			} catch (Exception e) {
			e.printStackTrace();
			}
		}
}

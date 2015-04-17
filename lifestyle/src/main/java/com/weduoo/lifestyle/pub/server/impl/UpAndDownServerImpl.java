package com.weduoo.lifestyle.pub.server.impl;

import org.springframework.stereotype.Service;

import com.hdsx.dao.query.base.BaseOperate;
import com.weduoo.lifestyle.pub.bean.Images;
import com.weduoo.lifestyle.pub.server.UpAndDownServer;
@Service
public class UpAndDownServerImpl extends BaseOperate implements UpAndDownServer{


	public UpAndDownServerImpl() {
		super(Images.class, "jdbc");
	}
	public void saveImages(Images image) {
		this.insert("saveImages", image);
	}
	
}

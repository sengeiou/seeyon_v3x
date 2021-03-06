package com.seeyon.v3x.edoc.event;

import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.seeyon.v3x.common.authenticate.domain.User;
import com.seeyon.v3x.common.constants.ApplicationCategoryEnum;
import com.seeyon.v3x.common.exceptions.MessageException;
import com.seeyon.v3x.common.usermessage.MessageContent;
import com.seeyon.v3x.common.usermessage.MessageReceiver;
import com.seeyon.v3x.common.usermessage.UserMessageManager;
import com.seeyon.v3x.common.web.login.CurrentUser;
import com.seeyon.v3x.edoc.domain.EdocSignReceipt;
import com.seeyon.v3x.exchange.dao.EdocSendDetailDao;
import com.seeyon.v3x.exchange.domain.EdocSendDetail;
import com.seeyon.v3x.exchange.domain.EdocSendRecord;
import com.seeyon.v3x.exchange.manager.EdocExchangeManager;
import com.seeyon.v3x.exchange.manager.SendEdocManager;
import com.seeyon.v3x.exchange.util.Constants;
import com.seeyon.v3x.util.annotation.ListenEvent;
/**
 * 外部系统签收A8公文时候产生回执方法的类对象。
 * @author muj
 */
public class EdocSignHandler {
	private static final Log log = LogFactory.getLog(EdocSignHandler.class);

	private EdocSendDetailDao edocSendDetailDao;
	
	private UserMessageManager userMessageManager;
	
	private SendEdocManager sendEdocManager;
	
	public EdocSendDetailDao getEdocSendDetailDao() {
		return edocSendDetailDao;
	}
	public void setEdocSendDetailDao(EdocSendDetailDao edocSendDetailDao) {
		this.edocSendDetailDao = edocSendDetailDao;
	}
	public UserMessageManager getUserMessageManager() {
		return userMessageManager;
	}
	public void setUserMessageManager(UserMessageManager userMessageManager) {
		this.userMessageManager = userMessageManager;
	}
	public SendEdocManager getSendEdocManager() {
		return sendEdocManager;
	}
	public void setSendEdocManager(SendEdocManager sendEdocManager) {
		this.sendEdocManager = sendEdocManager;
	}
	
	/**
	 * 外部系统签收A8的公文时候,调用此方法对A8产生回执。
	 * @param edocSendDetailId ：公文发送详细ID
	 * @param receipt		：回执数据实体
	 */
	
	public void signEdoc(long edocSendDetailId,EdocSignReceipt receipt){
		
		User user = CurrentUser.get();
		
		EdocSendDetail edocSendDetail = edocSendDetailDao.get(edocSendDetailId);
		
		if(edocSendDetail!=null)
		{
			//签收意见
			edocSendDetail.setContent(receipt.getOpinion());
			//签收人
			edocSendDetail.setRecUserName(receipt.getReceipient());
			//签收单位名称
			edocSendDetail.setRecOrgName(receipt.getSignUnit());
			//签收时间
			long l = System.currentTimeMillis();
			edocSendDetail.setRecTime(new Timestamp(l));
			//签收状态
			edocSendDetail.setStatus(Constants.C_iStatus_Recieved);
			
			edocSendDetailDao.update(edocSendDetail);
		}
	}
}

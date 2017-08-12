/**
* Organization: lemon-china <br>
* Date: 2017-08-12 16:44:02 <br>
* Automatically Generate By EasyCodeGenerine <br>
* Copyright (c) 2017 All Rights Reserved.
*/
package cn.lemon.dubbo.message.service;

import java.util.List;
import javax.annotation.Resource;
import com.alibaba.dubbo.config.annotation.Service;
import cn.lemon.framework.query.Page;
import cn.lemon.framework.query.QueryPage;
import cn.lemon.framework.utils.BeanUtil;
import cn.lemon.framework.utils.DateUtil;
import cn.lemon.framework.core.BasicService;
import cn.lemon.dubbo.message.dao.IMessageRecordDao;
import cn.lemon.dubbo.message.entity.MessageRecord;
import cn.lemon.dubbo.message.dto.MessageRecordDto;
import cn.lemon.dubbo.message.api.IMessageRecordService;

/**************************
 * MessageRecordService
 * 消息发送记录表
 * @author lonyee
 * @date 2017-08-12 16:44:02
 * 
 **************************/
@Service
public class MessageRecordService extends BasicService implements IMessageRecordService {
	
	@Resource
	private IMessageRecordDao messageRecordDao;
	
	/**
	 * 获取对象
	 */
	@Override
	public MessageRecordDto getById(Long userId, Long id) {
		MessageRecord messageRecord = messageRecordDao.findById(id);
		MessageRecordDto messageRecordDto = BeanUtil.toBeanValues(messageRecord, MessageRecordDto.class);
		return messageRecordDto;
	}

	/**
	 * 获取分页数据
	 */
	@Override
	public Page getListByPager(Long userId, QueryPage queryPage) {
		List<MessageRecord> list = messageRecordDao.findPage(queryPage);
		List<MessageRecordDto> listDto = BeanUtil.getBeanList(list, MessageRecordDto.class);
		return queryPage.setRows(listDto);
	}

	 /**
	 * 保存数据
	 */
	 @Override
	public int save(Long userId, MessageRecordDto messageRecordDto) {
		MessageRecord messageRecord = BeanUtil.toBeanValues(messageRecordDto, MessageRecord.class);
		messageRecord.setCreator(userId);
		messageRecord.setCreatedDate(DateUtil.getNowTime());
		return messageRecordDao.save(messageRecord);
	}

	 /**
	 * 更新数据
	 */
	 @Override
	public int update(Long userId, MessageRecordDto messageRecordDto) {
		MessageRecord messageRecord = BeanUtil.toBeanValues(messageRecordDto, MessageRecord.class);
		return messageRecordDao.update(messageRecord);
	}

	/**
	 * 删除对象
	 */
	@Override
	public int delete(Long userId, Long id) {
		return messageRecordDao.deleteBySoft(id);
	}

}

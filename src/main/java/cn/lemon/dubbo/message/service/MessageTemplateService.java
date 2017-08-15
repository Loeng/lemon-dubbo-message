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
import cn.lemon.dubbo.message.dao.IMessageTemplateDao;
import cn.lemon.dubbo.message.entity.MessageTemplate;
import cn.lemon.dubbo.message.dto.MessageTemplateDto;
import cn.lemon.dubbo.message.api.IMessageTemplateService;

/**************************
 * MessageTemplateService
 * 消息模板表
 * @author lonyee
 * @date 2017-08-12 16:44:02
 * 
 **************************/
@Service
public class MessageTemplateService extends BasicService implements IMessageTemplateService {
	
	@Resource
	private IMessageTemplateDao messageTemplateDao;
	
	/**
	 * 获取对象
	 */
	@Override
	public MessageTemplateDto getById(Long userId, Long id) {
		MessageTemplate messageTemplate = messageTemplateDao.findById(id);
		MessageTemplateDto messageTemplateDto = BeanUtil.toBeanValues(messageTemplate, MessageTemplateDto.class);
		return messageTemplateDto;
	}

	/**
	 * 获取分页数据
	 */
	@Override
	public Page getListByPager(Long userId, QueryPage queryPage) {
		List<MessageTemplate> list = messageTemplateDao.findPage(queryPage);
		List<MessageTemplateDto> listDto = BeanUtil.getBeanList(list, MessageTemplateDto.class);
		return queryPage.setRows(listDto);
	}

	 /**
	 * 保存数据
	 */
	 @Override
	public int save(Long userId, MessageTemplateDto messageTemplateDto) {
		MessageTemplate messageTemplate = BeanUtil.toBeanValues(messageTemplateDto, MessageTemplate.class);
		messageTemplate.setId(this.generateId());
		messageTemplate.setCreator(userId);
		messageTemplate.setCreatedDate(DateUtil.getNowTime());
		return messageTemplateDao.save(messageTemplate);
	}

	 /**
	 * 更新数据
	 */
	 @Override
	public int update(Long userId, MessageTemplateDto messageTemplateDto) {
		MessageTemplate messageTemplate = BeanUtil.toBeanValues(messageTemplateDto, MessageTemplate.class);
		return messageTemplateDao.update(messageTemplate);
	}

	/**
	 * 删除对象
	 */
	@Override
	public int delete(Long userId, Long id) {
		return messageTemplateDao.deleteBySoft(id);
	}

}

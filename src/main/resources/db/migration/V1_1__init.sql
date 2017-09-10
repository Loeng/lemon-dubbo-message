create table  `message_template`
(
       `id`              BIGINT  primary key not null comment '编号',
       `center_id`       VARCHAR(40) comment '第三方模板中心ID',
       `message_type`    VARCHAR(12) comment '消息类型',
       `push_method`     VARCHAR(12) comment '推送方式 INL: 站内信 SMS：短信 EMI：邮件 WXM：微信 IOS：ios ANR：android RMQ：rabbitMQ',
       `title`           VARCHAR(50) comment '标题',
       `template`        VARCHAR(1000) comment '内容模板',
       `url`             VARCHAR(150) comment '地址 带http/https为全路径地址/不带自动加上当前项目地址',
       `usable`          TINYINT comment '是否可用 是否可用 0 不可用 1 可用',
       `created_date`    DATETIME comment '创建时间',
       `creator`         BIGINT comment '创建人员'
);
alter table `message_template` comment= '消息模板表';

create table  `message_record`
(
       `id`              BIGINT auto_increment primary key not null comment '编号',
       `template_id`     BIGINT comment '模板ID',
       `center_id`       VARCHAR(40) comment '第三方模板中心ID',
       `push_method`     VARCHAR(12) comment '推送方式 INL: 站内信 SMS：短信 EMI：邮件 WXM：微信 IOS：ios ANR：android RMQ：rabbitMQ',
       `receiver`        VARCHAR(30) comment '发送地址 手机号、openId等',
       `receiver_id`     BIGINT comment '接收人员ID',
       `title`           VARCHAR(80) comment '标题',
       `message`         VARCHAR(1000) comment '名称',
       `url`             VARCHAR(100) comment '访问地址',
       `send_times`      TINYINT comment '发送次数 0 待发送 1-3 发送失败次数 9 发送成功',
       `schedule_time`   DATETIME comment '预约发送时间 空为立即发送',
       `usable`          TINYINT comment '是否可用 0 不可用 1 可用',
       `created_date`    DATETIME comment '创建时间',
       `creator`         BIGINT comment '创建人员'
);
alter table `message_record` comment= '消息发送记录表';
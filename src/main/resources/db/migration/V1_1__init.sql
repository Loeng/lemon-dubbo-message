create table  `message_template`
(
       `id`              INT primary key not null auto_increment comment '编号',
       `center_id`       VARCHAR(50) not null DEFAULT '' comment '第三方模板中心ID',
       `message_type`    VARCHAR(12) not null DEFAULT '' comment '消息类型',
       `push_method`     VARCHAR(12) not null DEFAULT '' comment '推送方式 INL: 站内信 SMS：短信 EMI：邮件 WXM：微信 IOS：ios ANR：android RMQ：rabbitMQ',
       `title`           VARCHAR(50) not null DEFAULT '' comment '标题',
       `template`        VARCHAR(1000) not null DEFAULT '' comment '内容模板',
       `url`             VARCHAR(150) not null DEFAULT '' comment '地址 带http/https为全路径地址/不带自动加上当前项目地址',
       `usable`          TINYINT not null DEFAULT 1 comment '是否可用 是否可用 0 不可用 1 可用',
       `created_date`    DATETIME not null default current_timestamp comment '创建时间',
       `creator`         BIGINT not null comment '创建人员'
);
alter table `message_template` comment= '消息模板表';

create table  `message_record`
(
       `id`              INT primary key not null auto_increment comment '编号',
       `template_id`     INT not null comment '模板ID',
       `center_id`       VARCHAR(50) not null DEFAULT '' comment '第三方模板中心ID',
       `push_method`     VARCHAR(12) not null DEFAULT '' comment '推送方式 INL: 站内信 SMS：短信 EMI：邮件 WXM：微信 IOS：ios ANR：android RMQ：rabbitMQ',
       `receiver`        VARCHAR(30) not null DEFAULT '' comment '发送地址 手机号、openId等',
       `receiver_id`     INT not null DEFAULT 0 comment '接收人员ID',
       `title`           VARCHAR(80) not null DEFAULT '' comment '标题',
       `message`         VARCHAR(1000) not null DEFAULT '' comment '名称',
       `url`             VARCHAR(100) not null DEFAULT '' comment '访问地址',
       `send_times`      TINYINT not null DEFAULT 0 comment '发送次数 0 待发送 1-3 发送失败次数 9 发送成功',
       `schedule_time`   DATETIME not null DEFAULT '1970-01-01' comment '预约发送时间 空为立即发送',
       `usable`          TINYINT not null DEFAULT 1 comment '是否可用 0 不可用 1 可用',
       `created_date`    DATETIME not null default current_timestamp comment '创建时间',
       `creator`         BIGINT not null comment '创建人员'
);
alter table `message_record` comment= '消息发送记录表';
CREATE TABLE agent_conversation
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    conversation_id VARCHAR(64)  NOT NULL COMMENT '对话唯一标识',
    user_id         VARCHAR(64)  NOT NULL COMMENT '用户ID',
    title           VARCHAR(100) NOT NULL DEFAULT '' COMMENT '对话标题',
    status          VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE/ARCHIVED',
    creator         VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '创建人',
    updater         VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '更新人',
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted         TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    UNIQUE KEY uk_conversation_id (conversation_id),
    INDEX           idx_user_status (user_id, status) -- 覆盖 user_id 单独查询 + user_id+status 组合查询
);

CREATE TABLE agent_message
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    message_id      VARCHAR(64) NOT NULL COMMENT '消息唯一标识',
    conversation_id VARCHAR(64) NOT NULL COMMENT '所属对话ID',
    role            VARCHAR(20) NOT NULL COMMENT '角色: USER/ASSISTANT/SYSTEM',
    content         TEXT        NOT NULL COMMENT '消息内容',
    sources         JSON COMMENT '来源引用 JSON',
    feedback        VARCHAR(20) COMMENT '反馈: USEFUL/NOT_USEFUL',
    feedback_reason VARCHAR(500) COMMENT '反馈原因',
    creator         VARCHAR(64) NOT NULL DEFAULT '' COMMENT '创建人',
    updater         VARCHAR(64) NOT NULL DEFAULT '' COMMENT '更新人',
    create_time     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted         TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    UNIQUE KEY uk_message_id (message_id),
    INDEX           idx_conversation_id (conversation_id)
);
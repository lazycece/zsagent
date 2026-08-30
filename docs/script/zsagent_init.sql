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

CREATE TABLE agent_document
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    document_id     VARCHAR(64)  NOT NULL COMMENT '文档唯一标识',
    title           VARCHAR(200) NOT NULL DEFAULT '' COMMENT '文档标题',
    summary         TEXT COMMENT '自动生成摘要（≤200字）',
    format          VARCHAR(20)  NOT NULL COMMENT '文件格式: pdf/docx/md/html/txt',
    file_size       BIGINT       NOT NULL DEFAULT 0 COMMENT '当前版本文件大小（字节）',
    file_path       VARCHAR(500) NOT NULL COMMENT '当前版本文件存储路径',
    directory_id    VARCHAR(64) COMMENT '所属目录ID',
    tags            JSON COMMENT '标签列表 JSON数组',
    visibility      VARCHAR(20)  NOT NULL DEFAULT 'public' COMMENT '可见范围: public/department/specific',
    visible_to      JSON COMMENT '可见对象列表 JSON数组（部门ID或用户ID）',
    status          VARCHAR(20)  NOT NULL DEFAULT 'draft' COMMENT '文档状态: draft/published/archived/deleted',
    etl_status      VARCHAR(20)  NOT NULL DEFAULT 'pending' COMMENT 'ETL状态: pending/parsing/chunking/enriching/embedding/indexing/completed/failed',
    etl_error_msg   VARCHAR(1000) COMMENT 'ETL失败原因',
    current_version INT          NOT NULL DEFAULT 1 COMMENT '当前版本号',
    deleted_time    DATETIME COMMENT '删除时间（回收站30天计时）',
    creator         VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '创建人',
    updater         VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '更新人',
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted         TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    UNIQUE KEY uk_document_id (document_id),
    INDEX idx_directory_status (directory_id, status),
    INDEX idx_creator (creator),
    INDEX idx_status_deleted_time (status, deleted_time)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='知识文档表';

CREATE TABLE agent_document_version
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    version_id     VARCHAR(64)  NOT NULL COMMENT '版本唯一标识',
    document_id    VARCHAR(64)  NOT NULL COMMENT '所属文档ID',
    version_number INT          NOT NULL COMMENT '版本号（1, 2, 3...）',
    file_path      VARCHAR(500) NOT NULL COMMENT '该版本文件存储路径',
    file_size      BIGINT       NOT NULL DEFAULT 0 COMMENT '该版本文件大小（字节）',
    change_log     VARCHAR(1000) COMMENT '变更说明',
    creator        VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '创建人',
    updater        VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '更新人',
    create_time    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted        TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    UNIQUE KEY uk_version_id (version_id),
    INDEX idx_document_id_version (document_id, version_number)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='文档版本表';

CREATE TABLE agent_directory
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    directory_id VARCHAR(64)  NOT NULL COMMENT '目录唯一标识',
    parent_id    VARCHAR(64) COMMENT '父目录ID（null表示根目录）',
    name         VARCHAR(100) NOT NULL COMMENT '目录名称',
    sort_order   INT          NOT NULL DEFAULT 0 COMMENT '排序序号（值越小越靠前）',
    creator      VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '创建人',
    updater      VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '更新人',
    create_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted      TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_directory_id (directory_id),
    UNIQUE KEY uk_parent_name (parent_id, name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='文档目录表';

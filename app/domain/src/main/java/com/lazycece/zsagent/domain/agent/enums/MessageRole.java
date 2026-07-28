package com.lazycece.zsagent.domain.agent.enums;

import com.lazycece.rapidf.domain.model.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息角色枚举
 *
 * @author lazycece
 */
@Getter
@AllArgsConstructor
public enum MessageRole implements BaseEnum<String> {

    USER("user", "用户"),
    ASSISTANT("assistant", "助手"),
    SYSTEM("system", "系统"),
    ;

    private final String code;
    private final String desc;
}

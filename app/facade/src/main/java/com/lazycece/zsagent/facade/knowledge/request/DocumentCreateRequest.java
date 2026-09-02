/*
 *    Copyright (C) 2026 lazycece<lazycece@gmail.com>. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.lazycece.zsagent.facade.knowledge.request;

import com.lazycece.rapidf.restful.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * 文档创建请求
 *
 * @author lazycece
 */
@Getter
@Setter
public class DocumentCreateRequest extends BaseRequest implements Serializable {

    /** 操作主体标识 */
    @NotBlank(message = "userId不能为空")
    private String userId;

    /** 文档标题（未填则取文件名） */
    private String title;

    /** 所属目录ID（可空） */
    private String directoryId;

    /** 标签列表（可空） */
    private List<String> tags;

    /** 可见范围：public / department / specific */
    @NotBlank(message = "visibility不能为空")
    private String visibility;

    /** 可见对象列表（DEPARTMENT/SPECIFIC 时必填） */
    private List<String> visibleTo;

    /** 文件相对路径（先经文件上传接口获取） */
    @NotBlank(message = "filePath不能为空")
    private String filePath;
}

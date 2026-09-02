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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 文档列表查询请求
 *
 * @author lazycece
 */
@Getter
@Setter
public class DocumentListQueryRequest extends BaseRequest implements Serializable {

    /** 操作主体标识 */
    @NotBlank(message = "userId不能为空")
    private String userId;

    /** 按目录过滤（可空） */
    private String directoryId;

    /** 标题/标签模糊搜索（可空） */
    private String keyword;

    /** 按状态过滤（可空） */
    private String status;

    /** 页码，从1开始 */
    @NotNull(message = "page不能为null")
    @Min(value = 1, message = "page必须大于0")
    private Integer page = 1;

    /** 每页大小 */
    @NotNull(message = "size不能为null")
    @Min(value = 1, message = "size必须大于0")
    private Integer size = 20;
}

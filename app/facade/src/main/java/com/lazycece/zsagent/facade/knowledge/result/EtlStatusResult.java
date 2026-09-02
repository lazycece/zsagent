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
package com.lazycece.zsagent.facade.knowledge.result;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * ETL 状态查询结果
 *
 * @author lazycece
 */
@Getter
@Setter
public class EtlStatusResult implements Serializable {

    /** 文档ID */
    private String documentId;

    /** ETL 状态 */
    private String etlStatus;

    /** 错误信息（仅 FAILED 时有值） */
    private String errorMessage;

    /** 文档状态 */
    private String documentStatus;
}

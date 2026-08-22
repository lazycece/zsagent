/*
 *    Copyright 2023 lazycece<lazycece@gmail.com>
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

package com.lazycece.zsagent.domain.common.enums;

import com.lazycece.cell.specification.model.CellType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lazycece
 */
@Getter
@AllArgsConstructor
public enum CellEnum implements CellType {

    AGENT_CONVERSATION("agent_conversation", "001"),
    AGENT_MESSAGE("agent_message", "002"),
    DIRECTORY("directory", "021"),
    DOCUMENT("document", "022"),
    DOCUMENT_VERSION("document_version", "023"),
    ;

    private final String name;
    private final String code;
}

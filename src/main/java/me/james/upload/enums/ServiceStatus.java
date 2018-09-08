/*
 * Copyright 2018 James
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package me.james.upload.enums;

/**
 * 云存储上传接口返回值状态信息
 *
 * @author James
 * @email 1146556298@qq.com
 * @date 2018-07-28
 */
public enum ServiceStatus {
    SUCCESS("Executed successfully"),
    FAIL("Executed failed");

    ServiceStatus(String desc) {
        this.desc = desc;
    }

    private String desc;
}

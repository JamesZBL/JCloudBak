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
package me.james.upload.service;

import me.james.upload.enums.ServiceStatus;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * 文件上传服务接口
 *
 * @author James
 * @email 1146556298@qq.com
 * @date 2018-07-28
 */
public interface UploadService {

    ServiceStatus upload(Collection<File> file) throws InterruptedException, IOException;
}

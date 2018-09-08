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
package me.james.upload;

import lombok.extern.slf4j.Slf4j;
import me.james.upload.config.Config;
import me.james.upload.service.DefaultMailService;
import me.james.upload.service.MailService;
import me.james.upload.service.QiniuOSSUploadService;
import me.james.upload.task.DeviceSchedulerTask;

import java.util.Timer;

/**
 * JCloudBak 启动入口
 *
 * @author James
 * @email 1146556298@qq.com
 * @date 2018-07-28
 */
@Slf4j
public class App {

    private static Config config = Config.getInstance();

    public static void main(String[] args) {
        log.info("Application started");
        MailService mailService = new DefaultMailService();
        try {
            mailService.mail("UPLOAD APP STARTED", "UPLOAD APP STARTED");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed sending email.");
        }
        new Timer().schedule(new DeviceSchedulerTask(new QiniuOSSUploadService()),
                config.getLong("begin_delay"),
                config.getLong("task_period"));
    }
}

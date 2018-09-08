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
package me.james.upload.task;

import lombok.extern.slf4j.Slf4j;
import me.james.upload.config.Config;
import me.james.upload.service.DefaultMailService;
import me.james.upload.service.MailService;
import me.james.upload.service.UploadService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 媒体存储设备探测定时任务
 *
 * @author James
 * @email 1146556298@qq.com
 * @date 2018-07-28
 */
@Slf4j
public class DeviceSchedulerTask extends TimerTask {

    private final UploadService service;
    private Config config = Config.getInstance();
    private IOFileFilter fileFilter = new DefaultIoFilter();
    private volatile Set<String> roots = new LinkedHashSet<>();
    private MailService mailService = new DefaultMailService();
    private List<String> fileNameList = new LinkedList<>();

    public DeviceSchedulerTask(UploadService service) {
        this.service = service;
    }

    @Override
    public void run() {
        log.info("Executing roots scan.");
        if (roots.isEmpty()) {
            // First startup
            log.info("Roots initialize.");
            updateRoots();
        } else if (roots.size() > File.listRoots().length) {
            // Some devices has been removed
            log.info("Some devices have been removed.");
            updateRoots();
        } else if (Stream.of(File.listRoots())
                .map(File::getAbsolutePath)
                .anyMatch(c -> !roots.contains(c))) {
            // New devices loaded
            log.info("New devices detected.");
            try {
                Stream.of(File.listRoots())
                        .filter(c -> !roots.contains(c.getAbsolutePath()))
                        .forEach(r -> {
                                    Collection<File> files = FileUtils.listFilesAndDirs(r, fileFilter, fileFilter)
                                            .stream()
                                            .filter(f -> Stream.of(
                                                    config.getStringArray("file_extensions")
                                            ).anyMatch(f.getName().toLowerCase()::endsWith))
                                            .filter(f -> Stream.of(
                                                    config.getStringArray("file_name_part_ignore")
                                                    ).noneMatch(f.getName()::contains)
                                            )
                                            .collect(Collectors.toCollection(LinkedList::new));
                                    updateFileList(files);
                                    try {
                                        service.upload(files);
                                    } catch (InterruptedException | IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                        );
                updateRoots();
                mail();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateRoots() {
        roots.clear();
        Stream.of(File.listRoots())
                .map(File::getAbsolutePath)
                .forEach(c -> roots.add(c));
    }

    private void updateFileList(Collection<File> files) {
        fileNameList.clear();
        List<String> pathNameList = files.stream().map(File::getAbsolutePath).collect(Collectors.toList());
        fileNameList.addAll(pathNameList);
    }

    private void mail() throws Exception {
        StringBuilder sb = new StringBuilder();
        fileNameList.stream().map(c -> c.concat("\n"))
                .collect(Collectors.toList())
                .forEach(sb::append);
        mailService.mail("ACTION-UPLOAD: Files updated.", "Files updated list:\n" + sb);
    }
}

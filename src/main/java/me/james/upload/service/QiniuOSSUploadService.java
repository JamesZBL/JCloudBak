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

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import me.james.upload.config.Config;
import me.james.upload.enums.ServiceStatus;
import me.james.upload.store.FileStore;
import me.james.upload.store.FileSystemFileStore;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * 七牛云对象存储上传服务
 * <p>
 * {@link UploadService} 的默认实现
 *
 * @author James
 * @email 1146556298@qq.com
 * @date 2018-07-28
 */
@Slf4j
public class QiniuOSSUploadService implements UploadService {

    private Config config = Config.getInstance();
    private FileStore repo = FileSystemFileStore.getInstance();

    @Override
    public ServiceStatus upload(Collection<File> files) {
        if (!files.isEmpty()) log.info("start uploading");
        try {
            files.forEach(f -> {
                try {
                    if (!repo.exist(f.getAbsolutePath())) {
                        log.info(f.getAbsolutePath());
                        //构造一个带指定Zone对象的配置类
                        Configuration cfg = new Configuration(Zone.zone1());
                        //...其他参数参考类注释
                        UploadManager uploadManager = new UploadManager(cfg);
                        //...生成上传凭证，然后准备上传
                        String accessKey = config.getString("qiniu.access_key");
                        String secretKey = config.getString("qiniu.secret_key");
                        String bucket = config.getString("qiniu.bucket_name");
                        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
                        String localFilePath = f.getAbsolutePath();
                        //默认不指定key的情况下，以文件内容的hash值作为文件名
                        String key = f.getName();
                        Auth auth = Auth.create(accessKey, secretKey);
                        String upToken = auth.uploadToken(bucket);

                        Response response = uploadManager.put(localFilePath, key, upToken);
                        //解析上传成功的结果
                        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                        log.info(putRet.key);
                        log.info(putRet.hash);

                        repo.store(f.getAbsolutePath());
                    }
                } catch (QiniuException e) {
                    Response r = e.response;
                    log.error(r.toString());
                    try {
                        log.error(r.bodyString());
                    } catch (QiniuException ex2) {
                        //ignore
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            return ServiceStatus.FAIL;
        }
        return ServiceStatus.SUCCESS;
    }
}

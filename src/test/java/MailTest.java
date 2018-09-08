/*
 * Copyright 2018 JamesZBL
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

import me.james.upload.config.Config;
import me.james.upload.service.DefaultMailService;
import me.james.upload.service.MailService;
import me.james.upload.service.QiniuOSSUploadService;
import me.james.upload.service.UploadService;
import me.james.upload.task.DefaultIoFilter;
import me.james.upload.utils.AESUtil;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * @author JamesZBL
 * @email 1146556298@qq.com
 * @date 2018-08-01
 */
public class MailTest {

    private Config config;
    private MailService mailService;
    private UploadService uploadService;

    @Before
    public void setup() {
        this.config = Config.getInstance();
        this.mailService = new DefaultMailService();
        this.uploadService = new QiniuOSSUploadService();
    }


    @Test
    public void testUpload() {
        Collection<File> files = FileUtils.listFiles(new File("I:\\"), new DefaultIoFilter(), new DefaultIoFilter());
        try {
            uploadService.upload(files);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJaveSimpleMail() {
        Email email = EmailBuilder.startingBlank()
                .to(config.getString("mail.to.name"), config.getString("mail.to.address"))
                .withSubject(config.getString("mail.subject"))
                .withHTMLText("Please view this email in a modern email client!<br>OK<br>OK<br>OK<br>OK.")
                .from(config.getString("mail.from.address"))
                .buildEmail();

        Mailer mailer = MailerBuilder
                .withSMTPServer(config.getString("mail.smtp.host"),
                        config.getInt("mail.smtp.port"),
                        config.getString("mail.from.username"),
                        config.getString("mail.from.password"))
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withSessionTimeout(10 * 1000)
                .clearEmailAddressCriteria() // turns off email validation
                .withDebugLogging(true)
                .buildMailer();

        mailer.sendMail(email);
    }

    @Test
    public void testMailService() {
        try {
            mailService.mail("Test mail service", "Ok\nOk\nOk\nOk\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAEDUtil() {
        try {
            System.out.println(AESUtil.encrypt(config.getString("qiniu.access_key")));
            System.out.println(AESUtil.encrypt(config.getString("qiniu.secret_key")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

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
package me.james.upload.service;

import me.james.upload.config.Config;
import me.james.upload.enums.ServiceStatus;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;

/**
 * 邮件提醒服务
 * <p>
 * {@link MailService} 的默认实现
 *
 * @author JamesZBL
 * @email 1146556298@qq.com
 * @date 2018-08-01
 */
public class DefaultMailService implements MailService {

    private Config config = Config.getInstance();

    @Override
    public ServiceStatus mail(String title, String content) {
        Email email = EmailBuilder.startingBlank()
                .to(config.getString("mail.to.name"), config.getString("mail.to.address"))
                .withSubject(title)
                .withHTMLText(
                        content.replace("\n", "<br>")
                                .replace("\r", "<br>"))
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

        try {
            mailer.sendMail(email);
        } catch (Exception e) {
            e.printStackTrace();
            return ServiceStatus.FAIL;
        }
        return ServiceStatus.SUCCESS;
    }
}

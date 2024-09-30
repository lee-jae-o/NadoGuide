package spring.tree.service;

import spring.tree.dto.MailDTO;

public interface IMailService {

    //메일 발송
    int doSendMail(MailDTO pDTO);
}
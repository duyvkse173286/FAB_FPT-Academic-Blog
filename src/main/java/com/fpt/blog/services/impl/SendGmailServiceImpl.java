package com.fpt.blog.services.impl;/*  Welcome to Jio word
    @author: Jio
    Date: 10/27/2023
    Time: 10:28 AM
    
    ProjectName: fpt-blog
    Jio: I wish you always happy with coding <3
*/

import com.fpt.blog.entities.Comment;
import com.fpt.blog.entities.User;
import com.fpt.blog.models.comment.request.DeleteCommentRequest;
import com.fpt.blog.services.ISendGmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SendGmailServiceImpl implements ISendGmailService {
        @Autowired
        private JavaMailSender mailSender;
    @Override
    public void sendMailBanCommentV1(User user, Comment commentBan, DeleteCommentRequest request){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String emailContent = "Dear " + user.getName() + ".\n"
                    + "Cảm ơn bạn đã tin tưởng và đồng hành cùng FBlog chúng tôi!\n"
                    + "Nhưng FBlog xin gửi thông báo về comment của bạn đã vi phạm quy định và bị xóa.:\n"
                    + "Với lý do là: "+ request.getReason() + ".\n"
                    + "Với nội dung comment là: "+ commentBan.getContent() + ".\n"
                    + "Lần Thứ: " + user.getDelCmtNumber() + ".\n"
                    + "Nhắc nhở: Lần thứ 3 sẽ bị cấm comment 1 tuần !!!.\n";

            if (user.getDelCmtNumber() >= 3) {
                emailContent += "Bạn đã comment vi phạm 3 lần!!! và bị cấm comment lúc:"+ user.getCmtBanAt() +".\n"
                        + "Thời hạn bạn bị cấm comment đến ngày: " + user.getCmtBanExpiredAt() + ".\n";
            }

            emailContent += "FBlog chúc bạn 1 ngày tốt lành.\n"
                    + new Date() + "\n"
                    + "Thân ái\n"
                    + "FYoGa.";

            // gui meo
            helper.setTo(user.getEmail());
            helper.setSubject("FBlog nhắc nhở");
            helper.setText(emailContent);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


}

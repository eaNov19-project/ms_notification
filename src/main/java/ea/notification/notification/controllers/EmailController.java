package ea.notification.notification.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private JavaMailSender javaMailSender;

    @GetMapping("/send")
    public void sendEmails() {
        List<String> followerList = new ArrayList<>();
        followerList.add("zayedemails@gmail.com");
        StringBuilder SendTo = new StringBuilder();
        for (String u : followerList) {
            SendTo.append(u).append(',');
        }
        if (!SendTo.toString().equals("")) {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(SendTo.toString());

            msg.setSubject("New Answer Added");
            msg.setText("Hi,\nDear User, A new answer is added.\nCheck out website to see details");

            javaMailSender.send(msg);
        }
    }
}

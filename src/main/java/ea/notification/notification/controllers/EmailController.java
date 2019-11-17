package ea.notification.notification.controllers;

import com.google.gson.Gson;
import ea.sof.shared.models.Answer;
import ea.sof.shared.models.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
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

    //Answer(id=1029, body=Answer about java, date=null, upvotes=0, topComments=[], userId=123, userName=rustem.bayetov@gmail.com)
    @KafkaListener(topics = "${topicNewAnswer}", groupId = "${subsNewAnswerNotification}")
    public void newQuestion(String message) {

        System.out.println("New message from topic: " + message);

        Gson gson = new Gson();
        Answer answer =  gson.fromJson(message, Answer.class);

        System.out.println("As object: " + answer);
    }

    /* should be kafka listener
    * for now, make it parameteized post request
    * will get the question ID
    * then
    * get question's followers
    * loop through the followers
    * Subject : Followed Question recevied an answer
    * Body: Question ...... (insert question body )
    *       Got
    *       .... (insert answer body)
    * */
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

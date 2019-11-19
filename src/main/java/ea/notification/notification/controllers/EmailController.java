package ea.notification.notification.controllers;

import com.google.gson.Gson;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import ea.sof.shared.models.*;
import ea.sof.shared.queue_models.AnswerQueueModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Set;


@Service
@RequestMapping("/email")
public class EmailController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private QuestionsService questionsService;

	@Autowired
	private Environment env;


	@HystrixCommand(fallbackMethod = "fallback")
	public ResponseEntity<QuestionFollowers> getFollowers(String questionId) {
		LOGGER.info("Trying to access questionsService...");
		return questionsService.getFollowersByQuestionId(questionId, env.getProperty("service-secret"));
	}

	public ResponseEntity<QuestionFollowers> fallback(String questionId) {
		LOGGER.error("Can't access to questionsService");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new QuestionFollowers());
	}


	//Answer(id=1029, body=Answer about java, date=null, upvotes=0, topComments=[], userId=123, userName=rustem.bayetov@gmail.com)
	@KafkaListener(topics = "${topicNewAnswer}", groupId = "${subsNewAnswerNotification}")
	public void newAnswer(String message) {
		System.out.println("New message from topic: " + message);
		LOGGER.info("New message from topic: " + message);

        AnswerQueueModel answer = new AnswerQueueModel();
		try {
			Gson gson = new Gson();
			answer = gson.fromJson(message, AnswerQueueModel.class);
			LOGGER.info("As object: " + answer);
		} catch (Exception ex) {
			LOGGER.error("newQuestion :: Failed to convert JSON to object: " + ex.getMessage());
		}

		ResponseEntity<QuestionFollowers> followersByQuestionId = getFollowers(answer.getQuestionId());
		if (followersByQuestionId.getStatusCode() != HttpStatus.OK) {
			LOGGER.error("ERROR retrieving data from questions service. " + followersByQuestionId.getStatusCode());
			return;
		}

		sendEmails(followersByQuestionId.getBody().getFollowerEmails());
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
	public void sendEmails(Set<String> followerList) {

		StringBuilder SendTo = new StringBuilder();
		for (String u : followerList) {
			SendTo.append(u).append(',');
		}
		if (!SendTo.toString().equals("")) {
			LOGGER.info("Sending message to: " + SendTo.toString());

			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(SendTo.toString());

			msg.setSubject("New Answer Added");
			msg.setText("Hi,\nDear User, A new answer is added.\nCheck out website to see details");

			javaMailSender.send(msg);
		} else {
			LOGGER.info("No followers to send emails");
		}
	}
}

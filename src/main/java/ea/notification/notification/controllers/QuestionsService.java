package ea.notification.notification.controllers;

import ea.sof.shared.showcases.MsQuestionsShowcase;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="questionsService", url = "${QUESTIONS_SERVICE}")
public interface QuestionsService extends MsQuestionsShowcase {
}

package ea.notification.notification.controllers;

import ea.sof.shared.showcases.MsQuestionsShowcase;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="questionsService", url = "${questions-service}")
public interface QuestionsService extends MsQuestionsShowcase {
}

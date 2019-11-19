package ea.notification.notification.controllers;

import ea.sof.shared.showcases.MsQuestionsShowcase;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="questions-ms-service" , url = "${questions-service}")
public interface QuestionsService extends MsQuestionsShowcase {
}

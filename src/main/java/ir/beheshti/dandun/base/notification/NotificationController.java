package ir.beheshti.dandun.base.notification;

import io.swagger.v3.oas.annotations.tags.Tag;
import ir.beheshti.dandun.base.user.common.BaseOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Notification")
@RestController
@RequestMapping(path = "/api/v1/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationOutputDto>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @GetMapping(path = "/{faqId}")
    public ResponseEntity<NotificationOutputDto> getNotificationById(@PathVariable Integer faqId) {
        return ResponseEntity.ok(notificationService.getNotificationById(faqId));
    }

    @PostMapping
    public ResponseEntity<BaseOutputDto> addFaq(@RequestBody NotificationInputDto notificationInputDto) {
        notificationService.addNotification(notificationInputDto);
        return ResponseEntity.ok(new BaseOutputDto("notification added successfully"));
    }
}

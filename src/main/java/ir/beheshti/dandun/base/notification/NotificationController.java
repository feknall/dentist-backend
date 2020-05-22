package ir.beheshti.dandun.base.notification;

import io.swagger.v3.oas.annotations.tags.Tag;
import ir.beheshti.dandun.base.faq.FaqInputDto;
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

    @GetMapping(path = "/timing")
    public ResponseEntity<List<NotificationTimingOutputDto>> getTimingList() {
        return ResponseEntity.ok(notificationService.getTimingList());
    }

    @GetMapping(path = "/group")
    public ResponseEntity<List<NotificationGroupOutputDto>> getGroupList() {
        return ResponseEntity.ok(notificationService.getGroupList());
    }

    @GetMapping(path = "/{notificationId}")
    public ResponseEntity<NotificationOutputDto> getNotificationById(@PathVariable Integer notificationId) {
        return ResponseEntity.ok(notificationService.getNotificationById(notificationId));
    }

    @PostMapping
    public ResponseEntity<BaseOutputDto> addNotification(@RequestBody NotificationInputDto notificationInputDto) {
        notificationService.addNotification(notificationInputDto);
        return ResponseEntity.ok(new BaseOutputDto("notification added successfully"));
    }

    @PutMapping
    public ResponseEntity<BaseOutputDto> updateNotification(@RequestBody NotificationInputDto notificationInputDto) {
        notificationService.updateNotification(notificationInputDto);
        return ResponseEntity.ok(new BaseOutputDto("notification updated successfully"));
    }

    @DeleteMapping(path = "/{notificationId}")
    public ResponseEntity<BaseOutputDto> deleteInformation(@PathVariable int notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok(new BaseOutputDto("notification deleted successfully"));
    }
}

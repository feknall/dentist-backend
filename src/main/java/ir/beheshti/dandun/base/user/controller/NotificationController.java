package ir.beheshti.dandun.base.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Notification", description = "Things related to notification CRUD")
@RestController
@RequestMapping(path = "/api/v1/operator/notification")
public class NotificationController {
}

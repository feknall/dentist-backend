package ir.beheshti.dandun.base.faq;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FAQ")
@RestController
@RequestMapping(path = "/api/v1/operator/faq")
public class FaqController {
}

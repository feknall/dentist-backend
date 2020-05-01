package ir.beheshti.dandun.base.faq;

import io.swagger.v3.oas.annotations.tags.Tag;
import ir.beheshti.dandun.base.user.common.BaseOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "FAQ")
@RestController
@RequestMapping(path = "/api/v1/faq")
public class FaqController {

    @Autowired
    private FaqService faqService;

    @GetMapping
    public ResponseEntity<List<FaqOutputDto>> getAllFaq() {
        return ResponseEntity.ok(faqService.getAllFaq());
    }

    @GetMapping(path = "/{faqId}")
    public ResponseEntity<FaqOutputDto> getFaqById(@PathVariable Integer faqId) {
        return ResponseEntity.ok(faqService.getFaqById(faqId));
    }

    @PostMapping
    public ResponseEntity<BaseOutputDto> addFaq(@RequestBody FaqInputDto faqInputDto) {
        faqService.addFaq(faqInputDto);
        return ResponseEntity.ok(new BaseOutputDto("faq added successfully"));
    }

    @PutMapping
    public ResponseEntity<BaseOutputDto> updateFaq(@RequestBody FaqInputDto faqInputDto) {
        faqService.updateFaq(faqInputDto);
        return ResponseEntity.ok(new BaseOutputDto("faq updated successfully"));
    }

    @DeleteMapping(path = "/{faqId}")
    public ResponseEntity<BaseOutputDto> deleteInformation(@PathVariable int faqId) {
        faqService.deleteFaq(faqId);
        return ResponseEntity.ok(new BaseOutputDto("faq deleted successfully"));
    }

}

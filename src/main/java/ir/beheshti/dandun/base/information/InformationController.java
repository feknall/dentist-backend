package ir.beheshti.dandun.base.information;

import io.swagger.v3.oas.annotations.tags.Tag;
import ir.beheshti.dandun.base.user.common.BaseOutputDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Information")
@RestController
@RequestMapping(path = "/api/v1/information")
public class InformationController {

    private final InformationService informationService;

    public InformationController(InformationService informationService) {
        this.informationService = informationService;
    }

    @GetMapping
    public ResponseEntity<List<InformationOutputDto>> getAllInformation() {
        return ResponseEntity.ok(informationService.getAllInformation());
    }

    @GetMapping(path = "/{informationId}")
    public ResponseEntity<InformationOutputDto> getInformationById(@PathVariable Integer informationId) {
        return ResponseEntity.ok(informationService.getInformationById(informationId));
    }

    @PostMapping
    public ResponseEntity<BaseOutputDto> addInformation(@RequestBody InformationInputDto informationInputDto) {
        informationService.addInformation(informationInputDto);
        return ResponseEntity.ok(new BaseOutputDto("information added successfully"));
    }
}

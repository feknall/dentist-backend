package ir.beheshti.dandun.base.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import ir.beheshti.dandun.base.user.dto.information.InformationOutputDto;
import ir.beheshti.dandun.base.user.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Information", description = "Basic information to show to user")
@RestController
@RequestMapping(path = "/api/v1/information")
public class InformationController {

    @Autowired
    private InformationService informationService;

    @GetMapping
    public ResponseEntity<List<InformationOutputDto>> getAllInformation() {
        return ResponseEntity.ok(informationService.getAllInformation());
    }

    @GetMapping(path = "/{informationId}")
    public ResponseEntity<InformationOutputDto> getInformationById(@PathVariable Integer informationId) {
        return ResponseEntity.ok(informationService.getInformationById(informationId));
    }
}

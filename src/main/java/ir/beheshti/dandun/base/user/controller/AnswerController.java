package ir.beheshti.dandun.base.user.controller;

import ir.beheshti.dandun.base.user.dto.question.AnswerOutputDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/answer")
public class AnswerController {



    @GetMapping
    public ResponseEntity<AnswerOutputDto> getAll() {
        return null;
    }


}

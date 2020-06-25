package ir.beheshti.dandun.base.video;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/video")
public class VideoStreamRest {

    @Autowired
    private VideoStreamService videoStreamService;

    @GetMapping("/stream/{fileName}")
    public ResponseEntity<byte[]> streamVideo(@RequestHeader(value = "Range", required = false) String httpRangeList,
                                              @PathVariable("fileName") String fileName) {
        return videoStreamService.prepareContent(fileName, httpRangeList);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Pair<String, String>>> getVideoNames() {
        return ResponseEntity.ok(videoStreamService.getVideoNames());
    }
}

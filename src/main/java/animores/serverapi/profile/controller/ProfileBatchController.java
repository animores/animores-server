package animores.serverapi.profile.controller;

import animores.serverapi.profile.service.ProfileBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile/batch")
public class ProfileBatchController {

    private final ProfileBatchService profileBatchService;

    @PostMapping("/")
    public void insertProfileBatch(@RequestParam Integer count, @RequestParam Integer accountStartId) {
        profileBatchService.insertProfileBatch(count, accountStartId);
    }
}

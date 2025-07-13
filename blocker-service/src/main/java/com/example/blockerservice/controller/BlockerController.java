package com.example.blockerservice.controller;

import com.example.blockerservice.dto.OperationRequest;
import com.example.blockerservice.service.BlockerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blocker")
public class BlockerController {

    private final BlockerService blockerService;

    public BlockerController(BlockerService blockerService) {
        this.blockerService = blockerService;
    }

    @PostMapping("/check")
    public boolean check(@RequestBody OperationRequest request) {
        return blockerService.isBlocked(request);
    }
}

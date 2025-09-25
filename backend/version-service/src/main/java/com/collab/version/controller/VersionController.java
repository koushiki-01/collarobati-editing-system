package com.collab.version.controller;

import com.collab.version.entity.Version;
import com.collab.version.service.VersionService;
import com.collab.version.dto.VersionRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//import java.util.Map;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/versions")
public class VersionController {

    private final VersionService versionService;

    public VersionController(VersionService versionService) {
        this.versionService = versionService;
    }

    /**
     * Accept JSON body with documentId, content, modifiedBy
     * so that RestTemplate from DocumentService works correctly
     */
    // @PostMapping
    // public Version createVersion(@RequestBody Map<String, Object> payload) {
    //     Long documentId = Long.valueOf(payload.get("documentId").toString());
    //     String content = payload.get("content").toString();
    //     String modifiedBy = payload.get("modifiedBy").toString();

    //     return versionService.createVersion(documentId, content, modifiedBy);
    // }
    @PostMapping
    public Version createVersion(@RequestBody VersionRequest request) {
        return versionService.createVersion(
            request.getDocumentId(),
            request.getContent(),
            request.getModifiedBy()
        );
    }

    @GetMapping("/{documentId}")
    public List<Version> getVersions(@PathVariable Long documentId) {
        return versionService.getVersionsByDocumentId(documentId);
    }

    @PostMapping("/revert/{versionId}")
    public Version revertVersion(
            @PathVariable Long versionId,
            @RequestParam String user) {

        return versionService.revertToVersion(versionId, user);
    }
}

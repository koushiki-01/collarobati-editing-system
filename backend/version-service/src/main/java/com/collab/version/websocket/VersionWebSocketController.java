package com.collab.version.websocket;

import com.collab.version.entity.Version;
import com.collab.version.service.VersionService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

/**
 * Optional WebSocket endpoint to notify clients about new versions.
 * Actual version creation is via REST API, this is for future real-time edits.
 */
@Controller
public class VersionWebSocketController {

    private final VersionService versionService;

    public VersionWebSocketController(VersionService versionService) {
        this.versionService = versionService;
    }

    @MessageMapping("/version")
    public void handleVersionUpdate(@Payload Version version) {
        if (version == null || version.getDocumentId() == null) return;

        versionService.createVersion(version.getDocumentId(), version.getContent(), version.getModifiedBy());
    }
}

package com.collab.document.websocket;

import com.collab.document.dto.EditMessage;
import com.collab.document.service.DocumentService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class DocumentWebSocketController {

    private final DocumentService documentService;

    public DocumentWebSocketController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * Handles WebSocket edits.
     * Only one version is created per edit.
     */
    @MessageMapping("/edit")
    public void handleEdit(@Payload EditMessage msg) {
        if (msg == null || msg.getOperation() == null || msg.getDocumentId() == null) return;

        switch (msg.getOperation()) {
            case "live":
                documentService.applyLiveEdit(msg.getDocumentId(), msg.getContent(), msg.getUser());
                break;
            case "full":
                documentService.applyFullReplace(msg.getDocumentId(), msg.getContent(), msg.getUser());
                break;
            default:
                System.out.println("⚠️ Unknown operation: " + msg.getOperation());
        }
    }
}

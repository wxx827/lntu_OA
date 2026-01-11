package com.oa.module.ai.controller;

import com.oa.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "AI Features (Mock)", description = "Mock AI endpoints for Meeting, Material, and Chat")
public class AiController {

    @GetMapping("/meeting/recommend")
    @Operation(summary = "Get Meeting Room Recommendation")
    public Result<RoomRecommendation> getMeetingRecommendation(@RequestParam(required = false) Integer peopleCount) {
        return Result.success(new RoomRecommendation(
                "Meeting Room 301",
                95,
                "Suitable for 24 people, rating 4.8/5"
        ));
    }

    @GetMapping("/material/prediction")
    @Operation(summary = "Get Material Inventory Prediction")
    public Result<List<PredictionData>> getMaterialPrediction(@RequestParam(required = false) String materialId) {
        return Result.success(Arrays.asList(
                new PredictionData("2026-02", 120),
                new PredictionData("2026-03", 135),
                new PredictionData("2026-04", 110),
                new PredictionData("2026-05", 145),
                new PredictionData("2026-06", 160)
        ));
    }

    @PostMapping("/chat")
    @Operation(summary = "Chat with AI Assistant")
    public Result<ChatResponse> chat(@RequestBody Map<String, String> payload) {
        String userInput = payload.get("message");
        if (userInput == null) userInput = "";
        
        String reply;
        if (userInput.contains("meeting")) {
            reply = "There are 3 available meeting rooms: 301, 305, 308";
        } else if (userInput.contains("material")) {
             reply = "Material inventory is normal";
        } else if (userInput.contains("help")) {
             reply = "How can I help you?";
        } else {
            reply = "I'm here to assist you with meetings, materials, and more";
        }
        
        return Result.success(new ChatResponse(reply));
    }

    @Data
    @AllArgsConstructor
    static class RoomRecommendation {
        private String roomName;
        private Integer matchScore;
        private String reason;
    }

    @Data
    @AllArgsConstructor
    static class PredictionData {
        private String month;
        private Integer value;
    }

    @Data
    @AllArgsConstructor
    static class ChatResponse {
        private String reply;
    }
}
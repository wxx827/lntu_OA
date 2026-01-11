package com.oa.module.meeting.controller;

import com.oa.common.Result;
import com.oa.common.utils.JwtUtil;
import com.oa.module.meeting.entity.OaMeetingBook;
import com.oa.module.meeting.entity.OaMeetingRoom;
import com.oa.module.meeting.service.MeetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/meeting")
@Tag(name = "Meeting Management", description = "Room and Booking")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/room/list")
    @Operation(summary = "Get meeting rooms")
    public Result<List<OaMeetingRoom>> getRoomList(@RequestParam(required = false) Integer capacity) {
        return Result.success(meetingService.getRoomList(capacity));
    }

    @PostMapping("/book/add")
    @Operation(summary = "Book a room")
    public Result<String> addBooking(@RequestBody OaMeetingBook book, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String empId = jwtUtil.extractUserId(token);
        book.setEmpId(empId);
        
        meetingService.addBooking(book);
        return Result.success("Booking submitted");
    }

    @GetMapping("/book/my")
    @Operation(summary = "Get my bookings")
    public Result<List<OaMeetingBook>> getMyBookings(@RequestParam(required = false) Integer status, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String empId = jwtUtil.extractUserId(token);
        return Result.success(meetingService.getMyBookings(empId, status));
    }

    @PutMapping("/book/cancel/{id}")
    @Operation(summary = "Cancel booking")
    public Result<String> cancelBooking(@PathVariable String id) {
        meetingService.cancelBooking(id);
        return Result.success("Cancelled");
    }
}
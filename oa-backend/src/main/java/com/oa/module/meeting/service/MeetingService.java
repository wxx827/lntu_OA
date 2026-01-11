package com.oa.module.meeting.service;

import com.oa.module.meeting.entity.OaMeetingBook;
import com.oa.module.meeting.entity.OaMeetingRoom;

import java.util.Date;
import java.util.List;

public interface MeetingService {
    List<OaMeetingRoom> getRoomList(Integer capacity);
    
    void addBooking(OaMeetingBook book);
    
    List<OaMeetingBook> getMyBookings(String empId, Integer status);
    
    void cancelBooking(String bookId);
    
    boolean hasConflict(String roomId, Date start, Date end);
}
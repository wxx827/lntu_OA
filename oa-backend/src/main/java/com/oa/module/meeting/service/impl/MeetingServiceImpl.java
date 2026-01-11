package com.oa.module.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oa.common.exception.BusinessException;
import com.oa.module.meeting.entity.OaMeetingBook;
import com.oa.module.meeting.entity.OaMeetingRoom;
import com.oa.module.meeting.mapper.OaMeetingBookMapper;
import com.oa.module.meeting.mapper.OaMeetingRoomMapper;
import com.oa.module.meeting.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class MeetingServiceImpl implements MeetingService {

    @Autowired
    private OaMeetingRoomMapper roomMapper;
    @Autowired
    private OaMeetingBookMapper bookMapper;

    @Override
    public List<OaMeetingRoom> getRoomList(Integer capacity) {
        LambdaQueryWrapper<OaMeetingRoom> query = new LambdaQueryWrapper<>();
        if (capacity != null) {
            query.ge(OaMeetingRoom::getCapacity, capacity);
        }
        return roomMapper.selectList(query);
    }

    @Override
    @Transactional
    public void addBooking(OaMeetingBook book) {
        if (book.getStartTime().after(book.getEndTime())) {
            throw new BusinessException("????????????");
        }
        
        if (hasConflict(book.getRoomId(), book.getStartTime(), book.getEndTime())) {
            throw new BusinessException("???????????");
        }
        
        book.setStatus(0); // Pending
        book.setCreateTime(new Date());
        bookMapper.insert(book);
    }

    @Override
    public List<OaMeetingBook> getMyBookings(String empId, Integer status) {
        LambdaQueryWrapper<OaMeetingBook> query = new LambdaQueryWrapper<>();
        query.eq(OaMeetingBook::getEmpId, empId);
        if (status != null) {
            query.eq(OaMeetingBook::getStatus, status);
        }
        query.orderByDesc(OaMeetingBook::getCreateTime);
        return bookMapper.selectList(query);
    }

    @Override
    public void cancelBooking(String bookId) {
        // Logic to cancel
        OaMeetingBook book = bookMapper.selectById(bookId);
        if (book != null) {
            bookMapper.deleteById(bookId); // Or set status to cancelled
        }
    }

    @Override
    public boolean hasConflict(String roomId, Date start, Date end) {
        // Validation logic: any booking that overlaps?
        // (ExistingStart < NewEnd) AND (ExistingEnd > NewStart)
        // And status is not Rejected (2)
        
        Long count = bookMapper.selectCount(new LambdaQueryWrapper<OaMeetingBook>()
                .eq(OaMeetingBook::getRoomId, roomId) // Same room
                .ne(OaMeetingBook::getStatus, 2) // Not rejected
                .lt(OaMeetingBook::getStartTime, end) // Existing start < New end
                .gt(OaMeetingBook::getEndTime, start) // Existing end > New start
        );
        return count > 0;
    }
}
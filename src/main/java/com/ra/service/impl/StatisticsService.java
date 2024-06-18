package com.ra.service.impl;

import com.ra.exception.MyRuntimeEx;
import com.ra.model.dto.response.TopUserResponse;
import com.ra.model.entity.User;
import com.ra.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StatisticsService {
    @Autowired
    OrderRepository orderRepository;
    public List<TopUserResponse> top10ByUser(Date startDate, Date endDate){
        List<User> userList = orderRepository.top10ByUserByForTime(startDate, endDate);
        if(userList.isEmpty()){
            throw new MyRuntimeEx("e hang roi");
        }
        List<TopUserResponse> topUserResponseList = new ArrayList<>();
        for (User user : userList) {
            TopUserResponse topUserResponse = new TopUserResponse();
            topUserResponse.setUser(user);
            topUserResponse.setByMoney(orderRepository.sumTotalPriceByUser(user.getId(), startDate,  endDate));
            topUserResponseList.add(topUserResponse);
        }
        return topUserResponseList;
    }
}

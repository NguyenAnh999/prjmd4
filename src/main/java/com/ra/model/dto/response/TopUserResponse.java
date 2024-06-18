package com.ra.model.dto.response;

import com.ra.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TopUserResponse {
    private User user;
    private Double byMoney;
}

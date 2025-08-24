package org.accify.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String userName;
    private String password;
    private String businessAccountName;
}

package com.api.ApiLabOnline.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeRequest {
	String username;
	String password;
	String passwordnew;
	String passwordnewconfirm;

}

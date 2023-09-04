package com.example.customerserver.web.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientRedirectUrlRequest {

	private String clientId;
	private String redirectUrl;
}

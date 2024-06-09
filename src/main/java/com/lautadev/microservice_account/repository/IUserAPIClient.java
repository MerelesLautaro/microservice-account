package com.lautadev.microservice_account.repository;

import com.lautadev.microservice_account.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservice-user")
public interface IUserAPIClient {
    @GetMapping("/api/user/info/{id}")
    public UserDTO findUserAndBenefit(@PathVariable ("id") Long id);
}

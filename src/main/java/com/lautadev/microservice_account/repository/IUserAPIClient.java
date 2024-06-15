package com.lautadev.microservice_account.repository;

import com.lautadev.microservice_account.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "microservice-user")
public interface IUserAPIClient {
    @GetMapping("/api/user/info/{id}")
    public UserDTO findUserAndBenefit(@PathVariable ("id") Long id);

    @PostMapping("/api/user/updateTickets/{id}")
    public UserDTO updateTickets(@PathVariable ("id") Long id, @RequestBody int ticket,
                                 @RequestHeader("X-HTTP-Method-Override") String methodOverride);
}

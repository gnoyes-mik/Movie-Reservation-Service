package kr.co.leadsoft.mrmserver.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;

@FeignClient(name = "reservations", url = "http://localhost:8080")
public interface MRMProxy {
    @RequestMapping(value = "/reservations", method = RequestMethod.POST)
    String reserveMovie(@RequestBody HashMap<String, String> reservationRequest);
}

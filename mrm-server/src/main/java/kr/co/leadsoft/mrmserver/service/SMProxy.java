package kr.co.leadsoft.mrmserver.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;

@FeignClient(name = "reservedSeats", url = "http://localhost:8083")
public interface SMProxy {

    @RequestMapping(value = "/seats/reserved/{theaterNumber}", method = RequestMethod.GET)
    HashMap<String, Object> checkTheaterStatus(@PathVariable int theaterNumber);

}

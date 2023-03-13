package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.controller.dto.EnglishRequest;
import org.example.controller.dto.EnglishResponse;
import org.example.repository.entity.English;
import org.example.service.EnglishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Controller {
    private final EnglishService service;

//    @GetMapping(path = "/english")
//    ResponseEntity <EnglishResponse> getAll() {
//        final EnglishResponse response = service.findAll();
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
@GetMapping(path = "/english")
    List <English> getAll() {
    List <English> list = service.findAll();
        return list;
    }
    @PostMapping(path = "/english")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addEnglishWord(@RequestBody EnglishRequest request) {
        service.save(request);
    }
}

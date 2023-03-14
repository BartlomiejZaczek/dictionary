package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.controller.dto.Request;
import org.example.repository.entity.Word;
import org.example.service.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Controller {
    private final Service service;


    @PostMapping(path = "/word")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addWord(@RequestBody Request request) {
        service.save(request);
    }

    @GetMapping(path = "/words")
    @ResponseStatus(code = HttpStatus.OK)
    List<Word> findAll() {
        List <Word> words = service.findAll();
        return words;
    }

}

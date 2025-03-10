package hrims.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.extern.slf4j.Slf4j;

import hrims.demo.data.Person;
import hrims.demo.services.PeopleService;

@Slf4j
@RestController
@RequestMapping("/People")
public class PeopleController {
    
    @Autowired
    private PeopleService _service;

    @GetMapping("{nid}")
	public ResponseEntity<Person> getPerson(@PathVariable String nid) throws Exception {
        log.info("getPerson started");
		var match = _service.getPersonByNid(nid);
        log.info("getPerson finished");


        if (match == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(match, HttpStatus.OK);
	}

    @PostMapping("")
	public String addOrUpdatePerson(@RequestBody Person person) throws Exception {
        log.info("addOrUpdatePerson started");
        var result = _service.addOrUpdatePerson(person);
        log.info("addOrUpdatePerson finished");

        return result;
	}

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        var error = "An error occurred: " + ex.getMessage();
        log.error(error);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

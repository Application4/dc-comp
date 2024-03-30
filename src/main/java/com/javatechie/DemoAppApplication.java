package com.javatechie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RestController
public class DemoAppApplication {

	@Autowired
	private PersonRepository personRepository;

	@GetMapping("/greetings")
	public String greetings(){
		return "welcome to javatechie !";
	}

	@PostMapping("/person")
	public Person addPerson(@RequestBody Person person){
		return personRepository.save(person);
	}

	@GetMapping("/persons")
	public List<Person> getPersons(){
		return personRepository.findAll();
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoAppApplication.class, args);
	}

}

package com.validations.demovalidations;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/validations")
public class DemoValidationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoValidationsApplication.class, args);
	}


	@PostMapping
	public MyTestBean createPerson(@Valid @RequestBody MyTestBean testBean) {
		return testBean;
	}



	record MyTestBean(@NotNull String name, @Max(100) int age, boolean isAdult){



		@AssertTrue(message = "Person is not Adult")
		public boolean isValidPersonToAdult(){
			if(age < 18){
				return !isAdult;
			}
			return true;
		}
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public String exceptionError(MethodArgumentNotValidException ex) {

		List<String> errors = new ArrayList<String>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}
		return errors.toString();
	}
}

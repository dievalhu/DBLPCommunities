package com.dblp.communities.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DefinitionValidator implements ConstraintValidator<Definition, String> {

	public void initialize(Definition arg0) {
		
	}
	
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		if (value.equalsIgnoreCase("weak") || value.equalsIgnoreCase("strong")
				|| value.equalsIgnoreCase("bounded") || value.equalsIgnoreCase("strongweighted")
				|| value.equalsIgnoreCase("weakweighted")) {
			return true;
		}
		
		return false;
	}
}

package com.dblp.communities.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GraphTypeValidator implements ConstraintValidator<GraphType, String> {
	
	@Override
	public void initialize(GraphType arg0) {
		
	}
	
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		if (value.equalsIgnoreCase("weighted") || value.equalsIgnoreCase("unweighted")) {
			return true;
		}
		
		return false;
	}
	
}

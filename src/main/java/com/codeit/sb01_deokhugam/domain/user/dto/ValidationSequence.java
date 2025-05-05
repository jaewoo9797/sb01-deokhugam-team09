package com.codeit.sb01_deokhugam.domain.user.dto;

import jakarta.validation.GroupSequence;

@GroupSequence({
	ValidationSequence.NotBlankGroup.class,
	ValidationSequence.SizeAndPatternGroup.class
})
public interface ValidationSequence {

	public interface NotBlankGroup {
	}

	public interface SizeAndPatternGroup {
	}

}

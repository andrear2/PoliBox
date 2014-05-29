package it.polito.ai.polibox.validator;

import it.polito.ai.polibox.entity.UploadedFiles;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class FileValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object uploadedFiles, Errors errors) {
		UploadedFiles files = (UploadedFiles) uploadedFiles;
		if (files.getFiles().size() == 0) {
			errors.rejectValue("file", "uploadForm.selectFile", "Nessun file selezionato");  
		}
	}

}

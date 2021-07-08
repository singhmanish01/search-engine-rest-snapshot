package com.mss.searchengine.constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class Messages {

    public final String catalogerSuccessMsg = "Cataloger added successfully";
    public final String catalogerFailureMsg = "Cataloger already exists";
    public final String DocumentUploadedMsg = "Document Uploaded Successfully";

}

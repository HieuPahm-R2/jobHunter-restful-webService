package AsukaSan.jobLancer.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import AsukaSan.jobLancer.domain.response.Data.ResUploadFileDTO;
import AsukaSan.jobLancer.service.FileDataService;
import AsukaSan.jobLancer.utils.error.StorageException;

@RestController
public class FileDataController {
    private final FileDataService fileDataService;
    public FileDataController(FileDataService fileDataService){
        this.fileDataService = fileDataService;
    }
    @Value("${hieupham.upload-file.base-uri}")
    private String baseURI;

    @PostMapping("/files")
    public ResponseEntity<ResUploadFileDTO> uploadData(
        @RequestParam(name = "file", required = false) MultipartFile file,
        @RequestParam("folder") String folder) throws URISyntaxException, IOException, StorageException{
            // CHECK VALIDATE
            if(file == null ||file.isEmpty()){
                throw new StorageException("Not leave blank, Please upload!!");
            }
            String fileName = file.getOriginalFilename();
            List<String> extensionsAllowed = Arrays.asList("pdf", "jpeg", "png","doc","docx", "jpg");
            boolean isValid = extensionsAllowed.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));
            if(!isValid){
                throw new StorageException("Invalid file format, Please try again!");
            }
            // handle create folder (Option)
            this.fileDataService.createUploadFolder(baseURI + folder);
            String fileUpload = this.fileDataService.store(file, folder);
            ResUploadFileDTO result = new ResUploadFileDTO(fileUpload,Instant.now());

            return ResponseEntity.ok().body(result);
        }
}

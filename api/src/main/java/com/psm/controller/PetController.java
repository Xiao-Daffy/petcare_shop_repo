package com.psm.controller;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.psm.blob.AzureStorageConfigure;
import com.psm.petcare.entity.Pet;
import com.psm.petcare.service.PetService;
import com.psm.petcare.vo.RespondStatus;
import com.psm.petcare.vo.ResultVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@Controller
//@ResponseBody // asynchronous request, it will return JSON format
@RestController // @Controller + @ResponseBody
@RequestMapping("/pet") // http://localhost/user/
@CrossOrigin // allow cross origin(允许前后端跨域访问)
public class PetController {

    @Resource
    private PetService petService;
    @Resource
    private AzureStorageConfigure azureStorageConfigure;

    //    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/imageupload")
    public ResultVO fileUpload(@RequestBody MultipartFile[] file) throws IOException {
        if(file ==null){
            return new ResultVO(RespondStatus.NO, "Image", null);
        }else {
            List<String> strings = azureStorageConfigure.uploadFiles(file);


            return new ResultVO(RespondStatus.NO, "Image", strings.get(0));
        }
    }

/* Duplicated Code
    @Value("${azure.storage.connection-string}")
    private String connectionString;
    @Value("${azure.storage.container-name}")
    private String containerName;
    @Value("${azure.storage.base-url}")
    private String baseUrl;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/imageupload")
    public ResultVO photoUpload(@RequestBody MultipartFile[] file) throws IOException {
        String fileUrl="";
        if(file ==null){
            return new ResultVO(RespondStatus.NO, "Image", null);
        }else {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            List<String> fileUrls = new ArrayList<>();
            for (MultipartFile filee : file) {
                String originalFileName = StringUtils.cleanPath(filee.getOriginalFilename());
                String fileName = UUID.randomUUID().toString() + "-" + originalFileName;
                BlobClient blobClient = containerClient.getBlobClient(fileName);

                blobClient.upload(filee.getInputStream(), filee.getSize());
                // Set content type metadata to specify that the blob is an image
                BlobHttpHeaders headers = new BlobHttpHeaders()
                        .setContentType(filee.getContentType());
                blobClient.setHttpHeaders(headers);

                fileUrl = baseUrl + "/" + containerName + "/" + fileName;
                fileUrls.add(fileUrl);
            }
            System.out.println("file link: "+fileUrls);
            return new ResultVO(RespondStatus.OK, "Image", fileUrl);
        }
    }
*/

    // get list of pets
    @GetMapping("/list/{sid}")
    public ResultVO getListPet(@PathVariable("sid") String storeId){
        return petService.getListPet(storeId);
    }

    @GetMapping("/get/{pid}")
    public ResultVO getPet(@PathVariable("pid") String petId){
        return petService.getPet(petId);
    }

    // edit
    @PutMapping("/edit/{pid}")
    public ResultVO editPet(@RequestBody Pet pet){

        return petService.editPet(pet);
    }


    // delete
    @DeleteMapping("delete/{pid}")
    public ResultVO deletePet(@PathVariable("pid") String pid){
        return petService.deletePet(pid);
    }


    // document
    @PostMapping("/document/{rid}")
    public ResultVO documentPet(@PathVariable("rid") String rid,  @RequestBody Pet pet){
        return petService.documentPet(rid, pet);
    }
}

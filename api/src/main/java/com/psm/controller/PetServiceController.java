package com.psm.controller;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;

import com.psm.petcare.entity.PetService;
import com.psm.petcare.entity.Store;
import com.psm.petcare.service.PetServiceService;
import com.psm.petcare.service.StoreService;
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
@RequestMapping("/petservice") // http://localhost/user/
@CrossOrigin // allow cross origin(允许前后端跨域访问)
public class PetServiceController {


    @Resource
    private PetServiceService petService;




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


    // get list of pet service by store id
    @GetMapping("/list/{sid}")
    public ResultVO getPetSerivceList(@PathVariable("sid") String sid){

        return petService.getListPetService(sid);
    }

    // get single pet service
    @GetMapping("/get/{srid}")
    public ResultVO getPetSerivce(@PathVariable("srid") String sid){

        return petService.getPetService(sid);
    }

    // edit
    @PutMapping("/edit/{srid}")
    public ResultVO editPetSerivce(@RequestBody PetService service){

        return petService.editPetService(service);
    }

    // add

    @PostMapping("/add")
    public ResultVO addPetService(@RequestBody PetService service){
        return petService.addPetService(service);
    }

    // delete
    @DeleteMapping("delete/{srid}")
    public ResultVO deleteService(@PathVariable("srid") String sid){
        return petService.deleteService(sid);
    }

    // pet service with store information
    @GetMapping("/all")
    public ResultVO getPetServiceStore(){
        return petService.getPetServiceAndStore();
    }
    @GetMapping("/detail/{pid}")
    public ResultVO getPetServiceStoreDetail(@PathVariable("pid") String pid){
        return petService.getOnePetServiceAndStore(pid);
    }

    @GetMapping("/reserved")
    public ResultVO getReservedPetService(){
        return petService.getReservedPetService();
    }
}

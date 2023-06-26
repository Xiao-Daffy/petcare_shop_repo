package com.psm.controller;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.psm.petcare.entity.Store;
import com.psm.petcare.service.StoreService;
import com.psm.petcare.vo.RespondStatus;
import com.psm.petcare.vo.ResultVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@Controller
//@ResponseBody // asynchronous request, it will return JSON format
@RestController // @Controller + @ResponseBody
@RequestMapping("/store") // http://localhost/user/
@CrossOrigin // allow cross origin(允许前后端跨域访问)
public class StoreController {

    @Resource
    private StoreService storeService;



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

    // open new store
    @PostMapping("/open/{uid}")
    public ResultVO openStore(@PathVariable("uid") String uid, @RequestBody Store store){

        return storeService.createStore(store);

    }


    @GetMapping("/get/{uid}")// by user id
    public ResultVO getAStore(@PathVariable("uid") String uid){
        return storeService.getStoreByUid(uid);
    }

    @GetMapping("/query/{sid}")// by store id
    public ResultVO queryAStore(@PathVariable("sid") String sid){


        return storeService.getStoreBySid(sid);
    }
    @GetMapping("/random")// by store id
    public ResultVO randomStores(){


        return storeService.getRandomStore();
    }
    @GetMapping("/all")// by store id
    public ResultVO getAllStores(){


        return storeService.getAllStores();
    }

    @DeleteMapping("/delete/{sid}")
    public ResultVO deleteStore(@RequestBody Store store){

        return storeService.deleteStore(store);
    }

    @PutMapping("/edit/{sid}")
    public ResultVO editStore(@RequestBody Store store){
        System.out.println("Edit Store");
        return storeService.editStore(store);
    }


}

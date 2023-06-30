package com.psm.controller;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.psm.blob.AzureStorageConfigure;
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

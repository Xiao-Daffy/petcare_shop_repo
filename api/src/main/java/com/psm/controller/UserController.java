package com.psm.controller;
   /*
        post: add
        get: query
        put: update
        delete: delete
    */
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.psm.blob.AzureStorageConfigure;
import com.psm.petcare.entity.User;
import com.psm.petcare.entity.UserAddress;
import com.psm.petcare.service.UserService;
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
@RequestMapping("/user") // http://localhost/user/
@CrossOrigin // allow cross origin(允许前后端跨域访问)
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private AzureStorageConfigure azureStorageConfigure;


    @PostMapping("/register")
    public ResultVO register(@RequestBody User user){

        ResultVO resultVO = userService.userRegister(user.getEmail(), user.getPassword());// return ResultVO to front end
        return resultVO;
    }

    @GetMapping("/login") // http://localhost/user/login
    //Postman test: http://localhost:8080/user/login?email=daffy@gmail.com&password=d1234
    public ResultVO login(@RequestParam(value = "email") String email,
                          @RequestParam(value = "password") String psw){

        ResultVO resultVO = userService.checkLogin(email, psw);
        return resultVO; // return ResultVO object to front end

    }

    // user reset password by email
    @PutMapping("/reset")
    public ResultVO reset(@RequestBody User user){

        return userService.restPassword(user.getEmail(),user.getPassword());
    }


    // users view their profile
    @GetMapping("/profile/{uid}") //eg. http:localhost:8080/user/profile/1
    public ResultVO viewProfile(@PathVariable("uid") String uid){
        return userService.getProfile(uid);
    }

    // users update their profile
    @PutMapping("/edit/{uid}") //eg. http:localhost:8080/user/edit/1
    public ResultVO editProfile(@RequestBody User user){
        return userService.updateProfile(user.getUserId()+"", user);
    }

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


}

package com.lcwd.electronic.store.ElectronicStore.services.impl;

import com.lcwd.electronic.store.ElectronicStore.exceptions.BadApiRequestException;
import com.lcwd.electronic.store.ElectronicStore.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String uploadImage(MultipartFile file, String path) throws IOException {
        String originalFilename = file.getOriginalFilename();
        logger.info("Filename : {}", originalFilename);

        // File name will be kept unique with the help of UUID
        String uniqueFilename = UUID.randomUUID().toString();

        // Pick out the extension
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));

        // Concatinated the unique file name with extension
        String fileNameWithExtension = uniqueFilename + extension;

        // Then we make the full path using the seperator i.e
        // On UNIX systems the value of this field is '/'; on Microsoft Windows systems it is '\\'.
//        String fullPathWithFileName = path + File.separator+fileNameWithExtension;
        String fullPathWithFileName = path +fileNameWithExtension;

        logger.info("Path : {}", fullPathWithFileName);
        logger.info("Current path : {}", System.getProperty("user.dir"));

        // We can also all only some specific file extensions to be uploaded like jpg, png, jpeg.
        if(extension.equalsIgnoreCase(".png") ||
        extension.equalsIgnoreCase(".jpg") ||
        extension.equalsIgnoreCase(".jpeg")) {
            logger.info("File extension : {}", extension);
            // File Save
            File folder = new File(path);
            if(!folder.exists()) {
                // create the folder
                folder.mkdirs();
            }
            // upload the file
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return fileNameWithExtension;

        } else {
            throw new BadApiRequestException("File with extension "+ extension + " not allowed");
        }
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath = path + File.separator+name;
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}

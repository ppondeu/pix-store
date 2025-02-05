package dev.ppondeu.java_starter.common.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


public interface IFileStorageService {
    public void init();
    public String store(MultipartFile file);
    public Resource loadAsResource(String fileName);
}

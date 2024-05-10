package com.example.groupbuying.service;


import com.example.groupbuying.domain.entity.File;
import com.example.groupbuying.domain.repository.FileRepository;
import com.example.groupbuying.dto.FileDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class FileService {
    private FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Transactional
    public Integer saveFile(FileDto fileDto) {
        return fileRepository.save(fileDto.toEntity()).getId();
    }

    @Transactional
    public FileDto getFile(Integer id) {
        File file = fileRepository.findById(id).get();

        FileDto fileDto = FileDto.builder()
                .id(id)
                .origFilename(file.getOrigFilename())
                .filename(file.getFilename())
                .filePath(file.getFilePath())
                .build();
        return fileDto;
    }
}

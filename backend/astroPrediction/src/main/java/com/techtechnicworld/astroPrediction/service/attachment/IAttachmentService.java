package com.techtechnicworld.astroPrediction.service.attachment;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.techtechnicworld.astroPrediction.dto.ApiResponse;
import com.techtechnicworld.astroPrediction.dto.CreateAttachmentRequest;
import com.techtechnicworld.astroPrediction.dto.DownloadAttachmentRequest;

public interface IAttachmentService {

    public ApiResponse<?> createAttachments(CreateAttachmentRequest createAttachmentRequest, List<MultipartFile> files);

    public ApiResponse<?> downloadAttachmentById(DownloadAttachmentRequest downloadAttachmentRequest);
}

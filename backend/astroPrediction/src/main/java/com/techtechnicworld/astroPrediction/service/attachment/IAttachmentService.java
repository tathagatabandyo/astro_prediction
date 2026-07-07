package com.techtechnicworld.astroPrediction.service.attachment;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.techtechnicworld.astroPrediction.dto.ApiResponse;
import com.techtechnicworld.astroPrediction.dto.CreateAttachmentRequest;
import com.techtechnicworld.astroPrediction.dto.AttachmentRequest;

public interface IAttachmentService {

    public ApiResponse<?> createAttachments(CreateAttachmentRequest createAttachmentRequest, List<MultipartFile> files);

    public ApiResponse<?> downloadAttachment(AttachmentRequest downloadAttachmentRequest);

    public ApiResponse<?> getAttachment(AttachmentRequest getAttachmentRequest);

    public ApiResponse<?> deleteAttachment(AttachmentRequest deleteAttachmentRequest);

    public ApiResponse<?> getAttachments(Integer start, Integer limit);
}

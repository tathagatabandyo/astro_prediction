package com.techtechnicworld.astroPrediction.service.attachment;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.techtechnicworld.astroPrediction.dto.ApiResponse;
import com.techtechnicworld.astroPrediction.dto.CreateAttachmentRequest;
import com.techtechnicworld.astroPrediction.dto.AttachmentRequest;
import com.techtechnicworld.astroPrediction.dto.AttachmentResponse;

public interface IAttachmentService {

    public ApiResponse<List<AttachmentResponse>> createAttachments(CreateAttachmentRequest createAttachmentRequest, List<MultipartFile> files);

    public Resource downloadAttachment(AttachmentRequest downloadAttachmentRequest);

    public AttachmentResponse getAttachmentMetadata(AttachmentRequest getAttachmentRequest);

    public ApiResponse<?> getAttachment(AttachmentRequest getAttachmentRequest);

    public ApiResponse<?> deleteAttachment(AttachmentRequest deleteAttachmentRequest);

    public ApiResponse<List<AttachmentResponse>> getAttachments(Integer start, Integer limit);
}

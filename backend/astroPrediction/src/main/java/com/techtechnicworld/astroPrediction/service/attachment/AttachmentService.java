package com.techtechnicworld.astroPrediction.service.attachment;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.techtechnicworld.astroPrediction.dto.ApiResponse;
import com.techtechnicworld.astroPrediction.dto.AttachmentRequest;
import com.techtechnicworld.astroPrediction.dto.CreateAttachmentRequest;
import com.techtechnicworld.astroPrediction.repository.AttachmentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AttachmentService implements IAttachmentService {

    private final AttachmentRepository attachmentRepository;

    @Override
    public ApiResponse<?> createAttachments(CreateAttachmentRequest createAttachmentRequest,
            List<MultipartFile> files) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createAttachments'");
    }

    @Override
    public ApiResponse<?> downloadAttachment(AttachmentRequest downloadAttachmentRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'downloadAttachment'");
    }

    @Override
    public ApiResponse<?> getAttachment(AttachmentRequest getAttachmentRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAttachment'");
    }

    @Override
    public ApiResponse<?> deleteAttachment(AttachmentRequest deleteAttachmentRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAttachment'");
    }

    @Override
    public ApiResponse<?> getAttachments(Integer start, Integer limit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAttachments'");
    }
}

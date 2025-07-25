package com.cywedding.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

import com.cywedding.common.DMLType;
import com.cywedding.dto.Image;
import com.cywedding.dto.QRUser;
import com.cywedding.mapper.ImageMapper;

import lombok.RequiredArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Autowired
    QRUserService userService;

    @Autowired
    JavaMailSender mailSender;

    private final ImageMapper imageMapper;

    public void uploadImage(String code, String fileName, byte[] file) {
        Image image = new Image();
        image.setQrCode(code);
        image.setFileName(fileName);
        image.setFile(file);

        imageMapper.uploadImage(image);

        // QRUser 객체 생성해서 업데이트
        QRUser user = new QRUser();
        user.setQrCode(image.getQrCode());
        user.setType(DMLType.UPLOAD.name()); // String으로 설정
        userService.updateUserList(user);
    }

    public List<Map<String, Object>> selectImageList() {
        return imageMapper.selectImageList("premium");
    }

    public byte[] selectImage(String fileName) {
        Map<String, Object> imageObj = fetchImage(fileName);

        Object fileObj = imageObj.get("file");
        if (fileObj instanceof byte[] bytes) {
            return bytes;
        } else {
            throw new IllegalArgumentException("파일형식 오류: " + fileObj);
        }
    }

    // 관리자 전용
    public void deleteImage(String code, String fileName) {
        Map<String, Object> imageObj = fetchImage(fileName);
        System.out.println(imageObj);

        QRUser user = new QRUser();
        user.setQrCode(String.valueOf(imageObj.get("code")));
        user.setType(DMLType.DELETE.name());

        userService.updateUserList(user);
        imageMapper.deleteImage(fileName);
    }

    public Map<String, Object> fetchImage(String fileName) {
        Map<String, Object> image = imageMapper.selectImage(fileName);
        if (image == null) {
            throw new IllegalArgumentException("이미지가 존재하지 않습니다: " + fileName);
        }
        return image;
    }

    @Async
    public void sendEmail(String emailAddress, String plan) throws MessagingException, IOException {
        List<Map<String, Object>> imageList = imageMapper.selectImageList(plan);

        ByteArrayOutputStream zipOutStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(zipOutStream);

        for (Map<String, Object> image : imageList) {
            String fileName = (String) image.get("name");
            Map<String, Object> imageData = imageMapper.selectImage(fileName);
            byte[] fileBytes = (byte[]) imageData.get("file");

            if (fileBytes != null) {
                ZipEntry entry = new ZipEntry(fileName);
                zip.putNextEntry(entry);
                zip.write(fileBytes);
                zip.closeEntry();
            }
        }
        zip.close();

        byte[] zipBytes = zipOutStream.toByteArray();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        List<String> emailList = new ArrayList<>();
        emailList.add(emailAddress);
        emailList.add("payo03@solomontech.net");

        helper.setTo(emailList.toArray(new String[0]));
        helper.setFrom("xsonyn14@gmail.com");
        helper.setSubject("웨딩 이미지 첨부파일");
        helper.setText(
            """
            안녕하세요, 고객님.

            소중한 순간을 함께해 주셔서 진심으로 감사드립니다.
            촬영된 웨딩 이미지를 ZIP 파일로 첨부해드리오니 확인 부탁드립니다.

            결혼식의 Photo Event가 두 분의 사랑을 더욱 특별하게 기록하는 시간이 되었길 바랍니다.
            앞으로도 변함없는 행복과 사랑이 가득하시길 기원합니다.

            감사합니다.
            - [송감자 & 임감자] 드림
            """,
            false
        );

        ByteArrayResource zipResource = new ByteArrayResource(zipBytes);
        helper.addAttachment("images.zip", zipResource);

        mailSender.send(message);
    }
}
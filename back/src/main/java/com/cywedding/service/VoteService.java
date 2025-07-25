package com.cywedding.service;

import com.cywedding.common.DMLType;
import com.cywedding.dto.QRUser;
import com.cywedding.dto.Vote;
import com.cywedding.mapper.VoteMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteService {

    @Autowired
    ImageService imageService;

    @Autowired
    QRUserService userService;

    private final VoteMapper voteMapper;

    public void voteImage(String code, String fileName) {
        imageService.fetchImage(fileName);

        Vote vote = new Vote();
        vote.setQrCode(code);
        vote.setFileName(fileName);
        voteMapper.insertVote(vote);

        QRUser user = new QRUser();
        user.setQrCode(code);
        user.setType(DMLType.VOTE.name());
        userService.updateUserList(user);
    }

    public void deleteVote(String code, String fileName) {
        imageService.fetchImage(fileName);

        Vote vote = new Vote();
        vote.setQrCode(code);
        voteMapper.deleteVote(vote);
    }
}
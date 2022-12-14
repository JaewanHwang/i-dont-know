package com.idk.api.vote.service;

import com.idk.api.common.exception.PermissionException;
import com.idk.api.user.domain.entity.User;
import com.idk.api.user.security.userdetails.CustomUserDetails;
import com.idk.api.vote.domain.entity.Ballot;
import com.idk.api.vote.domain.entity.Vote;
import com.idk.api.vote.domain.repository.BallotRepository;
import com.idk.api.vote.domain.repository.VoteRepository;
import com.idk.api.vote.dto.BallotDto;
import com.idk.api.vote.dto.BallotRequest;
import com.idk.api.vote.dto.BallotResponse;
import com.idk.api.vote.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BallotService {
    private final BallotRepository ballotRepository;
    private final VoteRepository voteRepository;

    @Transactional
    public BallotResponse.OnlyId create(BallotRequest.Create request, User user) {
        Vote vote = voteRepository.findById(request.getVoteId()).orElseThrow(VoteNotFoundException::new);
        if(ballotRepository.existsByUserAndVote(user, vote)){
            throw new BallotDuplicatedException();
        }
        checkVote(vote);
        Ballot ballot = Ballot.create(request, vote, user);
        vote.changeBallotCount(false, request.getChoice());
        Ballot savedBallot = ballotRepository.save(ballot);
        return BallotResponse.OnlyId.build(savedBallot);
    }

    @Transactional
    public BallotResponse.OnlyId delete(Long ballotId, User user) {
        Ballot ballot = ballotRepository.findById(ballotId).orElseThrow(BallotNotFoundException::new);
        Vote vote = voteRepository.findById(ballot.getVote().getId()).orElseThrow(VoteNotFoundException::new);
        checkVote(vote);
        if(Objects.equals(user.getId(), ballot.getUser().getId())){
            ballotRepository.deleteById(ballot.getId());
        }else throw new PermissionException();
        vote.changeBallotCount(true, ballot.getChoice());
        return BallotResponse.OnlyId.build(ballot);
    }

    public List<BallotDto.CountByDistrictCode> getCountByDistrictCode(Long voteId, User user) {
        return ballotRepository.countBallotByVoteAndDistrictCodeAndChoice(voteId);
    }

    public List<BallotDto.CountByGender> getCountByGender(Long voteId, User user) {
        return ballotRepository.countBallotByVoteAndGenderAndChoice(voteId);
    }

    public List<BallotDto.CountByAge> getCountByAge(Long voteId, User user) {
        return ballotRepository.countBallotByVoteAndAgeAndChoice(voteId);
    }

    private void checkVote(Vote vote) {
        if(vote.isStatus()) {
            throw new VoteCompletedException();
        }else if(vote.getDeletedAt() != null){
            throw new VoteDeletedException();
        }
    }
}

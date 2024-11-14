package com.yesterpay.team.controller;

import com.yesterpay.member.dto.Member;
import com.yesterpay.team.dto.Team;
import com.yesterpay.team.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "팀 API", description = "십자말 팀에 대한 API")
public class TeamController {
    private final TeamService service;

    @PostMapping("/puzzle/team")
    @Operation(summary = "팀 생성하기")
    public ResponseEntity<Team> makeTeam(@RequestParam Long memberId) {
        Team team = service.makeTeam(memberId);
        if (team == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(team);
    }

    @GetMapping("/puzzle/team")
    @Operation(summary = "모든 팀 리스트 조회")
    public ResponseEntity<List<Team>> selectAllTeam() {
        List<Team> teamList = service.selectAll();
        return ResponseEntity.ok(teamList);
    }

    @PostMapping("puzzle/team/join")
    @Operation(summary = "팀 가입하기")
    public ResponseEntity<Map<String, Boolean>> joinTeam(@RequestParam Long memberId, @RequestParam Long puzzleTeamId) {
        int result = service.joinTeam(memberId, puzzleTeamId);

        Map<String, Boolean> response = new HashMap<>();
        if (result == 0) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        response.put("success", true);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/puzzle/team/member")
    @Operation(summary = "팀원 승인, 거절")
    public ResponseEntity<Map<String, Boolean>> setMemberStatus(@RequestParam Long memberId, @RequestParam Long puzzleTeamId, @RequestParam boolean status) {
        Map<String, Boolean> response = new HashMap<>();
        int result;
        if (status == true) {
            result = service.acceptMember(memberId, puzzleTeamId);
        } else {
            result = service.rejectMember(memberId, puzzleTeamId);
        }

        if (result == 0) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        response.put("success", true);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/puzzle/{teamId}/member")
    @Operation(summary = "팀원 조회하기")
    public ResponseEntity<List<Member>> getTeamMembers(@PathVariable Long teamId) {
        Team team = service.selectById(teamId);
        if (team == null) {
            return ResponseEntity.badRequest().body(null);
        }

        List<Member> memberList = service.selectMembersByTeamId(teamId, team.getMemberId());
        return ResponseEntity.ok(memberList);
    }

    @PostMapping("/puzzle/leave")
    @Operation(summary = "팀 탈퇴하기")
    public ResponseEntity<Map<String, Boolean>> leaveTeam(@RequestHeader("member-id") Long userId, @RequestParam Long teamMemberId, @RequestParam Long puzzleTeamId) {
        Map<String, Boolean> response = new HashMap<>();
        if (userId != teamMemberId) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        int result = service.leaveTeam(teamMemberId, puzzleTeamId);

        if (result == 0) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        response.put("success", true);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/puzzle/release")
    @Operation(summary = "팀원 방출하기")
    public ResponseEntity<Map<String, Boolean>> releaseMember(@RequestHeader("member-id") Long userId, @RequestParam Long teamMemberId, @RequestParam Long puzzleTeamId) {
        Map<String, Boolean> response = new HashMap<>();

        int result = service.releaseMember(teamMemberId, puzzleTeamId, userId);

        if (result == 0) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        response.put("success", true);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

package com.example.demo.controller;

import com.example.demo.dto.ReservationRequestDto;
import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.dto.updateReservationRequestDto;
import com.example.demo.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public void createReservation(@RequestBody ReservationRequestDto reservationRequestDto) {
        reservationService.createReservation(reservationRequestDto.getItemId(),
                                            reservationRequestDto.getUserId(),
                                            reservationRequestDto.getStartAt(),
                                            reservationRequestDto.getEndAt());
    }

    @PatchMapping("/{id}/update-status")
    public ResponseEntity<String> updateReservation(@PathVariable Long id, @RequestBody updateReservationRequestDto requestDto) {

        reservationService.updateReservationStatus(id, requestDto.getStatus());
        return ResponseEntity.ok().body("정삭적으로 수정 되었습니다.");
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> findAll() {
        return ResponseEntity.ok().body(reservationService.getReservations());

    }

    @GetMapping("/search")
    public ResponseEntity<List<ReservationResponseDto>> searchAll(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long itemId) {
        return ResponseEntity.ok().body(reservationService.searchAndConvertReservations(userId, itemId));
    }
}

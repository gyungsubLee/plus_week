package com.example.demo.service;

import com.example.demo.dto.QReservationResponseDto;
import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.entity.*;
import com.example.demo.entity.enums.ReservationStatus;
import com.example.demo.exception.InvalidReservationStatusException;
import com.example.demo.exception.ReservationConflictException;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.demo.entity.QItem.item;
import static com.example.demo.entity.QReservation.reservation;
import static com.example.demo.entity.QUser.user;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final RentalLogService rentalLogService;
    private final JPAQueryFactory queryFactory;

    @Transactional
    public void createReservation(Long itemId, Long userId, LocalDateTime startAt, LocalDateTime endAt) {
        // 쉽게 데이터를 생성하려면 아래 유효성검사 주석 처리
        List<Reservation> haveReservations = reservationRepository.findConflictingReservations(itemId, startAt, endAt);
        if(!haveReservations.isEmpty()) {
            throw new ReservationConflictException("해당 물건은 이미 그 시간에 예약이 있습니다.");
        }

        Item item = itemRepository.findByIdOrElseThrow(itemId);
        User user = userRepository.findByIdOrElseThrow(userId);
        Reservation reservation = new Reservation(item, user, ReservationStatus.PENDING, startAt, endAt);
        Reservation savedReservation = reservationRepository.save(reservation);

//        RentalLog rentalLog = new RentalLog(savedReservation, "로그 메세지", "CREATE");
//        rentalLogService.save(rentalLog);
    }

    public List<ReservationResponseDto> getReservations() {
        List<Reservation> reservations = reservationRepository.findAllWithUserAndItem();

        return reservations.stream().map(reservation -> {
            User user = reservation.getUser();
            Item item = reservation.getItem();

            return new ReservationResponseDto(
                    reservation.getId(),
                    user.getNickname(),
                    item.getName(),
                    reservation.getStartAt(),
                    reservation.getEndAt()
            );
        }).toList();
    }

    public List<ReservationResponseDto> searchAndConvertReservations(Long userId, Long itemId) {

        return queryFactory
                .select(new QReservationResponseDto(
                        reservation.id,
                        user.nickname,
                        item.name,
                        reservation.startAt,
                        reservation.endAt
                ))
                .from(reservation)
                .join(reservation.user, user)
                .join(reservation.item, item)
                .where(
                    userIdEq(userId),
                    itemIdEq(itemId)
                )
                .fetch();
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? user.id.eq(userId) : null;
    }

    private BooleanExpression itemIdEq(Long itemId) {
        return itemId != null ? item.id.eq(itemId) : null;
    }


    @Transactional
    public void updateReservationStatus(Long reservationId, String status) {
        Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationId);

        ReservationStatus targetStatus;

        try {
            log.info("Status: {}", status);

            targetStatus = ReservationStatus.valueOf(status.toUpperCase());

            log.info("Update Status: {}", targetStatus);

        } catch (IllegalArgumentException e) {
            throw new InvalidReservationStatusException("유효하지 않은 상태: " + status);
        }

        reservation.updateStatus(targetStatus);
    }
}

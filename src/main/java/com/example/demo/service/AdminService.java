package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.enums.UserStatus;
import com.example.demo.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.entity.QUser.user;


@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

//    private final JPAQueryFactory queryFactory;
    private final UserRepository userRepository;


    @Transactional
    public void reportUsers(List<Long> userIds) {

        /**
         * 방법 1) Querydsl 벌크 연산 사용
         * 사용 시, 영속성 컨텍스트 와 DB 불인치 문제 주의 필요
         */
//        long count = queryFactory
//                .update(user)
//                .set(user.status, "BLOCKED")
//                .where(user.id.in(userIds)) // userIds에 포함된 사용자 ID로 필터링
//                .execute();

        /**
         * 방법 2) Spring Data JPA - @Modifying 사용
         */
        int count = userRepository.bulkUpdateUsersStatusBLoked(UserStatus.BLOCKED, userIds);

        log.info("유저 status 'BLOKED' 처리한 유저 수: {}", count);
    }
}

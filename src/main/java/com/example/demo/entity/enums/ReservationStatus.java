package com.example.demo.entity.enums;

public enum ReservationStatus {
    PENDING {
        @Override
        public boolean canTransitionTo(ReservationStatus targetStatus) {
            return targetStatus == APPROVED || targetStatus == CANCELED || targetStatus == EXPIRED;
        }
    },
    APPROVED {
        @Override
        public boolean canTransitionTo(ReservationStatus targetStatus) {
            return false; // APPROVED 상태에서는 변경 불가
        }
    },
    CANCELED {
        @Override
        public boolean canTransitionTo(ReservationStatus targetStatus) {
            return false; // CANCELED 상태에서는 변경 불가
        }
    },
    EXPIRED {
        @Override
        public boolean canTransitionTo(ReservationStatus targetStatus) {
            return false; // EXPIRED 상태에서는 변경 불가
        }
    };

    public abstract boolean canTransitionTo(ReservationStatus targetStatus);
}
package com.example.demo.entity;

import com.example.demo.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ItemTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("Item 저장 시, Status가 NULL 일때 기본값 설정 확인")
    void ItemDynamicInsertTest() {

        // given
        String defaultStatus = "PENDING";

        String role = "admin";
        String email = "test@email.com";
        String nickname = "nickname1";
        String password = "password1";

        User user = new User(role, email, nickname, password);
        // item.status = null
        Item item = new Item("name1", "description1", user, user);

        // when
        Item saveItem = itemRepository.save(item);

        // then
        assertThat(saveItem).isNotNull();
        assertThat(saveItem).isEqualTo(defaultStatus);
    }

}
package com.example.customerserver.service.customer;

import com.example.customerserver.config.AppConfig;
import com.example.customerserver.domain.Customer;
import com.example.customerserver.repository.CustomerRepository;
import com.example.customerserver.service.customer.profile.ProfileManager;
import com.example.customerserver.web.request.CustomerEditRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class CustomerEditorTest {

    @InjectMocks
    private CustomerEditor customerEditor;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProfileManager profileManager;

    @Test
    @DisplayName("nickname 수정 테스트")
    public void tryEditNickname() throws Exception {
        // given
        Customer customer = stubCustomer();
        given(customerRepository.findById(1L))
                .willReturn(Optional.of(customer));

        given(profileManager.download(null))
                .willReturn("default.png");

        CustomerEditRequest customerEditRequest = new CustomerEditRequest();
        customerEditRequest.setNickname("성급한");
        // when
        customerEditor.edit(1L, customerEditRequest);
        // then

        assertThat(customer.getNickname())
                .isEqualTo("성급한");

        assertThat(customer.getUsername())
                .isEqualTo("impati");

    }

    @Test
    @DisplayName("introduceComment 와 profile 수정 테스트")
    public void given_when_then() throws Exception {
        // given
        Customer customer = stubCustomer();
        given(customerRepository.findById(1L))
                .willReturn(Optional.of(customer));

        String storeName = UUID.randomUUID().toString();
        String fileUrl = "https://service-hub.org/file/logo/default.png";
        given(profileManager.download(fileUrl))
                .willReturn(storeName);

        CustomerEditRequest customerEditRequest = new CustomerEditRequest();
        customerEditRequest.setIntroduceComment("hi hello");
        customerEditRequest.setProfileUrl(fileUrl);
        // when
        customerEditor.edit(1L, customerEditRequest);
        // then

        assertThat(customer.getNickname())
                .isEqualTo("impati");

        assertThat(customer.getUsername())
                .isEqualTo("impati");

        assertThat(customer.getIntroduceComment())
                .isEqualTo("hi hello");

        assertThat(customer.getProfileImageUrl())
                .isEqualTo(AppConfig.getHost() + "/file/profile/" + storeName);
    }


    private Customer stubCustomer() {
        return Customer.builder()
                .username("impati")
                .nickname("impati")
                .email("test@test.com")
                .build();
    }
}
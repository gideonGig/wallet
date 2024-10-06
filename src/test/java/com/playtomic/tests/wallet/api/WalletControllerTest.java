package com.playtomic.tests.wallet.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.application.WalletService;
import com.playtomic.tests.wallet.command.WalletCreateCommand;
import com.playtomic.tests.wallet.dto.WalletResponseDto;
import com.playtomic.tests.wallet.entity.valueobject.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WalletService walletService;

    //@Test
    public void testWalletCreatedCreatesWallet() throws Exception {
        var walletCreateCommand = WalletCreateCommand.builder().walletName("dollar")
                .currency(Currency.USD).customerId("my_id").build();

        var uuid = UUID.randomUUID();

        var walletResponseDto = WalletResponseDto.builder()
                .walletTrackingId(uuid).Currency(Currency.USD.name())
                .amount(BigDecimal.ZERO).build();

        when(walletService.createWallet(walletCreateCommand)).thenReturn(walletResponseDto);


        RequestBuilder requestBuilder = post("/wallets/createWallet")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(walletCreateCommand));

        MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isCreated())
                .andReturn();
    }
}

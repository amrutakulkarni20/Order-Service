package com.etrusted.interview.demo.service;

import com.etrusted.interview.demo.entity.Shop;
import com.etrusted.interview.demo.repository.ShopRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;

@ExtendWith(MockitoExtension.class)
class ShopServiceImplTest {

    public ShopServiceImplTest(){}

    @Mock
    private ShopRepository shopRepository;

    @InjectMocks
    private ShopServiceImpl shopService;


    @Test
    void getShopByShopUrlAndVerifiesShopUrl(){
        final String shopUrl = "www.trustedshop.com";
        Shop expectedShopResponse = createShopResponse();
        when(shopRepository.findShopByUrl(shopUrl)).thenReturn(Optional.of(expectedShopResponse));
        Optional<Shop> actualShopResponse = shopService.findShopByUrl(shopUrl);
        assertNotNull(actualShopResponse);
        assertEquals(expectedShopResponse.getUrl(),actualShopResponse.get().getUrl());
    }

    @Test
    void returnEmptyShopWhenShopUrlIsNotPresent(){
        Shop expectedShopResponse = new Shop();
        when(shopRepository.findShopByUrl(anyString())).thenReturn(Optional.of(expectedShopResponse));
        Optional<Shop> actualShopResponse = shopService.findShopByUrl(anyString());
        assertNull(actualShopResponse.get().getUrl());
        assertEquals(0,actualShopResponse.get().getId());
    }

    private Shop createShopResponse() {
        return Shop.builder().id(1).url("www.trustedshop.com").build();
    }
}

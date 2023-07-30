package com.etrusted.interview.demo.service;

import com.etrusted.interview.demo.entity.Shop;
import com.etrusted.interview.demo.repository.ShopRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ShopServiceImpl implements ShopService {

    private ShopRepository shopRepository;

    public ShopServiceImpl(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Override
    public Optional<Shop> findShopByUrl(String shopUrl) {
        return shopRepository.findShopByUrl(shopUrl);
    }

}

package com.etrusted.interview.demo.service;

import com.etrusted.interview.demo.entity.Shop;
import java.util.Optional;

public interface ShopService {

    Optional<Shop> findShopByUrl(String shopUrl);

}

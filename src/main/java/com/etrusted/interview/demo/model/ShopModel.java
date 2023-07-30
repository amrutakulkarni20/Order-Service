package com.etrusted.interview.demo.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShopModel {

    private long id;

    @NotEmpty(message = "Shop URL cannot be left blank")
    private String url;

    public ShopModel(String url) {
        this.url = url;
    }

}

package com.franchise.franchiseapi;

import com.franchise.api.FranchiseApiApplication;
import org.springframework.boot.SpringApplication;

public class TestFranchiseApiApplication {

    public static void main(String[] args) {
        SpringApplication.from(FranchiseApiApplication::main).with(com.franchise.api.TestcontainersConfiguration.class).run(args);
    }

}

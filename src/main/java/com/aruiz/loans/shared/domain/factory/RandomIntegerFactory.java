package com.aruiz.loans.shared.domain.factory;

import java.util.concurrent.ThreadLocalRandom;

public class RandomIntegerFactory {
    public static Integer generateId(int max) {
        return ThreadLocalRandom.current().nextInt(max);
    }
}

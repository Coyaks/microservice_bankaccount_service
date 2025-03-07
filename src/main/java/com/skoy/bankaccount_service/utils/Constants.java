package com.skoy.bankaccount_service.utils;

import java.math.BigDecimal;

public class Constants {
    public static final int STATUS_OK = 200;
    public static final int STATUS_E404 = 404; // STATUS_NOT_FOUND
    public static final int STATUS_E500 = 500; // STATUS_INTERNAL_SERVER_ERROR
    public static final BigDecimal MIN_OPENING_BALANCE = BigDecimal.ZERO; // Monto mínimo de apertura
    public static final BigDecimal MAINTENANCE_COMMISSION = BigDecimal.valueOf(50);
}

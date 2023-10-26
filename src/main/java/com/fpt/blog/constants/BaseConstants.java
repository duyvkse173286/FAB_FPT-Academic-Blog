package com.fpt.blog.constants;

import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

public class BaseConstants {

    public static final String SESSION_MESSAGE = "message";

    public static final String SESSION_ERROR = "error";

    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    public static final long MIN_PASSWORD_LENGTH = 8;

    public static  final List<String> ALLOWED_DOMAINS = List.of("fpt.edu.vn");

    public static final int DEFAULT_PAGE_NUMBER = 1;

    public static final int DEFAULT_PAGE_SIZE = 10;

    public static final int DEFAULT_MAX_PAGE_SIZE = 200;

    public static final String DEFAULT_SORT_BY = "createdAt";

    public static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.DESC;

}

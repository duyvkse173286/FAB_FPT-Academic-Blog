package com.fpt.blog.enums;

public enum MemberType {

    COPPER(0),
    SILVER(10),
    GOLD(50),
    DIAMOND(100);

    private int postCount;

    MemberType(int postCount) {
        this.postCount = postCount;
    }

    public int getPostCount() {
        return this.postCount;
    }


}

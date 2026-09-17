package com.backend.blogplatform.dto.response;

public class LikeResponse {

    private boolean liked;
    private long likeCount;

    public LikeResponse(boolean liked, long likeCount) {
        this.liked = liked;
        this.likeCount = likeCount;
    }

    public boolean isLiked() {
        return liked;
    }

    public long getLikeCount() {
        return likeCount;
    }
}

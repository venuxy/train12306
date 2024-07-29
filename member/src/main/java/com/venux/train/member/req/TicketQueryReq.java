package com.venux.train.member.req;

import com.venux.train.common.req.PageReq;

public class TicketQueryReq extends PageReq {

    private Long memberId;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    @Override
    public String toString() {
        return "TicketQueryReq{" +
                "} " + super.toString();
    }
}

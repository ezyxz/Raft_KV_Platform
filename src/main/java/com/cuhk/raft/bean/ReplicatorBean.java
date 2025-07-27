package com.cuhk.raft.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReplicatorBean {

    private final int id;
    private final String address;


    @Override
    public String toString() {
        return "Replicator{id=" + id + ", address='" + address + "'}";
    }
}

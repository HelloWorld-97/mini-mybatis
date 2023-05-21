package com.danzz.datasource.pooled;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
public class PoolState {

    // 空闲池
    private List<PooledConnection> idleQueue;
    // 活跃池
    private List<PooledConnection> activeQueue;

    // 统计信息
    private int requestCnt;//连接获取次数

    private int waitCnt;

    private int checkoutCnt;

    private long totalRequestTime;//连接获取时间总和

    private long totalWaitTime;//获取连接等待时间总和

    private long totalCheckoutTime;//连接checkout时间总和

    public PoolState(){
        this.idleQueue = new LinkedList<>();
        this.activeQueue = new LinkedList<>();
        this.requestCnt = 0;
        this.waitCnt = 0;
        this.checkoutCnt = 0;
        this.totalRequestTime = 0;
        this.totalCheckoutTime = 0;
        this.totalWaitTime = 0;
    }
}

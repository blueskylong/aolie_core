package com.ranranx.aolie.core.common;

import org.springframework.stereotype.Component;

/**
 * @Author xxl
 * @Description
 * @Date 2020/9/18 17:38
 * @Version V0.0.1
 **/
@Component
public class IdGenerator {
    private static IdWorker worker = new IdWorker(1, 1, 1);

    public static long getNextId(String type) {
        return IdGenerator.worker.nextId();
    }

    /**
     * 搬运自煲煲菜的博客
     */
    public static class IdWorker {

        //下面两个每个5位，加起来就是10位的工作机器id
        private long workerId;    //工作id
        private long dataCenterId;   //数据id
        //12位的序列号
        private long sequence;

        public IdWorker(long workerId, long datacenterId, long sequence) {
            // sanity check for workerId
            if (workerId > maxWorkerId || workerId < 0) {
                throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
            }
            if (datacenterId > maxDatacenterId || datacenterId < 0) {
                throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
            }


            this.workerId = workerId;
            this.dataCenterId = datacenterId;
            this.sequence = sequence;
        }


        //长度为5位
        private long workerIdBits = 2L;
        private long datacenterIdBits = 2L;
        //最大值
        private long maxWorkerId = -1L ^ (-1L << workerIdBits);
        private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
        //序列号id长度
        private long sequenceBits = 9L;
        //序列号最大值
        private long sequenceMask = -1L ^ (-1L << sequenceBits);

        //工作id需要左移的位数，10位
        private long workerIdShift = sequenceBits;
        //数据id需要左移位数 10+2=12位
        private long datacenterIdShift = sequenceBits + workerIdBits;
        //时间戳需要左移位数 10+2+3=15位
        private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

        //上次时间戳，初始值为负数
        private long lastTimestamp = -1L;

        public long getWorkerId() {
            return workerId;
        }

        public long getDataCenterId() {
            return dataCenterId;
        }

        public long getTimestamp() {
            return System.currentTimeMillis();
        }

        //下一个ID生成算法
        public synchronized long nextId() {
            long timestamp = timeGen();

            //获取当前时间戳如果小于上次时间戳，则表示时间戳获取出现异常
            if (timestamp < lastTimestamp) {
                System.err.printf("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp);
                throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                        lastTimestamp - timestamp));
            }

            //获取当前时间戳如果等于上次时间戳（同一毫秒内），则在序列号加一；否则序列号赋值为0，从0开始。
            if (lastTimestamp == timestamp) {
                sequence = (sequence + 1) & sequenceMask;
                if (sequence == 0) {
                    timestamp = tilNextMillis(lastTimestamp);
                }
            } else {
                sequence = 0;
            }

            //将上次时间戳值刷新
            lastTimestamp = timestamp;

            /**
             * 返回结果：
             * (timestamp - twepoch) << timestampLeftShift) 表示将时间戳减去初始时间戳，再左移相应位数
             * (datacenterId << datacenterIdShift) 表示将数据id左移相应位数
             * (workerId << workerIdShift) 表示将工作id左移相应位数
             * | 是按位或运算符，例如：x | y，只有当x，y都为0的时候结果才为0，其它情况结果都为1。
             * 因为个部分只有相应位上的值有意义，其它位上都是0，所以将各部分的值进行 | 运算就能得到最终拼接好的id
             */
            return ((timestamp) << timestampLeftShift) |
                    (dataCenterId << datacenterIdShift) |
                    (workerId << workerIdShift) |
                    sequence;
        }

        //获取时间戳，并与上次时间戳比较
        private long tilNextMillis(long lastTimestamp) {
            long timestamp = timeGen();
            while (timestamp <= lastTimestamp) {
                timestamp = timeGen();
            }
            return timestamp;
        }

        //获取系统时间戳
        private long timeGen() {
//            return 999738870011L;
            return System.currentTimeMillis() - 1000000000000L;
        }
    }


    public static void main(String[] arge) {
        System.out.println(IdGenerator.getNextId(""));
        System.out.println(System.currentTimeMillis() - 1000000000000L);
        System.out.println(System.currentTimeMillis());
        System.out.println(System.currentTimeMillis());
        System.out.println(System.currentTimeMillis());
        System.out.println(System.currentTimeMillis());
    }
}

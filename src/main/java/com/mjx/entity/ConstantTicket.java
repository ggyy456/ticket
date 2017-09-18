package com.mjx.entity;

/**
 * Created by Administrator on 2017/9/15 0015.
 */
public interface ConstantTicket {
    public static final String TICKET_TYPE_FIRST_SEAT = "一等座";
    public static final String TICKET_TYPE_SECOND_SEAT = "二等座";
    public static final String TICKET_TYPE_BUSINESS_SEAT = "商务座";
    public static final String TICKET_TYPE_HARD_SEAT = "硬座";
    public static final String TICKET_TYPE_SOFT_SEAT = "软座";
    public static final String TICKET_TYPE_HARD_SLEEP = "硬卧";
    public static final String TICKET_TYPE_SOFT_SLEEP = "软卧";
    public static final String TICKET_TYPE_NO_SEAT = "无座";
    public static final String[] TICKET_TYPE_G = {TICKET_TYPE_FIRST_SEAT,TICKET_TYPE_SECOND_SEAT,TICKET_TYPE_BUSINESS_SEAT};
    public static final int[] TICKET_TYPE_G_NUM = {120,420,120};
    public static final String[] TICKET_TYPE_C = {TICKET_TYPE_HARD_SEAT,TICKET_TYPE_SOFT_SEAT,TICKET_TYPE_NO_SEAT};
    public static final int[] TICKET_TYPE_C_NUM = {200,100,50};
    public static final String[] TICKET_TYPE_D = {TICKET_TYPE_FIRST_SEAT,TICKET_TYPE_SECOND_SEAT,TICKET_TYPE_BUSINESS_SEAT};
    public static final int[] TICKET_TYPE_D_NUM = {100,400,100};
    public static final String[] TICKET_TYPE_Z = {TICKET_TYPE_HARD_SLEEP,TICKET_TYPE_SOFT_SLEEP};
    public static final int[] TICKET_TYPE_Z_NUM = {300,100};
    public static final String[] TICKET_TYPE_T = {TICKET_TYPE_HARD_SEAT,TICKET_TYPE_SOFT_SEAT,TICKET_TYPE_HARD_SLEEP,TICKET_TYPE_SOFT_SLEEP,TICKET_TYPE_NO_SEAT};
    public static final int[] TICKET_TYPE_T_NUM = {150,100,100,50,50};
    public static final String[] TICKET_TYPE_K = {TICKET_TYPE_HARD_SEAT,TICKET_TYPE_SOFT_SEAT,TICKET_TYPE_HARD_SLEEP,TICKET_TYPE_SOFT_SLEEP,TICKET_TYPE_NO_SEAT};
    public static final int[] TICKET_TYPE_K_NUM = {150,100,100,50,50};

    public static final String TRAIN_TYPE_G = "G";   //高铁
    public static final String TRAIN_TYPE_C = "C";   //城际
    public static final String TRAIN_TYPE_D = "D";   //动车
    public static final String TRAIN_TYPE_Z = "Z";   //直达
    public static final String TRAIN_TYPE_T = "T";   //特快
    public static final String TRAIN_TYPE_K = "K";   //快速
    public static final String[] TRAIN_TYPE = {TRAIN_TYPE_G,TRAIN_TYPE_C,TRAIN_TYPE_D,TRAIN_TYPE_Z,TRAIN_TYPE_T,TRAIN_TYPE_K};
    public static final int[] TRAIN_TYPE_NUM = {20,2,2,10,6,8};

    public static final String[] HOT_CITY = {
            "北京","上海","天津","重庆","长沙","长春",
            "成都","福州","广州","贵阳","呼和浩特","哈尔滨",
            "合肥","杭州","海口","济南","昆明","拉萨",
            "兰州","南宁","南京","南昌","沈阳","石家庄",
            "太原","乌鲁木齐","武汉","西宁","西安","银川",
            "郑州","深圳","厦门"
    };
}

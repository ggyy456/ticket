package com.mjx.entity;

/**
 * Created by Administrator on 2017/9/15 0015.
 */
public interface ConstantTicket {
    public static final String TICKET_TYPE_FIRST = "FIRST";
    public static final String TICKET_TYPE_SECOND = "SECOND";
    public static final String TICKET_TYPE_BUSINESS = "BUSINESS";
    public static final String TICKET_TYPE_HARD = "HARD";
    public static final String TICKET_TYPE_SOFT = "SOFT";
    public static final String TICKET_TYPE_NO = "MO";
    public static final String[] TICKET_TYPE = {TICKET_TYPE_FIRST,TICKET_TYPE_SECOND,TICKET_TYPE_BUSINESS,TICKET_TYPE_HARD,TICKET_TYPE_SOFT,TICKET_TYPE_NO};

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

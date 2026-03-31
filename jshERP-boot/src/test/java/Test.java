import cn.hutool.core.date.DateUtil;

public class Test {

    public static void main(String args[]) {
        System.out.println(DateUtil.parse("2023-03-02", "yy-MM-dd"));
        System.out.println(DateUtil.format(DateUtil.parse("2023-03-02"), "yyMMdd"));
        ;

//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd");
//        String dateString = formatter.format(date);
//        System.out.println(dateString);
    }
}

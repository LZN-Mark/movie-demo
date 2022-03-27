package com.mark.run;

import com.mark.pojo.Business;
import com.mark.pojo.Customer;
import com.mark.pojo.Movie;
import com.mark.pojo.User;

import java.util.*;

public class MovieSystem {
    /**
     * 定义系统的数据容器用于存储数据
     */

    //1.存储很多用户（客户对象，商家对象）
    public static final List<User> ALL_USERS = new ArrayList<>();

    //2.存储系统全部商家和其排片信息
    public static final Map<Business,List<Movie>> ALL_MOVIES = new HashMap<>();

    //3.准备一个扫描器
    public static final Scanner SYS_SC = new Scanner(System.in);

    //定义一个静态的User类型的变量记住当前登录成功的用户对象
    public static User loginUser;

    /**
     * 准备一些测试数据
     */
    static {
        Customer c = new Customer();
        c.setLoginName("zyf888");
        c.setPassWord("123456");
        c.setUserName("黑马刘德华");
        c.setSex('男');
        c.setMoney(10000);
        c.setPhone("110110");
        ALL_USERS.add(c);

        Customer c1 = new Customer();
        c1.setLoginName("gzl888");
        c1.setPassWord("123456");
        c1.setUserName("黑马关之琳");
        c1.setSex('女');
        c1.setMoney(2000);
        c1.setPhone("111111");
        ALL_USERS.add(c1);

        Business b = new Business();
        b.setLoginName("baozugong888");
        b.setPassWord("123456");
        b.setUserName("黑马包租公");
        b.setMoney(0);
        b.setSex('男');
        b.setPhone("110110");
        b.setAddress("火星6号2B二层");
        b.setShopName("甜甜圈国际影城");
        ALL_USERS.add(b);
        // 注意，商家一定需要加入到店铺排片信息中去
        List<Movie> movies = new ArrayList<>();
        ALL_MOVIES.put(b , movies); // b = []

        Business b2 = new Business();
        b2.setLoginName("baozupo888");
        b2.setPassWord("123456");
        b2.setUserName("黑马包租婆");
        b2.setMoney(0);
        b2.setSex('女');
        b2.setPhone("110110");
        b2.setAddress("火星8号8B八层");
        b2.setShopName("巧克力国际影城");
        ALL_USERS.add(b2);
        // 注意，商家一定需要加入到店铺排片信息中去
        List<Movie> movies2 = new ArrayList<>();
        ALL_MOVIES.put(b2 , movies2); // b2 = []
    }

    public static void main(String[] args) {
        showMain();
    }

    /**
     * 首页展示
     */
    private static void showMain() {
        while (true) {
            System.out.println("==================电影首页=================");
            System.out.println("1.登录");
            System.out.println("2.用户注册");
            System.out.println("3.商家注册");
            System.out.println("请输入操作命令：");
            String command = SYS_SC.nextLine();
            switch (command){
                case "1" :
                    //登录功能
                    login();
                    break;
                case "2" :
                    break;
                case "3" :
                    break;
                default:
                    System.out.println("命令有误，请重新输入");
            }
        }
    }

    /**
     * 登录功能
     */
    private static void login() {
        while (true) {
            System.out.println("请输入用户名称：");
            String loginName = SYS_SC.nextLine();
            System.out.println("请输入密码：");
            String password = SYS_SC.nextLine();

            //根据登录用户名查询用户对象
            User user = getUserByLoginName(loginName);
            //判断用户对象是否存在，存在说明登录名称正确
            if(user != null){
                //用户对象存在，比对密码是否正确
                if(user.getPassWord().equals(password)){
                    loginUser = user;//记住登录的用户
                    //密码正确,登录成功
                    //判断登录的对象是用户还是商家
                    if(user instanceof Customer){
                        //是用户对象
                        showCustomerMain();
                    }else{
                        //是商家对象
                        showBusinessMain();
                    }
                    return;
                }else{
                    //密码不正确
                    System.out.println("您输入的密码不正确，请重新输入");
                }
            }else{
                //用户对象不存在
                System.out.println("该用户不存在，请重新输入");
            }
        }
    }

    /**
     * 商家操作界面
     */
    private static void showBusinessMain() {
        while (true) {
            System.out.println("==================商家界面===================");
            System.out.println(loginUser.getUserName() + (loginUser.getSex()=='男'? "先生":"女士" + "欢迎您进入系统"));
            System.out.println("1、展示详情:");
            System.out.println("2、上架电影:");
            System.out.println("3、下架电影:");
            System.out.println("4、修改电影:");
            System.out.println("5、退出:");

            System.out.println("请输入您要操作的命令：");
            String command = SYS_SC.nextLine();
            switch (command){
                case "1":
                    // 展示全部排片信息
                    break;
                case "2":
                    // 上架电影信息
                    break;
                case "3":
                    // 下架电影信息
                    break;
                case "4":
                    // 修改电影信息
                    break;
                case "5":
                    System.out.println(loginUser.getUserName() +"请您下次再来啊~~~");
                    return; // 干掉方法
                default:
                    System.out.println("不存在该命令！！");
                    break;
            }
        }
    }

    /**
     * 用户操作界面
     */
    private static void showCustomerMain() {
        while (true) {
            System.out.println("==================客户界面===================");
            System.out.println(loginUser.getUserName() + (loginUser.getSex()=='男'? "先生":"女士" + "欢迎您进入系统" +
                    "\t余额：" + loginUser.getMoney()));
            System.out.println("请您选择要操作的功能：");
            System.out.println("1、展示全部影片信息功能:");
            System.out.println("2、根据电影名称查询电影信息:");
            System.out.println("3、评分功能:");
            System.out.println("4、购票功能:");
            System.out.println("5、退出系统:");
            System.out.println("请输入您要操作的命令：");
            String command = SYS_SC.nextLine();
            switch (command){
                case "1":
                    // 展示全部排片信息
                    break;
                case "2":
                    break;
                case "3":
                    // 评分功能
                    break;
                case "4":
                    // 购票功能
                    break;
                case "5":
                    return; // 干掉方法
                default:
                    System.out.println("不存在该命令！！");
                    break;
            }
        }
    }

    private static User getUserByLoginName(String loginName) {
        for (User user : ALL_USERS) {
            //判断用户名是否是我们想要的
            if(user.getLoginName().equals(loginName)){
                return user;
            }
        }
        return null;//查无此用户
    }
}

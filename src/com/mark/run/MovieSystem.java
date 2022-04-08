package com.mark.run;

import com.mark.pojo.Business;
import com.mark.pojo.Customer;
import com.mark.pojo.Movie;
import com.mark.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    //自定义一个日期格式化的格式
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    //创建一个日志对象
    public static final Logger LOGGER = LoggerFactory.getLogger("MovieSystem.class");

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
                    LOGGER.info(loginUser.getLoginName()+"登录成功");
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
                    showBusinessInfos();
                    break;
                case "2":
                    // 上架电影信息
                    addMovie();
                    break;
                case "3":
                    // 下架电影信息
                    deleteMovie();
                    break;
                case "4":
                    // 修改电影信息
                    updateMovie();
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
     * 影片修改功能
     */
    private static void updateMovie() {
        System.out.println("===================影片修改=================");
        Business business = (Business) loginUser;
        List<Movie> movies = ALL_MOVIES.get(business);
        if(movies.size() == 0){
            System.out.println("当前无片可以修改");
            return;
        }else{
            //2.让用户选择需要修改的电影名称
            while (true) {
                System.out.println("请输入您想修改的电影名称");
                String movieName = SYS_SC.nextLine();
                for (Movie movie : movies) {
                    if (movie.getName().equals(movieName)){
                        //该影片存在
                        System.out.println("请您输入修改后的电影名:");
                        String name = SYS_SC.nextLine();
                        System.out.println("请您输入修改后的主演:");
                        String actor = SYS_SC.nextLine();
                        System.out.println("请您输入修改后的时长:");
                        String time = SYS_SC.nextLine();
                        System.out.println("请您输入修改后的票价:");
                        String price = SYS_SC.nextLine();
                        System.out.println("请您输入修改后的票数:");
                        String totalNumber = SYS_SC.nextLine();
                        while (true) {
                            try {
                                System.out.println("请您输入修改后的影片放映时间:");
                                String startTime = SYS_SC.nextLine();
                                movie.setName(name);
                                movie.setActor(actor);
                                movie.setTime(Double.valueOf(time));
                                movie.setPrice(Double.valueOf(price));
                                movie.setNumber(Integer.valueOf(totalNumber));
                                movie.setStartTime(sdf.parse(startTime));
                                System.out.println("恭喜您，您成功修改了该影片");
                                showBusinessInfos();
                                return;//退出方法
                            } catch (Exception e) {
                                e.printStackTrace();
                                LOGGER.error("时间解析出错");
                                System.out.println("请重新输入影片放映时间");
                            }
                        }
                    }
                }
                System.out.println("您要修改的影片不存在，请重新输入电影名");
            }
        }
    }

    /**
     * 影片下架功能
     */
    private static void deleteMovie() {
        System.out.println("===================影片下架=================");
        Business business = (Business) loginUser;
        List<Movie> movies = ALL_MOVIES.get(business);
        if(movies.size() == 0){
            System.out.println("当前无片可以下架");
            return;
        }else{
            //2.让用户选择需要下架的电影名称
            while (true) {
                System.out.println("请输入您想下架的电影名称");
                String movieName = SYS_SC.nextLine();
                for (Movie movie : movies) {
                    if (movie.getName().equals(movieName)){
                        //该影片存在
                        movies.remove(movie);
                        System.out.println("下架成功");
                        showBusinessInfos();
                        return;
                    }
                }
                System.out.println("您要下架的影片不存在，请重新输入电影名");
            }
        }
    }

    /**
     * 商家上架电影
     */
    private static void addMovie() {
        System.out.println("=======================上架电影=====================");
        Business business = (Business) loginUser;
        List<Movie> movies = ALL_MOVIES.get(business);
        System.out.println("请您输入电影名:");
        String name = SYS_SC.nextLine();
        System.out.println("请您输入主演:");
        String actor = SYS_SC.nextLine();
        System.out.println("请您输入时长:");
        String time = SYS_SC.nextLine();
        System.out.println("请您输入票价:");
        String price = SYS_SC.nextLine();
        System.out.println("请您输入票数:");
        String totalNumber = SYS_SC.nextLine();
        while (true) {
            try {
            System.out.println("请您输入影片放映时间:");
            String startTime = SYS_SC.nextLine();
                Movie movie = new Movie(name, actor, Double.valueOf(time), Double.valueOf(price), Integer.valueOf(totalNumber), sdf.parse(startTime));
                movies.add(movie);
                System.out.println("《"+movie.getName()+"》"+"已成功上架");
                return;//退出方法
            } catch (ParseException e) {
                e.printStackTrace();
                LOGGER.error("时间解析出错");
                System.out.println("请重新输入影片放映时间");
            }
        }

    }

    /**
     * 展示商家的详细信息(当前登录的商家)
     */
    private static void showBusinessInfos() {
        System.out.println("==================商家详情界面=================");
        LOGGER.info(loginUser.getLoginName()+"商家正在查看详情");
        //根据商家对象(loginUser就是登录的用户)，作为Map集合的键，提取对应的值就是排片信息：Map<Business,List<Movie>> ALL_MOVIES
        Business business = (Business) loginUser;
        System.out.println(business.getShopName() + "\t\t电话" +business.getPhone() + "\t\t地址:" +business.getAddress()+ "\t\t金额:" +business.getMoney());
        List<Movie> movies = ALL_MOVIES.get(loginUser);
        if (movies.size() > 0) {
            System.out.println("片名\t\t\t主演\t\t时长\t\t评分\t\t票价\t\t余票数量\t\t放映时间");
            for (Movie movie : movies) {
                System.out.println(movie.getName()+"\t\t\t"+movie.getActor()+"\t\t"+movie.getTime()+"\t\t"
                        + movie.getScore()+"\t\t\t"+movie.getPrice()+"\t\t\t"+movie.getNumber()+"\t\t\t"+sdf.format(movie.getStartTime()));

            }
        }else{
            System.out.println("您的店铺目前没有电影可以播放");
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
                    showAllMovies();
                    break;
                case "2":
                    break;
                case "3":
                    // 评分功能
                    break;
                case "4":
                    // 购票功能
                    buyMovie();
                    break;
                case "5":
                    return; // 干掉方法
                default:
                    System.out.println("不存在该命令！！");
                    break;
            }
        }
    }

    /**
     * 用户购票功能
     */
    private static void buyMovie() {
        showAllMovies();
        System.out.println("==========================用户购票功能=========================");
        while (true) {
            System.out.println("请您输入想要购票的门店：");
            String shopName = SYS_SC.nextLine();
            //1.查询是否存在该商家
            Business business = getBusinessByShopName(shopName);
            if (business == null){
                //商家不存在
                System.out.println("对不起，没有该店铺，请重新输入店铺名");
            }else{
                //商家存在，展示商家全部的排片
                List<Movie> movies = ALL_MOVIES.get(business);
                //判断是否有影片
                if(movies.size() == 0){
                    //无片
                    System.out.println("该影院没有排片");
                    System.out.println("是否继续买票？Y/N");
                    String command = SYS_SC.nextLine();
                    switch (command){
                        case "Y":
                            break;
                        default:
                            return;
                    }
                }else{
                    //有片,并开始进行选片购买
                    while (true) {
                        System.out.println("请输入需要购买的电影名称：");
                        String movieName = SYS_SC.nextLine();
                        //去当前商家下，查询电影对象
                        Movie m = getMovieByShopAndName(business, movieName);
                        if(m != null){
                            //有该电影，开始购买
                            while (true) {
                                System.out.println("请您输入想购买的电影票数");
                                String number = SYS_SC.nextLine();
                                Integer num = Integer.valueOf(number);
                                //判断电影票是否足够
                                if(m.getNumber() >= num){
                                    //有票
                                    //算出当前需要花费的金额
                                    Double money = BigDecimal.valueOf(num).multiply(BigDecimal.valueOf(m.getPrice())).doubleValue();
                                    System.out.println("您总共需要花费"+money+"元");
                                    //判断钱够不够
                                    if(loginUser.getMoney() >= money){
                                        //钱够
                                        while (true) {
                                            System.out.println("您的余额充足，是否购买 Y/N");
                                            String commad = SYS_SC.nextLine();
                                            if("Y".equals(commad)){
                                                System.out.println("您已经成功购买了《"+m.getName()+"》的电影票"+num+"张");
                                                loginUser.setMoney(loginUser.getMoney() - money);
                                                business.setMoney(business.getMoney() + money);
                                                m.setNumber(m.getNumber() - num);
                                                System.out.println("购买成功，您的余额还剩下"+loginUser.getMoney()+"元");
                                                return;
                                            }else if ("N".equals(commad)){
                                                return;
                                            }else {
                                                System.out.println("您输入的指令有误，请确认");
                                            }
                                        }
                                    }else{
                                        //钱不够
                                        System.out.println("抱歉，您的余额不足");
                                        System.out.println("是否继续买票？ Y/N");
                                        String commad = SYS_SC.nextLine();
                                        switch (commad){
                                            case "Y":
                                                break;
                                            default:
                                                return;
                                        }
                                    }
                                }else{
                                    //没票
                                    System.out.println("没那么多票,您当前最多可以购买"+m.getNumber()+"张");
                                    System.out.println("是否继续买票？ Y/N");
                                    String commad = SYS_SC.nextLine();
                                    switch (commad){
                                        case "Y":
                                            break;
                                        default:
                                            return;
                                    }
                                }
                            }

                        }else{
                            //没有该电影
                            System.out.println("该商家没有该电影");
                        }
                    }
                }
            }
        }
    }

    public static Movie getMovieByShopAndName(Business business,String name){
        List<Movie> movies = ALL_MOVIES.get(business);
        for (Movie movie : movies) {
            if (movie.getName(). contains(name)){
                return movie;
            }
        }
        return null;
    }

    /**
     * 根据店铺名称寻找商家
     * @return
     */
    public static Business getBusinessByShopName(String shopName){
        Set<Business> businesses = ALL_MOVIES.keySet();
        for (Business business : businesses) {
            if (business.getShopName().equals(shopName)){
                return business;
            }
        }
        return null;
    }

    /**
     * 用户功能:展示全部商家排片信息
     */
    private static void showAllMovies() {
        System.out.println("=========================全部商家排片信息========================");
        ALL_MOVIES.forEach((business, movies) -> {
            System.out.println(business.getShopName() + "\t\t电话" +business.getPhone() + "\t\t地址:" +business.getAddress());
            System.out.println("片名\t\t\t主演\t\t时长\t\t评分\t\t票价\t\t余票数量\t\t放映时间");
            for (Movie movie : movies) {
                System.out.println(movie.getName() + "\t\t\t" + movie.getActor() + "\t\t" + movie.getTime() + "\t\t"
                        + movie.getScore() + "\t\t\t" + movie.getPrice() + "\t\t\t" + movie.getNumber() + "\t\t\t" + sdf.format(movie.getStartTime()));
            }
        });
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

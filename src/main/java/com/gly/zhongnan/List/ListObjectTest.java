package com.gly.zhongnan.List;

import com.gly.zhongnan.entity.User;
import com.gly.zhongnan.utils.MyDateUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author EugeneGe
 * @date 2023-03-01 9:57
 */
public class ListObjectTest {
    public static void main(String[] args) {
        List<User> userList = new ArrayList<>();
        userList.add(User.builder().id(1).account("AA").CreateDate("2023-03-08").build());
        userList.add(User.builder().id(2).account("AA").CreateDate("2023-05-08").build());
        userList.add(User.builder().id(2).account("AA").CreateDate("2023-02-08").build());
        userList.add(User.builder().id(3).account("AB").CreateDate("2022-01-08").build());

//        unique(userList);
        objectMinOrMax(userList);
    }

    /**
     * List集合对象去重
     */
    public static void unique(List<User> userList) {

        // 方式一：单属性去重
        List<User> unique1 = userList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(User::getAccount))), ArrayList::new));
        System.out.println(unique1.size());

        //方式一：多属性去重
        List<User> unique2 = userList.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(
                        User::getId).thenComparing(User::getAccount))), ArrayList::new));
        System.out.println(unique2.size());
    }

    /**
     * List集合对象获取日期最小
     */
    public static void objectMinOrMax(List<User> userList) {
        Date minDate = userList.stream().map(P -> MyDateUtil.parseDate(P.getCreateDate())).min(Date::compareTo).get();
        Date maxDate = userList.stream().map(P -> MyDateUtil.parseDate(P.getCreateDate())).max(Date::compareTo).get();
        System.out.println(minDate);
    }
    /**
     *  方式二：去重后对象在集合中顺序跟原集合一样
     *  List对象去重，按照对象的某个字段去重，返回去重后新的对象集合
     *  使用方法：用Stream接口的 filter()接收为参数
     * */
//    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
//        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
//        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
//    }
//    // filter单属性去重
//    System.out.println(JSONObject.toJSONString(collect.stream().filter(distinctByKey(Test::getAid)).collect(Collectors.toList())));
//    // filter多属性去重
//    System.out.println(JSONObject.toJSONString(collect.stream().filter(distinctByKey(test -> Stream.of(test.getAid(),test.getUid()).toArray())).collect(Collectors.toList())));

}

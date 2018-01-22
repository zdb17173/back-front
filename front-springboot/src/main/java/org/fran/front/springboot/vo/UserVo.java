package org.fran.front.springboot.vo;

import java.util.List;

/**
 * Created by fran on 2018/1/22.
 */
public class UserVo {
    String name;
    int age;
    List<String> tag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }
}

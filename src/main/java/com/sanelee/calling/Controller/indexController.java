package com.sanelee.calling.Controller;

import com.sanelee.calling.Entity.User;
import com.sanelee.calling.Service.NumberService;
import com.sun.jmx.remote.internal.ArrayQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class indexController {
    /**
     * 定义一个LinkedHashMap实现排队取号数据结构
     */
    LinkedHashMap<String, User> userMap = new LinkedHashMap<>();
    List<User> userslist = new ArrayList<>();
    /**
     * 定义一个变量，每次取号就加1
     */
    int orderNumber=0;
    @Autowired
    private NumberService numberService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    /**
     * 取号注册页面
     * @return
     */
    @GetMapping("/register")
    public String register(){
        return "register";
    }

    /**
     * 取号注册方法
     * @param model
     * @param map
     * @param username 用户名
     * @param userAge 年龄
     * @param userGender 性别
     * @param userPhone 手机号
     * @return
     */
    @RequestMapping(value = "/getNumber",method = RequestMethod.POST)
    public synchronized String getNumber(Model model,
                                         Map<String,Object> map,
                                         @RequestParam(name = "username") String username,
                                         @RequestParam(name = "userAge") Integer userAge,
                                         @RequestParam(name = "userGender") String userGender,
                                         @RequestParam(name = "userPhone") String userPhone,
                                         @RequestParam(name = "r",required = false) String r,
                                         @RequestParam(name = "r1",required = false) String r1,
                                         @RequestParam(name = "r2",required = false) String r2,
                                         @RequestParam(name = "r3",required = false) String r3,
                                         @RequestParam(name = "r4",required = false) String r4,
                                         @RequestParam(name = "r5",required = false) String r5,
                                         @RequestParam(name = "r6",required = false) String r6,
                                         @RequestParam(name = "r7",required = false) String r7,
                                         @RequestParam(name = "r8",required = false) String r8,
                                         @RequestParam(name = "r9",required = false) String r9,
                                         @RequestParam(name = "r10",required = false) String r10,
                                         @RequestParam(name = "r11",required = false) String r11,
                                         @RequestParam(name = "sco",required = false) String sco) {
        if (r==null || r1==null || r2==null || r3==null ||
                r4==null || r5==null || r6==null ||r7==null || r8==null || r9==null || r10==null
                || r11==null || sco==null) {
            map.put("msg", "评测信息填写不完整，请重新填写挂号信息");
            return "index";
        } else {
        List<String> numList = new ArrayList();
        numList.add(r);
        numList.add(r1);
        numList.add(r2);
        numList.add(r3);
        numList.add(r4);
        numList.add(r5);
        numList.add(r6);
        numList.add(r7);
        numList.add(r8);
        numList.add(r9);
        numList.add(r10);
        numList.add(r11);
        numList.add(sco);
        int score = 0;
        for (int i = 0; i < numList.size(); i++) {
            score = score + Integer.parseInt(numList.get(i));
        }
        System.out.println(score);

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        orderNumber += 1;
        User user = new User();
        user.setOrderNumber(orderNumber);
        user.setName(username);
        user.setAge(userAge);
        user.setGender(userGender);
        user.setPhoneNumber(userPhone);
        user.setDateTime(dateFormat.format(date));
        user.setFlag(false);
        user.setScore(score);
        if (userMap.containsKey(userPhone)) {
            orderNumber -= 1;
            map.put("msg", "您今天已经取过号了");
            return "index";
        } else {
            userMap.put(user.getPhoneNumber(), user);
            model.addAttribute("userInfo", user);
            return "userInfo";
        }
    }
    }

    /**
     * 查询页面
     * @return
     */
    @GetMapping("/searchNumber")
    public String searchNumber(){
        return "searchNumber";
    }

    /**
     * 查询方法
     * @param model
     * @param map
     * @param userPhone 用户手机号
     * @return
     */
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public String search(Model model,
                         Map<String,Object> map,
                         @RequestParam(name = "userPhone",required = true) String userPhone,
                         @RequestParam(name = "userName",required = true) String userName){
        User user = userMap.get(userPhone);
        if (null == user){
            map.put("msg","您还没有挂号");
            return "index";
        }else if(!user.getName().equals(userName)){
            map.put("msg","姓名与手机号不匹配");
            return "index";
        }else {
            model.addAttribute("userInfo",user);
            return "userInfo";
        }
    }
    @GetMapping("/userInfo")
    public String userInfo(){
        return "userInfo";
    }

    @GetMapping("/nurseIndex")
    public String nurseIndex(){
        return "nurseIndex";
    }
    @GetMapping("/nurse")
    public String nurse(Model model){
        List<User> userList = new ArrayList();
        for (Map.Entry<String,User> entry: userMap.entrySet()){
            userList.add(entry.getValue());
        }
        model.addAttribute("userInfo",userList);
        model.addAttribute("usersInfo",userslist);
        return "nurse";
    }

    @GetMapping("/callNumber")
    public String callNumber(Model model,Map<String,Object> map){
        if (userMap.size()!=0){
        Map.Entry<String,User> next = userMap.entrySet().iterator().next();
        User user = next.getValue();
        model.addAttribute("userInfo",user);
        return "callNumber";
        }else {
            map.put("msg","当前没有挂号病人");
            return "nurseIndex";
        }
    }
    @RequestMapping("/nextUser")
    public String nextUser(Map<String,Object> map){
        Map.Entry<String, User> next = userMap.entrySet().iterator().next();
        userslist.add(next.getValue());
        userMap.remove(next.getKey());
        if (userMap.size()!=0){
            return "redirect:/callNumber";
        }else {
            map.put("msg","当前没有挂号病人");
            return "nurseIndex";
        }
    }

    @GetMapping("/reset")
    public String reset(Model model){
        model.addAttribute("usersInfo",userslist);
        return "reset";
    }
    @RequestMapping("/resetNumber")
    public String resetNumber(){
        orderNumber = 0;
        return "redirect:/reset";
    }

    @GetMapping("/DevReset")
    public String DevReset(Model model){
        List<User> userList = new ArrayList();
        for (Map.Entry<String,User> entry: userMap.entrySet()){
            userList.add(entry.getValue());
        }
        model.addAttribute("userInfo",userList);
        model.addAttribute("usersInfo",userslist);
        return "DevReset";
    }
    @RequestMapping("/resetUser")
    public String resetUser(){

        while (userMap.size() != 0){
            Map.Entry<String, User> next = userMap.entrySet().iterator().next();
            userMap.remove(next.getKey());
        }
        userslist.clear();
        orderNumber = 0;
        return "redirect:/DevReset";
    }
}

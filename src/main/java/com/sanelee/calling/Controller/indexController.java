package com.sanelee.calling.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sanelee.calling.Entity.User;
import com.sanelee.calling.Service.NumberService;
import com.sanelee.calling.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class indexController {
    @Autowired
    private UserMapper mapper;
    /**
     * 定义一个LinkedHashMap实现排队取号数据结构
     */
//    LinkedHashMap<String, User> userMap = new LinkedHashMap<>();
//    List<User> userslist = new ArrayList<>();
    /**
     * 定义一个变量，每次取号就加1
     */
    static volatile int orderNumber=0;
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        orderNumber += 1;
        User user = new User();
        user.setOrderNumber(orderNumber);
        user.setUsername(username);
        user.setUserAge(userAge);
        user.setGender(userGender);
        user.setPhoneNumber(userPhone);
        user.setDateTime(dateFormat.format(date));
        user.setFlag(0);
        user.setScore(score);
        List<User> userlist1 = mapper.findByPhone(userPhone);
        if (userlist1.size()!=0 && userlist1.get(0).getFlag()==0) {
            orderNumber -= 1;
            map.put("msg", "您今天已经取过号了");
            return "index";
        } else {
            mapper.insert(user);
//            userMap.put(user.getPhoneNumber(), user);
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
//        User user = userMap.get(userPhone);
        List<User> userlist = mapper.findByPhone(userPhone);
        if (userlist.size()==0){
            map.put("msg","您还没有挂号");
            return "index";
        }else if(!userlist.get(0).getUsername().equals(userName)){
            map.put("msg","姓名与手机号不匹配");
            return "index";
        }else {
            model.addAttribute("userInfo",userlist.get(0));
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

    /**
     *
     * @param model
     * @return
     */
    @GetMapping("/nurse")
    public String nurse(Model model){

        List<User> userList = mapper.findByFlag(0);
        List<User> userList1 = mapper.findByFlag(1);
//        for (Map.Entry<String,User> entry: userMap.entrySet()){
//            userList.add(entry.getValue());
//        }
        model.addAttribute("userInfo",userList);
        model.addAttribute("usersInfo",userList1);
//        model.addAttribute("usersInfo",userslist);
        return "nurse";
    }

    @GetMapping("/callNumber")
    public String callNumber(Model model,Map<String,Object> map){
        List<User> userList = mapper.findByFlag(0);
        if (userList.size()!=0){
//        Map.Entry<String,User> next = userMap.entrySet().iterator().next();
//        User user = next.getValue();
        User user = userList.get(0);
        model.addAttribute("userInfo",user);
        return "callNumber";
        }else {
            map.put("msg","当前没有挂号病人");
            return "nurseIndex";
        }
    }
    @RequestMapping("/nextUser")
    public String nextUser(Map<String,Object> map){
        List<User> userList = mapper.findByFlag(0);
        if (userList.size()!=0){
            User currentuser = userList.get(0);
            User user = mapper.selectById(currentuser.getId());
            user.setFlag(1);
            mapper.updateById(user);
            return "redirect:/callNumber";
        }
//        Map.Entry<String, User> next = userMap.entrySet().iterator().next();
//        User currentuser = next.getValue();
//        currentuser.setFlag(true);
//        User user = mapper.selectById(currentuser.getId());
//        user.setFlag(true);
//        mapper.updateById(user);
//        userslist.add(currentuser);
//        userMap.remove(next.getKey());
//        if (userMap.size()!=0){
//            return "redirect:/callNumber";
        else {
            map.put("msg","当前没有挂号病人");
            return "nurseIndex";
        }
    }

    @GetMapping("/reset")
    public String reset(Model model){
        List<User> userList = mapper.findByFlag(0);
        List<User> userList1 = mapper.findByFlag(1);
        model.addAttribute("userInfo",userList);
        model.addAttribute("usersInfo",userList1);
        return "reset";
    }
    @RequestMapping("/resetNumber")
    public String resetNumber(){
        orderNumber = 0;
        return "redirect:/reset";
    }

    @GetMapping("/DevReset")
    public String DevReset(Model model){
        List<User> userList = mapper.findByFlag(0);
        List<User> userList1 = mapper.findByFlag(1);
        model.addAttribute("userInfo",userList);
        model.addAttribute("usersInfo",userList1);
        return "DevReset";
    }

    /**
     * 重置所有用户信息
     * @return
     */
    @RequestMapping("/resetUser")
    public String resetUser(){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.ne("flag",3);
        mapper.delete(wrapper);
        orderNumber = 0;
        return "redirect:/DevReset";
    }
}

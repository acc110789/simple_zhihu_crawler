package me.zhangxl;

import me.zhangxl.entity.Person;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import me.zhangxl.utils.DBUtils;
import me.zhangxl.utils.HttpUtils;
import me.zhangxl.utils.QueueUtils;

import java.util.Set;

/**
 * Created by zhangxiaolong on 16/11/6.
 */
public class Worker implements Runnable {

    @Override
    public void run() {
        while (!QueueUtils.isEmpty()) {
            String url = QueueUtils.peek();
            String html = HttpUtils.getContentOfHttpGET(url);
            if (html == null) {
                System.err.println("no content in " + url);
                continue;
            }
            Document document = Jsoup.parse(html);
            Elements infos = document.select("div.body.clearfix");
            /**
             * 用户基本信息获取
             */
            //下面开始build一个用户的信息
            Person.PersonBuilder builder = Person.create(DBUtils.getMd5(url));
            //找头像的地址
            Elements temp = infos.select("img.Avatar");
            if (temp.size() > 0) {
                builder.setPhotoUrl(temp.get(0).attr("src"));
            }
            //name
            builder.setName(getText(infos, ".top .title-section .name"));
            //shortIntroduction 一句话描述
            builder.setShortIntroduction(getText(infos, ".top .title-section .bio"));
            //先解析个人简介吧
            builder.setDescription(getText(infos,
                    ".zm-profile-header-description .info-wrap .unfold-item .content"));
            //location
            builder.setLocation(getItem(infos, "location"));
            //business
            builder.setBusiness(getItem(infos, "business"));
            //gender 这个稍微有点特殊
            temp = infos.select("span.item.gender i");
            if (temp.size() > 0) {
                Set<String> classNames = temp.get(0).classNames();
                if (classNames.contains("icon-profile-male")) {
                    builder.setMale();
                } else if (classNames.contains("icon-profile-female")) {
                    builder.setFemale();
                }
            }
            //employment
            builder.setEmployment(getItem(infos, "employment"));
            //position
            builder.setPosition(getItem(infos, "position"));
            //education
            builder.setEducation(getItem(infos, "education"));
            //educationExtra
            builder.setEducationExtra(getItem(infos, "education-extra"));

            /**
             * 关注数量和被关注数量
             */
            infos = document.select("div.zu-main-sidebar div.zm-profile-side-following.zg-clear .item");

            if (infos.size() > 0) {
                String followee = getText(infos.get(0), "strong");
                if (!StringUtils.isEmpty(followee)) {
                    builder.setFolloweeNum(Integer.valueOf(followee));
                }
            }
            if (infos.size() > 1) {
                String follower = getText(infos.get(1), "strong");
                if (!StringUtils.isEmpty(follower)) {
                    builder.setFollwerNum(Integer.valueOf(follower));
                }
            }

            /**
             * 下面是赞同数和感谢数量
             */
            String agreeNum = getText(document, "body > div.zg-wrap.zu-main.clearfix > " +
                    "div.zu-main-content > div > div.zm-profile-header.ProfileCard > " +
                    "div.zm-profile-header-operation.zg-clear > " +
                    "div.zm-profile-header-info-list > span.zm-profile-header-user-agree > " +
                    "strong");
            if (!StringUtils.isEmpty(agreeNum)) {
                builder.setAgreeNum(Integer.valueOf(agreeNum));
            }
            String thanksNum = getText(document, "body > div.zg-wrap.zu-main.clearfix > " +
                    "div.zu-main-content > div > div.zm-profile-header.ProfileCard > " +
                    "div.zm-profile-header-operation.zg-clear > div.zm-profile-header-info-list > " +
                    "span.zm-profile-header-user-thanks > strong");
            if (!StringUtils.isEmpty(thanksNum)) {
                builder.setThanksNum(Integer.valueOf(thanksNum));
            }

            /**
             * 提问、回答、文章、收藏、公共编辑数量
             */
            String askNum = getText(document, "body > div.zg-wrap.zu-main.clearfix > " +
                    "div.zu-main-content > div > div.zm-profile-header.ProfileCard > " +
                    "div.profile-navbar.clearfix > a:nth-child(2) > span");
            if (!StringUtils.isEmpty(askNum)) {
                builder.setAskNum(Integer.valueOf(askNum));
            }
            String answerNum = getText(document, "body > div.zg-wrap.zu-main.clearfix > " +
                    "div.zu-main-content > div > div.zm-profile-header.ProfileCard > " +
                    "div.profile-navbar.clearfix > a:nth-child(3) > span");
            if (!StringUtils.isEmpty(answerNum)) {
                builder.setAnswerNum(Integer.valueOf(answerNum));
            }
            String postNum = getText(document, "body > div.zg-wrap.zu-main.clearfix > " +
                    "div.zu-main-content > div > div.zm-profile-header.ProfileCard > " +
                    "div.profile-navbar.clearfix > a:nth-child(4) > span");
            if (!StringUtils.isEmpty(postNum)) {
                builder.setPostNum(Integer.valueOf(postNum));
            }
            String collectionNum = getText(document, "body > div.zg-wrap.zu-main.clearfix > " +
                    "div.zu-main-content > div > div.zm-profile-header.ProfileCard > " +
                    "div.profile-navbar.clearfix > a:nth-child(5) > span");
            if (!StringUtils.isEmpty(collectionNum)) {
                builder.setCollectionNum(Integer.valueOf(collectionNum));
            }
            String logNum = getText(document, "body > div.zg-wrap.zu-main.clearfix > " +
                    "div.zu-main-content > div > div.zm-profile-header.ProfileCard > " +
                    "div.profile-navbar.clearfix > a:nth-child(6) > span");
            if (!StringUtils.isEmpty(logNum)) {
                builder.setLogNum(Integer.valueOf(logNum));
            }
            Person person = builder.build();
            //person build出来了，下面插进数据库
            if (!DBUtils.containUrl(url)) {
                if (DBUtils.insertData(person)) {
                    System.out.println("person[" + person.briefString() + "] insert success!");
                }
            }

            //放入新的连接
            Elements tmp = document.select("#zh-profile-follows-list > " +
                    "div div.zm-profile-card.zm-profile-section-item");
            for (Element element : tmp) {
                if (QueueUtils.full()) {
                    break;
                }
                Elements current = element.select("span.author-link-line a");
                if (current.size() > 0) {
                    String tmpUrl = current.get(0).attr("href") + "/followees";
                    if (!DBUtils.containUrl(tmpUrl)) {
                        QueueUtils.offer(tmpUrl);
                    }
                }
            }
            //等把上面的数据全部解析完毕之后，就可以把这条url弹出去了
            QueueUtils.poll();
        }
    }

    private static String getItem(Elements infos, String target) {
        return getText(infos, "span." + target + ".item");
    }

    private static String getText(Elements infos, String target) {
        Element info = infos.select(target).first();
        return info == null ? null : info.text();
    }

    private static String getText(Element infos, String target) {
        Element info = infos.select(target).first();
        return info == null ? null : info.text();
    }

}

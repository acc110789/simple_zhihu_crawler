package me.zhangxl.entity;

/**
 * Created by zhangxiaolong on 16/11/3.
 */
public class Person {
    //一共21个属性

    //就是这个用户信息的url的md5散列
    private final String id;

    private final String name;
    //头像的链接
    private final String photoUrl;
    //一句话介绍
    private final String shortIntroduction;

    //位置
    private final String location;
    //行业
    private final String business;
    //性别
    private final Gender gender;

    //公司
    private final String employment;
    //职位
    private final String position;

    //学校
    private final String education;
    //专业
    private final String educationExtra;

    //个人简介
    private final String description;

    //获得赞同数量
    private final Integer agreeNum;
    //获得感谢数量
    private final Integer thanksNum;

    //提问数量
    private final Integer askNum;
    //回答数量
    private final Integer answerNum;
    //文章数量
    private final Integer postNum;
    //收藏数量
    private final Integer collectionNum;
    //公共编辑数量
    private final Integer logNum;

    //关注的人数
    private final Integer followeeNum;
    //关注本人的人数
    private final Integer follwerNum;

    public String briefString() {
        return "id:" + id +
                "   ,name:" + name +
                "   ,location:" + location +
                "   ,business:" + business +
                "   ,gender:" + gender +
                "   ,employment:" + employment +
                "   ,position:" + position +
                "   ,education:" + education +
                "   ,educationExtra:" + educationExtra;
    }

    @Override
    public String toString() {
        return "id:" + id +
                ",name:" + name +
                ",photoUrl:" + photoUrl +
                ",shortIntroduction:" + shortIntroduction +
                ",location:" + location +
                ",business:" + business +
                ",gender:" + gender +
                ",employment:" + employment +
                ",position:" + position +
                ",education:" + education +
                ",educationExtra:" + educationExtra +
                ",description:" + description +
                ",agreeNum:" + agreeNum +
                ",thanksNum:" + thanksNum +
                ",askNum:" + askNum +
                ",answerNum:" + answerNum +
                ",postNum:" + postNum +
                ",collectionNum:" + collectionNum +
                ",logNum:" + logNum +
                ",followeeNum:" + followeeNum +
                ",follwerNum:" + follwerNum;
    }

    private Person(String id, String name, String photoUrl, String shortIntroduction,
                   String location, String business, Gender gender,
                   String employment, String position, String education,
                   String educationExtra, String description,
                   Integer agreeNum, Integer thanksNum, Integer askNum,
                   Integer answerNum, Integer postNum, Integer collectionNum,
                   Integer logNum, Integer followeeNum, Integer follwerNum) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.shortIntroduction = shortIntroduction;
        this.location = location;
        this.business = business;
        this.gender = gender;
        this.employment = employment;
        this.position = position;
        this.education = education;
        this.educationExtra = educationExtra;
        this.description = description;
        this.agreeNum = agreeNum;
        this.thanksNum = thanksNum;
        this.askNum = askNum;
        this.answerNum = answerNum;
        this.postNum = postNum;
        this.collectionNum = collectionNum;
        this.logNum = logNum;
        this.followeeNum = followeeNum;
        this.follwerNum = follwerNum;
    }

    public static class PersonBuilder {
        private String id;
        private String name;
        private String photoUrl;
        private String shortIntroduction;
        private String location;
        private String business;
        private Gender gender;
        private String employment;
        private String position;
        private String education;
        private String educationExtra;
        private String description;
        private Integer agreeNum;
        private Integer thanksNum;
        private Integer askNum;
        private Integer answerNum;
        private Integer postNum;
        private Integer collectionNum;
        private Integer logNum;
        private Integer followeeNum;
        private Integer follwerNum;

        public PersonBuilder(String id) {
            this.id = id;
        }

        public PersonBuilder setGender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public PersonBuilder setAgreeNum(Integer agreeNum) {
            this.agreeNum = agreeNum;
            return this;
        }

        public PersonBuilder setThanksNum(Integer thanksNum) {
            this.thanksNum = thanksNum;
            return this;
        }

        public PersonBuilder setAskNum(Integer askNum) {
            this.askNum = askNum;
            return this;
        }

        public PersonBuilder setAnswerNum(Integer answerNum) {
            this.answerNum = answerNum;
            return this;
        }

        public PersonBuilder setPostNum(Integer postNum) {
            this.postNum = postNum;
            return this;
        }

        public PersonBuilder setCollectionNum(Integer collectionNum) {
            this.collectionNum = collectionNum;
            return this;
        }

        public PersonBuilder setLogNum(Integer logNum) {
            this.logNum = logNum;
            return this;
        }

        public PersonBuilder setFolloweeNum(Integer followeeNum) {
            this.followeeNum = followeeNum;
            return this;
        }

        public PersonBuilder setFollwerNum(Integer follwerNum) {
            this.follwerNum = follwerNum;
            return this;
        }

        public PersonBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public PersonBuilder setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
            return this;
        }

        public PersonBuilder setShortIntroduction(String shortIntroduction) {
            this.shortIntroduction = shortIntroduction;
            return this;
        }

        public PersonBuilder setLocation(String location) {
            this.location = location;
            return this;
        }

        public PersonBuilder setBusiness(String business) {
            this.business = business;
            return this;
        }

        public PersonBuilder setFemale() {
            this.gender = Gender.female;
            return this;
        }

        public PersonBuilder setMale() {
            this.gender = Gender.male;
            return this;
        }

        public PersonBuilder setEmployment(String employment) {
            this.employment = employment;
            return this;
        }

        public PersonBuilder setPosition(String position) {
            this.position = position;
            return this;
        }

        public PersonBuilder setEducation(String education) {
            this.education = education;
            return this;
        }

        public PersonBuilder setEducationExtra(String educationExtra) {
            this.educationExtra = educationExtra;
            return this;
        }

        public PersonBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Person build() {
            return new Person(id, name, photoUrl, shortIntroduction,
                    location, business, gender,
                    employment, position, education,
                    educationExtra, description,
                    agreeNum, thanksNum, askNum,
                    answerNum, postNum, collectionNum,
                    logNum, followeeNum, follwerNum);
        }
    }

    public static PersonBuilder create(String id) {
        return new PersonBuilder(id);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getShortIntroduction() {
        return shortIntroduction;
    }

    public String getLocation() {
        return location;
    }

    public String getBusiness() {
        return business;
    }

    public Gender getGender() {
        return gender;
    }

    public String getGenderAsString(){
        return gender == null ? null : gender.toString();
    }

    public String getEmployment() {
        return employment;
    }

    public String getPosition() {
        return position;
    }

    public String getEducation() {
        return education;
    }

    public String getEducationExtra() {
        return educationExtra;
    }

    public String getDescription() {
        return description;
    }

    private <T> T getNotNullOrDefault(T value,T defalut){
        return value == null ? defalut : value;
    }

    private Integer getNonNullOrZero(Integer value){
        return getNotNullOrDefault(value,0);
    }

    public Integer getAgreeNum() {
        return getNonNullOrZero(agreeNum);
    }

    public Integer getThanksNum() {
        return getNonNullOrZero(thanksNum);
    }

    public Integer getAskNum() {
        return getNonNullOrZero(askNum);
    }

    public Integer getAnswerNum() {
        return getNonNullOrZero(answerNum);
    }

    public Integer getPostNum() {
        return getNonNullOrZero(postNum);
    }

    public Integer getCollectionNum() {
        return getNonNullOrZero(collectionNum);
    }

    public Integer getLogNum() {
        return getNonNullOrZero(logNum);
    }

    public Integer getFolloweeNum() {
        return getNonNullOrZero(followeeNum);
    }

    public Integer getFollwerNum() {
        return getNonNullOrZero(follwerNum);
    }
}

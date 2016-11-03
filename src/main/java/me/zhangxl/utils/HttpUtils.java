package me.zhangxl.utils;

import me.zhangxl.config.AppConfig;
import me.zhangxl.config.ZhihuConfig;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by zhangxiaolong on 16/11/4.
 */
public class HttpUtils {

    private static final String SCHEME = "https";
    private static final String HOST = "www.zhihu.com";
    private static final String CAPTCHA_PATH = "/captcha.gif";
    private static final CloseableHttpClient client = HttpClients.createDefault();
    private static final HttpClientContext context = HttpClientContext.create();

    public static void init(){
        context.setRequestConfig(RequestConfig.custom()
                .setCookieSpec(CookieSpecs.STANDARD).build());
        populateCookie();
    }

    private static void populateCookie() {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(AppConfig.COOKIE_STORE_PATH);
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                context.setCookieStore((CookieStore) objectInputStream.readObject());
            } catch (Exception e) {
                login();
            }
        } catch (FileNotFoundException e) {
            login();
        }

    }

    private static void login(){
        //先访问 https://www.zhihu.com/question/52259604  获取必要的cookie
        try {
            getContentOfHttpGET(new URIBuilder()
                    .setScheme(SCHEME)
                    .setHost(HOST)
                    .setPath("/question/52259604").build());
            if(StringUtils.isEmpty(ZhihuConfig.password)){
                throw new IllegalStateException("password in zhihu_account.properties is empty");
            }
            if(StringUtils.isEmpty(ZhihuConfig.username)){
                throw new IllegalStateException("username in zhihu_account.properties is empty");
            }
            final String requestPath;
            BasicNameValuePair account;
            if(StringUtils.isNumeric(ZhihuConfig.username)){
                //是电话号码
                requestPath = "/login/phone_num";
                account = new BasicNameValuePair("phone_num",ZhihuConfig.username);
            } else if(ZhihuConfig.username.contains("@")){
                requestPath = "/login/email";
                account = new BasicNameValuePair("email",ZhihuConfig.username);
            } else {
                throw new IllegalStateException("username 不是电子邮件也不是电话号码");
            }

            //自此，账号有了，path有了，密码有了，remember_me默认设置为true
            //下面就是获取验证码了
            String captcha = getCaptcha();

            URI uri = new URIBuilder()
                    .setScheme(SCHEME)
                    .setHost(HOST)
                    .setPath(requestPath)
                    .build();
            HttpPost post = new HttpPost(uri);
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("remember_me","true"));
            list.add(account);
            list.add(new BasicNameValuePair("captcha",captcha));
            list.add(new BasicNameValuePair("password",ZhihuConfig.password));
            post.setEntity(EntityBuilder.create().setParameters(list).build());

            ResponseHandler<JSONObject> handler = new ResponseHandler<JSONObject>() {
                public JSONObject handleResponse(HttpResponse response) throws IOException {
                    return new JSONObject(EntityUtils.toString(response.getEntity()));
                }
            };
            JSONObject array = client.execute(post,handler,context);
            if(array.has("r") && array.getInt("r") == 0){
                System.out.println("登录成功");
                //登录成功之后保存一下cookie
                storeCookie();
            } else {
                System.out.println(array.getString("msg"));
            }
        } catch (Exception e) {
            throw new IllegalStateException("login error",e);
        }
    }

    public static void store(){
        storeCookie();
    }

    //在程序退出的时候将cookieStore和还没有访问的url持久化
    private static void storeCookie() {
        File file = new File(AppConfig.COOKIE_STORE_PATH);
        if(!file.exists()){
            file.getParentFile().mkdirs();
            FileOutputStream outputStream = null;
            ObjectOutputStream objectOutputStream = null;
            try {
                file.createNewFile();
                outputStream = new FileOutputStream(file);
                objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(context.getCookieStore());
            } catch (IOException e) {
                throw new IllegalStateException("can not create file");
            } finally {
                IOUtils.closeQuietly(objectOutputStream);
                IOUtils.closeQuietly(outputStream);
            }
        }
    }

    /**
     * 保存验证码文件
     */
    private static String getCaptcha()  {
        try {
            URI uri = new URIBuilder()
                    .setScheme(SCHEME)
                    .setHost(HOST)
                    .setPath(CAPTCHA_PATH)
                    .setParameter("type","login")
                    .build();
            HttpGet get = new HttpGet(uri);
            ResponseHandler<Void> handler = new ResponseHandler<Void>() {
                public Void handleResponse(HttpResponse response) throws IOException {
                    File file = new File(AppConfig.CAPTCHA_FILE);
                    if(!file.exists()){
                        File parent = file.getParentFile();
                        if(!parent.exists()){
                            parent.mkdirs();
                        }
                        file.createNewFile();
                    }
                    OutputStream outputStream = new FileOutputStream(file);
                    try {
                        IOUtils.copy(response.getEntity().getContent(), outputStream);
                    } finally {
                        IOUtils.closeQuietly(response.getEntity().getContent());
                        IOUtils.closeQuietly(outputStream);
                    }
                    return null;
                }
            };
            client.execute(get,handler,context);
            System.out.println("请输入验证码:");
            return new Scanner(System.in).nextLine();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }

    public static String getContentOfHttpGET(String src){
        try {
            return getContentOfHttpGET(new URI(src));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getContentOfHttpGET(URI uri) throws IOException {
        final HttpGet request = new HttpGet(uri);
        ResponseHandler<String> handler = new ResponseHandler<String>() {
            public String handleResponse(HttpResponse response) throws IOException {
                return EntityUtils.toString(response.getEntity());
            }
        };
        return client.execute(request,handler,context);
    }

    public static void main(String[] args){
        init();
    }
}

package com.wip.utils;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;

import java.util.Objects;

public class SMSUtil {
    public static class SMSBuilder {
        private Credential cred;
        private ClientProfile clientProfile;
        private SmsClient client;

        public SMSBuilder buildCredential(String secretId, String secretKey) {
            if (Objects.isNull(cred)) {
                cred = new Credential("", "");
            }
            cred.setSecretId(secretId);
            cred.setSecretKey(secretKey);
            return this;
        }

        public SMSBuilder buildClientProfile(String signMethod, HttpProfile httpProfile) {
            if (Objects.isNull(clientProfile)) {
                clientProfile = new ClientProfile();
            }
            clientProfile.setSignMethod(signMethod);
            clientProfile.setHttpProfile(httpProfile);
            return this;
        }

        public SMSBuilder buildClientProfile() {
            if (Objects.isNull(clientProfile)) {
                clientProfile = new ClientProfile();
            }
            return this;
        }

        public SmsClient buildClient(String region) {
            if (Objects.isNull(client)) {
                client = new SmsClient(cred, "", clientProfile);
            }
            return this.client;
        }

        public RequestBuilder requestBuilder() {
            return new RequestBuilder();
        }

        public class RequestBuilder {
            private SendSmsRequest req;

            /**
             * 填充请求参数,这里request对象的成员变量即对应接口的入参
             * 你可以通过官网接口文档或跳转到request对象的定义处查看请求参数的定义
             * 基本类型的设置:
             * 帮助链接：
             * 短信控制台: https://console.cloud.tencent.com/sms/smslist
             * sms helper: https://cloud.tencent.com/document/product/382/3773
             *
             * @param appId          短信应用ID: 短信SdkAppid在 [短信控制台] 添加应用后生成的实际SdkAppid，示例如1400006666
             * @param sign           短信签名内容: 使用 UTF-8 编码，必须填写已审核通过的签名，签名信息可登录 [短信控制台] 查看
             * @param templateID     模板 ID: 必须填写已审核通过的模板 ID。模板ID可登录 [短信控制台] 查看
             * @param templateParams 模板参数: 若无模板参数，则设置为空
             * @return RequestBuilder
             */
            public RequestBuilder buildBaseConfig(String appId, String sign, String templateID, String... templateParams) {
                req = new SendSmsRequest();
                req.setSmsSdkAppid(appId);
                req.setSign(sign);
                req.setTemplateID(templateID);
                req.setTemplateParamSet(templateParams);
                return this;
            }

            /**
             * @param senderId   国际/港澳台短信 senderId: 国内短信填空，默认未开通，如需开通请联系 [sms helper]
             * @param session    用户的 session 内容: 可以携带用户侧 ID 等上下文信息，server 会原样返回
             * @param extendCode 短信码号扩展号: 默认未开通，如需开通请联系 [sms helper]
             * @return RequestBuilder
             */
            public RequestBuilder buildMoreConfig(String senderId, String session, String extendCode) {
                if (Objects.isNull(req)) {
                    req = new SendSmsRequest();
                }
                req.setSenderId(senderId);
                req.setSessionContext(session);
                req.setExtendCode(extendCode);
                return this;
            }

            /**
             * @param phoneNumbers 下发手机号码，采用 e.164 标准，+[国家或地区码][手机号]
             *                     示例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号，最多不要超过200个手机号
             * @return
             */
            public SendSmsRequest buildRequest(String... phoneNumbers) {
                if (Objects.isNull(req)) {
                    req = new SendSmsRequest();
                }
                req.setPhoneNumberSet(phoneNumbers);
                return req;
            }
        }
    }

    public static SendSmsResponse SendSms(SmsClient smsClient, SendSmsRequest sendSmsRequest) throws TencentCloudSDKException {
        return smsClient.SendSms(sendSmsRequest);
    }

    public static SmsClient defaultClient() {

        SMSBuilder smsBuilder = new SMSBuilder();
        String secretId = System.getenv("txCloudSecretId");
        String secretKey = System.getenv("txCloudSecretKey");
        return smsBuilder.buildCredential(secretId, secretKey).buildClientProfile().buildClient("");
    }

    public static SendSmsRequest defaultRequest(String verifyCode, String effortTime, String... phoneNumber) {
        String appId = "1400393014";
        String sign = "lyn学习记录";
        String templateID = "652707";
        SMSBuilder smsBuilder = new SMSBuilder();
        return smsBuilder.requestBuilder().buildBaseConfig(appId, sign, templateID, verifyCode, effortTime).buildRequest(phoneNumber);
    }

    public static void main(String[] args) throws TencentCloudSDKException {

        SendSmsResponse sendSmsResponse = SendSms(defaultClient(), defaultRequest("0", "5", "+8618582690212"));
        // 输出json格式的字符串回包
        System.out.println(SendSmsResponse.toJsonString(sendSmsResponse));

        // 也可以取出单个值，你可以通过官网接口文档或跳转到response对象的定义处查看返回字段的定义
        System.out.println(sendSmsResponse.getRequestId());
    }

    private static void demo() {
        try {

            /* 必要步骤：
             * 实例化一个认证对象，入参需要传入腾讯云账户密钥对secretId，secretKey。
             * 这里采用的是从环境变量读取的方式，需要在环境变量中先设置这两个值。
             * 你也可以直接在代码中写死密钥对，但是小心不要将代码复制、上传或者分享给他人，
             * 以免泄露密钥对危及你的财产安全。
             * CAM密匙查询: https://console.cloud.tencent.com/cam/capi*/
            String secretId = System.getProperty("txCloudSecretId");
            String secretKey = System.getProperty("txCloudSecretKey");
            Credential cred = new Credential(secretId, secretKey);

            // 实例化一个http选项，可选，没有特殊需求可以跳过
            // HttpProfile httpProfile = new HttpProfile();
            // // 设置代理
            // httpProfile.setProxyHost("host");
            // int port=80;
            // httpProfile.setProxyPort(port);
            // /* SDK默认使用POST方法。
            //  * 如果你一定要使用GET方法，可以在这里设置。GET方法无法处理一些较大的请求 */
            // httpProfile.setReqMethod("POST");
            // /* SDK有默认的超时时间，非必要请不要进行调整
            //  * 如有需要请在代码中查阅以获取最新的默认值 */
            // httpProfile.setConnTimeout(60);
            // /* SDK会自动指定域名。通常是不需要特地指定域名的，但是如果你访问的是金融区的服务
            //  * 则必须手动指定域名，例如sms的上海金融区域名： sms.ap-shanghai-fsi.tencentcloudapi.com */
            // httpProfile.setEndpoint("sms.tencentcloudapi.com");
            //
            // /* 非必要步骤:
            //  * 实例化一个客户端配置对象，可以指定超时时间等配置 */
            ClientProfile clientProfile = new ClientProfile();
            // /* SDK默认用TC3-HMAC-SHA256进行签名
            //  * 非必要请不要修改这个字段 */
            clientProfile.setSignMethod("HmacSHA256");
            // clientProfile.setHttpProfile(httpProfile);
            /* 实例化要请求产品(以sms为例)的client对象
             * 第二个参数是地域信息，可以直接填写字符串ap-guangzhou，或者引用预设的常量 */
            SmsClient client = new SmsClient(cred, "", clientProfile);
            /* 实例化一个请求对象，根据调用的接口和实际情况，可以进一步设置请求参数
             * 你可以直接查询SDK源码确定接口有哪些属性可以设置
             * 属性可能是基本类型，也可能引用了另一个数据结构
             * 推荐使用IDE进行开发，可以方便的跳转查阅各个接口和数据结构的文档说明 */
            SendSmsRequest req = new SendSmsRequest();

            /* 填充请求参数,这里request对象的成员变量即对应接口的入参
             * 你可以通过官网接口文档或跳转到request对象的定义处查看请求参数的定义
             * 基本类型的设置:
             * 帮助链接：
             * 短信控制台: https://console.cloud.tencent.com/sms/smslist
             * sms helper: https://cloud.tencent.com/document/product/382/3773 */

            /* 短信应用ID: 短信SdkAppid在 [短信控制台] 添加应用后生成的实际SdkAppid，示例如1400006666 */
            String appid = "1400393014";
            /* 短信签名内容: 使用 UTF-8 编码，必须填写已审核通过的签名，签名信息可登录 [短信控制台] 查看 */
            String sign = "lyn学习记录";
            /* 模板 ID: 必须填写已审核通过的模板 ID。模板ID可登录 [短信控制台] 查看 */
            String templateID = "652707";


            req.setSmsSdkAppid(appid);
            req.setSign(sign);
            req.setTemplateID(templateID);

            /* 国际/港澳台短信 senderid: 国内短信填空，默认未开通，如需开通请联系 [sms helper] */
            String senderid = "";
            req.setSenderId(senderid);

            /* 用户的 session 内容: 可以携带用户侧 ID 等上下文信息，server 会原样返回 */
            String session = "";
            req.setSessionContext(session);

            /* 短信码号扩展号: 默认未开通，如需开通请联系 [sms helper] */
            String extendcode = "";
            req.setExtendCode(extendcode);



            /* 下发手机号码，采用 e.164 标准，+[国家或地区码][手机号]
             * 示例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号，最多不要超过200个手机号*/
            String[] phoneNumbers = {"+8618283240432"};
            req.setPhoneNumberSet(phoneNumbers);

            /* 模板参数: 若无模板参数，则设置为空*/
            String[] templateParams = {"2500", "5"};
            req.setTemplateParamSet(templateParams);

            /* 通过 client 对象调用 SendSms 方法发起请求。注意请求方法名与请求对象是对应的
             * 返回的 res 是一个 SendSmsResponse 类的实例，与请求对象对应 */
            SendSmsResponse res = client.SendSms(req);

            // 输出json格式的字符串回包
            System.out.println(SendSmsResponse.toJsonString(res));

            // 也可以取出单个值，你可以通过官网接口文档或跳转到response对象的定义处查看返回字段的定义
            System.out.println(res.getRequestId());

        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
    }
}

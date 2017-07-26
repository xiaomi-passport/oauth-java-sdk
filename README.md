# 小米帐号开放平台 OAuth JAVA SDK 使用说明

说明：本文档适用于 1.0 以上 sdk 版本，历史版本可以 [点此查看](https://github.com/xiaomi-passport/oauth-java-sdk/wiki/v0.0.1)。

> - jdk version : 1.6+
> - maven version : 3.0+

## 一. SDK 结构说明
> - com.xiaomi.passport.api.AuthorizationCodeGrantHelper ： 授权码模式授权类
> - com.xiaomi.passport.api.ImplicitGrantHelper ： 隐式授权模式授权类
> - com.xiaomi.passport.api.RefreshAccessTokenHelper ： 刷新访问令牌操作类
> - com.xiaomi.passport.api.OpenApiHelper : 开放数据接口操作类

## 二. SDK 使用说明

### 2.1 授权接口

小米帐号开放针对第三方 APP 支持 __授权码模式__ 和 __隐式授权模式__ 两种授权方式，两种模式的区别请参考 ‘[OAuth2.0协议原理与实现：协议原理](https://dev.mi.com/console/doc/detail?pId=711)’。简而言之，如果您的 APP 具备服务端，那么授权码模式将是您的最佳选择。

#### 2.1.1 授权码模式授权

授权码模式 SDK 接口位于 com.xiaomi.passport.api.AuthorizationCodeGrantHelper 类中，接口使用说明如下：

- __获取授权码 : getAuthorizationCode__

获取授权码授权码模式两步流程的第一步，因为该过程需要与用户交互（即询问用户是否确认授权），所以整个过程无法仅仅通过程序完成。我们在方法中仅仅描述了获取授权码的概要流程，并推荐您参考 ‘[授权码授权模式](https://dev.mi.com/console/doc/detail?pId=707)’ 接口 1 中的说明构建请求地址，请求过程中需要用户确认授权，我们最终会以参数的形式将授权码追加到回调地址后面，并以 302 回调形式下发。

- __获取访问令牌 : getAccessTokenByCode__

在您通过上一步拿到授权之后，接下去可以通过授权码请求小米授权服务器下发访问令牌，SDK 使用示例：

```java
// 构建您的 APP 信息
Client client = new Client();
// 构建授权请求实例
AuthorizationCodeGrantHelper helper = new AuthorizationCodeGrantHelper(client);
// 利用授权码请求下发访问令牌
AccessToken accessToken = helper.getAccessTokenByCode(code);
```

如果请求成功，则会返回 `AccessToken` 对象，对象属性含义可以参考 ‘[授权码授权模式](https://dev.mi.com/console/doc/detail?pId=707)’ 接口 2 中的释义。

如果请求失败，则会抛出 `OAuthSdkException`，异常中包含响应的错误码和描述信息。

注意事项：

1. 授权码生命周期为 10 分钟，且只能被使用一次
2. 访问令牌和刷新令牌最终都应该由服务端持有
2. 授权过程中需要传递的 `client_secret` 只能由服务端持有，因为使用不当造成的用户数据泄露由第三方企业负责

#### 2.1.2 隐式授权模式授权

隐式授权模式 SDK 接口位于 com.xiaomi.passport.api.ImplicitGrantHelper 类中。隐式授权模式针对没有服务端的 APP 做了优化和妥协，可以一步拿到访问令牌，但是考虑到安全问题，不会下发刷新令牌。同样，因为该过程需要与用户交互（即询问用户是否确认授权），所以整个过程无法仅仅通过程序完成。我们在方法中仅仅描述了获取隐式访问令牌的概要流程，并推荐您参考 ‘[隐式授权模式](https://dev.mi.com/console/doc/detail?pId=709)’ 接口 1 中说明构建请求地址，请求过程中需要用户确认授权，访问令牌最终会以 fragment 形式追加到回调地址后面，并以 302 形式回调下发。

#### 2.1.3 刷新访问令牌

授权码模式下发令牌时，默认会同时下发访问令牌和刷新令牌，刷新令牌相对于访问令牌具备较长的生命周期，可以在访问令牌过期时更新访问令牌。

SDK 使用示例：

```java
// 构建您的 APP 信息
Client client = new Client();
// 构建刷新访问令牌请求实例
RefreshAccessTokenHelper helper = new RefreshAccessTokenHelper(client);
// 利用刷新令牌更新访问令牌
AccessToken accessToken = helper.refreshAccessToken(refreshToken);
```

如果请求成功，则会返回 `AccessToken` 对象，对象属性含义可以参考 ‘[访问令牌更新](https://dev.mi.com/console/doc/detail?pId=712)’ 接口 1 中的释义。

如果请求失败，则会抛出 `OAuthSdkException`，异常中包含响应的错误码和描述信息。

### 2.2 开放数据接口

授权完成之后，您可以通过手上持有的访问令牌请求权限范围内的小米开放数据和服务，帐号这边开放了用户资料，包括用户的基本资料、手机号、邮箱等信息。

SDK 使用示例：

```java
// 构建您的 APP 信息
Client client = new Client();
// 准备访问令牌
String accessToken = "your access token here";
// 构建开放数据请求实例
OpenApiHelper helper = new OpenApiHelper(client.getId(), accessToken);

// 1. 获取用户名片
UserProfile profile = helper.getUserProfile();

// 2. 获取用户OpenID
String openId = helper.getOpenId()

// 3. 获取用户手机号（左）和邮箱（右）
Pair<String, String> pair = helper.getPhoneAndEmail();

// 4. 获取用户米聊好友关系
List<Long> friends = helper.getFriendIdList();
```

## 相关文档

1. [小米帐号开放平台接入指南](https://dev.mi.com/console/doc/detail?pId=897)
2. [OAuth 2.0 协议原理与实现](https://dev.mi.com/console/doc/detail?pId=711)

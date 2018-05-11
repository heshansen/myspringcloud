# Authentication Server

认证服务器负责整个系统中的认证与鉴权， 整个认证过程通过OAuth2 协议完成，将Auth 作为一个单独服务可以方便的进行水平扩展。
OAUTH的工作原理，请参考OAUTH2 文档

## 工作流程

系统中使用2中token 流， UI登录通过password flow， 服务之间调用通过creditial flow

## 服务依赖:

认证服务依赖Configuration Server（提供认证服务器启动时候的配置数据），Discovery Server（注册认证服务）


## 调用方式:

* 终结点：

授权服务默认提供两个终结点： /oauth/authorize  用来获取授权码
/oauth/token 用来获取token

* 获取token:

```shell
curl fooClientIdPassword:secret@localhost:8082/uaa/oauth/token -d "grant_type=password&username=restUser&password=1234"
```


返回示例:

```
{
    "access_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MDk1NjAzMDUsInVzZXJfbmFtZSI6InJlc3RVc2VyIiwianRpIjoiZWY1M2VlNTItNzA5Zi00YTliLWEyZWMtNzg5ZDBmYTRkYjA1IiwiY2xpZW50X2lkIjoiZm9vQ2xpZW50SWRQYXNzd29yZCIsInNjb3BlIjpbImZvbyIsInJlYWQiLCJ3cml0ZSJdfQ.wbta1v8YTeqanbavjFViPqUgeYxnXnItLFWM-CMeT0Y",
    "token_type":"bearer",
    "refresh_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MTIxMDkxMDUsInVzZXJfbmFtZSI6InJlc3RVc2VyIiwianRpIjoiMzg2MTFjMTYtZWVlOC00M2RjLWFmY2UtMjdjNTM5MDRmNTI3IiwiY2xpZW50X2lkIjoiZm9vQ2xpZW50SWRQYXNzd29yZCIsInNjb3BlIjpbImZvbyIsInJlYWQiLCJ3cml0ZSJdLCJhdGkiOiJlZjUzZWU1Mi03MDlmLTRhOWItYTJlYy03ODlkMGZhNGRiMDUifQ.LxFiGST5pn1fGSTJSqZf_EWKf3VmzzyGbRvFwTPxdO8",
    "expires_in":43194,
    "scope":"foo read write",
    "jti":"ef53ee52-709f-4a9b-a2ec-789d0fa4db05"
}
```

* 访问资源

获取到token 以后，就可以使用token 来访问资源，我们以获取已认证的用户信息资源为例:

```shell
TOKEN = eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MDk1NjAzMDUsInVzZXJfbmFtZSI6InJlc3RVc2VyIiwianRpIjoiZWY1M2VlNTItNzA5Zi00YTliLWEyZWMtNzg5ZDBmYTRkYjA1IiwiY2xpZW50X2lkIjoiZm9vQ2xpZW50SWRQYXNzd29yZCIsInNjb3BlIjpbImZvbyIsInJlYWQiLCJ3cml0ZSJdfQ.wbta1v8YTeqanbavjFViPqUgeYxnXnItLFWM-CMeT0Y
curl -H "Authorization: Bearer $TOKEN" localhost:8081/uaa/user
```

* token签名公钥， 授权服务颁发的token 经过了非对称加密， 资源服务器拿到token以后，需要对token进行签名认证

1. 生成公钥

```
keytool -genkeypair -alias jwt-token-key \
       -keyalg RSA -keysize 4096 -sigalg SHA512withRSA \
       -dname 'CN=jwt,OU=Topbaby Cloud,O=Topbaby' \
       -keypass 61topbaby-jwt -keystore jwt-token.jks \
       -storepass 61topbaby-jwt
```

2. 导出公钥

```
keytool -list -rfc --keystore jwt-token.jks | openssl x509 -inform pem -pubkey
```

公钥的值, **资源服务器通过以下部门的值来验证token签名** 具体配置方式参考demo-config-client项目:

```
-----BEGIN PUBLIC KEY-----
MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEApoPaYicYDLzX7gJL4BPw
6suBS/xMfCXGIxu1Jwb3yb3vyvsQE0ureby6xzkfgizmlJnmNckVUw2ZhuSuPZGU
xVjNUZZB8KgKYDoGJ4GiOKYqjcO7Z+slJYV7sljXUqqupIqaEPxeqa4HaW9sFKje
S87vFe6N3WD2HL5vkG2Itz4Q6YF7qcDPT2rEJEOtjU0UCx4OdqCLYkOCcr2y3lSL
NBakik5xSW2nypXim4UbTJkXyIFRy9kRp47/4Vd19VgE6fCO8mBacAmVfruKlCFV
sPG7KRlptI6MDIugkV9M9PXP+CmTSwSzFAmeu9KUx0pUC1/d0NnyiP3bB+rGeIvJ
RYGYAW/oSvY69937g6+uiGok4d/eIkwJc9eNigeMgneZh95zzjY+tejzdh8mGYKy
RWaR5Ei2l/TkTV6JqTfYiJx0yFXhIWGyMIFHkOvaYX2ACDxsI+zNqBhujG3HFHlM
bwKpXCc8Vgo7YXVdXv9z3x88B9jdJXb4KShTkZle/49jcOq70uUAZK5H1z0o55Zp
lsD2Atj3+IQHmtHhVpaK+eJS6/MemMhT6/u/IDeECES1gIcKlvA1zi+qtTK98Gnv
H8ZUtxbuM3dslT72xNyf0QGFALM24OrZ17SZsk9EHbD6FMzYAhF9AHHgpo3hLOA7
eDDjsipLEraPvFZMXzZIPzMCAwEAAQ==
-----END PUBLIC KEY-----
```

```
docker run --network spring_cloud -e REGISTRY_SERVER="registry-server"  --name auth-server topbaby/auth
```



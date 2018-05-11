# 配置服务概述

配置服务通过调用外部配置数据为spring cloud 集群中的其他服务提供配置数据支持.

# 前置条件

### 服务启动顺序

* 配置服务是整个分布式集群中的第一个需要启动的服务， 为所以其他的服务提供基础配置数据支持

### 服务启动方法

提供了三个配置方式: development, production, registration-first

* development 用于配置文件提供开发人员开发配置, 可以通过以下方式激活开发配置:

    开发配置默认使用的是已经clone 到本地的git仓库， 默认位置为: ${HOME}/code/topbaby/config-data， 如果你想
    使用自己本地的config-data 仓库位置, 使用一下命令:

    mvn spring-boot:run -Dspring.profiles.active=development -DgitUri={$HOME}/你的目录

* production 用于生产配置, 通过以下方式激活生产配置:

mvn spring-boot:run -Dspring.profiles.active=production

production Profile 使用远程git repository, 如果在启动的时候遇到SSL 证书验证错误，通过以下配置解决此问题:

```
git config --global http.sslVerify false
```


* registration-first 是注册优先启动的配置方式，他的工作方式是注册配置服务器到服务注册服务， 然后通过在注册服务中查找
配置服务的元数据，然后客户端通过这些数据查找配置信息， 因为注册启动优先存在测试不便及bug比较多， 所以我们使用主流的配置
优先启动的方式。更新信息参考: [Eureka First Bootstrap & retry defaulting to localhost](https://github.com/spring-cloud/spring-cloud-config/issues/275)

# JKS 加密

JKS 生成：

```
keytool -genkeypair -alias config-server-key \
       -keyalg RSA -keysize 4096 -sigalg SHA512withRSA \
       -dname 'CN=Config Server,OU=Topbaby Cloud,O=Topbaby' \
       -keypass 61-topbaby-2015-0108 -keystore config-server.jks \
       -storepass 61-topbaby
```

**备注**

以上命令使用RSA算法生成了一个名称为config-server-key 的密钥对, 选项 keypass 的值是用来访问指定的秘钥对， storepass 的值是用来访问整个keystore 的密码。

生产环境添加JKS非对称加密方式支持， 将需要加密的属性值通过调用 http://localhost:8080/encrypt 总结点获取加密值，通过http://localhost:8080/decrypt
终结点可以获取解密值:

比如: 需要将存在的配置文件中的password 属性的值mysecret进行加密存放, 可以按照以下方式:

```
curl localhost:8080/encrypt -d mysecret
```

配置服务器返回:

```
AgA86NAFdXlJQidJEubOv5AELjkAYpketmWpUMrPD/DKDoas1nKSwla4uFyRjGJAS+wfw9ddi59QgzFRs4xBrvg1kclSI4xTYVxWunwV1dUMzg2/ekkoDXR8BU86hPvjl1ikJDs4EIOJlsCIwcOSx5bNAKjCtk1YniSPzXiP3MQSOsNCA10eP2RTpwOe2vv4R6xOr5PtsQHaSUPyb8oH7gBOBhFwh3wbL9c8Py24qidPgjvW/7YgbAaAoE2lCnJ0bSXoDvDGRAczEDNYzRBrS/9eQS+BOaSKimMsXwfgCRElMskmVW9VmI+5U1YWZFg8MBv5P2PwtX2YB3WZ9ov+rt6pbd8apJKvt3EoUWw0cx0ozTitxdhih4CFlbBXH6bnCResGev4dzMn0RsMpoDB2OXPrn+PllJu39RMy773tiilTrcB9M4X7L3sboTxRpFWwLkd/b+rvOtFQMFpQb4N+B5pHi38a57rHl1Ro9ofyVtQfxYYJq5LPbUGFZSmU3QBQ7xHYqdJM0dKFvnJbMGMIoeyeGyxCVsXkaGaNMeHc4qEds/5F4IGNt3ejJEH7tuYRoPt9ztAGD8wJs1funeCDsp/dlsMK9DMNzATow86oLjf9z0s1DwgaDEndTrWIExB15ilM6A1KEI1wj5mBIit3/mvGTTQqVBK8+gelGK47JCxFPXnbRQJInWeOZ92y+j22LVO6tERtastnm5L7OpW+xzD
```

然后将这个值以以下方式存在的配置文件的pasword 中 :

yml:

password: '{cipher}AgA86NAFdXlJQidJEubOv5AELjkAYpketmWpUMrPD/DKDoas1nKSwla4uFyRjGJAS+wfw9ddi59QgzFRs4xBrvg1kclSI4xTYVxWunwV1dUMzg2/ekkoDXR8BU86hPvjl1ikJDs4EIOJlsCIwcOSx5bNAKjCtk1YniSPzXiP3MQSOsNCA10eP2RTpwOe2vv4R6xOr5PtsQHaSUPyb8oH7gBOBhFwh3wbL9c8Py24qidPgjvW/7YgbAaAoE2lCnJ0bSXoDvDGRAczEDNYzRBrS/9eQS+BOaSKimMsXwfgCRElMskmVW9VmI+5U1YWZFg8MBv5P2PwtX2YB3WZ9ov+rt6pbd8apJKvt3EoUWw0cx0ozTitxdhih4CFlbBXH6bnCResGev4dzMn0RsMpoDB2OXPrn+PllJu39RMy773tiilTrcB9M4X7L3sboTxRpFWwLkd/b+rvOtFQMFpQb4N+B5pHi38a57rHl1Ro9ofyVtQfxYYJq5LPbUGFZSmU3QBQ7xHYqdJM0dKFvnJbMGMIoeyeGyxCVsXkaGaNMeHc4qEds/5F4IGNt3ejJEH7tuYRoPt9ztAGD8wJs1funeCDsp/dlsMK9DMNzATow86oLjf9z0s1DwgaDEndTrWIExB15ilM6A1KEI1wj5mBIit3/mvGTTQqVBK8+gelGK47JCxFPXnbRQJInWeOZ92y+j22LVO6tERtastnm5L7OpW+xzD'

properties:

password: {cipher}AgA86NAFdXlJQidJEubOv5AELjkAYpketmWpUMrPD/DKDoas1nKSwla4uFyRjGJAS+wfw9ddi59QgzFRs4xBrvg1kclSI4xTYVxWunwV1dUMzg2/ekkoDXR8BU86hPvjl1ikJDs4EIOJlsCIwcOSx5bNAKjCtk1YniSPzXiP3MQSOsNCA10eP2RTpwOe2vv4R6xOr5PtsQHaSUPyb8oH7gBOBhFwh3wbL9c8Py24qidPgjvW/7YgbAaAoE2lCnJ0bSXoDvDGRAczEDNYzRBrS/9eQS+BOaSKimMsXwfgCRElMskmVW9VmI+5U1YWZFg8MBv5P2PwtX2YB3WZ9ov+rt6pbd8apJKvt3EoUWw0cx0ozTitxdhih4CFlbBXH6bnCResGev4dzMn0RsMpoDB2OXPrn+PllJu39RMy773tiilTrcB9M4X7L3sboTxRpFWwLkd/b+rvOtFQMFpQb4N+B5pHi38a57rHl1Ro9ofyVtQfxYYJq5LPbUGFZSmU3QBQ7xHYqdJM0dKFvnJbMGMIoeyeGyxCVsXkaGaNMeHc4qEds/5F4IGNt3ejJEH7tuYRoPt9ztAGD8wJs1funeCDsp/dlsMK9DMNzATow86oLjf9z0s1DwgaDEndTrWIExB15ilM6A1KEI1wj5mBIit3/mvGTTQqVBK8+gelGK47JCxFPXnbRQJInWeOZ92y+j22LVO6tERtastnm5L7OpW+xzD


# Refresh Configuration Properties

TODO: need add how to refresh application config property automatic.

```
docker run --name config-server --env PROFILE_ACTIVE=development -v /Users/Qing/code/topbaby/spring-cloud/config-data:/app/config -p 8080:8080 topbaby/config
```

```
docker run --network=spring_cloud  --name config-server --env PROFILE_ACTIVE=development -v /Users/Qing/code/topbaby/spring-cloud/config-data:/app/config -p 8080:8080 topbaby/config
```


Git SSH configuration using properties, we plan using this config in test environment

```xml
uri: ssh://git@git.91topbaby.com:10022/topbaby/config-data.git
ignore-local-ssh-settings: true
host-key: AAAAE2VjZHNhLXNoYTItbmlzdHAyNTYAAAAIbmlzdHAyNTYAAABBBDxhX6Zk4BWc4ER5KiFxKn0rbMoLZzg63WzwzQQULTTbT7tbDz9U8Ygf3BBGTdofEIRL9IARzKTdUVmvjFN0Y54=
host-key-algorithm: ecdsa-sha2-nistp256
private-key: |
                -----BEGIN RSA PRIVATE KEY-----
                MIIJKQIBAAKCAgEAtpsrfT1r38LR8XZR8Nbo3+Xk0PCS3u/318YxU9fPz9wp8sep
                AYMKkfHflDgC7EHBbs3qfb8FkFMk6ZlKVGJfeRC8Y4KQDGqUpYiLHpwSN67qFXp9
                nHYUPStHWZbrQ/mQyuYx3FaRMm3YgALjJLap7PJffq01kQ4XXcmkSCuz5Fp7wWun
                D5uzOggkk4s4MUqQkVki7+SLWGIu65d37bIBLu6CMNKyxupr9rSI7m8Lhg/Ezd7E
                pN7yvE+pD8u8dZ8+h9AtRNvkszEB3aiwGLetqin7I5TnHkoZfmj4faotZKT0WTSu
                floabgqH6/BxmVHcBvwQgIN0RJyLvEyJu6+wvD2ULZt/bZi+Ub/y6ZsF1wvBvmjP
                rmfTtJ54XXdnqYtA6GZ3vBMGnlfD7a+SVS76A7S9BctytbewY80EHqh74+e1de9h
                WPylpGYy8tChvT/7SqTVnuL7aae9CphGp1rnakf7V6O8O7oR0PNw2M0jTU9UbuPR
                zxi+JWGCyFJKz6W71z6qh4R1+b+2fHsYC/xgvsZLKzg6CW2NQC6ADW7F3vsAKFxQ
                KRGzRMic3FiNEhYCjRXPsBXF6Y5u+he146Um14GffVAwqWjuzXK5gkgmG7Bw73UL
                fcKJ2WE6J628kBTzFufni30mzfZhcyYk1m1XsKqUeetNTByICuU9EngWGj0CAwEA
                AQKCAgBRhCN93RVy5j1iVTdvFAERMkdbHonyC53QLSGIFCco21e+kXLYwe7OB/OO
                7xIe9ZusIzAywjtxmiw4O736vY9xNA9lPLuhT1LcMoAoQfO/9813mR7J4cxXIiLP
                05di2N3SNcC8Y8idgnvLaX6MvR5OMESBAEGRZ4D21TUJIrlDj0D/r1b8mL0BgvER
                DG0hU/AFco0uGhA6fFoFwbA/496zXLWxpd04/x0DHMSj695xEkkAvhl6oCwr9EO8
                UW7n8xau+je7LY6Cj7YNhWhXIGfeNq9tyuMBmoKW87U6I+Xvr9OEnwfmT8LMrNbQ
                dJ3Bm9FlLNERDZ2xo1Cd1m0F5AA4xmbGwv5XwjezF5Vy/SSQLbVEHndD4u5gCBO1
                xK1pkNseDqMZD/N2hkUGXR0uLxeerrFK8f8gvLexd/adFlG84JlkRyAWg2uAlKhQ
                /GmZUeLG6iCF/uTVQotaVFkLJRf+nXT6XMfMJUyL0ngPuQRQdBvRBV9DO5IPOSrV
                OOpIkpbkyk1V5jkE3+3mZuWjr6Avv7jKkivfcMdWNPYMEV9D4TNhAS+4G5SOiGrX
                l8oiC+ILYZ1/64RNhnT38Q3Ap1FGA2z2cT/FygW0rFftTp+3R+sU5c4Wm7mFrHC6
                vRViigit1D/A9C1CIpZMzXVK9EAsG72S0kzKQ9396oNtxkenAQKCAQEA63iuEGdJ
                dRpc1ZiMbU80sgAxRh64OMV4xP+ikKv5TIrLspPAVtf6SoykrXVbmMQaqegKZQqi
                Q2OU6J/rTnJmSLnoZCYqDhudUU+5kDP/YUe3f/twJE1r/rdcUJ51S2kSKbKs6i43
                yrBjdJPPmkGcb0+B+llqiNC3KYPc6EF3YXfJaQZE0b3f3iDODN0g/bNuBaCuneX3
                QvZV3mSicslD5gaExVGIIJ0qUKVwv6t9WJ9hIuZQEj8jEvsWzyga4sOI4bKYAB7q
                lBcqGQJ1EZhiN6I9L/r/22e9Me8x7uih4r+n8nxH/kvCCGa78wi77ZlX+sMNckLW
                vaXFPy6jTdj/pQKCAQEAxoagkbMsUxtnp/xT6l/smofsTnu9Lq+QbSUGkiRboHbW
                0xoHMFjdphIkqHqKLM5BeHTanCaHjiCVEqmZX14Yli1sE/pzkuNQ7t5GKSpHZRIk
                N3pZCBZ39eE8mRxVo2HAKicjHThBALw2Icu54FAkciXIJnCv7izCMZuHoKSZC+sW
                A69Rt7Lkz5HYh77rphb0fWN74HwZMikXAc6LwCzcmbTycWWXZHrjiv7FP2cPZuXx
                du3Xf+JmWCZE0ITy7TZ1b2MvHH4wJm9Ybfm/8VjSHbaSwsNhU20kCvmxpuLGOsh4
                b5hMxYEJsNz4RPEwL7P7nnFSkqpOBfpoS6vMQPEsuQKCAQEA3arSufBxz4KMyCr+
                b1yz76mMuUTsCc2kHlfEssqiOnMixS6gT2CsZ4e08vHa05owu6gMOQhkrmnNYZxC
                SKwbK/WPFtByoKtQUp5pwp2gmPSJgzqxXAtiUfxNNDunkeEMzyI784ykyvN3/OO0
                +LMxT/waERIh30AUXUmcKSyP9LpuaacUHBrR52scbjGxAAPs2/Fi0JQPTANX3K8B
                2msrtxyF2rchHgq2LgtAc789lbMcH12PHU5Ad3rWAxfvD3AhqM6Wlu6Dx/tyju6o
                RHMeat8ZGNUCDYrAKYTSjeSDmVDxCq4CnBq3Q29rGaQsPcUwdIkwXFMdkpS+dfcy
                lnNkXQKCAQEAjep1ZFJCEFLZ4aMmVbgmyjz6NwyLq5EDXJKIRPZKrcXmQdgsZWpR
                YEM+A1Y96lOzR6m5EMEWTAqMpl+o7Ry11L05snpqHgnztB4e55R6nwy96Q7EEF0H
                ejR9/jHcIkRhe4CowF2TUjQ5OiWJhYmk1BigIKKTkV5bn9y/sp2XboaqIQ42C2yN
                1rg6oqdfi0aDlhME3e+4jzwCX19IVaN8O0C3w0pNVB3pBVakqHNC9lrQJyVU1O2C
                HeeVb75qbhptDqhHaEXoo8Ea5NsRVDUbVvRJq4LB00KE7neagGNCM4lVVuj3kIRy
                6gS8UYRc3uz5pUT7My8dQXbFEbsI8y+0uQKCAQAuqwPGie4UjfZNbfCGazSIuKQT
                yKa89PcJesFATYvcmv05rQipxRe4+oygc2MwxsZtCcSCLBK9CmDFjZiXmW9EUgWX
                B3lxu6Y3VFoRhgQd8Uw5tzmOBqoAqhLvHxldKZkV0W1qqgy48NOJiRUJULnat5Bi
                Fbep26S57RKneyadk9cTV3ZTOZn1dHkr+Sc3COSfYNQfjmlSCBKAzzD2nfpDCmv7
                5SBQJIEKMjClXOaZKF7vc9z43wkpCiu6QWwUQMqkIaG8ZVsIzqAWBIwuZUBitOKx
                PvXvchVRAErHj62c4eXBx16N/9M0affjqdNzSrA59ugd6Fj4wQ1y0Rzypm7n
                -----END RSA PRIVATE KEY-----
```

Git SSH configuration file based, we using this config in production environment.
```
 uri: topbaby:/topbaby/config-data.git
 strictHostKeyChecking: false
```
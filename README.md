管理端代码见“VinoHouse-vue-ts”仓库，用户端代码见“VinoHouse-wexin”仓库。

> 前端代码：
>
> - node v12.x；
>
> 后端代码：
>
> - JDK 8-17；
> - IDEA Community <= 2019.3.5；IDEA Ultimate > 2019.3.5。

# 1. 项目介绍

这是一个专为酒类餐厅设计的**店内点餐系统**，主要供顾客在店内扫码下单使用。用户端界面参考了美团、饿了么等外卖平台的设计风格，但该系统不支持外部商家入驻。

## 1.1 项目概述

万酒屋 VinoHouse 是专为 Pub、Bar、Club 等酒类餐饮企业定制的软件产品，包含系统管理后台（管理端）与小程序端应用（客户端）。管理端主要供企业内部员工使用，支持餐厅分类、酒水、套餐、订单、员工等信息的管理维护，可对餐厅各类数据进行统计，同时具备来单语音播报功能；小程序端则面向消费者，支持在线浏览商品、添加购物车、下单、支付、催单等操作。

1. **功能架构**：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506022023077.png" alt="image-20250602202309979" style="zoom: 50%;" />

2. **技术选型**：

![image-20250602202346823](https://gitee.com/Koletis/pic-go/raw/master/202506022023937.png)

1）用户层：

- Node.js：一个基于 Chrome V8 引擎的 JavaScript 运行时环境，用于在服务器端运行 JavaScript。
- Vue.js：一款渐进式前端 JavaScript 框架，用于构建用户界面。
- Element UI：基于 Vue.js 的 UI 组件库，提供丰富的预设组件。
- Apache ECharts：一个由百度开源、后捐献给 Apache 的 数据可视化图表库。

2）网关层

- Nginx 是高性能 HTTP 服务器网关，可部署静态资源，兼具反向代理与负载均衡功能，常用于实现 Tomcat 等服务的负载均衡部署。

3）应用层

- Spring Boot：快速构建 Spring 项目, 采用 "约定优于配置" 的思想，简化 Spring 项目的配置开发。
- SpringMVC：SpringMVC 是 Spring 框架的一个模块，SpringMVC 和 Spring 无需通过中间整合层进行整合，可以无缝集成。
- Spring Task：由 Spring 提供的定时任务框架。
- HttpClient：主要实现了对 HTTP 请求的发送。
- Spring Cache：由 Spring 提供的数据缓存框架
- JWT：用于对应用程序上的用户进行身份验证的标记。
- 阿里云OSS：对象存储服务，在项目中主要存储文件，如图片等。
- Swagger：可以自动的帮助开发人员生成接口文档，并对接口进行测试。
- POI：封装了对 Excel 表格的常用操作。
- WebSocket：一种通信网络协议，使客户端和服务器之间的数据交换更加简单，用于项目的来单、催单功能实现。

4）数据层

- MySQL：关系型数据库，本项目的核心业务数据都会采用 MySQL 进行存储。
- Redis： 基于 key-value 格式存储的内存数据库，访问速度快，经用作缓存。
- Mybatis： 本项目持久层将会使用 Mybatis 开发。
- PageHelper：分页插件。
- Spring Data Redis：简化 Java 代码操作 Redis 的 API。

5）工具层

- Git：版本控制工具，在团队协作中，使用该工具对项目中的代码进行管理。
- Maven：项目构建工具。
- JUnit：单元测试工具，开发人员功能实现完毕后，需要通过 JUnit 对功能进行单元测试。
- Postman：接口测试工具，模拟用户发起的各类 HTTP 请求，获取对应的响应结果。

## 1.2 环境搭建

**环境配置**：

Windows 11、cpolar 3.2.82。

node v12.22.0、微信开发者工具 1.06.2503300 Windows 64、Postman 11.49.1、Vue 5.0.8。

Nginx 1.20.2、IDEA Ultimate 2023.3.3、JDK 11.0.26、Maven 3.9.9、Spring Boot 2.7.3、Redis-x64-3.2.100、MySQL 8.0.42。

**整体结构**：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506021925395.png" alt="image-20250602192433223" style="zoom:67%;" />

### 1.2.1 管理端 Web

> 学习前端前，用 Nginx 部署好的前端工程（苍穹外卖）；学完前端后，用 IDEA 终端的 `npm run serve` 命令运行前端工程（万酒屋）。

存放位置 `D:\DevelopmentTools\nginx-1.20.2`。双击 `nginx.exe` 启动 Nginx 服务，访问端口号为 80：http://localhost:80。

![image-20250604093818511](https://gitee.com/Koletis/pic-go/raw/master/202506040938718.png)

### 1.2.2 后端服务 Java

IDEA 工程名：VinoHouse-take-out。

> 注：sky → VinoHouse；dish → beverage；菜品 → 酒水

| **名称**           | **说明**                                                     |
| ------------------ | :----------------------------------------------------------- |
| VinoHouse-take-out | Maven 父工程，统一管理依赖版本，聚合其他子模块。             |
| VinoHouse-common   | 子模块，存放公共类，例如：工具类、常量类、异常类等。         |
| VinoHouse-pojo     | 子模块，存放实体类、VO、DTO 等。                             |
| VinoHouse-server   | 子模块，后端服务，存放配置文件、Controller、Service、Mapper 等。 |

1）VinoHouse-common 模块每个包的作用：

| 名称        | 说明                                |
| ----------- | ----------------------------------- |
| constant    | 存放相关常量类。                    |
| context     | 存放上下文类。                      |
| enumeration | 项目的枚举类存储。                  |
| exception   | 存放自定义异常类。                  |
| json        | 处理 JSON 转换的类。                |
| properties  | 存放 Spring Boot 相关的配置属性类。 |
| result      | 返回结果类的封装。                  |
| utils       | 常用工具类。                        |

2）VinoHouse-pojo 模块每个包的作用：

| **名称** | **说明**                                                     |
| -------- | ------------------------------------------------------------ |
| Entity   | 实体，通常和数据库中的表对应。                               |
| DTO      | 数据传输对象，通常用于程序中各层之间传递数据。               |
| VO       | 视图对象，为前端展示数据提供的对象。                         |
| POJO     | 普通 Java 对象，只有属性和对应的 getter 和 setter；以上三个也属于 POJO。 |

3）VinoHouse-server 模块每个包的作用：

| 名称                 | 说明                 |
| -------------------- | -------------------- |
| config               | 存放配置类。         |
| controller           | 存放 Controller 类。 |
| interceptor          | 存放拦截器类。       |
| mapper               | 存放 Mapper 接口。   |
| service              | 存放 Service 类。    |
| VinoHouseApplication | 启动类。             |

### 1.2.3 测试验证

> [!IMPORTANT]
>
> 编译后端服务前，请先打开 Nginx。

测试顺序：`nginx.exe` > `compile` > `VinoHouseApplication` > http://localhost:80

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506040934208.png" alt="image-20250604093426009" style="zoom:80%;" />

`compile`：

![image-20250604093709130](https://gitee.com/Koletis/pic-go/raw/master/202506040937220.png)

 `VinoHouseApplication`：

![image-20250604095420635](https://gitee.com/Koletis/pic-go/raw/master/202506040954744.png)

登录管理端：

![image-20250604100035440](https://gitee.com/Koletis/pic-go/raw/master/202506041000586.png)

## 1.3 Nginx

> 配置文件在：`D:\DevelopmentTools\nginx-1.20.2\conf\nginx.conf`

### 1.3.1 反向代理

前端请求地址：http://localhost/api/employee/login

<img src="https://gitee.com/Koletis/pic-go/raw/master/202507020955213.png" alt="image-20250702095532732" style="zoom:80%;" />

后端接口地址：http://localhost:8080/admin/employee/login

<img src="https://gitee.com/Koletis/pic-go/raw/master/202507020957058.png" alt="image-20250702095755550" style="zoom:80%;" />

前端（浏览器）、Nginx 、后端（Tomcat）交互图：

![image-20250604125755972](https://gitee.com/Koletis/pic-go/raw/master/202506041257071.png)

**Nginx 反向代理的好处**：

- 提高访问速度：可以做缓存。

- 进行负载均衡。

- 保证后端服务安全。

**Nginx 反向代理的配置方式**：

```nginx
# 监听 80 端口，当访问 http://localhost:80/api/../.. 接口时，会通过 location /api/{} 反向代理到 http://localhost:8080/admin/。
server{
    listen 80;
    server_name localhost;
    
    location /api/{
        proxy_pass http://localhost:8080/admin/;  # 反向代理
    }
}
```

![image-20250604151619779](https://gitee.com/Koletis/pic-go/raw/master/202506041516898.png)

### 1.3.2 负载均衡

> 本质上基于反向代理实现。

**Nginx  负载均衡的配置方式**：

```nginx
# 监听 80 端口，当访问 http://localhost:80/api/../.. 接口时，通过 location /api/{} 反向代理到 http://webservers/admin，再根据 webservers 找到一组服务器，根据设置的负载均衡策略转发到具体的服务器。
upstream webservers{  # 后端服务器组
    server 192.168.100.128:8080;
    server 192.168.100.129:8080;
}
server{
    listen 80;
    server_name localhost;
    
    location /api/{
        proxy_pass http://webservers/admin;  # 负载均衡
    }
}
```

**Nginx 负载均衡策略**：

| **名称**                 | **说明**                                                     |
| :----------------------- | ------------------------------------------------------------ |
| 轮询（Round Robin）      | 默认方式。按照顺序依次将请求分配到后端的服务器节点上。       |
| weight（加权轮询）       | 权重方式，默认为1，权重越高，被分配的客户端请求就越多。      |
| least_conn（最少连接数） | 依据最少连接方式，把请求优先分配给连接数少的后端服务。       |
| ip_hash                  | 依据 ip 分配方式，这样每个访客可以固定访问一个后端服务。     |
| url_hash                 | 依据 url 分配方式，这样相同的 url 会被分配到同一个后端服务。 |
| fair                     | 依据响应时间方式，响应时间短的服务将会被优先分配。           |

**具体配置方式**：

1）轮询：

```nginx
upstream webservers{
    server 192.168.100.128:8080;
    server 192.168.100.129:8080;
}
```

2）weight：

```nginx
upstream webservers{
    server 192.168.100.128:8080 weight=90;
    server 192.168.100.129:8080 weight=10;
}
```

3）least_conn：

```nginx
upstream webservers{
    least_conn;
    server 192.168.100.128:8080;
    server 192.168.100.129:8080;
}
```

4）ip_hash：

```nginx
upstream webservers{
    ip_hash;
    server 192.168.100.128:8080;
    server 192.168.100.129:8080;
}
```

5）url_hash：

```nginx
upstream webservers{
    hash &request_uri;
    server 192.168.100.128:8080;
    server 192.168.100.129:8080;
}
```

5）fair：

```nginx
upstream webservers{
    server 192.168.100.128:8080;
    server 192.168.100.129:8080;
    fair;
}
```

## 1.4 完善登录功能

**MD5 加密**：明文 → 32位字符串，不可逆。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506041538926.png" alt="image-20250604153824807" style="zoom:50%;" />

**实现步骤：**

1. 修改数据库（`employee`）中明文密码（123456），改为 MD5 加密后的密文（`e10adc3949ba59abbe56e057f20f883e`）。


![image-20250604154422182](https://gitee.com/Koletis/pic-go/raw/master/202506041544288.png)

2. 修改 Java 代码，前端提交的密码先进行 MD5 加密，后再跟数据库中密码比对。

```java
// EmployeeServiceImpl
public Employee login(EmployeeLoginDTO employeeLoginDTO) {
    // ...
	// MD5 加密
    password = DigestUtils.md5DigestAsHex(password.getBytes ());
    // 密码比对
    // ...
}
```

## 1.5 导入接口文档

> 为实现前后端分离开发，需预先定制接口文档。

**前后端分离开发流程图**：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506041609353.png" alt="image-20250604160958205" style="zoom:50%;" />

**操作流程**：

1. 登录接口管理平台 Apifox。访问地址：[Apifox - 可视化接口管理平台](https://apifox.com/)
2. 在 Apifox 上新建项目：万酒屋 VinoHouse。

![image-20250604165141901](https://gitee.com/Koletis/pic-go/raw/master/202506041651118.png)

3. 选择 YApi 导入数据。

![image-20250604165400160](https://gitee.com/Koletis/pic-go/raw/master/202506041654324.png)

4. 将 `万酒屋-管理端接口.json`、`万酒屋-用户端接口.json` 导入 `万酒屋 VinoHouse` 项目。

![image-20250604165924279](https://gitee.com/Koletis/pic-go/raw/master/202506041659436.png)

5. 导入成功。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506041658416.png" alt="image-20250604165843286" style="zoom:80%;" />

## 1.6 后端接口测试

**Swagger** 是一个规范和完整的框架，用于生成、描述、调用和可视化 RESTful 风格的 Web 服务。

knife4j 是为 Java MVC 框架集成 Swagger 生成 API 文档的增强解决方案，前身是 swagger-bootstrap-ui。目前，一般都使用 knife4j 框架。

### 1.6.1 使用步骤

1. 运行 `VinoHouseApplication`，并在访问浏览器：http://localhost:8080/doc.html。

![image-20250604174512021](https://gitee.com/Koletis/pic-go/raw/master/202506041745218.png)

2. 测试：

```
{
  "password": "123456",
  "username": "admin"
}
```

![image-20250604174922302](https://gitee.com/Koletis/pic-go/raw/master/202506041749461.png)

### 1.6.2 常用注解

通过注解可以控制生成的接口文档，使接口文档拥有更好的可读性。

| **注解**          | **说明**                                                   |
| ----------------- | ---------------------------------------------------------- |
| @Api              | 用在类上，例如 Controller，表示对类的说明。                |
| @ApiModel         | 用在类上，例如entity、DTO、VO。                            |
| @ApiModelProperty | 用在属性上，描述属性信息。                                 |
| @ApiOperation     | 用在方法上，例如 Controller 的方法，说明方法的用途、作用。 |

运行 `VinoHouseApplication`，并在访问浏览器：http://localhost:8080/doc.html。

![image-20250604180333067](https://gitee.com/Koletis/pic-go/raw/master/202506041803242.png)



# 2. 基础数据模块

## 2.1 员工管理

> 单表的增删查改

### 2.1.1 新增员工

1. **管理端原型设计**

业务规则：

- 账号必须是唯一的（唯一索引）；
- 手机号为合法的 11 位手机号码；
- 身份证号为合法的 18 位身份证号码；
- 密码默认为 123456，员工后期可以自己修改密码。



2. **接口设计**

明确接口的**请求路径 Path、请求方式 Method、请求参数、返回数据**。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506051517958.png" alt="image-20250605151733814" style="zoom:80%;" />

本项目约定：

- **管理端**发出的请求，统一使用 **/admin** 作为前缀。
- **用户端**发出的请求，统一使用 **/user** 作为前缀。



3. **代码开发**

前端传递的参数列表（左图）、Employee 实体类（右图）：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506051034504.png" alt="image-20250605103435339" style="zoom:50%;" />

注意：当前端提交的数据和实体类中对应的属性差别比较大时，建议使用 DTO 来封装数据。因此，本项目将前端传递的参数列表封装为 EmployeeDTO 类。

VinoHouse-server

- conntroller > admin > EmployeeController > save
- service > EmployeeService > save
- service > impl > EmployeeServiceImpl > save
- mapper > EmploveeMapper > insert



4. **测试验证**

> 注意：由于开发阶段前端和后端是并行开发的，后端完成某个功能后，此时前端对应的功能可能还没有开发完成，导致无法进行前后端联调测试。所以在开发阶段，后端测试主要以接口文档测试为主。

**1）通过接口文档测试**

测试顺序：`nginx.exe` >  `VinoHouseApplication` > http://localhost:8080/doc.html > 员工相关接口 > 新增员工

```json
{
  "id": 0,
  "idNumber": "555555222200006666",
  "name": "张木生",
  "phone": "11113333000",
  "sex": "1",
  "username": "zms" // 不能大写，空格
}
```

![image-20250605141038615](https://gitee.com/Koletis/pic-go/raw/master/202506051410792.png)

JWT（`JwtTokenAdminInterceptor`） 令牌校验不通过，返回 401。

调用员工登录接口获得一个合法的 JWT 令牌。这个 **token 存在 TTL**，若失效请重新获取，并填入全局参数。

```json
{
  "password": "123456",
  "username": "admin"
}
```

![image-20250605113221973](https://gitee.com/Koletis/pic-go/raw/master/202506051132163.png)

将合法的 JWT 令牌添加到全局参数中。


![image-20250605113458853](https://gitee.com/Koletis/pic-go/raw/master/202506051134013.png)

刷新，重新调试。

![image-20250605141309386](https://gitee.com/Koletis/pic-go/raw/master/202506051413572.png)

查看数据库 `employee` 表。

![image-20250605141425172](https://gitee.com/Koletis/pic-go/raw/master/202506051414304.png)

测试成功。

**2）通前后端联调测试**

测试顺序：`nginx.exe` >  `VinoHouseApplication` > http://localhost:80 > 登录 > 员工管理

![image-20250605115738046](https://gitee.com/Koletis/pic-go/raw/master/202506051157226.png)

查看数据库 `employee` 表。

![image-20250605141508775](https://gitee.com/Koletis/pic-go/raw/master/202506051415915.png)

测试成功。



5. **代码完善**

**1）**录入的用户名 `username`（唯一索引）已存在，抛出异常后未处理。

![image-20250605141825495](https://gitee.com/Koletis/pic-go/raw/master/202506051418652.png)

在全局异常处理器 `GlobalExceptionHandler` 中，新增一个处理 SQL 异常 `exceptionHandler` 的方法。再进行接口测试。

![image-20250605142011045](https://gitee.com/Koletis/pic-go/raw/master/202506051420228.png)

**2）**新增员工时（`EmployeeServiceImpl > save`），创建人 id 和修改人 id 设置为固定值。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506051358631.png" alt="image-20250605135828504" style="zoom:80%;" />

通过 ThreadLocal 进行传递。

> ThreadLocal 并不是一个 Thread，而是 Thread 的局部变量，它为每个线程提供单独一份存储空间，具有线程隔离的效果，只有在线程内才能获取到对应的值，线程外则不能访问。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506051437437.png" alt="image-20250605143751295" style="zoom:80%;" />

在拦截器 `JwtTokenAdminInterceptor` 中将用户 id 存储到 ThreadLocal：

```java
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    //...
    // 将用户 id 存储到 ThreadLocal
    BaseContext.setCurrentId(empId);
    // 3、通过，放行
    //...
}
```

在用户服务实现类 `EmployeeServiceImpl` 中，修改创建人 id 和修改人 id：

```java
public void save(EmployeeDTO employeeDTO) {
    //...
    // 4.设置当前记录创建人id和修改人id
    // employee.setCreateUser(10L);
    // employee.setCreateUser(10L);
    employee.setCreateUser(BaseContext.getCurrentId());
    employee.setUpdateUser(BaseContext.getCurrentId());
    //...
}
```

使用 admin 用户登录后新增一名员工：

![image-20250605145805762](https://gitee.com/Koletis/pic-go/raw/master/202506051458953.png)

查看 `employee` 表记录：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506051459418.png" alt="image-20250605145902286" style="zoom:80%;" />



### 2.1.2 员工分页查询

1. **管理端原型设计**

业务规则：

- 根据页码展示员工信息。
- 每页展示 10 条数据。
- 分页查询可以根据员工姓名进行查询。



2. **接口设计**

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506051516631.png" alt="image-20250605151611465" style="zoom:80%;" />

- Query参数是指将请求参数拼接在URL路径后（非JSON格式），如 `admin/employee/page?name=zms`；
- 返回数据中 records 数组中使用 Employee 实体类对属性进行封装；

注：数据格式如果是 json，需要加 @RequestBody；如果数据格式是 Query，直接声明参数，SpringMVC 框架会把数据封装成 DTO 对象。



3. **代码开发**

前端传递的参数列表（左图） → 封装的 `EmployeePageQueryDTO`（右图）：

![image-20250605153017001](https://gitee.com/Koletis/pic-go/raw/master/202506051530183.png)

后面所有的分页查询，统一都封装为 `PageResult` 对象。

![image-20250605153709447](https://gitee.com/Koletis/pic-go/raw/master/202506051537791.png)

员工信息分页查询后端返回的对象类型为：`Result<PageResult>` 。

![image-20250605154213656](https://gitee.com/Koletis/pic-go/raw/master/202506051542923.png)

VinoHouse-server

- conntroller > admin > EmployeeController > page
- service > EmployeeService > pageQuery
- service > impl > EmployeeServiceImpl > pageQuery（底层是基于 MySQL 的 limit 关键字实现）
- mapper > EmploveeMapper > pageQuery
- resources > mapper > EmployeeMapper.xml > pageQuery 



4. **测试验证**

**1）通过接口文档测试**

测试顺序：`nginx.exe` >  `VinoHouseApplication` >  http://localhost:8080/doc.html > 员工相关接口 > 员工分页查询

![image-20250606092101571](https://gitee.com/Koletis/pic-go/raw/master/202506060921871.png)

**2）通前后端联调测试**

测试顺序：`nginx.exe` >  `VinoHouseApplication` > http://localhost:80 > 登录 > 员工管理

![image-20250606092613348](https://gitee.com/Koletis/pic-go/raw/master/202506060926554.png)



5. **代码完善**

操作时间字段显示有问题。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506060933698.png" alt="image-20250606093325502" style="zoom: 65%;" />

1）方法一：

在属性上加上注解，对日期进行格式化：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506060936730.png" alt="image-20250606093649569" style="zoom:80%;" />

但该方法需在每个时间属性上添加注解，无法全局处理，使用较为繁琐。

2）方法二：

```java
// WebMvcConfiguration：扩展 Spring MVC 框架的消息转化器，统一对日期类型进行格式处理
protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    log.info("扩展消息转换器...");
    // 创建一个消息转换器对象
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    // 需要为消息转换器设置一个对象转换器，对象转换器可以将 Java 对象序列化为 json 数据
    converter.setObjectMapper(new JacksonObjectMapper());
    // 将自己的消息转化器加入容器中
    converters.add(0,converter);
}
```

再次测试：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506060945757.png" alt="image-20250606094516494" style="zoom:65%;" />



### 2.1.3 启用禁用员工账号

1. **管理端原型设计**

业务规则：

- 可以对状态为“启/禁用”的员工账号进行“禁/启用”操作；
- 状态为“禁用”的员工账号不能登录系统。



2. **接口设计**

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506061035818.png" alt="image-20250606103526646" style="zoom:80%;" />

- 路径参数携带状态值，状态值要被 `@PathVariable` 修饰；
- 同时，把 id 传递过去，明确对哪个用户进行操作；
- 返回数据 code 状态是必须，其它是非必须。



3. **代码开发**

VinoHouse-server

- conntroller > admin > EmployeeController > startOrStop
- service > EmployeeService > startOrStop
- service > impl > EmployeeServiceImpl > startOrStop
- mapper > EmploveeMapper > update
- resources > mapper > EmployeeMapper.xml > update



4. **测试验证**

**1）通过接口文档测试**

测试顺序：`nginx.exe` >  `VinoHouseApplication` >  http://localhost:8080/doc.html > 员工相关接口 > 启用禁用员工账号

![image-20250606135357729](https://gitee.com/Koletis/pic-go/raw/master/202506061353997.png)

 `employee` 表测试前后：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506061354330.png" alt="image-20250606135452096"  />

**2）通前后端联调测试**

测试顺序：`nginx.exe` >  `VinoHouseApplication` > http://localhost:80 > 登录 > 员工管理 > 启用/禁用

![image-20250606140034970](https://gitee.com/Koletis/pic-go/raw/master/202506061400234.png)

  `employee` 表测试前后：

![image-20250606135924420](https://gitee.com/Koletis/pic-go/raw/master/202506061359584.png)



### 2.1.4 修改员工

1. **管理端原型设计**

业务规则：在员工管理列表页点击"修改"跳转到修改页，回显信息并修改后点击"保存"完成操作。



2. **接口设计**

根据上述业务规则分析，修改员工功能涉及到两个接口。

1）根据 id 查询员工信息：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506061416111.png" alt="image-20250606141605937" style="zoom:80%;" />

2）修改员工信息：因为是修改功能，请求方式可设置为 PUT。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506061419496.png" alt="image-20250606141903330" style="zoom:80%;" />



3. **代码开发**

1）根据 id 查询员工信息：

VinoHouse-server

- conntroller > admin > EmployeeController > getById
- service > EmployeeService > getById
- service > impl > EmployeeServiceImpl > getById
- mapper > EmploveeMapper > getById

2）修改员工信息：

VinoHouse-server

- conntroller > admin > EmployeeController > update
- service > EmployeeService > update
- service > impl > EmployeeServiceImpl > update
- mapper > EmploveeMapper > update



4. **测试验证**

**1）通过接口文档测试**

测试顺序：`nginx.exe` >  `VinoHouseApplication` >  http://localhost:8080/doc.html > 员工相关接口 > 根据 id 查询员工信息

![image-20250606144830913](https://gitee.com/Koletis/pic-go/raw/master/202506061448136.png)

测试顺序：`nginx.exe` >  `VinoHouseApplication` >  http://localhost:8080/doc.html > 员工相关接口 > 编辑员工信息

```json
{
  "id": 4,
  "idNumber": "247893200209092274",
  "name": "阿权",
  "phone": "18759056835",
  "sex": "0",
  "username": "aaq"
}
```

![image-20250606145504291](https://gitee.com/Koletis/pic-go/raw/master/202506061455571.png)

  `employee` 表测试前后：

![image-20250606145730361](https://gitee.com/Koletis/pic-go/raw/master/202506061457545.png)

**2）通前后端联调测试**

测试顺序：`nginx.exe` >  `VinoHouseApplication` > http://localhost:80 > 登录 > 员工管理 > 修改

![image-20250606145945882](https://gitee.com/Koletis/pic-go/raw/master/202506061459083.png)

对“阿权”点击修改，数据已回显，并对其账号进行修改。

![image-20250606150214333](https://gitee.com/Koletis/pic-go/raw/master/202506061502536.png)

员工信息修改成功。

![image-20250606150304447](https://gitee.com/Koletis/pic-go/raw/master/202506061503646.png)



### 2.1.5 修改密码

1. **需求分析与设计**

业务规则：

- 身份验证：修改密码前需输入当前密码，验证用户操作权限；
- 密码复杂度：新密码需满足长度、大小写字母、数字及特殊字符组合要求；
- 加密存储：采用哈希加盐方式对密码加密，避免泄露后被破解；
- 异常处理：设置密码不匹配提示、修改失败错误信息等友好的异常处理机制。



2. **代码开发**

VinoHouse-server

- conntroller > admin > EmployeeController > editPassword
- service > EmployeeService > editPassword



3. **测试验证**

测试顺序：`nginx.exe` >  `VinoHouseApplication` > http://localhost:80 > 登录 > 修改密码 > 原始密码、新密码、确认密码 > 保存

在管理端右上角选择“修改密码”：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506171720971.png" alt="image-20250617172003460" style="zoom:80%;" />

修改成功：

![image-20250617172108197](https://gitee.com/Koletis/pic-go/raw/master/202506171721689.png)

后续也能使用修改后的密码登录系统。



### 2.1.6 删除员工

> 基于 `VinoHouse-vue-ts` 前端工程文件。

1. **需求分析与设计**

业务规则：仅能删除禁用状态下的员工账户。



2. **代码开发**

VinoHouse-server

- conntroller > admin > EmployeeController > delete
- service > EmployeeService > delete
- service > impl > EmployeeServiceImpl > delete
- mapper > EmploveeMapper > deleteById



3. **测试验证**

测试顺序：`nginx.exe` >  `VinoHouseApplication` > http://localhost:80 > 登录 > 员工管理 > 删除

禁止删除启用账户：

![image-20250630103156609](https://gitee.com/Koletis/pic-go/raw/master/202506301031241.png)

禁用账户成功删除：

![image-20250630103242505](https://gitee.com/Koletis/pic-go/raw/master/202506301032985.png)

`employee` 表也成功删除。



## 2.2 分类管理

> 类似 *2.1*，这里直接导入分类模块功能代码。

### 2.2.1 **管理端原型设计**

1. 酒水分类：

- 新增分类：后台添加酒水时需选分类，移动端按分类展示；

- 分页查询：分类数量多时分页展示，提升查阅效率；

- 按 id 删除：分类关联酒水/套餐时不可删除，保障数据完整；

- 修改分类：列表页点击修改按钮，回显信息编辑后保存；

- 启用/禁用：控制分类在前端的显示状态；

- 类型查询：下拉框动态获取数据库分类数据，实时更新选项。


2. 套餐分类：类似酒水分类。



### 2.2.2 接口设计

酒水分类模块共涉及 6 个接口：新增分类、分类分页查询、根据 id 删除分类、修改分类、启用禁用分类、根据类型查询分类。

- 分类名称必须是唯一的；
- 分类按照类型可以分为酒水分类和套餐分类；
- 新添加的分类状态默认为“禁用”。

套餐分类类似酒水分类。



### 2.2.3 代码导入

VinoHouse-server

- controller > admin > CategoryController

- service > CategoryService

- service > impl > CategoryServicelmpl
- mapper > BeverageMapper、CategoryMapper、SetmealMapper
- resources > mapper > CategoryMapper.xml

全部导入完毕后，进行编译：

![image-20250606154724049](https://gitee.com/Koletis/pic-go/raw/master/202506061547283.png)



### 2.2.4 测试验证

测试顺序：`nginx.exe` >  `VinoHouseApplication` > http://localhost:80 > 登录 > 分类管理

![image-20250606160202912](https://gitee.com/Koletis/pic-go/raw/master/202506061602135.png)

分类类型：

![image-20250606160326693](https://gitee.com/Koletis/pic-go/raw/master/202506061603879.png)

启用禁用：

![image-20250606160619778](https://gitee.com/Koletis/pic-go/raw/master/202506061606088.png)

修改分类：

- 回显：

![image-20250606160954837](https://gitee.com/Koletis/pic-go/raw/master/202506061609021.png)

- 修改后：

![image-20250606161048530](https://gitee.com/Koletis/pic-go/raw/master/202506061610745.png)

新增：

![image-20250606161307246](https://gitee.com/Koletis/pic-go/raw/master/202506061613448.png)

删除：

![image-20250606161435704](https://gitee.com/Koletis/pic-go/raw/master/202506061614915.png)



## 2.3 酒水管理

###  2.3.1 公共字段自动填充

> 非业务功能，目的是为了减少代码冗余，便于后期维护。

1.**实现思路**：使用 AOP 切面编程，来完成公共字段自动填充功能。

本项目的 4 个公共字段具体情况如下: 

| **序号** | **字段名**  | **含义**  | **数据类型** |  **操作类型**  |
| :------: | :---------: | :-------: | :----------: | :------------: |
|    1     | create_time | 创建时间  |   datetime   |     insert     |
|    2     | create_user | 创建人 id |    bigint    |     insert     |
|    3     | update_time | 修改时间  |   datetime   | insert、update |
|    4     | update_user | 修改人 id |    bigint    | insert、update |

实现步骤：

1）自定义注解 AutoFill，用于标识需要进行公共字段自动填充的方法。

2）自定义切面类 AutoFillAspect，统一拦截加入了 AutoFill 注解的方法，通过反射为公共字段赋值。

3）在 Mapper 的方法上加入 AutoFill 注解。

**技术点：**枚举、注解、AOP、反射。



2. **代码开发**

**1）自定义注解 AutoFill**

annotation > AutoFill：用于标识某个方法需要进行功能字段自动填充处理。

**2）自定义切面类 AutoFillAspect**

aspect > AutoFillAspect：实现公共字段自动填充处理逻辑。

**3）在 Mapper 的方法上加入 AutoFill 注解**

VinoHouse-server > mapper > BeverageMapper、CategoryMapper、EmployeeMapper、SetmealMapper > insert、update

注：学习到这里时，BeverageMapper、SetmealMapper 代码不完整，需要在后期加上 @AutoFill。



3. **测试验证**

以“新增酒水分类”为例：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506061735484.png" alt="image-20250606173557283" style="zoom:80%;" />

查看控制台：

![image-20250606173718374](https://gitee.com/Koletis/pic-go/raw/master/202506061737549.png)

查看 `category` 表：

![image-20250606173905741](https://gitee.com/Koletis/pic-go/raw/master/202506061739935.png)



### 2.3.2 新增酒水

1. **需求分析与设计**

业务规则：

- 酒水名称必须是唯一的；
- 酒水必须属于某个分类下，不能单独存在；
- 新增酒水时可以根据情况选择酒水的口味；
- 每个酒水必须对应一张图片。

接口设计：

- 根据类型查询分类（已完成）；
- 文件上传；
- 新增酒水：涉及以下两个表。

|      表名       |    说明    |
| :-------------: | :--------: |
|    beverage     |   酒水表   |
| beverage_flavor | 酒水口味表 |



2. **代码开发**

 **1）文件上传**

文件上传，是指将本地图片、视频、音频等文件上传到服务器上，可以供其他用户浏览或下载的过程。

实现文件上传服务，需要有存储的支持，分为以下几种解决方案：

- 本地存储（直接存服务器硬盘）：SpringMVC 文件上传、Nginx 静态资源托管。类似于单机。
- 分布式文件系统：FastDFS、MinIO、HDFS（Hadoop）。类似于内网。
- 对象存储（OSS/S3）：阿里云 OSS、AWS S3、七牛云。类似于外网。

在本项目选用阿里云的 OSS 服务进行文件存储：[OSS Java SDK快速入门](https://help.aliyun.com/zh/oss/developer-reference/getting-started?spm=a2c4g.11186623.help-menu-31815.d_5_2_1_0.46db4e4cKE5NmH#e32cab6130atj)。

![image-20250607171452497](https://gitee.com/Koletis/pic-go/raw/master/202506071714712.png)

**实现步骤**：

VinoHouse-server

- resources > application.yml

  ```yaml
  vino-house:
    alioss:
      endpoint: ${vino-house.alioss.endpoint}
      access-key-id: ${vino-house.alioss.access-key-id}
      access-key-secret: ${vino-house.alioss.access-key-secret}
      bucket-name: ${vino-house.alioss.bucket-name}
  ```


- resources > application-dev.yml

  ```yaml
  vino-house:
    alioss:
      endpoint: oss-cn-shanghai.aliyuncs.com
      access-key-id: LTAI5t**********  # 敏感信息，git 默认不上传
      access-key-secret: h0sUty**********  # 敏感信息
      bucket-name: vino-house
  ```

- config > OssConfiguration：配置类，用于创建 AliOssUtil 对象。

- conntroller > admin > CommonController：通用接口。



**2）新增酒水**

VinoHouse-server

- conntroller > admin > BeverageController > save
- service > BeverageService > saveWithFlavor
- service > impl > BeverageServiceImpl > saveWithFlavor
- mapper > 
  - BeverageMapper > insert
  - BeverageFlavorMapper > insertBatch

- resources > mapper > 
  - BeverageMapper.xml > insert
  - BeverageFlavorMapper.xml > insertBatch




3. **测试验证**

测试顺序：`nginx.exe` >  `VinoHouseApplication` > http://localhost:80 > 登录 > 酒水管理 > 新增酒水

> 这里的图片回显速度有点慢，不是立即的。

![image-20250607212537032](https://gitee.com/Koletis/pic-go/raw/master/202506072125350.png)

由于没有实现酒水查询功能，所以保存后只能在表中查看添加的数据。

`beverage` 表：

![image-20250607220212438](https://gitee.com/Koletis/pic-go/raw/master/202506072202663.png)

`beverage_flavor` 表：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506072203687.png" alt="image-20250607220330513" style="zoom:80%;" />



### 2.3.3 酒水分页查询

1. **需求分析与设计**

酒水列表展示时，除基本信息（名称、售价、售卖状态、最后操作时间）外，另有两个特殊字段：一是图片字段，需根据数据库查询到的图片名下载图片后回显；二是酒水分类，需通过分类 id 查询分类表获取名称后展示。

业务规则：

- 根据页码展示酒水信息；
- 每页展示 10 条数据；
- 分页查询时可以根据需要输入酒水名称、酒水分类、酒水状态进行查询。



2. **代码开发**

VinoHouse-server

- conntroller > admin > BeverageController > page
- service > BeverageService > pageQuery
- service > impl > BeverageServiceImpl > pageQuery
- mapper > BeverageMapper > pageQuery
- resources > mapper > BeverageMapper.xml > pageQuery



3. **测试验证**

**1）通过接口文档测试**

测试顺序：`nginx.exe` >  `VinoHouseApplication` >  http://localhost:8080/doc.html > 酒水相关接口 > 酒水分页查询

注意：响应码 401 时需用 admin 重新登录获取 token 并填入全局参数。

![image-20250608165253743](https://gitee.com/Koletis/pic-go/raw/master/202506081652008.png)

**2）通前后端联调测试**

测试顺序：`nginx.exe` >  `VinoHouseApplication` > http://localhost:80 > 登录 > 酒水管理

![image-20250608165358915](https://gitee.com/Koletis/pic-go/raw/master/202506081653172.png)



### 2.3.4 删除酒水

1. **需求分析与设计**

业务规则：

- 可以一次删除一个酒水，也可以批量删除酒水；
- 启售中的酒水不能删除；
- 被套餐关联的酒水不能删除；
- 删除酒水后，关联的口味数据也需要删除掉。

注意事项：

- 删除 `beverage` 表中的酒水数据时，需同步删除 `beverage_flavor` 表中的关联数；
- `setmeal_beverage` 表为酒水与套餐的关联中间表；
- 若待删除酒水关联套餐，则删除操作失败；
- 如需删除套餐关联的酒水，需先解除关联关系，再执行酒水删除。



2. **代码开发**

VinoHouse-server

- conntroller > admin > BeverageController > delete
- service > BeverageService > deleteBatch
- service > impl > BeverageServiceImpl > deleteBatch
- mapper > 
  - BeverageMapper > deleteById
  - BeverageFlavorMapper > deleteByBeverageId
  - SetmealBeverageMapper > getSetmealIdsByBeverageIds

- resources > mapper > 
  - SetmealBeverageMapper.xml > getSetmealIdsByBeverageIds



3. **测试验证**

测试顺序：`nginx.exe` >  `VinoHouseApplication` > http://localhost:80 > 登录 > 酒水管理 > 删除

删除停售酒水：

![image-20250608184215127](https://gitee.com/Koletis/pic-go/raw/master/202506081842399.png)

`beverage` 表：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506081853262.png" alt="image-20250608185321040" style="zoom: 50%;" />

`beverage_flavor` 表：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506081924277.png" alt="image-20250608192421048" style="zoom:50%;" />

批量删除启售酒水：

![image-20250608185220225](https://gitee.com/Koletis/pic-go/raw/master/202506081852459.png)



4. **代码优化**

VinoHouse-server

- service > impl > BeverageServiceImpl
- mapper > 
  - BeverageMapper > deleteByIds
  - BeverageFlavorMapper > deleteByBeverageIds

- resources > mapper > 
  - BeverageMapper.xml > deleteByIds
  - BeverageFlavorMapper.xml > deleteByBeverageIds


```java
// BeverageServiceImpl
/**
 * 酒水批量删除
 */
@Transactional
public void deleteBatch(List<Long> ids) {
    // ...
    // 1. 多条 SQL 语句
    // 删除酒水表中的酒水数据
//        for (Long id : ids) {
//            beverageMapper.deleteById(id);
//            // 删除酒水关联的口味数据
//            beverageFlavorMapper.deleteByBeverageId(id);
//        }

    // 2. 一条 SQL 语句
    // 根据酒水 id 集合批量删除酒水数据：delete from beverage where id in(?,?,?)
    beverageMapper.deleteByIds(ids);
    // 根据酒水 id 集合批量删除关联的口味数据：delete from beverage_flavor where beverage_id in(?,?,?)
    beverageFlavorMapper.deleteByBeverageIds(ids);
}
```



### 2.3.5 修改酒水

1. **需求分析与设计**

在酒水管理列表页面点击修改按钮，跳转到修改酒水页面，在修改页面回显酒水相关信息并进行修改，最后点击保存按钮完成修改操作。

接口设计：

- 根据 id 查询酒水；
- 根据类型查询分类（已实现）；
- 文件上传（已实现）；
- 修改酒水：请求方式 PUT。



2. **代码开发**

**1）根据 id 查询酒水**

VinoHouse-server

- conntroller > admin > BeverageController > getById
- service > BeverageService > getByIdWithFlavor
- service > impl > BeverageServiceImpl > getByIdWithFlavor
- mapper > BeverageFlavorMapper > getByBeverageId

**2）修改酒水**

VinoHouse-server

- conntroller > admin > BeverageController > update
- service > BeverageService > updateWithFlavor
- service > impl > BeverageServiceImpl > updateWithFlavor
- mapper > BeverageMapper > update
- resources > mapper > BeverageMapper.xml > update



3. **测试验证**

测试顺序：`nginx.exe` >  `VinoHouseApplication` > http://localhost:80 > 登录 > 酒水管理 > 修改

![image-20250608193257134](https://gitee.com/Koletis/pic-go/raw/master/202506081932376.png)

数据回显：

![image-20250608195505182](https://gitee.com/Koletis/pic-go/raw/master/202506081955453.png)

修改价格：

![image-20250608193606287](https://gitee.com/Koletis/pic-go/raw/master/202506081936519.png)

测试成功。



### 2.3.6 启售停售酒水

1. **需求分析与设计**

业务规则：

- 可以对状态为“启/停售”的酒水进行“停/启售”操作；
- 仅启售酒水对用户端可见，停售酒水自动隐藏；
- 停售酒水时，系统将自动停售包含该酒水的所有套餐。



2. **代码开发**

VinoHouse-server

- conntroller > admin > BeverageController > startOrStop
- service > BeverageService > startOrStop
- service > impl > BeverageServiceImpl > startOrStop
- mapper > SetmealMapper > update
- resources > mapper > SetmealMapper.xml > update



3. **测试验证**

测试顺序：`nginx.exe` >  `VinoHouseApplication` > http://localhost:80 > 登录 > 酒水管理 > 停售

> 下图已经修改为停售状态。

![image-20250608213612831](https://gitee.com/Koletis/pic-go/raw/master/202506082136170.png)

`beverage` 表：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506082137053.png" alt="image-20250608213730828" style="zoom:50%;" />

`setmeal` 表：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506082138119.png" alt="image-20250608213855877" style="zoom:50%;" />



## 2.4 套餐管理

### 2.4.1 新增套餐

1. **需求分析与设计**

业务规则：

- 套餐名称唯一；
- 套餐必须包含酒水，并属于某个分类；
- 名称、分类、价格、图片为必填项；
- 添加酒水窗口需要根据分类类型来展示酒水；
- 新增的套餐默认为停售状态。

接口设计：

- 根据类型查询分类（已完成）；
- 根据分类 id 查询酒水；
- 图片上传（已完成）；
- 新增套餐。

涉及以下两个表：

|       表名       |    说明    |
| :--------------: | :--------: |
|     setmeal      |   套餐表   |
| setmeal_beverage | 套餐酒水表 |



2. **代码开发**

**1）根据分类 id 查询酒水**

VinoHouse-server

- conntroller > admin > BeverageController > list
- service > BeverageService > list
- service > impl > BeverageServiceImpl > list
- mapper > BeverageMapper > list
- resources > mapper > BeverageMapper.xml > list

**2）新增套餐**

VinoHouse-server

- conntroller > admin > SetmealController > save
- service > SetmealService > saveWithBeverage
- service > impl > SetmealServiceImpl > saveWithBeverage
- mapper > 
  - SetmealMapper > insert
  - SetmealBeverageMapper > insertBatch

- resources > mapper > 
  - SetmealMapper.xml > insert
  - SetmealBeverageMapper.xml > insertBatch




3. **测试验证**

测试顺序：`nginx.exe` >  `VinoHouseApplication` > http://localhost:80 > 登录 > 套餐管理 > 新增套餐

![image-20250609093259292](https://gitee.com/Koletis/pic-go/raw/master/202506090932677.png)

根据分类 id 查询酒水：

> 使用 `nginx.exe` 启动的前端工程，右上角的搜索功能无法正常使用，搜索结果显示混乱。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506090956170.png" alt="image-20250609095634908" style="zoom: 50%;" />

新增套餐：

- `setmeal` 表：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506091418152.png" alt="image-20250609141829879" style="zoom:80%;" />

- `setmeal_beverage` 表：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506091420241.png" alt="image-20250609142005994" style="zoom:80%;" />



### 2.4.2 套餐分页查询

1. **需求分析与设计**

业务规则：

- 根据页码进行分页展示；
- 每页展示 10 条数据；
- 可以根据需要，按照套餐名称、分类、售卖状态进行查询。



2. **代码开发**

VinoHouse-server

- conntroller > admin > SetmealController > page
- service > SetmealService > pageQuery
- service > impl > SetmealServiceImpl > pageQuery
- mapper > SetmealMapper > pageQuery
- resources > mapper > SetmealMapper.xml > pageQuery



3. **测试验证**

**1）通过接口文档测试**

测试顺序：`nginx.exe` >  `VinoHouseApplication` >  http://localhost:8080/doc.html > 套餐相关接口 > 套餐分页查询

![image-20250609110712875](https://gitee.com/Koletis/pic-go/raw/master/202506091107146.png)

**2）通前后端联调测试**

测试顺序：`nginx.exe` >  `VinoHouseApplication` > http://localhost:80 > 登录 > 套餐管理

![image-20250609110815785](https://gitee.com/Koletis/pic-go/raw/master/202506091108042.png)



### 2.4.3 删除套餐

1. **需求分析与设计**

业务规则：

- 可以一次删除一个套餐，也可以批量删除套餐；
- 启售中的套餐不能删除。



2. **代码开发**

VinoHouse-server

- conntroller > admin > SetmealController > delete
- service > SetmealService > deleteBatch
- service > impl > SetmealServiceImpl > deleteBatch
- mapper > SetmealMapper > getById、deleteById



3. **测试验证**

测试顺序：`nginx.exe` >  `VinoHouseApplication` > http://localhost:80 > 登录 > 套餐管理

删除停售套餐：

![image-20250609145935388](https://gitee.com/Koletis/pic-go/raw/master/202506091459658.png)

批量删除启售套餐：

![image-20250609145656255](https://gitee.com/Koletis/pic-go/raw/master/202506091456590.png)



### 2.4.4 修改套餐

1. **需求分析与设计**

接口设计：

- 根据 id 查询套餐；
- 根据类型查询分类（已完成）；
- 根据分类 id 查询酒水（已完成）；
- 图片上传（已完成）；
- 修改套餐。



2. **代码开发**

**1）根据 id 查询套餐**

VinoHouse-server

- conntroller > admin > SetmealController > getById
- service > SetmealService > getByIdWithBeverage
- service > impl > SetmealServiceImpl > getByIdWithBeverage
- mapper > SetmealMapper > getByIdWithBeverage
- resources > mapper > SetmealMapper.xml > getByIdWithBeverage

**2）修改套餐**

VinoHouse-server

- conntroller > admin > SetmealController > update
- service > SetmealService > update
- service > impl > SetmealServiceImpl > update
- mapper > SetmealMapper > update
- resources > mapper > SetmealMapper.xml > update



3. **测试验证**

测试顺序：`nginx.exe` >  `VinoHouseApplication` > http://localhost:80 > 登录 > 套餐管理 > 修改

![image-20250609150805415](https://gitee.com/Koletis/pic-go/raw/master/202506091508675.png)

数据回显：

![image-202506091536964](https://gitee.com/Koletis/pic-go/raw/master/202506091536964.png)

修改成功：

![image-20250609153706400](https://gitee.com/Koletis/pic-go/raw/master/202506091537679.png)



### 2.4.5 启售停售套餐

1. **需求分析与设计**

业务规则：

- 可以对状态为启售的套餐进行停售操作，可以对状态为停售的套餐进行启售操作；
- 启售的套餐可以展示在用户端，停售的套餐不能展示在用户端；
- 启售套餐时，如果套餐内包含停售的酒水，则不能启售。



2. **代码开发**

VinoHouse-server

- conntroller > admin > SetmealController > startOrStop
- service > SetmealService > startOrStop
- service > impl > SetmealServiceImpl > startOrStop
- mapper > BeverageMapper > getBySetmealId



3. **测试验证**

测试顺序：`nginx.exe` >  `VinoHouseApplication` > http://localhost:80 > 登录 > 套餐管理

启售：

![image-20250609153944126](https://gitee.com/Koletis/pic-go/raw/master/202506091539377.png)

停售：

![image-20250609154023714](https://gitee.com/Koletis/pic-go/raw/master/202506091540969.png)



# 3. 点餐业务模块

## 3.1 环境搭建

使用 Redis 作为数据库。Redis 的 Java 客户端：

- Jedis：官方推荐。
- Lettuce：基于 Netty 框架。
- Spring Data Redis：对 Jedis 和 Lettuce 进行了封装。

### 3.1.1 Spring Data Redis

VinoHouse-server

- config > RedisConfiguration
- resources > application-dev.yml、application.yml > redis
- test > SpringDataRedisTest

### 3.1.2 测试验证

测试顺序：`redis-server.exe redis.windows.conf` > `Another Redis Desktop Manager.exe`  >  `SpringDataRedisTest`

 `Another Redis Desktop Manager.exe` ：

![image-20250609220705370](https://gitee.com/Koletis/pic-go/raw/master/202506092207601.png)

 `SpringDataRedisTest`：

![image-20250609212713291](https://gitee.com/Koletis/pic-go/raw/master/202506092127497.png)



## 3.2 店铺营业状态设置

### 3.2.1 需求分析与设计

万酒屋后台可查看酒馆营业状态：

- 营业中：自动接收所有订单，客户可通过小程序下单；
- 打烊中：拒绝所有订单，小程序下单功能关闭。

接口设计：

- 设置营业状态：基于 Redis 字符串存储，`1`= 营业中，`0`= 打烊中；
- 管理端查询营业状态：路径前缀 `/admin`，接口 `/admin/shop/status`；
- 用户端查询营业状态：路径前缀 `/user`，接口 `/user/shop/status`。



### 3.2.2 代码开发

1. **设置营业状态**

VinoHouse-server > conntroller > admin > ShopController > setStatus

2. **管理端查询营业状态**

VinoHouse-server > conntroller > admin > ShopController > getStatus

3. **用户端查询营业状态**

VinoHouse-server > conntroller > user > ShopController > getStatus



### 3.2.3 测试验证

**1）通过接口文档测试**

1. 设置营业状态

测试顺序：`redis-server.exe redis.windows.conf` > `Another Redis Desktop Manager.exe`  >  `nginx.exe` >  `VinoHouseApplication` >  http://localhost:8080/doc.html > 店铺相关接口

![image-20250609215618186](https://gitee.com/Koletis/pic-go/raw/master/202506092156453.png)

查看 IDEA 控制台日志：

![image-20250609215859325](https://gitee.com/Koletis/pic-go/raw/master/202506092158641.png)

查看 Redis 中数据：

> ③ 看最后一个数，即：1。

![image-20250609220019608](https://gitee.com/Koletis/pic-go/raw/master/202506092200853.png)

2. 管理端查询营业状态

![image-20250609220256609](https://gitee.com/Koletis/pic-go/raw/master/202506092202878.png)

3. 用户端查询营业状态

![image-20250609220411544](https://gitee.com/Koletis/pic-go/raw/master/202506092204804.png)

**2）通前后端联调测试**

测试顺序：`redis-server.exe redis.windows.conf` > `nginx.exe` >  `VinoHouseApplication` > http://localhost:80 > 登录 > 营业状态设置 > 打烊中 > 确定

![image-20250609222836745](https://gitee.com/Koletis/pic-go/raw/master/202506092228032.png)

打烊中：

![image-20250609222938098](https://gitee.com/Koletis/pic-go/raw/master/202506092229360.png)

小程序界面：

> *3.3* 之后才能进行该测试。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202507040941038.png" alt="image-20250704094119467" style="zoom:80%;" />

测试成功。



### 3.2.4 代码优化

在 *3.2.3* 接口文档测试中，管理端和用户端的接口放在一起，不方便区分。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506092209214.png" alt="image-20250609220912992" style="zoom:67%;" />

可以通过以下代码实现管理端和用户端接口的物理隔离。

```java
// WebMvcConfiguration
@Bean
public Docket docket1(){
    //...
    // 指定生成接口需要扫描的包
    .apis(RequestHandlerSelectors.basePackage("com.VinoHouse.controller.admin"))
   //...
}
@Bean
public Docket docket2(){
    //...
    // 指定生成接口需要扫描的包
    .apis(RequestHandlerSelectors.basePackage("com.VinoHouse.controller.admin"))
   //...
}
```

![image-20250609222120573](https://gitee.com/Koletis/pic-go/raw/master/202506092221862.png)



## 3.3 微信登录、商品浏览

微信开发者工具 工程名：VinoHouse-weixin。

### 3.3.1 HttpClient

1. **介绍**

**HttpClient**：是 Apache Jakarta Common 下的子项目，可以提供高效、最新、功能丰富的支持 HTTP 协议的客户端编程工具包，并且支持 HTTP 协议最新的版本和建议。

**作用**：发送 HTTP 请求、接收响应数据。

**核心API**：

- HttpClient：Http 客户端对象类型，使用该类型对象可发起 Http 请求。
- HttpClients：可认为是构建器，可创建 HttpClient 对象。
- CloseableHttpClient：实现类，实现了 HttpClient 接口。
- HttpGet：Get 方式请求类型。
- HttpPost：Post 方式请求类型。

**发送请求步骤**：

- 创建 HttpClient 对象；
- 创建 HttpGet/HttpPost 请求对象；
- 创建参数列表（POST）；
- 调用 HttpClient 的 execute 方法发送请求，接受响应结果；
- 解析结果；
- 关闭资源。



2. **使用及测试**

测试顺序：`redis-server.exe redis.windows.conf` > `Another Redis Desktop Manager.exe`  >  `nginx.exe` >  `VinoHouseApplication` 

**1）GET 方式请求**

> 状态码有几率 500，如果是 500 重新按着测试顺序进行一遍，并更新 token。

VinoHouse-server > test > HttpClientTest > testGET

![image-20250610113912080](https://gitee.com/Koletis/pic-go/raw/master/202506101139358.png)

**2）POST 方式请求**

VinoHouse-server > test > HttpClientTest > testPOST

![image-20250610114054408](https://gitee.com/Koletis/pic-go/raw/master/202506101140657.png)



### 3.3.2 导入用户端代码

> 小程序的开发本质上属于前端开发，主要使用 JavaScript 开发。

1. **导入代码**

> AppID：`wx1561e3829392b939`

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506101624951.png" alt="image-20250610162402683" style="zoom:80%;" />

2. **微信登录流程**

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506101633152.jpeg" alt="img" style="zoom:80%;" />

步骤分析：

- 小程序端，调用 wx.login() 获取授权码（code）；
- 小程序端，调用 wx.request() 发送请求并携带 code，请求开发者服务器（后端服务）；
- 开发者服务端，通过 HttpClient 向微信接口服务发送请求，并携带 appId + appsecret + code 三个参数；
- 开发者服务端，接收微信接口服务返回的数据，session_key（会话密钥） + opendId（微信用户的唯一标识）等；
- 开发者服务端，自定义登录态，生成令牌（token）和 openid 等数据返回给小程序端，方便后续请求身份校验；
- 小程序端，收到自定义登录态，存储 storage；
- 小程序端，后绪通过 wx.request() 发起业务请求时，携带 token；
- 开发者服务端，收到请求后，通过携带的 token，解析当前登录用户的 id；
- 开发者服务端，身份校验通过后，继续相关的业务逻辑处理，最终返回业务数据。

3. **使用 Postman 进行测试**

测试顺序：`redis-server.exe redis.windows.conf` >  `VinoHouseApplication`

**1）获取授权码（js_code）**

点击确定按钮，获取授权码，每个授权码只能使用一次，每次测试，需重新获取。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506101724816.png" alt="image-20250610172449451" style="zoom:80%;" />

![image-20250610220135178](https://gitee.com/Koletis/pic-go/raw/master/202506102201494.png)

**2）明确请求接口**

微信服务接口地址：`https://api.weixin.qq.com/sns/jscode2session`

请求参数：

```Query Params
appid:wx1561e3829392b939
secret:1ce5c30c4b349572432e551be53411ba
js_code:0c3p0J000tXDpU1NXW20099RLQ1p0J03  // 每次测试需要重新获取
grant_type:authorization_code
```

完整请求数据 ：`https://api.weixin.qq.com/sns/jscode2session?appid=wx1561e3829392b939&secret=1ce5c30c4b349572432e551be53411ba&js_code=0c3p0J000tXDpU1NXW20099RLQ1p0J03&grant_type=authorization_code`

**3）发送请求**

黄②：输入完整请求数据。

或者“黄②输入微信服务接口地址 + 绿②输入请求参数”。

![image-20250610221109945](https://gitee.com/Koletis/pic-go/raw/master/202506102211278.png)

若出现 `code been used` 错误提示，说明 `js_code` 已被使用过，请重新获取。

![image-20250610221655016](https://gitee.com/Koletis/pic-go/raw/master/202506102216273.png)



### 3.3.3 微信登录

> 用户端仅支持申请该小程序的微信号（开发者）扫码登录，其他微信号扫码登录，主页菜单呈现空白。
>
> 机型：iPhone 12/13 (Pro Max)；调试基础库：2.20.3。

1. **需求分析与设计**

业务规则：

- 基于微信登录实现小程序的登录功能；
- 如果是新用户需要自动完成注册。

后端微信登录接口说明：请求路径 `/user/user/login`，第一个 user 代表用户端，第二个 user 代表用户模块。

`user` 表手机号 `phone` 字段说明：个人身份注册的小程序，无权限获取微信用户手机号；企业资质注册的小程序，可获取微信用户手机号。



2. **代码开发**

VinoHouse-server

- config > WebMvcConfiguration > addInterceptors
- interceptor > JwtTokenUserInterceptor > preHandle
- conntroller > user > UserController > login
- service > UserService > wxLogin
- service > impl > UserServiceImpl > wxLogin、getOpenid
- mapper > UserMapper > getByOpenid、insert
- resources > mapper > UserMapper.xml > insert
- resources > 
  - application.yml > wechat、jwt
  - application-dev.yml > wechat



3. **功能测试**

测试顺序：`redis-server.exe redis.windows.conf` >  `VinoHouseApplication` > [微信开发者工具] 编译 > [微信开发者工具] 登录

调试器：

![image-20250610230141950](https://gitee.com/Koletis/pic-go/raw/master/202506102301238.png)

后台日志：

![image-20250610230242752](https://gitee.com/Koletis/pic-go/raw/master/202506102302178.png)

`user` 表：

![image-20250610230449317](https://gitee.com/Koletis/pic-go/raw/master/202506102304609.png)



### 3.3.4 商品浏览

1. **需求分析与设计**

业务规则：

- 成功登录后自动进入店铺首页；
- 首页根据分类来展示酒水、套餐；
- 酒水含口味，则显示"选择规格"按钮，否则显示"＋"按钮；
- 套餐始终显示"＋"按钮。

接口设计：

- 查询分类；
- 根据分类 id 查询酒水、套餐；
- 根据套餐 id 查询包含的酒水。



2. **代码导入**

VinoHouse-server

- controller > user > BeverageController、CategoryController、SetmealController
- service > 
  - BeverageService > listWithFlavor

  - SetmealService > list、getBeverageItemBySetmealId

- service > impl > 
  - BeverageServicelmpl > listWithFlavor
  - SetmealServicelmpl > list、getBeverageItemBySetmealId

- mapper > SetmealMapper > list、getBeverageItemBySetmealId
- resources > mapper > SetmealMapper.xml > list



3. **测试验证**

测试顺序：`redis-server.exe redis.windows.conf` >  `VinoHouseApplication` > [微信开发者工具] 编译 > [微信开发者工具] 登录

查询分类：

![image-20250610232713195](https://gitee.com/Koletis/pic-go/raw/master/202506102327534.png)

根据分类 id 查询酒水：

![image-20250610233029370](https://gitee.com/Koletis/pic-go/raw/master/202506102330702.png)

根据分类 id 查询套餐：

![image-20250704102633993](https://gitee.com/Koletis/pic-go/raw/master/202507041026551.png)

酒水口味查询：

![image-20250610233145527](https://gitee.com/Koletis/pic-go/raw/master/202506102331856.png)

根据套餐 id 查询包含的酒水：

![image-20250611114225331](https://gitee.com/Koletis/pic-go/raw/master/202506111142790.png)



## 3.4 缓存商品

### 3.4.1 缓存酒水

1. **需求分析与设计**

通过 Redis 来缓存酒水数据，减少数据库查询操作。

![image-20250611115311598](https://gitee.com/Koletis/pic-go/raw/master/202506111153892.png)

缓存逻辑分析：

- 加入缓存：每个分类下的酒水保存一份缓存数据；
- 修改酒水：数据库中酒水数据有变更时清理缓存数据。



2. **代码开发**

VinoHouse-server

- controller > 
  - admin > BeverageController > save、delete、update、startOrStop、cleanCache
  - user > BeverageController > list



3. **测试验证**

测试顺序：`redis-server.exe redis.windows.conf` > `Another Redis Desktop Manager.exe` > `nginx.exe`  >  `VinoHouseApplication`

**1）加入缓存**

登录微信小程序，选择干邑白兰地（categoryId = 18）。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506111646266.png" alt="image-20250611164616919" style="zoom:80%;" />

查看控制台 SQL：有查询语句，说明是从数据库中进行查询。

![image-20250611164751426](https://gitee.com/Koletis/pic-go/raw/master/202506111647744.png)

查看 Redis 中的缓存数据：说明缓存成功。

![image-20250611164944425](https://gitee.com/Koletis/pic-go/raw/master/202506111649761.png)

点击“进口红酒”，清空控制台，微信小程序再次访问“干邑白兰地”。

![image-20250611170545387](https://gitee.com/Koletis/pic-go/raw/master/202506111705680.png)

无查询语句，说明是从 Redis 中查询数据。

**2）修改酒水**

登录管理端（http://localhost:80）> 修改“干邑白兰地”分类下的任一酒水。

![image-20250611171320508](https://gitee.com/Koletis/pic-go/raw/master/202506111713951.png)

刷新 Redis，缓存数据已清空缓存。

![image-20250611171555156](https://gitee.com/Koletis/pic-go/raw/master/202506111715473.png)

微信小程序再次访问“干邑白兰地”时，会重新查询数据库，并将结果同步到 Redis 中。



### 3.4.2 缓存套餐

1. **需求分析与设计**

Spring Cache 是基于注解的缓存框架，通过抽象层无缝切换 EHCache、Caffeine、Redis 等多种缓存实现。导入对应的 maven 坐标就可以实现切换，因为注解是通用的。

| **注解**       | **说明**                                                     |
| -------------- | ------------------------------------------------------------ |
| @EnableCaching | 开启缓存注解功能，通常加在启动类上。                         |
| @Cacheable     | 在方法执行前先查询缓存中是否有数据，如果有数据，则直接返回缓存数据；如果没有缓存数据，调用方法并将方法返回值放到缓存中。 |
| @CachePut      | 将方法的返回值放到缓存中。                                   |
| @CacheEvict    | 将一条或多条数据从缓存中删除。                               |

实现步骤：

- 导入 Spring Cache 和 Redis 相关 maven 坐标；
- 在启动类上加入 @EnableCaching 注解，开启缓存注解功能；
- 在用户端接口 SetmealController 的 list 方法上加入 @Cacheable 注解；
- 在管理端接口 SetmealController 的 save、delete、update、startOrStop 等方法上加入 CacheEvict 注解。



2. **代码开发**

VinoHouse-server

- VinoHouseApplication
- controller > 
  - admin > SetmealController > save、delete、update、startOrStop
  - user > SetmealController > list



3. **测试验证**

测试顺序：`redis-server.exe redis.windows.conf` > `Another Redis Desktop Manager.exe` > `nginx.exe`  >  `VinoHouseApplication`

**1）加入缓存**

登录微信小程序，选择VIP尊贵品鉴套餐（categoryId = 15）。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506111801521.png" alt="image-20250611180145163" style="zoom:80%;" />

查看控制台 SQL：有查询语句，说明是从数据库中进行查询。

![image-20250611180305030](https://gitee.com/Koletis/pic-go/raw/master/202506111803348.png)

查看 Redis 中的缓存数据：说明缓存成功。

![image-20250611180349791](https://gitee.com/Koletis/pic-go/raw/master/202506111803103.png)

点击“欢乐时光双人套餐”，清空控制台，微信小程序再次访问“VIP尊贵品鉴套餐”。

![image-20250611180505701](https://gitee.com/Koletis/pic-go/raw/master/202506111805994.png)

无查询语句，说明是从 Redis 中查询数据。

**2）修改套餐**

登录管理端（http://localhost:80）> 修改“VIP尊贵品鉴套餐”分类下的任一酒水。

![image-20250611180646670](https://gitee.com/Koletis/pic-go/raw/master/202506111806004.png)

刷新 Redis，缓存数据已清空缓存。

![image-20250611180757065](https://gitee.com/Koletis/pic-go/raw/master/202506111807428.png)

微信小程序再次访问“VIP尊贵品鉴套餐”时，会重新查询数据库，并将结果同步到 Redis 中。



## 3.5 购物车

### 3.5.1 添加购物车

1. **需求分析与设计**

业务规则：

- 用户可以将酒水、套餐添加到购物车；
- 酒水如果设置了口味信息，则需要“选择规格”后才能加入购物车；
- 购物车可修改酒水、套餐的数量，清空购物车。

接口设计：

- 购物车数据与用户关联，表结构需记录各用户的购物车数据。
- 酒水列表含套餐与酒水，用户选套餐存套餐 id（setmeal_id），选酒水存酒水 id（beverage_id）。
- 同一酒水/套餐选多份时，不新增记录，直接增加数量（number）。



2. **代码开发**

VinoHouse-server

- controller > user > ShoppingCartController > add
- service > ShoppingCartService > addShoppingCart
- service > impl > ShoppingCartServicelmpl > addShoppingCart
- mapper > ShoppingCartMapper > list、updateNumberById、insert
- resources > mapper > ShoppingCartMapper.xml > list



3. **测试验证**

测试顺序：`redis-server.exe redis.windows.conf`  >  `VinoHouseApplication` > [微信开发者工具] 编译 > [微信开发者工具] 登录

添加酒水、套餐：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506112244933.png" alt="image-20250611224406498" style="zoom:80%;" />

`shopping_cart` 表：

![image-20250611224655981](https://gitee.com/Koletis/pic-go/raw/master/202506112246293.png)

因为现在没有实现查看购物车功能，所以只能在表中进行查看。



### 3.5.2 查看购物车

1. **需求分析与设计**

当用户添加完酒水、套餐后，可进入到购物车中，查看购物中的酒水、套餐。



2. **代码开发**

VinoHouse-server

- controller > user > ShoppingCartController > list
- service > ShoppingCartService > showShoppingCart
- service > impl > ShoppingCartServicelmpl > showShoppingCart



3. **测试验证**

测试顺序：`redis-server.exe redis.windows.conf`  >  `VinoHouseApplication` > [微信开发者工具] 编译 > [微信开发者工具] 登录 > [微信开发者工具] 购物车图标

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506112252310.png" alt="image-20250611225216951" style="zoom:80%;" />



### 3.5.3 清空购物车

1. **需求分析与设计**

当点击"🗑️清空"时，会把购物车中的数据全部清空。



2. **代码开发**

VinoHouse-server

- controller > user > ShoppingCartController > clean
- service > ShoppingCartService > cleanShoppingCart
- service > impl > ShoppingCartServicelmpl > cleanShoppingCart
- mapper > ShoppingCartMapper > deleteByUserId



3. **测试验证**

测试顺序：`redis-server.exe redis.windows.conf`  >  `VinoHouseApplication` > [微信开发者工具] 编译 > [微信开发者工具] 登录

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506112257693.png" alt="image-20250611225717401" style="zoom:80%;" />

`shopping_cart` 表：

![image-20250611225814514](https://gitee.com/Koletis/pic-go/raw/master/202506112258792.png)

说明当前用户的购物车数据已全部删除。



### 3.5.4 删除购物车商品

1. **需求分析与设计**

点击购物车中的"-"，商品数量减一。



2. **代码开发**

VinoHouse-server

- controller > user > ShoppingCartController > sub
- service > ShoppingCartService > subShoppingCart
- service > impl > ShoppingCartServicelmpl > subShoppingCart
- mapper > ShoppingCartMapper > deleteById



3. **测试验证**

测试顺序：`redis-server.exe redis.windows.conf`  >  `VinoHouseApplication` > [微信开发者工具] 编译 > [微信开发者工具] 登录

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506112305717.png" alt="image-20250611230543385" style="zoom:80%;" />



## 3.6 用户地址簿

> 单表增删改查。

### 3.6.1 需求分析与设计

同一个用户可以有多个地址信息，但是只能有一个**默认地址**。

业务规则：

- 查询地址列表；
- 新增地址；
- 修改地址；
- 删除地址；
- 设置默认地址；
- 查询默认地址。

接口设计：

- 查询登录用户所有地址；
- 新增地址；
- 根据 id 查询地址；
- 根据 id 修改地址；
- 根据 id 删除地址；
- 设置默认地址；
- 查询默认地址。



### 3.6.2 代码导入

VinoHouse-server

- controller > user > AddressBookController > list、save、getById、update、deleteById、setDefault、getDefault
- service > AddressBook > list、save、getById、update、deleteById、setDefault

- service > impl > AddressBooklmpl > list、save、getById、update、deleteById、setDefault

- mapper > AddressBookMapper > list、insert、getById、update、deleteById、updateIsDefaultByUserId
- resources > mapper > AddressBookMapper.xml > list、update



### 3.6.3 测试验证

测试顺序：`redis-server.exe redis.windows.conf` >  `VinoHouseApplication` > [微信开发者工具] 编译 > [微信开发者工具] 登录

地址管理：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506121421726.png" alt="image-20250612142152233" style="zoom:80%;" />

新增地址：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506121455627.png" alt="image-20250612145523342" style="zoom:80%;" />

查询登录用户所有地址：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506121430777.png" alt="image-20250612143020485" style="zoom:80%;" />

`address_boss` 表：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506121434830.png" alt="image-20250612143407542" style="zoom:80%;" />

设置、查看默认地址：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506121433458.png" alt="image-20250612143304166" style="zoom:80%;" />

`address_boss` 表：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506121435331.png" alt="image-20250612143549050" style="zoom:80%;" />

根据 id 查询、修改地址：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506121448710.png" alt="image-20250612144843390" style="zoom:80%;" />

`address_boss` 表：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506121449535.png" alt="image-20250612144928241" style="zoom:80%;" />

根据 id 删除地址：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506121454124.png" alt="image-20250612145405743" style="zoom:80%;" />

`address_boss` 表：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506121440204.png" alt="image-20250612144007892" style="zoom:80%;" />



## 3.7 用户下单

### 3.7.1 需求分析与设计

用户将酒水、套餐加入购物车后，可以点击购物车中的"去结算"按钮，页面跳转到订单确认页面，点击"去支付"按钮则完成下单操作，同时清空购物车。

> 最后一张图要完成 *3.8* “订单支付”才能显示。

![image-20250612171728283](https://gitee.com/Koletis/pic-go/raw/master/202506121717737.png)

注：每个商品需加收 1 元餐盒费，配送费固定为 6 元，预计下单后 1 小时内送达。



### 3.7.2 代码开发

VinoHouse-server

- controller > user > OrderController > submit
- service > OrderService > submitOrder

- service > impl > OrderServiceImpl > submitOrder

- mapper > 
  - OrderMapper > insert
  - OrderDetailMapper > insertBatch
- resources > mapper > 
  - OrderMapper.xml > insert
  - OrderDetailMapper.xml > insertBatch



### 3.7.3 测试验证

测试顺序：`redis-server.exe redis.windows.conf` >  `VinoHouseApplication` > [微信开发者工具] 编译 > [微信开发者工具] 登录

查看购物车：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506121552503.png" alt="image-20250612155256094" style="zoom:80%;" />

`shopping_car` 表：

![image-20250612155334686](https://gitee.com/Koletis/pic-go/raw/master/202506121553984.png)

去结算 > 支付订单

> 订单地址会自动选上默认地址。因为还没完成 *3.8* “订单支付”功能，因此点击确认支付无反应。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506121559507.png" alt="image-20250612155956133" style="zoom:80%;" />

`orders` 表：

![image-20250612155617218](https://gitee.com/Koletis/pic-go/raw/master/202506121556489.png)

`order_detail` 表：

![image-20250612155553952](https://gitee.com/Koletis/pic-go/raw/master/202506121555241.png)

`shopping_car` 表：

![image-20250612155752463](https://gitee.com/Koletis/pic-go/raw/master/202506121557766.png)



### 3.7.4 代码优化

校验收货地址是否超出配送范围。

1. **准备工作**

登录百度地图开放平台：[百度地图](https://lbsyun.baidu.com/)。这里使用的是手机号登录。

控制台 > 应用管理 > 我的应用 > 创建应用：

- 应用名称：万酒屋；
- 应用类型：服务端；
- 启用服务：默认；
- 请求校验方式：IP白名单；
- IP白名单：0.0.0.0/0。

![image-20250615161551660](https://gitee.com/Koletis/pic-go/raw/master/202506151615043.png)

AK：`1hybSH6Z56KGXXvbuIazkPXN5aNxY8rS`

![image-20250615161850620](https://gitee.com/Koletis/pic-go/raw/master/202506151618971.png)



2. **代码开发**

VinoHouse-server

- service > impl > OrderServiceImpl > checkOutOfRange、submitOrder
- resources > application.yml > shop、baidu



3. **测试验证**

测试顺序：`redis-server.exe redis.windows.conf` > `nginx.exe` >   `VinoHouseApplication`

> 这里测试的是 `vino-house.shop.address` = 上海市浦东新区xxx。

模拟器：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506151718751.png" alt="image-20250615171852424" style="zoom:80%;" />

调试器：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506151717792.png" alt="image-20250615171737423" style="zoom:80%;" />

控制台：

![image-20250615172020543](https://gitee.com/Koletis/pic-go/raw/master/202506151720894.png)



## 3.8 订单支付

由于微信支付需企业资质（商户号），个人开发者无法申请。因此，本项目未接入真实支付功能，而是通过**模拟流程**实现下单逻辑，用于学习微信支付的交互流程。

### 3.8.1 需求分析与设计

微信小程序支付时序图：关键步骤 5、10、13。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506121604134.png" alt="image-20250612160423807" style="zoom:80%;" />

- [5：调用微信下单接口] [JSAPI/小程序下单](https://pay.weixin.qq.com/doc/v3/merchant/4012791856)：商户系统调用该接口在微信支付服务后台生成预支付交易单。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506131427263.png" alt="image-202506131427263"  />

- [10：[调起微信支付](https://pay.weixin.qq.com/doc/v3/merchant/4012791857)] 通过 JSAPI 下单接口获取到发起支付的必要参数 prepay_id，然后使用微信支付提供的小程序方法调起小程序支付。

![image-20250613142814542](https://gitee.com/Koletis/pic-go/raw/master/202506131428858.png)

通过内网穿透微信后台可以调用到商户系统。

- 内网穿透工具：cpolar 3.2.82；
- 获取临时域名指令：`cpolar.exe http 8080`（在 cpolar 安装目录下进入 CMD）；

- 临时域名：https://xxxx.rx.cpolar.top。



### 3.8.2 代码导入

VinoHouse-server

- controller > 
  - user > OrderController > payment
  - nofity > PayNotifyController > paySuccessNotify、readData、decryptData、responseToWeixin
- service > OrderService > payment、paySuccess

- service > impl > OrderServiceImpl > payment、paySuccess

- mapper > OrderMapper > getByNumberAndUserId、update
- resources > 
  - application-dev.yml、application.yml > wechat
  - mapper > OrderMapper.xml > update



由于个人账户申请的小程序无法开通微信支付功能，需对代码进行如下修改：

- VinoHouse-server > controller > user > OrderController > payment

```java
public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
    // ...
    // 业务处理：修改订单状态、来单状态
    // 模拟调用微信支付接口
    orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
    log.info("模拟交易成功：{}", ordersPaymentDTO.getOrderNumber());
	// ...
}
```

- VinoHouse-server > service > impl > OrderServiceImpl > payment

```java
public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
	// ...
    // 2. 模拟调用微信支付
    // 生成空 JSON，跳过微信支付流程
    JSONObject jsonObject = new JSONObject();
	// ...
}
```

- 用户端 > index.js

```js
// 1. 真实调用微信支付
// wx.requestPayment({
//   nonceStr: res.data.nonceStr,
//   package: res.data.packageStr,
//   paySign: res.data.paySign,
//   timeStamp: res.data.timeStamp,
//   signType: res.data.signType,
//   success:function(res){
//     wx.showModal({
//       title: '提示',
//       content: '支付成功',
//       success:function(){
//         uni.redirectTo({url: '/pages/success/index?orderId=' + _this.orderId });
//       }
//     })
//     console.log('支付成功!')
//   }
// })
// 2. 模拟调用微信支付
wx.showModal({
  title: '提示',
  content: '支付成功',
  success:function(){
    uni.redirectTo({url: '/pages/success/index?orderId=' + _this.orderId});
  }
})
console.log('支付成功!')
```



### 3.8.3 测试验证

测试顺序：`redis-server.exe redis.windows.conf` >  `VinoHouseApplication` > [微信开发者工具] 编译 > [微信开发者工具] 登录

下单：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506121719549.png" alt="image-20250612171929225" style="zoom:80%;" />

支付：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506121720239.png" alt="image-20250612172035847" style="zoom:80%;" />

支付成功：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506121720589.png" alt="image-20250612172047295" style="zoom:80%;" />



## 3.9 历史订单

> 用户端

### 3.9.1 查询历史订单

1. **需求分析与设计**

业务规则：

- 分页查询历史订单；
- 可以根据订单状态查询；
- 展示订单数据时，需要展示的数据包括：下单时间、订单状态、订单金额、订单明细（商品名称、图片）。



2. **代码开发**

VinoHouse-server

- controller > user > OrderController > page
- service > OrderService > pageQuery4User
- service > impl > OrderServiceImpl > pageQuery4User
- mapper > 
  - OrderMapper > pageQuery
  - OrderDetailMapper > getByOrderId
- resources > mapper > OrderMapper.xml > pageQuery



3. **测试验证**

测试顺序：`redis-server.exe redis.windows.conf` >  `VinoHouseApplication` > [微信开发者工具] 编译 > [微信开发者工具] 登录 > 个人中心 > 历史订单

![image-20250613164516534](https://gitee.com/Koletis/pic-go/raw/master/202506131645132.png)



### 3.9.2 订单详情

1. **需求分析与设计**

点击想要查看的订单，即可进入”订单详情“页面。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506131707267.png" alt="image-20250613170705900" style="zoom:80%;" />



2. **代码开发**

VinoHouse-server

- controller > user > OrderController > Result
- service > OrderService > details
- service > impl > OrderServiceImpl > details
- mapper > OrderMapper > getById



3. **测试验证**

测试顺序：`redis-server.exe redis.windows.conf` >  `VinoHouseApplication` > [微信开发者工具] 编译 > [微信开发者工具] 登录 > 个人中心 > 订单详情

> 在“最近订单/历史订单”页面点击订单都可以进入“订单详情”。

![image-20250613165827131](https://gitee.com/Koletis/pic-go/raw/master/202506131658612.png)



### 3.9.3 取消订单

1. **需求分析与设计**

业务规则：

- 待支付、待接单状态下，用户可直接取消订单；
- 已接单、派送中状态下，用户取消订单需电话沟通商家；
- ~~如果在待接单状态下取消订单，需要给用户退款；~~因非商家主体，暂无法调用微信退款接口，功能暂时无法实现；
- 取消订单后需要将订单状态修改为“已取消”。



2. **代码开发**

VinoHouse-server

- controller > user > OrderController > cancel
- service > OrderService > userCancelById
- service > impl > OrderServiceImpl > userCancelById



3. **测试验证**

测试顺序：`redis-server.exe redis.windows.conf` >  `VinoHouseApplication` > [微信开发者工具] 编译 > [微信开发者工具] 登录 > 个人中心 > 订单详情 > 取消订单

待支付、待接单状态下：

![image-20250613171211427](https://gitee.com/Koletis/pic-go/raw/master/202506131712904.png)

已接单、派送中状态下：

![image-20250615144704621](https://gitee.com/Koletis/pic-go/raw/master/202506151447979.png)



### 3.9.4 再来一单

1. **需求分析与设计**

再来一单就是将原订单中的商品重新加入到购物车中。



2. **代码开发**

VinoHouse-server

- controller > user > OrderController > repetition
- service > OrderService > repetition
- service > impl > OrderServiceImpl > repetition
- mapper > ShoppingCartMapper > insertBatch
- resources > mapper > ShoppingCartMapper.xml > insertBatch



3. **测试验证**

测试顺序：`redis-server.exe redis.windows.conf` >  `VinoHouseApplication` > [微信开发者工具] 编译 > [微信开发者工具] 登录 > 个人中心 > 再来一单

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506131736653.png" alt="image-20250613173657154" style="zoom: 67%;" />



## 3.10 订单管理

> 管理端（商家端）

### 3.10.1 订单搜索

1. **需求分析与设计**

业务规则：

- 输入订单号/手机号进行搜索，支持模糊搜索；
- 根据订单状态/下单时间进行筛选；
- 搜索内容为空，提示未找到相关订单；
- 分页展示搜索到的订单数据。



2. **代码开发**

VinoHouse-server

- controller > admin > OrderController > conditionSearch
- service > OrderService > conditionSearch
- service > impl > OrderServiceImpl > conditionSearch、getOrderVOList、getOrderDishesStr



3. **测试验证**

测试顺序：`redis-server.exe redis.windows.conf` > `nginx.exe` >   `VinoHouseApplication` > http://localhost:80 > 登录 > 订单管理

输入订单号/手机号进行搜索，支持模糊搜索：

![image-20250615111724654](https://gitee.com/Koletis/pic-go/raw/master/202506151117049.png)

根据订单状态/下单时间进行筛选：

![image-20250615111905804](https://gitee.com/Koletis/pic-go/raw/master/202506151119218.png)

搜索内容为空，提示未找到相关订单：

![image-20250615105335917](https://gitee.com/Koletis/pic-go/raw/master/202506151053294.png)

分页展示搜索到的订单数据：

![image-20250615105126356](https://gitee.com/Koletis/pic-go/raw/master/202506151051803.png)



### 3.10.2 各个状态的订单数量统计

1. **需求分析与设计**

在订单管理界面，显示“待接单”、“待派送”、“派送中”的订单数量。



2. **代码开发**

VinoHouse-server

- controller > admin > OrderController > statistics
- service > OrderService > statistics
- service > impl > OrderServiceImpl > statistics
- mapper > OrderMapper > countStatus



3. **测试验证**

测试顺序：`redis-server.exe redis.windows.conf` > `nginx.exe` >   `VinoHouseApplication` > http://localhost:80 > 登录 > 订单管理

![image-20250615135354099](https://gitee.com/Koletis/pic-go/raw/master/202506151353529.png)



### 3.10.3 查询订单详情

1. **需求分析与设计**

订单详情页面需要展示订单基本信息（状态、订单号、下单时间、收货人、电话、收货地址、金额等）、订单明细数据（商品名称、数量、单价）。



2. **代码开发**

VinoHouse-server > controller > admin > OrderController > details



3. **测试验证**

测试顺序：`redis-server.exe redis.windows.conf` > `nginx.exe` >   `VinoHouseApplication` > http://localhost:80 > 登录 > 订单管理

![image-20250615140041821](https://gitee.com/Koletis/pic-go/raw/master/202506151400229.png)



### 3.10.4 接单

1. **需求分析与设计**

商家接单其实就是将订单的状态修改为“已接单”。



2. **代码开发**

VinoHouse-server

- controller > admin > OrderController > confirm
- service > OrderService > confirm
- service > impl > OrderServiceImpl > confirm



3. **测试验证**

测试顺序：`redis-server.exe redis.windows.conf` > `nginx.exe` >   `VinoHouseApplication` > http://localhost:80 > 登录 > 订单管理

![image-20250615140412573](https://gitee.com/Koletis/pic-go/raw/master/202506151404984.png)



### 3.10.5 拒单

1. **需求分析与设计**

业务规则：

- 商家拒单其实就是将订单状态修改为“已取消”；
- 只有订单处于“待接单”状态时可以执行拒单操作；
- 商家拒单时，应从预设原因中选择或填写自定义原因，且自定义原因内容不得为空；
- ~~商家拒单时，需要为用户退款。~~因非商家主体，暂无法调用微信退款接口，功能暂时无法实现。



2. **代码开发**

VinoHouse-server

- controller > admin  > OrderController > rejection
- service > OrderService > rejection
- service > impl > OrderServiceImpl > rejection



3. **测试验证**

测试顺序：`redis-server.exe redis.windows.conf` > `nginx.exe` >   `VinoHouseApplication` > http://localhost:80 > 登录 > 订单管理

预设原因：

![image-20250615141201028](https://gitee.com/Koletis/pic-go/raw/master/202506151412399.png)

自定义原因：

![image-20250615141853219](https://gitee.com/Koletis/pic-go/raw/master/202506151418600.png)



### 3.10.6 取消订单

1. **需求分析与设计**

业务规则：

- 取消订单其实就是将订单状态修改为“已取消”；
- 商家取消订单时，应从预设原因中选择或填写自定义原因，且自定义原因内容不得为空；
- ~~商家取消订单时，需要为用户退款。~~因非商家主体，暂无法调用微信退款接口，功能暂时无法实现。



2. **代码开发**

VinoHouse-server

- controller > admin > OrderController > cancel
- service > OrderService > cancel
- service > impl > OrderServiceImpl > cancel



3. **测试验证**

测试顺序：`redis-server.exe redis.windows.conf` > `nginx.exe` >   `VinoHouseApplication` > http://localhost:80 > 登录 > 订单管理

预设原因：

![image-20250615142512441](https://gitee.com/Koletis/pic-go/raw/master/202506151425816.png)

自定义原因：

![image-20250615142350652](https://gitee.com/Koletis/pic-go/raw/master/202506151423077.png)

已取消：

> 这里的”取消原因“包含两个部分：拒单、取消订单。

![image-20250615154338324](https://gitee.com/Koletis/pic-go/raw/master/202506151543751.png)



### 3.10.7 派送订单

1. **需求分析与设计**

业务规则：

- 派送订单其实就是将订单状态修改为“派送中”；
- 只有状态为“待派送”的订单可以执行派送订单操作。



2. **代码开发**

VinoHouse-server

- controller > admin > OrderController > delivery
- service > OrderService > delivery
- service > impl > OrderServiceImpl > delivery



3. **测试验证**

测试顺序：`redis-server.exe redis.windows.conf` > `nginx.exe` >   `VinoHouseApplication` > http://localhost:80 > 登录 > 订单管理

![image-20250615154955267](https://gitee.com/Koletis/pic-go/raw/master/202506151549649.png)



### 3.10.8 完成订单

1. **需求分析与设计**

业务规则：

- 完成订单其实就是将订单状态修改为“已完成”；
- 只有状态为“派送中”的订单可以执行订单完成操作。



2. **代码开发**

VinoHouse-server

- controller > admin > OrderController > complete
- service > OrderService > complete
- service > impl > OrderServiceImpl > complete



3. **测试验证**

测试顺序：`redis-server.exe redis.windows.conf` > `nginx.exe` >   `VinoHouseApplication` > http://localhost:80 > 登录 > 订单管理

![image-20250615155234997](https://gitee.com/Koletis/pic-go/raw/master/202506151552382.png)



## 3.11 Spring Task

**Spring Task** 是 Spring 框架提供的任务调度工具，可以按照约定的时间自动执行某个代码逻辑。

定位：定时任务框架。

作用：定时自动执行某段 Java 代码。

应用场景：

- 信用卡每月还款提醒；
- 银行贷款每月还款提醒；
- 火车票售票系统处理未支付订单；
- 入职纪念日为用户发送通知。

注：只要是需要**定时处理**的场景都可以使用 Spring Task。



### 3.11.1 Cron 表达式

**Cron 表达式**本质上是一个字符串，通过 Cron 表达式可以定义任务触发的时间。

构成规则：分为 6/7 个域，由空格分隔开，每个域代表不同的时间单位——秒、分钟、小时、日、月、周、年（可选）。

举例：2022 年 10 月 12 日上午 9 点整 —— `0 0 9 12 10 ? 2022`。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506151940801.png" alt="image-20250615194013349" style="zoom:67%;" />

注：

- 通常**日**和**周**不同时设置（因两者均用于指定具体日期），若设置其中一个，另一个需用 `?` 占位。上图示例时间可写为 `0 0 9 ? 10 3#2 2022`，其中 `3#2` 表示第 2 个周三（3 代表周三，#2 代表第 2 个）。
- 当需要描述不确定或复杂的时间规则时，可使用特殊字符。
  - 如，2 月天数在平年为 28 天、闰年为 29 天，无法确定具体日期，此时可使用特殊字符 `L`（表示月份的最后一天，用于日、周字段）。即，2 月的最后一天可表示为 `0 0 0 L 2 ?`。
  - 如，要表示最接近指定日期（10 号）的工作日，此时可使用特殊字符 `W`（仅用于日字段）。即，每月离 10 号最近的工作日可表示为 `0 0 0 10W * ?`。


Cron 表达式在线生成器：https://cron.qqe2.com/

通配符：

- \* 表示所有值，对于不同时间单位范围不一样：秒 0-59、分钟 0-59 、小时 0-23、日 1-31、月 1-12、周 1-7、年 1970-2099； 
- ? 表示未说明的值，即不关心它为何值； 
- \- 表示一个指定的范围； 
- , 表示附加一个可能值； 
- / 符号前表示开始时间，符号后表示每次递增的值。

案例：

|  秒  | 分钟 |   小时   |  日  |  月  |  周  |                        含义                         |
| :--: | :--: | :------: | :--: | :--: | :--: | :-------------------------------------------------: |
| */5  |  *   |    *     |  *   |  *   |  ?   |                   每 5 秒执行一次                   |
|  0   | */1  |    *     |  *   |  *   |  ?   |                  每 1 分钟执行一次                  |
|  0   |  0   |   5-15   |  *   |  *   |  ?   |        每天 5:00-15:00 间，每 1 小时执行一次        |
|  0   | 0/3  |    *     |  *   |  *   |  ?   |                  每 3 分钟执行一次                  |
|  0   | 0-5  |    14    |  *   |  *   |  ?   |       每天 14:00-14:05 间，每 1 分钟执行一次        |
|  0   | 0/5  |    14    |  *   |  *   |  ?   |       每天 14:00-14:55 间，每 5 分钟执行一次        |
|  0   | 0/5  |  14,18   |  *   |  *   |  ?   | 每天 14:00-14:55、18:00-18:55 间，每 5 分钟执行一次 |
|  0   | 0/30 |   9-17   |  *   |  *   |  ?   |       每天 9:00-17:00 间，每 30 分钟执行一次        |
|  0   |  0   | 10,14,16 |  *   |  *   |  ?   |          每天 10:00、14:00、16:00 执行一次          |



### 3.11.2 代码开发

VinoHouse-server

- VinoHouse > @EnableScheduling
- task > MyTask



### 3.11.3 测试验证

测试顺序：`redis-server.exe redis.windows.conf` >  `VinoHouseApplication` 

控制台：

![image-20250615205236591](https://gitee.com/Koletis/pic-go/raw/master/202506152052009.png)

每隔 5 秒执行一次。



## 3.12 订单状态定时处理

### 3.12.1 需求分析与设计

用户下单后可能存在的情况：

- 下单后未支付，订单一直处于“待支付”状态；
- 派送完成后，管理端未点击完成按钮，订单一直处于“派送中”状态。

对于上面两种情况需要通过**定时任务**来修改订单状态，具体逻辑为：

- 通过定时任务每分钟检查一次是否存在支付超时订单（下单后超过 15 分钟仍未支付则判定为支付超时订单），如果存在则修改订单状态为“已取消”；
- 通过定时任务每天凌晨 1 点检查一次是否存在“派送中”的订单，如果存在则修改订单状态为“已完成”。



### 3.12.2 代码开发

VinoHouse-server

- task > OrderTask > processTimeoutOrder、processDeliveryOrder
- mapper > OrderMapper > getByStatusAndOrderTimeLT



### 3.12.3 测试验证

测试顺序：`redis-server.exe redis.windows.conf` >  `VinoHouseApplication` 

1）“待支付”：1 → 6

`orders` 表：订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款

![image-20250615210740935](https://gitee.com/Koletis/pic-go/raw/master/202506152107287.png)

控制台：

![image-20250615212816494](https://gitee.com/Koletis/pic-go/raw/master/202506152128851.png)

`orders` 表：

![image-20250615212643191](https://gitee.com/Koletis/pic-go/raw/master/202506152126526.png)

2）“派送中”：4 → 5

`orders` 表：

![image-20250615213634010](https://gitee.com/Koletis/pic-go/raw/master/202506152136358.png)

控制台：

![image-20250615213513927](https://gitee.com/Koletis/pic-go/raw/master/202506152135284.png)

`orders` 表：

![image-20250615213555063](https://gitee.com/Koletis/pic-go/raw/master/202506152135418.png)



## 3.13 WebSocket

**WebSocket** 是基于 TCP 的一种新的网络协议。它实现了浏览器与服务器全双工通信——浏览器和服务器只需要完成一次握手，两者之间就可以创建**持久性**的连接， 并进行**双向**数据传输。

与 HTTP 协议对比：

- WebSocket 是**长连接**，HTTP 是**短连接**；
- WebSocket 支持**双向**通信，HTTP 通信是**单向**的（基于请求响应模式）；
- WebSocket 和 HTTP 底层都基于 TCP 连接。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506161034669.png" alt="image-20250616103409295" style="zoom: 67%;" />

缺点：

- 服务器长期维护长连接需要一定的成本;
- 各个浏览器支持程度不一；
- WebSocket 是长连接，受网络限制比较大，需要处理好重连。

应用场景：

- 视频弹幕；
- 网页聊天；
- 体育实况更新；
- 股票基金报价实时更新。



### 3.13.1 需求分析与设计

需求：实现浏览器与服务器全双工通信，即浏览器既可以向服务器发送消息，服务器也可主动向浏览器推送消息。

实现步骤：

- 直接使用 websocket.html 页面作为 WebSocket 客户端；

- 导入 WebSocket 的 maven 坐标；

- 导入 WebSocket 服务端组件 WebSocketServer，用于和客户端通信；

- 导入配置类 WebSocketConfiguration，注册 WebSocket 的服务端组件；

- 导入定时任务类 WebSocketTask，定时向客户端推送数据。



### 3.13.2 代码开发

前端 > websocket.html

后端 > VinoHouse-server

- websocket > WebSocketServer > onOpen、onMessage、onClose、sendToAllClient
- config > WebSocketConfiguration > serverEndpointExporter
- task > WebSocketTask > sendMessageToClient



### 3.13.3 测试验证

测试顺序：`redis-server.exe redis.windows.conf` >  `VinoHouseApplication`  > `websocket.html`

① （双击 `websocket.html` 后）客户端 xxx 建立连接

② 服务器每 5 秒向浏览器推送数据

③ 发送消息：1234

④ 收到来自客户端 xxx 的信息：1234

⑤ 关闭连接

⑥ close

⑦ 客户端 xxx 连接断开

![image-20250615220236084](https://gitee.com/Koletis/pic-go/raw/master/202506152202479.png)



## 3.14 来单提醒与客户催单

### 3.14.1 需求分析与设计

用户下单并且支付成功后，需第一时间通知外卖商家。通知的形式有两种：语音播报、弹出提示框。

设计思路：

- 通过 WebSocket 实现管理端页面和服务端保持长连接状态；
- 当客户支付后，调用 WebSocket 的相关 API 实现服务端向客户端推送消息；
- 客户端浏览器解析服务端推送的消息，判断是来单提醒还是客户催单，进行相应的消息提示和语音播报；
- 约定服务端发送给客户端浏览器的数据格式为 JSON，字段包括：type，orderId，content。
  - type 为消息类型，1 为来单提醒，2 为客户催单；
  - orderId 为订单 id；
  - content 为消息内容。



### 3.14.2 代码开发

VinoHouse-server

- controller > user > OrderController > reminder
- service > OrderService > reminder
- service > impl > OrderServiceImpl > paySuccess、reminder



### 3.14.3 测试验证

> WebSocket 无法自动重连。因此，每次后端服务   `VinoHouseApplication`  重启时，管理端 Web 需重新登录以完成双方的 WebSocket 握手连接。

测试顺序：`redis-server.exe redis.windows.conf` > `nginx.exe` >  `VinoHouseApplication` >

**1）来电提醒**

 http://localhost:80 > 登录 > F12 > 网络 Netwotk > 名称 Name > 标头 Headers 

> 这里的 ws 是 WebSocket 的缩写。

![image-20250615224159355](https://gitee.com/Koletis/pic-go/raw/master/202506152241704.png)

控制台：

![image-20250615224224347](https://gitee.com/Koletis/pic-go/raw/master/202506152242725.png)

下单支付： [微信开发者工具] 编译 > [微信开发者工具] 登录

![image-20250615224937042](https://gitee.com/Koletis/pic-go/raw/master/202506152249485.png)

支付成功后，后台实时接收来单提醒并语音播报，点击「待接单」提示框可跳转至订单详情页查看并接单： 

![image-20250615224422115](https://gitee.com/Koletis/pic-go/raw/master/202506152244512.png)

**2）客户催单**

下单支付： [微信开发者工具] 编译 > [微信开发者工具] 登录

![image-20250615230215378](https://gitee.com/Koletis/pic-go/raw/master/202506152302775.png)

催单后，后台实时接收来单提醒并语音播报，点击「催单」提示框可跳转至订单详情页查看并接单： 

![image-20250615225831044](https://gitee.com/Koletis/pic-go/raw/master/202506152258484.png)



# 4. 统计报表模块

本项目采用基于 JavaScript 的数据可视化图表库 Apache ECharts 进行开发。

注意：数据格式由前端图表库固定要求决定，后端需按前端指定格式返回数据（如特定 JSON 结构），确保直接适配图表渲染。

## 4.1 营业额统计

### 4.1.1 需求分析与设计

营业额统计以动态折线图展示，横轴日期根据时间选择器（如近 7 天、本月）自动生成，纵轴显示每日营业额，悬停数据点可查看具体数值。

业务规则：

- 营业额指订单状态为已完成的订单金额合计；
- 基于可视化报表的折线图展示营业额数据，X 轴为日期，Y 轴为营业额；
- 根据时间选择区间，展示每天的营业额数据。



### 4.1.2 代码开发

VinoHouse-server

- controller > admin > ReportController > turnoverStatistics
- service > ReportService > getTurnoverStatistics

- service > impl > ReportServiceImpl > getTurnoverStatistics

- mapper > OrderMapper > sumByMap
- resources > mapper > OrderMapper.xml > sumByMap



### 4.1.3 测试验证

测试顺序：`redis-server.exe redis.windows.conf` > `nginx.exe` >  `VinoHouseApplication` > http://localhost:80 > 登录 > F12 > 网络 Netwotk > 名称 Name > 预览 Preview

1）查看昨日营业额统计

![image-20250617142510447](https://gitee.com/Koletis/pic-go/raw/master/202506171425936.png)

2）查看近7日营业额统计

![image-20250617142608324](https://gitee.com/Koletis/pic-go/raw/master/202506171426754.png)

3）查看近30日营业额统计

![image-20250617142652549](https://gitee.com/Koletis/pic-go/raw/master/202506171426009.png)

4）查看本周营业额统计

![image-20250617142727058](https://gitee.com/Koletis/pic-go/raw/master/202506171427582.png)

5）查看本月营业额统计

![image-20250617142804427](https://gitee.com/Koletis/pic-go/raw/master/202506171428877.png)



## 4.2 用户统计

### 4.2.1 需求分析与设计

用户统计通过折线图展示两个核心指标：蓝色线表示每日总用户量，绿色线表示每日新增用户量。

业务规则：

- 基于可视化报表的折线图展示用户数据，X 轴为日期，Y 轴为用户数；
- 根据时间选择区间，展示每天的用户总量和新增用户量数据。



### 4.2.2 代码开发

VinoHouse-server

- controller > admin > ReportController > userStatistics
- service > ReportService > getUserStatistics

- service > impl > ReportServiceImpl > getUserStatistics、getUserCount

- mapper > UserMapper > countByMap
- resources > mapper > UserMapper.xml > countByMap



### 4.2.3 测试验证

测试顺序：`redis-server.exe redis.windows.conf` > `nginx.exe` >  `VinoHouseApplication` > http://localhost:80 > 登录 > F12 > 网络 Netwotk > 名称 Name > 预览 Preview

1）查看昨日用户统计

![image-20250617152843319](https://gitee.com/Koletis/pic-go/raw/master/202506171528846.png)

2）查看近7日用户统计

![image-20250617152936591](https://gitee.com/Koletis/pic-go/raw/master/202506171529179.png)

3）查看近30日用户统计

![image-20250617153013630](https://gitee.com/Koletis/pic-go/raw/master/202506171530088.png)

4）查看本周用户统计

![image-20250617153041795](https://gitee.com/Koletis/pic-go/raw/master/202506171530269.png)

5）查看本月用户统计

![image-20250617153118458](https://gitee.com/Koletis/pic-go/raw/master/202506171531905.png)



## 4.3 订单统计

### 4.3.1 需求分析与设计

订单统计采用双线折线图展示：蓝色线表示每日订单总量，绿色线表示每日有效订单量（状态为已完成）。图表上方同步显示统计周期内的订单总数、有效订单数、订单完成率三项汇总数据。

业务规则：

- 有效订单指状态为“已完成”的订单；
- 基于可视化报表的折线图展示订单数据，X 轴为日期，Y 轴为订单数量；
- 根据时间选择区间，展示每天的订单总数和有效订单数；
- 展示所选时间区间内的有效订单数、总订单数、订单完成率，订单完成率 = 有效订单数 / 总订单数 * 100%。



### 4.3.2 代码开发

VinoHouse-server

- controller > admin > ReportController > orderStatistics
- service > ReportService > getOrderStatistics

- service > impl > ReportServiceImpl > getOrderStatistics、getOrderCount

- mapper > OrderMapper > countByMap
- resources > mapper > OrderMapper.xml > countByMap



### 4.3.3 测试验证

1）查看昨日订单统计


![image-20250617153858149](https://gitee.com/Koletis/pic-go/raw/master/202506171538664.png)

2）查看近7日订单统计


![image-20250617153755668](https://gitee.com/Koletis/pic-go/raw/master/202506171537272.png)

3）查看近30日订单统计


![image-20250617153932556](https://gitee.com/Koletis/pic-go/raw/master/202506171539036.png)

4）查看本周订单统计


![image-20250617154009739](https://gitee.com/Koletis/pic-go/raw/master/202506171540178.png)

5）查看本月订单统计


![image-20250617154038141](https://gitee.com/Koletis/pic-go/raw/master/202506171540612.png)



## 4.4 销量排名 Top10

### 4.4.1 需求分析与设计

销量排名统计展示酒水与套餐的销售量 Top10，采用降序排列的柱状图直观呈现商品销量对比。

业务规则：

- 根据时间选择区间，展示销量前 10 的商品（包括酒水和套餐）；
- 基于可视化报表的柱状图降序展示商品销量；
- 此处的销量为商品销售的份数。



### 4.4.2 代码开发

VinoHouse-server

- controller > admin > ReportController > top10
- service > ReportService > getSalesTop10

- service > impl > ReportServiceImpl > getSalesTop10

- mapper > OrderMapper > getSalesTop10
- resources > mapper > OrderMapper.xml > getSalesTop10



### 4.4.3 测试验证

测试顺序：`redis-server.exe redis.windows.conf` > `nginx.exe` >  `VinoHouseApplication` > http://localhost:80 > 登录 > F12 > 网络 Netwotk > 名称 Name > 预览 Preview

1）查看昨日销量排名 Top10


![image-20250617153555601](https://gitee.com/Koletis/pic-go/raw/master/202506171535066.png)

2）查看近7日销量排名 Top10


![image-20250617153524121](https://gitee.com/Koletis/pic-go/raw/master/202506171535661.png)

3）查看近30日销量排名 Top10


![image-20250617153452008](https://gitee.com/Koletis/pic-go/raw/master/202506171534575.png)

4）查看本周销量排名 Top10


![image-20250617153352298](https://gitee.com/Koletis/pic-go/raw/master/202506171533768.png)

5）查看本月销量排名 Top10


![image-20250617153301640](https://gitee.com/Koletis/pic-go/raw/master/202506171533085.png)



## 4.5 工作台

工作台是系统运营的数据看板，并提供快捷操作入口，可以有效提高商家的工作效率。

### 4.5.1 需求分析与设计

工作台展示的数据：今日数据、订单管理、酒水总览、套餐总览、订单信息。

名词解释：

- 营业额：已完成订单的总金额；
- 有效订单：已完成订单的数量；
- 订单完成率：有效订单数 / 总订单数 * 100%；
- 平均客单价：营业额 / 有效订单数；
- 新增用户：新增用户的数量。

接口设计：

- 今日数据接口；
- 订单管理接口；
- 酒水总览接口；
- 套餐总览接口；
- 订单搜索（已完成）；
- 各个状态的订单数量统计（已完成）。



### 4.5.2 代码导入

VinoHouse-server

- controller > admin > WorkSpaceController > businessData、orderOverView、beverageOverView、setmealOverView
- service > WorkspaceService > getBusinessData、getOrderOverView、getBeverageOverView、getSetmealOverView

- service > impl > WorkspaceServiceImpl > getBusinessData、getOrderOverView、getBeverageOverView、getSetmealOverView

- mapper > BeverageMapper、SetmealMapper > countByMap
- resources > mapper > BeverageMapper.xml、SetmealMapper.xml > countByMap



### 4.5.3 测试验证

**1）通过接口文档测试**

测试顺序：`redis-server.exe redis.windows.conf` > `nginx.exe` >  `VinoHouseApplication` > http://localhost:8080/doc.html > 工作台相关接口

今日数据查询：

![image-20250618135304163](https://gitee.com/Koletis/pic-go/raw/master/202506181353792.png)

酒水总览查询：

![image-20250618135356568](https://gitee.com/Koletis/pic-go/raw/master/202506181353076.png)

订单管理数据查询：

![image-20250618135445074](https://gitee.com/Koletis/pic-go/raw/master/202506181354511.png)

套餐总览查询：

![image-20250618135535991](https://gitee.com/Koletis/pic-go/raw/master/202506181355461.png)

**2）通前后端联调测试**

测试顺序：`redis-server.exe redis.windows.conf` > `nginx.exe` >  `VinoHouseApplication` > http://localhost:80 > 登录 > 工作台

![image-20250618135649759](https://gitee.com/Koletis/pic-go/raw/master/202506181356239.png)

今日数据查询：

![image-20250618140216425](https://gitee.com/Koletis/pic-go/raw/master/202506181402926.png)

订单管理数据查询：

![image-20250618140336171](https://gitee.com/Koletis/pic-go/raw/master/202506181403695.png)

酒水总览查询：

![image-20250618140417228](https://gitee.com/Koletis/pic-go/raw/master/202506181404727.png)

套餐总览查询：

![image-20250618140451055](https://gitee.com/Koletis/pic-go/raw/master/202506181404513.png)



## 4.6 Apache POI

### 4.6.1 需求分析与设计

Apache POI 是处理 Microsoft Office 文件格式的 Java 开源项目，常用于 Excel 文件读写操作 。

应用场景：

- 银行网银系统导出交易明细；
- 各种业务系统导出 Excel 报表；
- 批量导入业务数据。



### 4.6.2 代码开发

VinoHouse-server > test > POITest > write、read



### 4.6.3 测试验证

测试顺序：POITest 

D 盘 Excel：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506181408298.png" alt="image-20250618140800882" style="zoom:80%;" />

控制台：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506181407335.png" alt="image-20250618140723889" style="zoom:80%;" />



## 4.7 导出运营数据 Excel 报表

### 4.7.1 需求分析与设计

在数据统计页面设置数据导出按钮，点击后下载含最近 30 日运营数据的 Excel 文件。文件采用固定表格格式，由概览数据与明细数据两部分组成，导出后自动填充数据以便存档。

业务规则：导出最近 30 天运营数据的 Excel 报表文件。

接口设计：

- 接口无需传参，直接导出后端计算的最近 30 天运营数据；

- 接口无返回值，通过服务端输出流将 Excel 文件下载至客户端浏览器。

实现步骤：

- 设计 Excel 模板文件；
- 查询近 30 天的运营数据；
- 将查询到的运营数据写入模板文件；
- 通过输出流将 Excel 文件下载到客户端浏览器。



### 4.7.2 代码开发

VinoHouse-server

- controller > admin > ReportController > export
- service > ReportService > exportBusinessData

- service > impl > ReportServiceImpl > exportBusinessData

- mapper > OrderMapper > getSalesTop10
- resources > mapper > OrderMapper.xml > getSalesTop10



### 4.7.3 测试验证

测试顺序：`redis-server.exe redis.windows.conf` > `nginx.exe` >  `VinoHouseApplication` > http://localhost:80 > 登录 > 数据统计 > 数据导出 > 确定

![image-20250618142253642](https://gitee.com/Koletis/pic-go/raw/master/202506181422134.png)

浏览器下载文件，查看 Excel 数据：

![image-20250618142431376](https://gitee.com/Koletis/pic-go/raw/master/202506181424857.png)



# 前端 - 管理端

Vue 工程名：VinoHouse-vue-ts。

技术栈：HTML、CSS、JavaScript、Axios、Vue、Element UI

## 1. Vue 

Vue.js（通常简称为 **Vue**）是一个用于构建用户界面的渐进式 **JavaScript 框架**。

### 1.1 环境配置

基于脚手架创建前端工程，需要具备如下环境要求：

- node.js：前端项目的运行环境
- npm：JavaScript 的包管理工具
- Vue CLI： 基于 Vue 进行快速开发的完整系统，实现交互式的项目脚手架。

下载后，查看：

```cmd
node -v
npm -v
vue -V  # 大写
```

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506201618646.png" alt="image-20250620161818171" style="zoom:80%;" />



### 1.2 测试验证

使用 Vue CLI 创建前端工程有两种方式，本项目主要使用第二种方法：

1. `vue create 项目名称`：

> vue3-demo

以管理员身份执行命令行：

```cmd
D:
cd D:\DevelopmentTools\Vue_WorkPlace
vue create vue3-demo
```

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506221758754.png" alt="image-20250622175841339" style="zoom:80%;" />

完成创建：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506221756339" alt="img-202506212134299" style="zoom:80%;" />

2. `vue ui`：

> vue-demo-1

以管理员身份执行命令行（停止是 Ctrl + C），浏览器弹出 “Vue 项目管理器”界面。

```cmd
D:
cd D:\DevelopmentTools\Vue_WorkPlace
vue ui
```

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506201646564.png" alt="image-20250620164636150" style="zoom:80%;" />

已经在本地创建了存放项目的文件夹：`D:\DevelopmentTools\Vue_WorkPlace`。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506201633512.png" alt="image-20250620163329103" style="zoom:80%;" />

项目文件夹、包管理器、初始化 git 仓库（建议）。

> 初始化 git 仓库（建议）按下图没勾选的话，后期要使用 git 需要自己配置。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506201638978.png" alt="image-20250620163827565" style="zoom:80%;" />

Default (Vue 2)。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506201639788.png" alt="image-20250620163949378" style="zoom:80%;" />

完成创建。

![image-20250620164905536](https://gitee.com/Koletis/pic-go/raw/master/202506201649279.png)



### 1.3 工程结构

工程目录结构：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506201651686.png" alt="image-20250620165158257" style="zoom:80%;" />

重点文件或目录介绍：

- node_modules：当前项目依赖的 js 包；
- src > 
  - assets：静态资源存放目录，如图片；
  - components：公共组件存放目录；
  - App.vue：项目的主组件，页面的入口文件；
  - main.js：整个项目的入口文件；
- package.json：项目的配置信息、依赖包管理；
- vue.config.js：vue-cli 配置文件。



### 1.4 启动前端服务

使用 IDEA 打开创建的前端工程，并在终端输入 `npm run serve`。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506201658904.png" alt="image-20250620165807432" style="zoom:80%;" />

上面命令里的 `serve` 并非固定写法，它来自 `package.json` 中自动定义的配置项（下图），执行该命令即运行对应脚本。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506221809499.png" alt="image-20250622180944101" style="zoom:80%;" />

访问前端工程：`http://localhost:8080/`。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506201659158.png" alt="image-20250620165950692" style="zoom:80%;" />

注：终端使用 Ctrl + C 可停止前端服务（下图）。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506221829821.png" alt="image-20250622182937396" style="zoom:80%;" />

前端项目服务端口默认为 8080，和后端 tomcat 端口号冲突，在 vue.config.js 中配置前端服务端口号：

~~~javascript
const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,  // 这里有一个逗号
  devServer: {
    port: 7070  //指定前端服务端口号
  }
})
~~~

![image-20250620171300231](https://gitee.com/Koletis/pic-go/raw/master/202506201713671.png)

配置完后，终端重新输入  `npm run serve`，前端工程地址为 `http://localhost:7070/`。



## 2. Axios

Axios 是一个基于 promise 的 网络请求库，作用于浏览器和 Node.js 中。使用 Axios 可以在前端项目中发送各种方式的 HTTP 请求。

管理员安装命令：

```cmd
D:
cd D:\DevelopmentTools\Vue_WorkPlace\vue-demo-1
npm install axios
```

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506212102067.png" alt="image-20250621210231625" style="zoom:80%;" />

解决 Axios 跨域问题，在 vue.config.js 文件中配置代理：

```javascript
// ...
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    pathRewrite: {
      '^/api': ''
    }
  }
}
// ...
```

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506212112333.png" alt="image-20250621211201875" style="zoom:80%;" />

导入语句：

```vue
<script>
import axios from 'axios'
</script>
```

进行以下请求，需要启动后端服务 `redis-server.exe redis.windows.conf` > `VinoHouseApplication`。

post 请求示例：

```javascript
// 通过 Axios 发送 POST 请求
axios.post('api/admin/employee/login',{
	username:'admin',
	password: '123456'
}).then(res => {  // 调用成功
	console.log(res.data)
}).catch(error => {  // 调用失败
	console.log(error.response)
})
```

get 请求示例：

```javascript
// 通过 Axios 发送 GET 请求
axios.get('/api/admin/shop/status',{
    headers: {
        token: 'xxx.yyy.zzz'  // 令牌
    }
}).then(res => {
	console.log(res.data)
})
```

统一调用方式：

```javascript
// 通过 Axios 提供的统一调用方式发送请求
  axios({
    url: 'api/admin/employee/login',
    method: 'post',
    data:{  // 通过请求体传参
      username:'admin',
      password: '123456'
    }
  }).then(res => {
    // res.data 为完整返回数据，其中 res.data.data 对应后端返回 JSON 中的 data 字段，该 data 内嵌套 token。
    console.log(res.data.data.token)
    axios({
      url: '/api/admin/shop/status',
      method: 'get',
      headers: {
        token: res.data.data.token
      }
    })
  })
```



## 3. Vue-Router

Vue 属于单页面应用，所谓路由，就是监听浏览器地址变化，动态切换页面中需要更新的视图组件。

如下图所示：不同的访问路径，对应不同的页面展示。

- 登录页面：http://localhost:8888/#/login

![image-20250704143410959](https://gitee.com/Koletis/pic-go/raw/master/202507041434720.png)

- 工作台：http://localhost:8888/#/dashboard

![image-20250704143542278](https://gitee.com/Koletis/pic-go/raw/master/202507041435895.png)



### 3.1 安装

在 Vue 应用中使用路由功能，需要安装 Vue-Router，同样使用 `vue ui` 创建工程：

> vue-router-demp

以管理员身份执行命令行（停止是 Ctrl + C），浏览器弹出 “Vue 项目管理器”界面。

```cmd
D:
cd D:\DevelopmentTools\Vue_WorkPlace
vue ui
```

在此创建新项目：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506232141317.png" alt="image-20250623214121210" style="zoom:80%;" />

项目文件夹（vue-router-demo）、包管理器（npm）：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506232137270.png" alt="image-20250623213742131" style="zoom:80%;" />

手动：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506232142653.png" alt="image-20250623214219547" style="zoom:80%;" />

勾选 Router：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506232143252.png" alt="image-20250623214341146" style="zoom:80%;" />

2.x、ESLint with error prevention only：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506232145674.png" alt="image-20250623214503564" style="zoom:80%;" />

遇到 `Cannot read properties of undefined (reading 'indexOf')` 错误时，请为给 `Authenticated Users` 添加"完全控制"权限（[参考教程](https://blog.csdn.net/D_Zhou_Sir/article/details/124946646)：右键目录→安全→编辑权限）。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506232200637.png" alt="image-20250623220012516" style="zoom:80%;" />

安装完成：

![image-20250623215804657](https://gitee.com/Koletis/pic-go/raw/master/202506232158790.png)



### 3.2 路由配置

路由由以下 3 个部分组成：

1. VueRouter：路由器，根据路由请求/路径在路由视图中动态渲染对应的视图组件。

路由表：

![image-20250623223445872](https://gitee.com/Koletis/pic-go/raw/master/202506232234985.png)

2. <router-link>：路由链接组件，浏览器会解析成超链接 <a>。

3. <router-view>：路由视图组件，用来展示与路由路径匹配的视图组件，类似于占位符，指定位置。

![image-20250623223536453](https://gitee.com/Koletis/pic-go/raw/master/202506232235561.png)

三者关系：

![image-20250623221916643](https://gitee.com/Koletis/pic-go/raw/master/202506232219780.png)

要实现路由跳转，可以通过标签式和编程式两种：

- 标签式：<router-link to="/about">About</router-link>
- 编程式：this.$router.push('/about')。
  - this.$router 是获取到路由对象；
  - push 方法是根据 url 进行跳转。

![image-20250623224610852](https://gitee.com/Koletis/pic-go/raw/master/202506232246961.png)

如果用户访问的路由地址不存在，可以通过配置一个 404 视图组件，当访问的路由地址不存在时，则重定向到此视图组件，具体配置如下：

~~~javascript
// index.js
  {
    path: '/404',
    component: () => import('../views/404View.vue')
  },
  {
    path: '*',  // 所有当前不存在的地址
    redirect: '/404'  // 重定向
  }
~~~



### 3.3 嵌套路由

嵌套路由（子路由）：可以实现在组件内动态切换局部内容。

实现步骤：

1. 安装并导入 [ElementUI](https://element.eleme.io/#/zh-CN/component/installation)，实现页面布局（Container 布局容器）。——ContainerView.vue

终端：

```cmd
npm i element-ui -S
```

![image-20250624093850198](https://gitee.com/Koletis/pic-go/raw/master/202506240938604.png)

main.js：

```javascript
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
// 全局使用 ElementUI
Vue.use(ElementUI);
```

src > views > container > ContainerView.vue

2. 提供子视图组件，用于效果展示。——P1View.vue、P2View.vue、P3View.vue

src > views > container > P1View.vue、P2View.vue、P3View.vue

3. 在 src/router/index.js 中配置路由映射规则（嵌套路由配置）。

src > router > index.js：children

4. 在 ContainerView.vue 布局容器视图中添加 <router-view>，实现子视图组件展示。

```html
<el-main>
    <router-view/>
</el-main>
```

5. 在 ContainerView.vue 布局容器视图中添加 <router-link>，实现路由请求。

```html
<el-aside width="200px">
    <router-link to="/c/p1">P1</router-link><br>
    <router-link to="/c/p2">P2</router-link><br>
    <router-link to="/c/p3">P3</router-link><br>
</el-aside>
```

注意：子路由变化，切换的是【ContainerView 组件】中 `<router-view></router-view>` 部分的内容

问题思考：

1. 对于前面的案例，如果用户访问的路由是 `/c`，呈现什么效果。

![image-20250624114254766](https://gitee.com/Koletis/pic-go/raw/master/202506241142905.png)

2. 如何实现访问 `/c` 时，默认展示某个子视图组件。

配置重定向，当访问 `/c` 时，直接重定向到 `/c/p1` 即可，如下配置：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506241145771.png" alt="image-20250624114535664" style="zoom:80%;" />



## 4. Vuex 

Vuex 是专为 Vue.js 应用设计的状态管理库，采用集中式存储应用所有组件的状态，并以响应式方式支持组件间数据共享与同步更新。

Vuex 应用的核心是 Store（仓库），是包含应用大部分 state（状态）的容器。与普通全局对象不同： 

- 状态存储具备响应式特性（实时更新），组件读取状态后，状态变化会高效触发组件更新。 
- 状态变更需 mutations 通过显式 commit（提交） 实现，便于跟踪状态变化及开发调试工具。

Vuex 中的几个核心概念：

- state：状态对象，集中定义各个组件共享的数据；
- mutations：类似于一个事件，用于**修改共享数据**（唯一手段），要求必须是**同步**函数；
- actions：类似于 mutations，可以包含**异步**操作（axios），通过调用 mutations 来改变共享数据。

使用 `vue ui` 创建工程：

> vue-vuex-demo

以管理员身份执行命令行（停止是 Ctrl + C），浏览器弹出 “Vue 项目管理器”界面。

```cmd
D:
cd D:\DevelopmentTools\Vue_WorkPlace
vue ui
```

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506261215716.png" alt="image-20250626121506188" style="zoom:80%;" />

手动配置：

![image-20250626121612805](https://gitee.com/Koletis/pic-go/raw/master/202506261216307.png)

Vue 2.x、ESLint with error prevention only、创建项目，不保存预设。

在 src/store/index.js 文件中集中定义和管理共享数据：

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506261251409.png" alt="image-20250626125105976" style="zoom:80%;" />



## 5. TypeScript

TypeScript（简称：TS） 是微软推出的开源语言， JavaScript ⊂ TS = JS +  Type（类型支持）。其文件采用 .ts 扩展名，需要编译（tsc）成标准的 JS 才可以运行。编译过程中会进行类型检查，但编译后的 JS 文件不再保留类型信息（类型擦除）。

<img src="https://gitee.com/Koletis/pic-go/raw/master/202506261445481.png" alt="image-20250626144544999"  />

安装命令：`npm install -g typescript`。

查看 TS 版本：`tsc -v`

![image-20250626144907059](https://gitee.com/Koletis/pic-go/raw/master/202506261449514.png)

思考：TS 为什么要增加类型支持 ？

- TS 属于静态类型编程语言，JS 属于动态类型编程语言；
- 静态类型在编译期做类型检查，动态类型在执行期做类型检查；
- 对于 JS 来说，需要等到代码执行的时候才能发现错误（晚）；
- 对于 TS 来说，在代码编译的时候就可以发现错误（早）；
- 配合 VSCode 开发工具，TS 可以提前到在编写代码的同时就发现代码中的错误，减少找 Bug、改 Bug 的时间。

TS 中的常用类型：

| **类型**   | **例**                                  | **备注**                       |
| ---------- | --------------------------------------- | ------------------------------ |
| 字符串类型 | string                                  |                                |
| 数字类型   | number                                  |                                |
| 布尔类型   | boolean                                 |                                |
| 数组类型   | number[], string[],  boolean[] 以此类推 |                                |
| 任意类型   | any                                     | 相当于没有类型                 |
| 复杂类型   | type 与 interface                       |                                |
| 函数类型   | () =>  void                             | 对函数的参数和返回值进行说明   |
| 字面量类型 | "a"\|"b"\|"c"                           | 限制变量或参数的取值，类似枚举 |
| class 类   | class Animal                            |                                |

在前端项目中使用 TS，需要创建基于 TS 的前端工程：

> vue-ts-demo

TypeScript：

![image-20250626151427518](https://gitee.com/Koletis/pic-go/raw/master/202506261514003.png)

.ts 文件：

![image-20250626151744579](https://gitee.com/Koletis/pic-go/raw/master/202506261517058.png)



## 6. 项目环境搭建

前端技术栈：

- Node.js：类似于 JDK；
- Vue：框架，类似 Spring Boot；
- ElementUI：提供页面布局组件；
- Axios：发送异步请求，实现前后端交互；
- Vue-Router：路由，实现单页面应用下不同访问地址渲染对应组件；
- Vuex：状态管理，解决组件间数据共享与状态同步的问题；
- TypeScript：基于 JS，使开发代码更规范。



### 6.1 代码导入与验证

Vue 工程名：VinoHouse-vue-ts

主要代码在 src 目录中，src 目录结构如下：

![image-20250626200243863](https://gitee.com/Koletis/pic-go/raw/master/202506262002332.png)

导入的初始工程后，需要在终端输入 `npm install` 命令，安装前端项目运行所依赖的 JS 包。

必须需要注意 node 版本要为 v12.x ，否则无法下载依赖包，通过 `nvm use 12.22.0` 可切换 Node.js 版本。

![image-20250626220726386](https://gitee.com/Koletis/pic-go/raw/master/202506262207907.png)

安装完成后，会生成了 node_modules 目录。

验证前端项目：npm run serve

![image-20250704143927074](https://gitee.com/Koletis/pic-go/raw/master/202507041439736.png)

注意：需要先启动后端 Java 服务，才能进行前后端交互。



## 7. 员工管理

### 7.1 员工分页查询

1. 需求分析与设计

业务规则：

- 根据页码展示员工信息（员工姓名、账号、手机号、账号状态、最后操作时间等）；
- 每页展示 10 条数据；
- 输入员工姓名进行查询。

实现步骤：

- 制作页面头部效果（输入框、查询按钮等），为查询按钮绑定单击事件发送 Ajax 请求获取员工分页数据，并在页面加载时通过 Vue 初始化方法自动查询首屏数据；
- 使用 ElementUI 提供的表格组件展示分页数据；
- 使用 ElementUI 提供的分页条组件实现翻页效果。



2. 代码开发

在 router.ts 中可以找到员工管理页面 `\src\views\employee\index.vue`（组件）。

![image-20250627203749316](https://gitee.com/Koletis/pic-go/raw/master/202506272037182.png)

- 页面头部：为“查询”按钮绑定单击事件 `@click="pageQuery()"`，并在 `methods` 中定义 `pageQuery` 方法，去调用 Ajax 请求（`src/api/employee.ts/getEmployeelist`），并将 `pageQuery()` 加入生命周期方法 `created` 中，实现自动发送 Ajax 请求。
- 分页数据：直接导入 [Element](https://element.eleme.io/#/zh-CN/component/table) Table 表格中“带斑马纹表格"代码，并将其中代码修改为后端发送的字段名。
- 分页条：直接导入 [Element](https://element.eleme.io/#/zh-CN/component/pagination) Pagination 分页中“附加功能：完整功能"代码，并修改其中的字段名，并在 `methods` 中实现 `handleSizeChange` 、`handleCurrentChange` 方法。



3. 测试验证

测试顺序：`redis-server.exe redis.windows.conf` > `VinoHouseApplication` > `npm run serve` > 登录 > 员工管理

![image-20250704144056015](https://gitee.com/Koletis/pic-go/raw/master/202507041440584.png)



### 7.2 启用禁用员工账号

1. 需求分析与设计

业务规则：

- 可以对状态为“启/禁用” 的员工账号进行“禁/启用”操作；
- 状态为“禁用”的员工账号不能登录系统；
- 禁止停用“管理员”账户状态。



2. 代码开发

`\src\views\employee\index.vue`：

为启/禁用按钮绑定单击事件 `@click="handleStartOrStop(scope.row)"`，并在 `methods` 中实现以下逻辑：

- 若当前账号为管理员（`row.username === 'admin'`），则提示"admin 为系统的管理员账号，禁止修改账号状态！"并终止操作。

- 调用 `this.$confirm()` 弹窗确认是否修改状态，防止误操作。

- 通过 `enableOrDisableEmployee` API（位于 `src/api/employee.ts`）提交状态变更请求：

  - 成功时刷新页面数据并提示操作成功；

  - 取消或失败时给出相应提示。



3. 测试验证

测试顺序：`redis-server.exe redis.windows.conf` > `VinoHouseApplication` > `npm run serve` > 登录 > 员工管理

禁用：

![image-20250704144222706](https://gitee.com/Koletis/pic-go/raw/master/202507041442233.png)

禁用成功：

![image-20250704144315525](https://gitee.com/Koletis/pic-go/raw/master/202507041443073.png)

禁止停用管理员账号状态：

![image-20250628140322961](https://gitee.com/Koletis/pic-go/raw/master/202506281403455.png)



### 7.3 新增员工

1. 需求分析与设计

新增员工时需录入账号、员工姓名、手机号、性别、身份证号等信息，具体要求如下：

- 账号：作为登录凭证，必须唯一；
- 手机号：需为合法的 11 位手机号码； 
- 身份证号：需为合法的 15、18 位身份证号码。 

新员工入职后，可通过新增员工功能开通账号，凭此账号登录商家管理端系统。

实现步骤：

- 在员工管理列表页面，点击“添加员工”按钮，跳转到新增页面；
- 在新增员工页面录入员工相关信息；
- 点击“保存”按钮完成新增操作。



2. 代码开发

`\src\views\employee\index.vue`：

- 为“添加员工”按钮绑定点击事件 `@click="handleAddEmp()"`，并在 `methods` 中进行路由跳转（`/employee/add`）。

`\src\views\employee\addEmployee.vue`：

- 参考 [Element](https://element.eleme.io/#/zh-CN/component/form) 中的”表单验证“代码，在 `data` 中定义模型数据 `ruleForm` 和表单校验规则 `rules`，在 `methods` 中实现 `submitForm` 方法，通过 `addEmployee` API（`src/api/employee.ts`）发送 Ajax 请求，实现前后端交互。



3. 测试验证

测试顺序：`redis-server.exe redis.windows.conf` > `VinoHouseApplication` > `npm run serve` > 登录 > 员工管理 > 添加员工

表单校验：

![image-20250704144446648](https://gitee.com/Koletis/pic-go/raw/master/202507041444225.png)

员工添加成功：

![image-20250704144745411](https://gitee.com/Koletis/pic-go/raw/master/202507041447970.png)



### 7.4 修改员工

1. 需求分析与设计

修改员工信息时，需回显账号、员工姓名、手机号、性别、身份证号等信息。

接口说明：

- 根据 id 查询员工信息；
- 根据 id 修改员工信息。

实现步骤：

- 在员工管理列表页面点击“修改”按钮，跳转到修改页面；
- 在修改员工页面录入员工相关信息；
- 点击“保存”按钮完成修改操作。



2. 代码开发

`\src\views\employee\index.vue`：

- 为“添加员工”按钮绑定点击事件 `@click="handleUpdateEmp(scope.row)`，并在 `methods` 中进行路由跳转（`/employee/add`），并传入修改员工的 id。

`\src\views\employee\addEmployee.vue`：

- 在 `submitForm` 方法中，通过 `updateEmployee` API（`src/api/employee.ts`）发送 Ajax 请求，实现前后端交互。



3. 测试验证

测试顺序：`redis-server.exe redis.windows.conf` > `VinoHouseApplication` > `npm run serve` > 登录 > 员工管理

管理员修改界面：

![image-20250704144846486](https://gitee.com/Koletis/pic-go/raw/master/202507041448050.png)

修改成功：

![image-20250704144917034](https://gitee.com/Koletis/pic-go/raw/master/202507041449610.png)



### 7.5 删除员工

1. 需求分析与设计

仅能删除禁用状态下的员工账户。



2. 代码开发

`\src\views\employee\index.vue`：

为删除按钮绑定单击事件 `@click="handleDelete(scope.row)`，并在 `methods` 中实现以下逻辑：

- 若当前账号为管理员（`row.username === 'admin'`），则提示"admin 为系统的管理员账号，禁止删除账户！"并终止操作。

- 调用 `this.$confirm()` 弹窗确认是否修改状态，防止误操作。

- 通过 `deleteEmployee` API（位于 `src/api/employee.ts`）提交状态变更请求：

  - 成功时刷新页面数据并提示操作成功；

  - 取消或失败时给出相应提示。



3. 测试验证

测试顺序：`redis-server.exe redis.windows.conf` > `VinoHouseApplication` > `npm run serve` > 登录 > 员工管理

删除：

![image-20250704145030690](https://gitee.com/Koletis/pic-go/raw/master/202507041450251.png)

禁止删除启用账户：

![image-20250704145116637](https://gitee.com/Koletis/pic-go/raw/master/202507041451178.png)

禁用账户成功删除：

![image-20250704145204530](https://gitee.com/Koletis/pic-go/raw/master/202507041452110.png)



## 8. 套餐管理

### 8.1 套餐分页查询

1. 需求分析与设计

业务规则：

- 根据页码展示套餐信息（套餐名称、套餐图片、套餐分类、套餐价、售卖状态、最后操作时间等）；
- 每页展示 10 条数据；
- 输入套餐名称、套餐分类、售卖状态进行查询。

接口说明：

- 分类查询接口（用于套餐分类下拉框中 分类数据展示）；
- 套餐分页查询接口。

实现步骤：

- 制作页面头部效果（输入框、下拉框、查询按钮等），为查询按钮绑定单击事件发送 Ajax 请求获取套餐分页数据，并在页面加载时通过 Vue 初始化方法自动查询首屏数据；
- 动态填充套餐分类下拉框中的分类数据；
- 使用 ElementUI 提供的表格组件展示分页数据；
- 使用 ElementUI 提供的分页条组件实现翻页效果。



2. 代码开发

在 router.ts 中可以找到套餐管理页面 `\src\views\setmeal\index.vue`（组件）。

![image-20250629154226556](https://gitee.com/Koletis/pic-go/raw/master/202506291542162.png)

- 页面头部：为“查询”按钮绑定单击事件 `@click="pageQuery()"`，并在 `methods` 中定义 `pageQuery` 方法，去调用 Ajax 请求（`src/api/employee.ts/getSetmealPage`），并将 `pageQuery()` 加入生命周期方法 `created` 中，实现自动发送 Ajax 请求。
- 套餐分类下拉框：直接导入 [Element](https://element.eleme.io/#/zh-CN/component/select) Select 选择器中“基础用法”代码，并将其中代码修改为后端发送的字段名，并在生命周期方法 `created` 中实现 `getCategoryByType` 方法。
- 分页数据：直接导入 [Element](https://element.eleme.io/#/zh-CN/component/table) Table 表格中“带斑马纹表格"代码，并将其中代码修改为后端发送的字段名。
- 分页条：直接导入 [Element](https://element.eleme.io/#/zh-CN/component/pagination) Pagination 分页中“附加功能：完整功能"代码，并修改其中的字段名，并在 `methods` 中实现 `handleSizeChange` 、`handleCurrentChange` 方法。



3. 测试验证

测试顺序：`redis-server.exe redis.windows.conf` > `VinoHouseApplication` > `npm run serve` > 登录 > 套餐管理

![image-20250704145300915](https://gitee.com/Koletis/pic-go/raw/master/202507041453498.png)



### 8.2 启售停售套餐

1. 需求分析与设计

业务规则：

- 可以对状态为“启/停售”的套餐进行“停/启售”操作；
- 状态为“停售”的套餐不展示在用户端小程序中。



2. 代码开发

`\src\views\setmeal\index.vue`：

为启/停售按钮绑定单击事件 `@click="statusHandle(scope.row)"`，并在 `methods` 中实现以下逻辑：

- 调用 `this.$confirm()` 弹窗确认是否修改状态，防止误操作。

- 通过 `enableOrDisableSetmeal` API（位于 `src/api/setMeal.ts`）提交状态变更请求：

  - 成功时刷新页面数据并提示操作成功；

  - 取消或失败时给出相应提示。



3. 测试验证

测试顺序：`redis-server.exe redis.windows.conf` > `VinoHouseApplication` > `npm run serve` > 登录 > 套餐管理

停售：

![image-20250704145338145](https://gitee.com/Koletis/pic-go/raw/master/202507041453720.png)

停售成功：

![image-20250704145546480](https://gitee.com/Koletis/pic-go/raw/master/202507041455076.png)



### 8.3 删除套餐

1. 需求分析与设计

业务规则：

- 点击“删除”按钮可删除指定套餐，勾选需删除的套餐后点击“批量删除”按钮则能删除选中的一/多个套餐；
- 状态为“启售”的套餐提示不能删除。

实现步骤：

- 在套餐管理列表页面，点击“删除”按钮或勾选套餐后点击“批量删除”按钮，弹出确认对话框。
- 点击确认对话框”确定“时，若套餐状态为“启售”则提示不可删除，否则执行删除操作。
- 点击确认对话框”取消“，则关闭对话框且不执行删除操作。



2. 代码开发

`\src\views\setmeal\index.vue`：

为“批量删除”和“删除”绑定单击事件 `@click="handleDelete('xx', xx)"`，并在 `methods` 中实现以下逻辑：

- 调用 `this.$confirm()` 弹窗确认是否修改状态，防止误操作。

- 通过 `deleteSetmeal` API（位于 `src/api/setMeal.ts`）提交状态变更请求：

  - 成功时刷新页面数据并提示操作成功；

  - 取消或失败时给出相应提示。



3. 测试验证

测试顺序：`redis-server.exe redis.windows.conf` > `VinoHouseApplication` > `npm run serve` > 登录 > 套餐管理

删除：

![image-20250704145630690](https://gitee.com/Koletis/pic-go/raw/master/202507041456289.png)

启售中不能删除：

![image-20250704145712373](https://gitee.com/Koletis/pic-go/raw/master/202507041457023.png)

删除成功：

![image-20250704145756956](https://gitee.com/Koletis/pic-go/raw/master/202507041457532.png)



### 8.4 新增/修改套餐

1. 需求分析与设计

业务规则：

- 新增套餐需录入套餐名称、所属分类、价格、包含酒水、图片及描述等信息；
- 修改套餐需回显套餐名称、所属分类、价格、包含酒水、图片及描述等信息；
- 套餐包含酒水需在弹出窗口中按分类勾选添加。

接口设计：

- 根据类型查询分类接口；
- 根据分类查询酒水接口；
- 文件上传接口；
- 新增/修改套餐接口。

实现步骤：

- 点击 “新建套餐”按钮，跳转到新增页面；
- 在新增套餐页面录入套餐相关信息；
- 点击“保存”按钮完成新增操作。



2. 代码开发

`\src\views\setmeal\index.vue`：

为“新增套餐”和“修改”绑定单击事件 `@click="handleAdd(xxx)"`，并在 `methods` 中进行路由跳转（`/setmeal/add`），修改套餐需要传入修改套餐的 id。



3. 测试验证

测试顺序：`redis-server.exe redis.windows.conf` > `VinoHouseApplication` > `npm run serve` > 登录 > 套餐管理

新增套餐页面：

![image-20250629224025801](https://gitee.com/Koletis/pic-go/raw/master/202506292240478.png)

添加酒水：

![image-20250704145944065](https://gitee.com/Koletis/pic-go/raw/master/202507041459718.png)

- 查询并添加

![image-20250704150023326](https://gitee.com/Koletis/pic-go/raw/master/202507041500896.png)

修改：

![image-20250629224158249](https://gitee.com/Koletis/pic-go/raw/master/202506292241795.png)


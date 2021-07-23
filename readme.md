产品后台

部署方法请参见说明工程：https://github.com/blueskylong/aolie_illustrate

后台设计以数据模型为基础，向外扩展。数据模型分方案设计。分方案的目的是为了将业务分开，简化设置界面，其中第一个方案是引用源方案。其定的引用 源是全局数据，其它的方案里可以引用。
数据模型中一个数据表对应数据库中的一张表或视图，数据表之间的连接表示他们之间的关系.
**约定，二张表之间，只能最多存在一种关系，在设置里要注意。每张表需要有一个主键列，并且只能有一个列作为主键列，且列类型要是长整型（bigint）**
数据模型中会定义列的约束条件，作为前后端检查的依据。

1. 数据模型

   数据模型是系统的核心和基础，后续的界面、功能、插件、拦截器等，都是基于此模型。

   数据模型设置包括了元数据设置，表的约束，表内表间公式，表关联关系，引用源配置等。

   **元数据**属性包含数据类型，长度，引用源（枚举项），约束及公式，是否主键等。
   **表约束**指表需要满足的条件，条件是表达式，可以是表内条件，也可以是表外条件或系统参数。
   **字段公式**，可以为字段增加公式，可以是多个，通过不同的条件，应用不同的公式。公式也是一个表达式，可以引用表内，表外和系统的参数。
   **表关联关系**，表关系有一对一，一对多，及多对多的关系。
   
2. 界面设计
   界面设计为可视化操作，可将数据表显示在表格、面板、卡片、树等集成界面中，设置显示的的标题、样式、位置、大小等，还可以通过设置可见条件，使能条件来做额外的控制。
   界面设计器设计出来的界面，可以用在页面配置中，也可以单独用到自定义的代码功能中。
   界面的显示形式可以在调用时显式的指定，只要设计时，必要的信息存在，都可以正常显示，这样是避免为一个数据表生成多个重复的视图。
   界面控件可以自定义，需要前后端注册，后端注册即增加控件类型的数据。
   >界面设计中虽然可以选多个数据源，但多数据源在运行时有多方面的限制，所以先不要使用多数据源。

3. 权限模型
   a. **操作权限**基于spring security,授权码来源有二个
   一个是功能按钮的授权：默认情况下，一个按钮对应着对数据源的操作，有增、删、改的标准操作，用户在被授权时，就取得相应的操作权限.
   另一个是自定义操作权限，在代码里通过注解```@CustomRightPermission("xxx")```来定义需要的权限，而在操作按钮中，附加相应的授权码。
   b. **数据权限**，先将引用源定义成权限组件，再在授权中与用户或角色挂钩，便可实现级联式的权限授权。权限居有传递性。设置数据权限时，一定要注意不要出现循环关系。

4. 插件系统
   每一个模块，都可以认为是一个插件，作为插件，主要是实现了com.ranranx.aolie.core.interfaces.IPlus接口的类，此类负责生成、删除、更新应用的配置数据及表。
   目前系统中的流程管理，报表功能，监控等都是按插件方式开发。
5. 结构化查询
   提供一套类来定义操作数据库的动作，而不直接使用语句或第三方的持久化库，这样做的益处是，可以分析查询的内容，操作的明细信息，以便各拦截器进行干预。目前底层操作数据库是MyBatis.
   结构化查询的主要类有：
   ```
   QueryParam: 查询参数 
   DeleteParam: 删除参数
   UpdateParam: 更新参数
   InsertParam: 插件参数
   Criteria： 复杂条件
   ```
   >计划中是要实现多数据源操作，包括跨数据源的操作，只有这样分解，才好实现此功能。
                                                                                        
6. 数据库操作拦截器
   当前系统中的数据权限，系统监控，都是通过增加拦截器的方式实现。拦截器，可以在对数据库进行增、删、改、查时，对其进行拦截增加相应的逻辑。
   ```java
public interface IOperInterceptor extends Ordered, IHandleFilter {
    /**
     * 操作前调用,如果返回有内容,则会直接返回
     */
    default HandleResult beforeOper(OperParam param, String handleType, Map<String, Object> globalParamData) throws InvalidException {
        return null;
    }

    /**
     * 数据查询过后,整理前调用,如果返回有数据,则直接返回
     */
    default HandleResult afterOper(OperParam param, String handleType, Map<String, Object> globalParamData,
                                   HandleResult result) {
        return null;
    }

    /**
     * 返回前调用,这里会遍历调用,所以要返回结果,不需要处理的就直接返回
     */
    default HandleResult beforeReturn(OperParam param, String handleType, Map<String, Object> globalParamData,
                                      HandleResult handleResult) {
        return null;
    }
}
```

7. 多数据源（TODO）
这是计划中的功能。目前方案中的每一个数据表都可以指定数据源，这对一次数据库操作对同一个库，是没有问题的，但如果一个操作需要跨库，还没有实现。
还有一个重要问题没有解决，跨数据库的事务问题。


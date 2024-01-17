## Module

我将为笔者的模块系统提供一个简单的接口


### IAftermathModule
* 实现此模块的将作为核心模块，用于处理战斗的数据提供

我已提供默认的实现，`BaseRaidModule`
他具有以下数据
* `name` 用于存储该模块的名称,他的数据类型为`String`
* `rewards` 用于存储该模块的奖励,他的数据类型为`ItemWeightedModule`
* `conditions` 用于存储该模块的创建激活条件,他的数据类型为`List<IConditionModule>`
* `waves` 用于存储该模块的战斗波次,他的数据类型为`List<List<IEntityInfoModule>>`
* `ready_time` 用于存储该模块的准备时间,他的数据类型为`int`
* `reward_time` 用于存储该模块的奖励时间,他的数据类型为`int`

```java
//现在我将演示如何创建一个BaseRaid模块
BaseRaidModule module = new BaseRaidModule.Builder("common_raid")
        .readyTime(100)
        .rewardTime(100)
        .waves(List.of(
                List.of(
                        new EntityInfoModule.Builder("minecraft:zombie")
                                .amountModule(new IntegerAmountModule.Builder(5)
                                        .build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:skeleton")
                                .amountModule(new RandomAmountModule.Builder(1,5)
                                        .build())
                                .build()
                ),
                List.of(
                        new EntityInfoWithEquipmentModule.Builder("minecraft:zombie")
                                .canDrop(false)
                                .amountModule(new IntegerAmountModule.Builder(5)
                                        .build())
                                .equipment(new ItemWeightedModule.Builder()
                                        .add("minecraft:iron_helmet", 1)
                                        .add("minecraft:iron_chestplate", 1)
                                        .add("minecraft:iron_leggings", 1)
                                        .add("minecraft:iron_boots", 1)
                                        .build()
                                )
                                .build()
                )
        ))
        .rewards(new ItemWeightedModule.Builder()
                .add("minecraft:stone", 1)
                .add("minecraft:iron_ingot", 1)
                .build()
        )
        .build();

```


### IAmountModule
* 实现此模块的将作为数量模块，用于处理各种模块的数量支持

我已提供个默认的实现，
 1. `IntegerAmountModule`
    1. 他只有一个属性`amount`
    2. 他的构造器为`new IntegerAmountModule.Builder(int amount)`
    3. 在json中他的格式为
    ```json
    {
      "amount_module": {
          "amount": "surviving_the_aftermath:integer_amount",
          "value": 1
      }
    }
    ```

 2. `RandomAmountModule`
    1. 他有两个属性`min`和`max`
    2. 他的构造器为`new RandomAmountModule.Builder(int min,int max)`
    3. 在json中他的格式为
    ```json
    {
      "amount_module": {
          "amount": "surviving_the_aftermath:random_amount",
          "min": 1,
          "max": 5
      }
    }
    ```
    

### IConditionModule
* 实现此模块的将作为条件模块，用于处理各种模块的条件支持

我已提供个默认的实现，
 1. `LevelConditionModule`  
`LevelConditionModule`是一个抽象的模块,他有属性`level`和`pos`
    * `StructureConditionModule`
        1. `StructureConditionModule`可以用于判断玩家是否在某个结构物中
        2. 他的构造器为`new StructureConditionModule.Builder(String structureName)`
        3. 在json中他的格式为
        ```json 
        {
          "condition": "surviving_the_aftermath:structure_condition",
          "value": "surviving_the_aftermath:nether_invasion_portal"
        }
        ```
    * `BiomeConditionModule`
        1. `BiomeConditionModule`可以用于判断玩家是否在某个生物群系中
        2. 他的构造器为`new BiomeConditionModule.Builder(String biomeName)`
        3. 在json中他的格式为
        ```json 
        {
          "condition": "surviving_the_aftermath:biome_condition",
          "value": "minecraft:plains"
        }
        ```
      
    * `YAxisConditionModule`
        1. `YAxisConditionModule`可以用于判断玩家是否在某个Y轴范围内
        2. 他的构造器为`new YAxisConditionModule.Builder(int yAxisHeight,int flag)`
        3. flag的值为0时,表示玩家的Y轴高度等于yAxisHeight,flag的值为1时,表示玩家的Y轴高度大于yAxisHeight,flag的值为-1时,表示玩家的Y轴高度小于yAxisHeight
        4. 在json中他的格式为
           ```json 
           {
             "condition": "surviving_the_aftermath:y_axis_condition",
             "yAxisHeight": 64,
             "flag": 0
           }
           ```
    * `WeatherConditionModule`
      1. `WeatherConditionModule`可以用于判断玩家是否在某个天气中
      2. 他的构造器为`new WeatherConditionModule.Builder(String weatherName)`
      3. 在json中他的格式为
        ```json 
        {
          "condition": "surviving_the_aftermath:weather_condition",
          "value": "minecraft:clear"
        }
        ```
      
2. `PlayerConditionModule`  
`PlayerConditionModule`是一个抽象的模块,他有属性`player`
    * `XpConditionModule`
        1. `XpConditionModule`可以用于判断玩家是否有足够的经验
        2. 他的构造器为`new XpConditionModule.Builder(int xp)`
        3. 在json中他的格式为
        ```json 
        {
          "condition": "surviving_the_aftermath:xp_condition",
          "value": 10
        }
        ```

### IEntityInfoModule
* 实现此模块的将作为实体信息模块，用于处理各种模块的实体支持

我已提供个默认的实现，
 1. `EntityInfoModule`
    1. 他有属性`entityName`和`amountModule`
    2. 他的构造器为`new EntityInfoModule.Builder(String entityName)`
    3. 在json中他的格式为
    ```json
    {
      "entity_info": "surviving_the_aftermath:entity_info",
      "entity_type": "minecraft:zombie",
      "amount_module": {
          "amount": "surviving_the_aftermath:random_amount",
          "max": 5,
          "min": 1
      }
    }
    ```
 2. `EntityInfoWithEquipmentModule`
    1. 他有属性`entityName`和`amountModule`和`equipment`和`canDrop`
    2. 他的构造器为`new EntityInfoWithEquipmentModule.Builder(String entityName)`
    3. 在json中他的格式为
    ```json
    {
      "entity_info": "surviving_the_aftermath:entity_info_with_equipment",
      "entity_type": "minecraft:zombie",
      "canDrop": false,
      "amount_module": {
        "amount": "surviving_the_aftermath:integer_amount",
        "value": 1
      },
      "equipment": [
          {
            "data": "minecraft:stone",
            "weight": 10
          },
          {
            "data": "minecraft:stone_axe",
            "weight": 20
          }
        ]
      }
    ```



### IWeightedModule
* 实现此模块的将作为权重模块，用于处理各种模块的权重支持

我已提供个默认的实现，
 1. `ItemWeightedModule`
    1. 他有属性`items`
    2. 他的构造器为`new ItemWeightedModule.Builder()`
    3. 在json中他的格式为
    ```json
    {
      "rewards": [
        {
            "data": "minecraft:stone",
            "weight": 10
        },
        {
            "data": "minecraft:stone_axe",
            "weight": 20
        }
      ]
    }
    ```
 2. `EntityWeightedModule`
    1. 他有属性`entities`
    2. 他的构造器为`new EntityWeightedModule.Builder()`
    3. 在json中他的格式为
    ```json
    {
      "equipment": [
          {
            "data": "minecraft:stone",
            "weight": 10
          },
          {
            "data": "minecraft:stone_axe",
            "weight": 20
          }
        ]
    }
    ```


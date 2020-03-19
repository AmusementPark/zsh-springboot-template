GET /idx_sample
# 运行NESTED类型，可以查看ES中生成的索引属性，其中的嵌套对象多了一个类型属性："type" : "nested"
#! Deprecation: [types removal] The parameter include_type_name should be explicitly specified in get indices requests to prepare for 7.0. In 7.0 include_type_name will default to 'false', which means responses will omit the type name in mapping definitions.
{
  "idx_sample" : {
    "aliases" : { },
    "mappings" : {
      "type_sample" : {
        "properties" : {
          "l1String1" : {
            "type" : "text",
            "fields" : {
              "keyword" : {
                "type" : "keyword",
                "ignore_above" : 256
              }
            }
          },
          "l1String2" : {
            "type" : "text",
            "fields" : {
              "keyword" : {
                "type" : "keyword",
                "ignore_above" : 256
              }
            }
          },
          "layer2BeansList" : {
            "type" : "nested",
            "properties" : {
              "l2String1" : {
                "type" : "text",
                "fields" : {
                  "keyword" : {
                    "type" : "keyword",
                    "ignore_above" : 256
                  }
                }
              },
              "l2String2" : {
                "type" : "text",
                "fields" : {
                  "keyword" : {
                    "type" : "keyword",
                    "ignore_above" : 256
                  }
                }
              },
              "layer3Bean" : {
                "type" : "nested",
                "properties" : {
                  "l3String1" : {
                    "type" : "text",
                    "fields" : {
                      "keyword" : {
                        "type" : "keyword",
                        "ignore_above" : 256
                      }
                    }
                  },
                  "l3String2" : {
                    "type" : "text",
                    "fields" : {
                      "keyword" : {
                        "type" : "keyword",
                        "ignore_above" : 256
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    },
    "settings" : {
      "index" : {
        "refresh_interval" : "1s",
        "number_of_shards" : "5",
        "provided_name" : "idx_sample",
        "creation_date" : "1584598353593",
        "store" : {
          "type" : "fs"
        },
        "number_of_replicas" : "1",
        "uuid" : "iHkg7-D6TtmRIerWVepi9A",
        "version" : {
          "created" : "6070099"
        }
      }
    }
  }
}

# 对于NESTED类型，查询方式有所变化，查询嵌套对象中的元素，需要在query中加入nested

GET /idx_sample/type_sample/_search
{
  "query": {
    "bool": {
      "must": [
        {"match": { "l1String1": "ajID"} },
        {"nested": {
          "path": "layer2BeansList",
          "query": {
            "bool": {
              "must": [
                {"match": {"layer2BeansList.l2String1": "tfyP"}},
                {"match": {"layer2BeansList.l2String2": "idAF"}},
                {"nested": {
                  "path": "layer2BeansList.layer3Bean",    #注意这里需要逐层的添加路径
                  "query": {
                    "bool": {
                      "must": [
                        {"match": {"layer2BeansList.layer3Bean.l3String1": "tngZ"}}
                      ]
                    }
                  }
                }}
              ]
            }
          }
        }}
      ]
    }
  }
}

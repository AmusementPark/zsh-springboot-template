# 普通查询
GET /idx_sample/type_sample/_search
{
  "query": {
    "match" : {
      "l1String1" : "udyT"
    }
  }
}

####
#### 不包含nested类型的复杂文档结构
#object 和 nested
#object：对象

# 存储一篇博客，同时将博客对应的评论也存储到该博客里面，
# 评论comments是一个数组，数组的每一个元素是一个对象object, 因为没有预先显式的创建索引，所以es会字段自动映射，将comments的类型映射为object
PUT /my_index/blogpost/1
{
  "title": "Nest eggs",
  "body":  "Making your money work...",
  "tags":  [ "cash", "shares" ],
  "comments": [
    {
      "name":    "John Smith",
      "comment": "Great article",
      "age":     28,
      "stars":   4,
      "date":    "2014-09-01"
    },
    {
      "name":    "Alice White",
      "comment": "More like this please",
      "age":     31,
      "stars":   5,
      "date":    "2014-10-22"
    }
  ]
}

###
# 对于类型为object的对象数组es会被处理成如下的扁平式键值对的结构
# 这种处理方式是将数组中的相同的字段的所有值作为一个数组来作为整个字段的值，如果这样处理的话，就和下面的文档索引方式是一致的，但是这两个文档的数据类型明显是不同的，一个是普通对象，对象中的每个属性的值都是数组；另一个是字段是数组类型(虽然ES中的数据类型是没有数组类型)，数组中的每个元素是一个普通对象，每个对象的字段值都是基本数据类型，非数组类型
#{
#  "comments": {
#     "name": [ alice, john, smith, white ],
#     "comment": [ article, great, like, more, please, this ],
#     "age": [ 28, 31 ],
#     "stars": [ 4, 5 ],
#     "date": [ 2014-09-01, 2014-10-22 ]
#  }
#}
#{
#  "title":            [ eggs, nest ],
#  "body":             [ making, money, work, your ],
#  "tags":             [ cash, shares ],
#  "comments.name":    [ alice, john, smith, white ],
#  "comments.comment": [ article, great, like, more, please, this ],
#  "comments.age":     [ 28, 31 ],
#  "comments.stars":   [ 4, 5 ],
#  "comments.date":    [ 2014-09-01, 2014-10-22 ]
#}

#该查询类似于 select * from blogpost where comments.name like "%Alice%" and comments.age like "%28%"
#根据es对类型为object的类型的分析，可以看到 comments.name 是一个数组，数组中有个元素是alice，comments.age是一个数组，数组中有一个元素是28，所以两个条件都满足，所以该文档满足查询条件
#现实结果是该文档满足这两个条件，但是这是我们想要的结果吗？？？
#我们的意思是像表达，数组中的某一个元素如果满足这两个条件就查询出来，也就是说如果数组中有一个元素同时满足这两个条件，我们看数组中有两个元素，
#其中 name like '%Alice%' and age like '%28%' 同时满足这个条件的数组元素是没有的，name包含"alice"的那个元素对应的age是31岁，不是28岁，
#28岁是name为”john smith“的， 所以这并不是我们想要的查询结果
GET /my_index/blogpost/_search
{
  "query": {
    "bool": {
      "must": [
        { "match": { "comments.name": "Alice" }},
        { "match": { "comments.age":  28      }}
      ]
    }
  }
}
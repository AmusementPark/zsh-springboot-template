根据测试，例如有如下的配置
```
[{
  "apiName": "zsh-another-v1-s",
  "predicateItems": [
    {
      "pattern": "/ano/another/v1",
      "matchStrategy": 0
    }
  ]
},{
  "apiName": "zsh-another-v2-s",
  "predicateItems": [
    {
      "pattern": "/ano/another/v2",
      "matchStrategy": 0
    }
  ]
},{
  "apiName": "zsh-another-s",
  "predicateItems": [
    {
      "pattern": "/ano/another",
      "matchStrategy": 0
    }
  ]
}]
```
```
[{
  "resource": "zsh-another-s",
  "count": 4,
  "resourceMode": 0
},{
  "resource": "zsh-another-v1-s",
  "count": 9,
  "resourceMode": 0
},{
  "resource": "zsh-another-v2-s",
  "count": 4,
  "resourceMode": 0
}]
```
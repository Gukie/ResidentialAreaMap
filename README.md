# refer
- https://www.cnblogs.com/tangs/articles/5873622.html


## baidu java doc
- http://wiki.lbsyun.baidu.com/cms/androidloc/doc/v7.5/index.html

## android studio mock location service
- https://www.jesusamieiro.com/android-studio-simulate-multiple-gps-points-with-mock-location-plugin/ (install plugin)
- http://www.northborder-software.com/emulator_location_services.html


## cannot fetch the map data from baidu
error shows like following:
```
java.net.UnknownHostException: Unable to resolve host "loc.map.baidu.com": No address associated with hostname
```

## AVD 对于地图，又一个 load GPX/KML

## 20180720 issue
- map not loaded and report following issue:
```
    =============================================
    ----------------- 鉴权错误信息 ------------
    sha1;package:46:54:B7:D4:BD:56:96:49:A1:0D:C2:C2:88:83:BD:A6:A0:6D:63:0B;lokia.com
    key:IFw00zd03c8eYva43zH3zfHyQbYFUQjx
    errorcode: -11 uid: -1 appid -1 msg: httpsPost failed,IOException:Unable to resolve host "api.map.baidu.com": No address associated with hostname
    请仔细核查 SHA1、package与key申请信息是否对应，key是否删除，平台是否匹配
    errorcode为230时，请参考论坛链接：
    http://bbs.lbsyun.baidu.com/forum.php?mod=viewthread&tid=106461
    =============================================
```
exist-service
=============
A CTS2 Implementation based on the eXist XML DB.


eXist Install
-------------
In the ```conf.xml``` file, make sure full text indexing is turned on:

```xml
<index>
  <fulltext attributes="true" default="all">
    <exclude path="/auth"/>
  </fulltext>
</index>
```

exist-service
=============
A CTS2 Implementation based on the eXist XML DB.


eXist Install
-------------
Older versions of exist-service required full text indexing to be enabled in the ```conf.xml```.  This is no longer the case.

Indexing should now be set to (the default value) of disabled - per below.

For other configuration settings, see the suggested-conf.xml file.

```xml
<index>
  <fulltext attributes="false" default="none">
    <exclude path="/auth"/>
  </fulltext>
</index>
```

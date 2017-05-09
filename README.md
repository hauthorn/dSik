# dSik
Repo for the mandatory programming exercises in Sikkerhed.

Remember to setup your iml file as follows:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<module type="JAVA_MODULE" version="4">
  <component name="NewModuleRootManager" inherit-compiler-output="true">
    <exclude-output />
    <content url="file://$MODULE_DIR$">
      <sourceFolder url="file://$MODULE_DIR$/src" isTestSource="false" />
      <sourceFolder url="file://$MODULE_DIR$/test" isTestSource="true" />
    </content>
    <orderEntry type="inheritedJdk" />
    <orderEntry type="sourceFolder" forTests="false" />
  </component>
</module>
```

## Handin 1
We implemented and tested an implemantation of RSA. Source package `src/uge1`

Run `test/uge1/RSATest` to see test results.

## Handin 2
We implemented the Signature class to hash a message and apply a signature to it.

We also implemented `uge2/Main`, which experimentally shows how fast the algorithms are.

## Handin 4 (2017-05-09)
We implemented Authenticated Key Exchange using the Diffie-Hellman key exchange algorithm.

Run the Server class first, then `java Class <ip address of server> <port number of server (4321)>`.
# SHA-256 Java realisation

That project is just a selfmade SHA-256 algorithm wrote on Java, that mainly manipulates on String. Integer or Long are used for convinient hex/dec/bin convertations where it's possible without time/memory looses. All other operations are mainly bitwise, so I've decided to let 'em be in String form.

## Files

```sha256.java``` - contains whole algorithm;

```shaMain.java``` - just an example of realisation.

## Quick method reference

```genSHA(String input)``` - executes all necessary operations, converting ```input``` into SHA-256 hash.

```getHash()``` - returns hash value, stored in class.

```prettyBytePrint(String input)``` - outputs ```input``` as 8-bit table with 8 words in a row.

## GLHF
That project is totally GPL - feel free to use it for ant needs you may have (except for sale).

###### [06.12.21] btw, that project's hash is 2990D384CB383478EF0143B8EDEB34984EB45CD410491B2CF23DE623 :)

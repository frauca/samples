# Word counter

I want to make the typical book word counter problem of map reduce in rust to make a little bit of parallel processing.

The idea has quite a bit logic to force me to work in rust.

## Objective

Use gutenberg.org to get the book content, then open the content of the book and get all the words of the book so we know how many times apears each word.

If I'm not borred on a second round I could use a dictionary to remove all the words that are not verbs or nouns.

## Resources

### Gutenberg

Gutenberg.org is a web page where you can find all the books without copyright.

I have not found any rest api for gutenberg so I will need to parse html files.

gutenberg has a [robot access](https://www.gutenberg.org/policy/robot_access.html)

I think the easiest way will be to get an rdf [catalog](https://www.gutenberg.org/ebooks/offline_catalogs.html#the-project-gutenberg-catalog-metadata-in-machine-readable-format) and from there 
try to get the file by

```xml
<pgterms:etext rdf:ID="etext2489">
<dc:publisher>&pg;</dc:publisher>
<dc:title rdf:parseType="Literal">Moby Dick; Or, The Whale</dc:title>
<dc:description rdf:parseType="Literal">See also Etext #15, Etext #2701, and a computer-generated audio file, Etext #9147.</dc:description>
<dc:creator rdf:parseType="Literal">Melville, Herman, 1819-1891</dc:creator>
<pgterms:friendlytitle rdf:parseType="Literal">Moby Dick; Or, The Whale by Herman Melville</pgterms:friendlytitle>
<dc:language><dcterms:ISO639-2><rdf:value>en</rdf:value></dcterms:ISO639-2></dc:language>
<dc:subject>
<rdf:Bag>
<rdf:li><dcterms:LCSH><rdf:value>Adventure stories</rdf:value></dcterms:LCSH></rdf:li>
<rdf:li><dcterms:LCSH><rdf:value>Sea stories</rdf:value></dcterms:LCSH></rdf:li>
<rdf:li><dcterms:LCSH><rdf:value>Psychological fiction</rdf:value></dcterms:LCSH></rdf:li>
<rdf:li><dcterms:LCSH><rdf:value>Whales -- Fiction</rdf:value></dcterms:LCSH></rdf:li>
<rdf:li><dcterms:LCSH><rdf:value>Whaling ships -- Fiction</rdf:value></dcterms:LCSH></rdf:li>
<rdf:li><dcterms:LCSH><rdf:value>Ahab, Captain (Fictitious character) -- Fiction</rdf:value></dcterms:LCSH></rdf:li>
<rdf:li><dcterms:LCSH><rdf:value>Mentally ill -- Fiction</rdf:value></dcterms:LCSH></rdf:li>
<rdf:li><dcterms:LCSH><rdf:value>Whaling -- Fiction</rdf:value></dcterms:LCSH></rdf:li>
<rdf:li><dcterms:LCSH><rdf:value>Ship captains -- Fiction</rdf:value></dcterms:LCSH></rdf:li>
</rdf:Bag>
</dc:subject>
<dc:subject><dcterms:LCC><rdf:value>PS</rdf:value></dcterms:LCC></dc:subject>
<dc:created><dcterms:W3CDTF><rdf:value>2001-01-01</rdf:value></dcterms:W3CDTF></dc:created>
<pgterms:downloads><xsd:nonNegativeInteger><rdf:value>325</rdf:value></xsd:nonNegativeInteger></pgterms:downloads>
<dc:rights rdf:resource="&lic;" />
</pgterms:etext> 
```
try to get the file 

`wget -qO- "http://gutenberg.org/files/2489/2489-0.txt"`
some of the files must be blank

The program default path for the catalog is resources/main. You could download there or choose on the config file where to look for it

```shell
scripts/download_catalog.sh
mv catalog.rdf resources/main
```

### Database

I will use postgres database with diesel.

I will use docker database. To create database I will make


```shell
scripts/install_database.sh
```
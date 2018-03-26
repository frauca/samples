##could not read from hhtps
##doc<- xmlTreeParse("https://www.w3schools.com/xml/simple.xml",useInternalNodes = TRUE)
library(XML)
library(RCurl)
xData <- getURL("https://www.w3schools.com/xml/simple.xml")
doc <- xmlParse(xData)
rootNode<-xmlRoot(doc)
xmlName(rootNode)
names(rootNode)
rootNode[[1]][[1]]
xmlSApply(rootNode,xmlValue)
xpathSApply(rootNode,"//name",xmlValue)

htmlDoc<-htmlTreeParse("http://www.espn.com/nfl/scoreboard",useInternalNodes = TRUE)
scores<-xpathSApply(htmlDoc,"//td[@class='score']",xmlValue)
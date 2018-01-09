
if(!file.exists("./data")){
  dir.create("./data")
}
if(!file.exists("./data/housing.csv")){
  download.file("https://d396qusza40orc.cloudfront.net/getdata%2Fdata%2Fss06hid.csv",destfile = "./data/housing.csv",method = "curl") 
}

housing<-read.csv("./data/housing.csv")
housing[,c("VAL")]<-sapply(housing[,c("VAL")], as.numeric)

if(!file.exists("./data/govNGAP.xlsx")){
  
  download.file("https://d396qusza40orc.cloudfront.net/getdata%2Fdata%2FDATA.gov_NGAP.xlsx",destfile = "./data/govNGAP.xlsx",method = "curl")
  
}
library(xlsx)
NGAP<-read.xlsx("./data/govNGAP.xlsx",sheetIndex = 1,rowIndex = 18:23,colIndex = 7:15)

if(!file.exists("./data/restaurants.xml")){
  
  download.file("http://d396qusza40orc.cloudfront.net/getdata%2Fdata%2Frestaurants.xml",destfile = "./data/restaurants.xml",method = "curl")
  
}

library(XML)

restaurantsDoc<-xmlTreeParse("./data/restaurants.xml",useInternalNodes = TRUE)
restaurantsRoot<-xmlRoot(restaurantsDoc)
zipCodes<-xpathSApply(restaurantsRoot,"//zipcode",xmlValue)

if(!file.exists("./data/comunity.csv")){
  
  download.file("https://d396qusza40orc.cloudfront.net/getdata%2Fdata%2Fss06pid.csv",destfile = "./data/comunity.csv",method = "curl")
                 
}
library(data.table)
comunity<-fread("./data/comunity.csv")


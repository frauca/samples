if(!file.exists("./data")){
  dir.create("./data")
}
if(!file.exists("./data/housing.csv")){
  
  download.file("https://d396qusza40orc.cloudfront.net/getdata%2Fdata%2Fss06hid.csv",destfile = "./data/housing.csv",method = "curl")
  
}
housing <- read.csv("./data/housing.csv")
library(dplyr)
housing_bl<-tbl_df(housing)
agricultureLogical <- housing_bl$ACR == 3 & housing_bl$AGS == 6
which(agricultureLogical)  

if(!file.exists("./data/jeff.jpg")){
  
  download.file("https://d396qusza40orc.cloudfront.net/getdata%2Fjeff.jpg",destfile = "./data/jeff.jpg",method = "curl")
  
}
library(jpeg)
jeffjpg<-readJPEG("./data/jeff.jpg", native = TRUE)
quantile(jeffjpg, probs = c(0.3, 0.8))

if(!file.exists("./data/gross.csv")){
  
  download.file("https://d396qusza40orc.cloudfront.net/getdata%2Fdata%2FGDP.csv",destfile = "./data/gross.csv",method = "curl")
  
}
if(!file.exists("./data/educational.csv")){
  
  download.file("https://d396qusza40orc.cloudfront.net/getdata%2Fdata%2FEDSTATS_Country.csv",destfile = "./data/educational.csv",method = "curl")
  
}

gross<-read.csv("./data/gross.csv",skip=4,nrows = 190)
gross<-rename(gross,CountryCode = X)
educational<-read.csv("./data/educational.csv")
merged<-merge(gross,educational,by="CountryCode")
arrange(merged, desc(X.1))[13, ]
library(data.table)
gdp = fread("./data/gross.csv", skip=4, nrows = 190, select = c(1, 2, 4, 5), col.names=c("CountryCode", "Rank", "Economy", "Total"))
edu = fread("./data/educational.csv")
merge = merge(gdp, edu, by = 'CountryCode')
nrow(merge)


merge %>% 
  group_by(`Income Group`) %>% 
  summarise(rank=mean(Rank))

merge$RankGroups <- cut2(merge$Rank, g=5)
table(merge$RankGroups, merge$`Income Group`)

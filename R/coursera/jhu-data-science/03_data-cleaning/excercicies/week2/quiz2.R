if(!file.exists("./data")){
  dir.create("./data")
}

library(jsonlite)
jtleekRepos<-fromJSON("https://api.github.com/users/jtleek/repos")
#created at 
jtleekRepos[jtleekRepos$name=='datasharing',]$created_at

library(sqldf)
if(!file.exists("./data/comunity.csv")){
  
  download.file("https://d396qusza40orc.cloudfront.net/getdata%2Fdata%2Fss06pid.csv",destfile = "./data/comunity.csv",method = "curl")
  
}

acs<-read.csv("./data/comunity.csv")

#read web as text
con<-url("http://biostat.jhsph.edu/~jleek/contact.html")
htmlCode<-readLines(con)
sapply(htmlCode[c(10,20,30,100)],nchar)
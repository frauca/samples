if(!dir.exists("data")){
  dir.create("data")
}
if(!file.exists("data/pm25_data.zip")){
  download.file("https://raw.githubusercontent.com/bcaffo/courses/master/04_ExploratoryAnalysis/CaseStudy/pm25_data.zip",destfile = "data/pm25_data.zip",method = "curl")
}
if(!file.exists("data/pm25_data/RD_501_88101_1999-0.txt")){
  unzip("data/pm25_data.zip",exdir = "data/")
}

pm0 <- read.table("data/pm25_data/RD_501_88101_1999-0.txt",comment.char = "#",na.strings = "",sep = "|",header = FALSE)
pm0Names<-strsplit(readLines("data/pm25_data/RD_501_88101_1999-0.txt",1),"|",fixed = TRUE)
names(pm0)<-make.names(pm0Names[[1]])


pm1 <- read.table("data/pm25_data/RD_501_88101_2012-0.txt",comment.char = "#",na.strings = "",sep = "|",header = FALSE)
pm1Names<-strsplit(readLines("data/pm25_data/RD_501_88101_2012-0.txt",1),"|",fixed = TRUE)
names(pm1)<-make.names(pm1Names[[1]])

boxplot(pm0$Sample.Value,pm1$Sample.Value)
boxplot(log10(pm0$Sample.Value),log10(pm1$Sample.Value))

pm0$DateAsDate<-as.Date(as.character(pm0$Date),"%Y%m%d")
pm1$DateAsDate<-as.Date(as.character(pm1$Date),"%Y%m%d")
hist(pm1$DateAsDate,"months")

hist(pm1$DateAsDate[pm1$Sample.Value<0],"month")


state36_0<-unique(subset(pm0,State.Code==36,c(County.Code,Site.ID)))
state36_0<-paste(state36_0[,1],".",state36_0[,2],sep="")
state36_1<-unique(subset(pm1,State.Code==36,c(County.Code,Site.ID)))
state36_1<-paste(state36_1[,1],".",state36_1[,2],sep="")

both<-intersect(state36_0,state36_1)
pm0$County.Site<-with(pm0,paste(County.Code,Site.ID,sep="."))
pm1$County.Site<-with(pm1,paste(County.Code,Site.ID,sep="."))

nywBoth0<-subset(pm0,State.Code==36 & County.Site %in% both)
nywBoth1<-subset(pm1,State.Code==36 & County.Site %in% both)

sapply(split(nywBoth0,nywBoth0$County.Site),nrow)
sapply(split(nywBoth1,nywBoth1$County.Site),nrow)

nyw632008_1<-subset(pm1,State.Code==36 & County.Code==63 & Site.ID==2008)
nyw632008_0<-subset(pm0,State.Code==36 & County.Code==63 & Site.ID==2008)

plot(nyw632008_0$DateAsDate,nyw632008_0$Sample.Value,pch=20)
plot(nyw632008_1$DateAsDate,nyw632008_1$Sample.Value,pch=18)
par(mfrow=c(1,2),mar=c(4,4,2,1))


#plots has diferent ranges so better homogenizar
rng<-range(nyw632008_0$Sample.Value,nyw632008_1$Sample.Value,na.rm = TRUE )
plot(nyw632008_0$DateAsDate,nyw632008_0$Sample.Value,pch=20,ylim=rng)
abline(h=median(nyw632008_0$Sample.Value,na.rm = TRUE))
plot(nyw632008_1$DateAsDate,nyw632008_1$Sample.Value,pch=12,ylim=rng)
abline(h=median(nyw632008_1$Sample.Value,na.rm = TRUE))


mn0<-with(pm0,tapply(Sample.Value,State.Code,mean,na.rm=TRUE))
mn1<-with(pm1,tapply(Sample.Value,State.Code,mean,na.rm=TRUE))

d0<-data.frame(name=names(mn0), mean=mn0)
d1<-data.frame(name=names(mn1), mean=mn1)
mrg<-merge(d0,d1,by = "name")

plot(rep(1999,52),mrg[,2],xlim=c(1998,2013))
points(rep(2012,52),mrg[,3])
segments(rep(1999,52),mrg[,2],rep(2012,52),mrg[,3])

setwd("~/samples/R/coursera/jhu-data-science/04_exploratory-data-analysis/exercicies")


if(!file.exists("data/NEI_data.zip")){
  download.file("https://d396qusza40orc.cloudfront.net/exdata/data/NEI_data.zip","data/NEI_data.zip",method = "curl")
}
if(!file.exists("data/Source_Classification_Code.rds")){
  unzip("data/NEI_data.zip",exdir = "data/")
}  

## This first line will likely take a few seconds. Be patient!
NEI <- readRDS("data/summarySCC_PM25.rds")
SCC <- readRDS("data/Source_Classification_Code.rds")

byYear<-tapply(NEI$Emissions, NEI$year, sum)

plot(as.numeric(names(byYear)),byYear/1000,col=names(byYear))
s<-seq(length(byYear)-1)
segments( as.numeric(names(byYear))[s],byYear[s]/1000, as.numeric(names(byYear))[s+1],byYear[s+1]/1000,col=names(byYear))
text(as.numeric(names(byYear)),byYear/1000+c(-200,rep(200,3)),round(byYear/1000,2))

baltimore<-NEI[NEI$fips == "24510",]
byYear<-tapply(baltimore$Emissions, baltimore$year, sum)     
barplot(byYear/1000,names.arg=names(byYear),col=names(byYear))


byTypeYear<-tapply(baltimore$Emissions, list(baltimore$type, baltimore$year), sum)
byTypeYear<-as.data.frame(as.table(byTypeYear))
names(byTypeYear)<-c("type","year","Emissions")
library(ggplot2)
ggplot(byTypeYear,aes(x=factor(year),y=Emissions,fill=year))+geom_bar(stat="identity")+facet_grid(.  ~  type)
ggplot(byTypeYear,aes(x=factor(year),y=Emissions))+geom_line(group=1)+facet_grid(.  ~  type)

coalEmisionsCodes<-SCC[grepl("Fuel Comb.*Coal", SCC$EI.Sector),]
coalEmisions<-NEI[NEI$SCC %in% coalEmisionsCodes$SCC,]
byYear<-aggregate(coalEmisions$Emissions, list(coalEmisions$year), sum)
names(byYear)<-c("year","Emissions")
ggplot(byYear, aes(x=factor(year), y=Emissions/1000,fill=year, label = round(Emissions/1000,2))) +
  geom_bar(stat="identity") +
  xlab("year") +
  ylab(expression("total PM"[2.5]*" emissions in kilotons")) +
  ggtitle("Emissions from coal combustion-related sources in kilotons")+
  geom_label(aes(fill = year),colour = "white", fontface = "bold")

baltimore<-NEI[NEI$fips == "24510" & NEI$type=="ON-ROAD",]
byYear<-aggregate(baltimore$Emissions, list(baltimore$year), sum)
names(byYear)<-c("year","Emissions")
ggplot(byYear, aes(x=factor(year), y=Emissions,fill=year, label = round(Emissions,2))) +
  geom_bar(stat="identity") +
  xlab("year") +
  ylab(expression("total PM"[2.5]*" emissions in tons")) +
  ggtitle("Emissions from motor vehicle sources in tons")+
  geom_label(aes(fill = year),colour = "white", fontface = "bold")

baltimore<-NEI[NEI$fips %in% c("24510","06037") & NEI$type=="ON-ROAD",]
byYear<-aggregate(baltimore$Emissions, list(fips=baltimore$fips,year=baltimore$year), sum)
names(byYear)<-c("fips","year","Emissions")
byYear$fipsNames<-ifelse(byYear$fips=="24510","Baltimore","Angeles")
ggplot(byYear, aes(x=factor(year), y=Emissions,fill=year, label = round(Emissions,2))) +
  geom_bar(stat="identity") +
  xlab("year") +
  ylab(expression("total PM"[2.5]*" emissions in tons")) +
  ggtitle("Emissions from motor vehicle sources in tons")+
  geom_label(aes(fill = year),colour = "white", fontface = "bold")+
  facet_grid(.  ~  fipsNames)

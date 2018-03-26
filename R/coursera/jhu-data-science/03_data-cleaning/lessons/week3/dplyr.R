library(dplyr)
if(!file.exists("./data")){
  dir.create("./data")
}
if(!file.exists("./data/chicago.rds")){
  download.file("https://github.com/DataScienceSpecialization/courses/raw/master/03_GettingData/dplyr/chicago.rds",destfile = "./data/chicago.rds",method = "curl")
}

chicago<- readRDS("./data/chicago.rds")


filter(chicago,o3tmean2>mean(o3tmean2))  

head(arrange(chicago,date))
head(arrange(chicago,desc(date)))

chicago <- rename(chicago, dewpoint = dptp,pm25 = pm25tmean2)

chicago <- mutate(chicago,
                  pm25detrend=pm25-mean(pm25, na.rm=TRUE))

head(filter(select(chicago,pm25,pm25detrend),!is.na(pm25)))

chicago <- mutate(chicago,
                  tempcat = factor(1 * (tmpd > 80),
                  labels = c("cold", "hot")))

hotcold <- group_by(chicago, tempcat)

summarise(hotcold,n=n())
summarize(hotcold, pm25 = mean(pm25, na.rm = TRUE),
          o3 = max(o3tmean2),
          no2 = median(no2tmean2))

chicago %>% 
  mutate(month = as.POSIXlt(date)$mon + 1) %>% 
  group_by(month) %>% 
  summarize(pm25 = mean(pm25, na.rm = TRUE),
              o3 = max(o3tmean2, na.rm = TRUE),
              no2 = median(no2tmean2, na.rm = TRUE))
if(!dir.exists("data")){
  dir.create("data")
}

if(!file.exists("data/bmi_pm25_no2_sim.csv")){
  download.file("https://raw.githubusercontent.com/rdpeng/artofdatascience/master/manuscript/data/bmi_pm25_no2_sim.csv",destfile = "data/bmi_pm25_no2_sim.csv");
}

maacs <- read.csv("data/bmi_pm25_no2_sim.csv")

qplot(maacs$logpm25,maacs$NocturnalSympt,data=maacs, facets = . ~ bmicat,geom = c("point","smooth"),method="lm")

g<- ggplot(maacs,aes(maacs$logpm25,maacs$NocturnalSympt))

g + geom_point() + facet_grid(. ~ bmicat) + geom_smooth(method = "lm")

g + geom_point() + geom_smooth(aes(color="red")) + geom_smooth(aes(color="blue"),method = "lm")

g + geom_point(color = "steelblue", size = 4, alpha = 1/2)

g + geom_point(aes(color = bmicat), size = 4, alpha = 1/2)
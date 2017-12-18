library(datasets)
head(airquality)
#aquest no esta be i no se perque es un lio
#tapply(airquality, airquality$Month, mean)

s<-split(airquality,airquality$Month)
lapply(s, function(x) colMeans(x[,c("Ozone","Solar.R","Wind")]))
sapply(s, function(x) colMeans(x[,c("Ozone","Solar.R","Wind")]))
sapply(s, function(x) colMeans(x[,c("Ozone","Solar.R","Wind")],na.rm = TRUE))
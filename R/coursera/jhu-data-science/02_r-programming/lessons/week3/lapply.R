x<-list(1:5,rnorm(10))
lapply(x, mean)

x<-1:4
lapply(x,runif)

x<- list(a=matrix(1:4, nrow = 2, ncol = 2),b=matrix(1:6,3,2))
lapply(x, function(elt)elt[,1])

x<-list(1:5,rnorm(10))
sapply(x, mean)
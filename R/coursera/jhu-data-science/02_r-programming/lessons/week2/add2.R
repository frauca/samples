add2<- function(x,y){
  x+y
}

avobe10<- function(x){
  avobe(x,10)
}

avobe <- function(x,y){
  x[x>y]
}

columnsmean <-function(x,removeNA = TRUE){
  nc <- ncol(x)
  means <- numeric(nc)
  for(i in 1:nc){
    means[i] <- mean(x[,i],na.rm = removeNA)
  }
  means
}
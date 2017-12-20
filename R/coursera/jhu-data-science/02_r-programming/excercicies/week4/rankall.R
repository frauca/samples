rankall <- function(outcome, num = "best") {
  ## Read outcome data
  df <- read.csv("outcome-of-care-measures.csv", colClasses = "character")
  dfs<-df[,c(2, 7, 11, 17, 23)]
  names(dfs)<-c("name","state","heart attack", "heart failure",  "pneumonia")
  ## Check that state and outcome are valid
  if(!(outcome  %in% c("heart attack", "heart failure",  "pneumonia"))
     || !(num %in% c("best","worst") ||class(num)=="numeric" )){
    warning("invalid input")
    return(NA)
  }
  dfs<-dfs[dfs[outcome]!="Not Available",]
  dfs[,outcome]<-as.numeric(dfs[,outcome])
  ## For each state, find the hospital of the given rank
  findNthRank<-function(state){
    tmp<-dfs[dfs["state"]==state,]
    tmp<-tmp[order(tmp[,outcome],tmp$name),]
    if(num=="best"){
      num<-1
    }else if (num=="worst"){
      num<-dim(tmp)[1]
    }
    nth<-tmp[num,c(1,2)]
    nth$state<-state#set null values as NA will be empty
    nth
  }
  ## Return a data frame with the hospital names and the
  ## (abbreviated) state name
  res<-lapply(sort(unique(dfs$state)),findNthRank)
  do.call(rbind,lapply(res,data.frame))
}
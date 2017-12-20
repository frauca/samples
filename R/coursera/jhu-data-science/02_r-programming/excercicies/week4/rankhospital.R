rankhospital <- function(state, outcome, num = "best") {
  ## Read outcome data
  df <- read.csv("outcome-of-care-measures.csv", colClasses = "character")
  dfs<-df[,c(2, 7, 11, 17, 23)]
  names(dfs)<-c("name","state","heart attack", "heart failure",  "pneumonia")
  ## Check that state and outcome are valid
  if(!(state %in% dfs$state)
     || !(outcome  %in% c("heart attack", "heart failure",  "pneumonia"))
     || !(num %in% c("best","worst") ||class(num)=="numeric" )){
    warning("state is not on the state list or outcome is not one of 'hear attack' 'heart failure' or 'pnemonia'")
    return(NA)
  }
  ## Return hospital name in that state with the given rank
  ## 30-day death rate
  dfs<-dfs[dfs[outcome]!="Not Available" & dfs["state"]==state,]
  dfs[,outcome]<-as.numeric(dfs[,outcome])
  dfs<-dfs[order(dfs[,outcome],dfs$name),]
  if(num=="best"){
    num<-1
  }else if (num=="worst"){
    num<-dim(dfs)[1]
  }
  dfs$name[num]
}
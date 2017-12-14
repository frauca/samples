complete <- function(directory, id = 1:332) {
  ## 'directory' is a character vector of length 1 indicating
  ## the location of the CSV files
  
  ## 'id' is an integer vector indicating the monitor ID numbers
  ## to be used
  
  ## Return a data frame of the form:
  ## id nobs
  ## 1  117
  ## 2  1041
  ## ...
  ## where 'id' is the monitor ID number and 'nobs' is the
  ## number of complete cases
  
  id<- id[!is.na(id)]
  nobs<-1:length(id)
  for(i in 1:length(id)){
    #print(paste(directory,"/",formatC(id[i], width=3, flag="0"),".csv",sep = ""))
    d<-read.csv(paste(directory,"/",formatC(id[i], width=3, flag="0"),".csv",sep = ""))
    #print(data[pollutant])
    nobs[i]<-dim(d[complete.cases(d),])[1]
  }
  data.frame(id = id, nobs = nobs)
} 
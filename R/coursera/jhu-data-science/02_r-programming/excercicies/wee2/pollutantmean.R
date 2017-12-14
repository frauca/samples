pollutantmean <- function(directory, pollutant = "sulfate", id = 1:332) {
  ## 'directory' is a character vector of length 1 indicating
  ## the location of the CSV files
  
  ## 'pollutant' is a character vector of length 1 indicating
  ## the name of the pollutant for which we will calculate the
  ## mean; either "sulfate" or "nitrate".
  
  ## 'id' is an integer vector indicating the monitor ID numbers
  ## to be used
  
  ## Return the mean of the pollutant across all monitors list
  ## in the 'id' vector (ignoring NA values)
  
  id<- id[!is.na(id)]
  allValues<-c()
  for(i in 1:length(id)){
    #print(paste(directory,"/",formatC(id[i], width=3, flag="0"),".csv",sep = ""))
    data<-read.csv(paste(directory,"/",formatC(id[i], width=3, flag="0"),".csv",sep = ""))
    #print(data[pollutant])
    allValues<-c(allValues,data[[pollutant]])
  }
  mean(allValues,na.rm=TRUE)
}
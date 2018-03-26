library(jsonlite)
jsonData<-fromJSON("https://api.github.com/users/jtleek/repos")
names(jsonData$owner)
toJSON(iris,pretty = TRUE)
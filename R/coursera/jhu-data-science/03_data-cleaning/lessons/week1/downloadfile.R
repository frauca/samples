##download baltimore camere spped
##https://data.baltimorecity.gov/api/views/dz54-2aru/rows.csv?accessType=DOWNLOAD
if(!file.exists("./data")){
  dir.create("./data")
}
download.file("https://data.baltimorecity.gov/api/views/dz54-2aru/rows.csv?accessType=DOWNLOAD",destfile = "./data/cameras.csv",method = "curl")
list.files("./data")
